/*
 * [The "BSD licence"]
 *  Copyright (c) 2017 David J Hait
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 *  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 *  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 *  THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.optionmetrics.ztext;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.optionmetrics.ztext.impl.Formal;
import org.optionmetrics.ztext.impl.SectionHeader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ZMarkupProcessor {

    private SearchPath searchPath;
    private List<Section> sections;

    public ZMarkupProcessor() {
    }

    public void setSearchPath(SearchPath searchPath) {
        this.searchPath = searchPath;
    }

    public SearchPath getSearchPath() {
        return searchPath;
    }

    public ZMarkupProcessor(SearchPath searchPath) {
        this.searchPath = searchPath;
    }

    public List<Section> getSections() {
        return sections;
    }

    public String process(String name) throws IOException, SectionDependencyException {
        // parser and order the sections
        sortSections(name);
        expandDefinitions();  // convert
        StringBuilder sb = new StringBuilder();
        for (Section s : sections) {
            for (Paragraph p : s.getParagraphs()) {
                if (p instanceof SectionHeader || p instanceof Formal)
                    sb.append(p).append("\n");
            }
        }
        return sb.toString();
    }

    private List<Paragraph> load(InputStream fileStream, String fileName) throws IOException {

        CharStream stream = CharStreams.fromStream(fileStream);
        ZMarkupLexer lexer = new ZMarkupLexer(stream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ZMarkupParser parser = new ZMarkupParser(tokens);
        parser.removeErrorListeners();
        ZMarkupErrorListener errorListener = new ZMarkupErrorListener(fileName);
        parser.addErrorListener(errorListener);

        ParserRuleContext tree = parser.specification();

        ZMarkupTranslator listener = new ZMarkupTranslator(tokens, fileName);

        if (errorListener.getErrorCount() > 0) {
            System.err.println(errorListener.getErrorCount() + " errors in file " + fileName);
        } else {
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);
        }
        return listener.getParagraphs();
    }

    private Set<String> sectionsToParents(Collection<Section> sections) {
        Set<String> parents = new HashSet<>();
        for (Section s : sections) {
            parents.addAll(s.getParents());
        }
        return parents;
    }
    private List<Paragraph> filenameToParagraphs(String name) throws IOException {
        List<Paragraph> paragraphs;
        String fname = name;
        if (!name.endsWith(".ztx"))
            fname = fname + ".ztx";
        InputStream inputStream = searchPath.find(fname);
        if (inputStream != null) {
            paragraphs = load(inputStream, fname);
        }
        else {
            throw new IOException("File: " + fname + " not found!");
        }
        return paragraphs;
    }

     private List<Paragraph> addHeader(String name) throws IOException {
        List<Paragraph> ps = filenameToParagraphs(name);

        // split this list of paragraphs into two parts
        List<Paragraph> pref = new ArrayList<>();
        List<Paragraph> suff = new ArrayList<>(ps);
        while (!suff.isEmpty() && !(suff.get(0) instanceof SectionHeader)) {
            pref.add(suff.get(0));
            suff.remove(0);
        }

         String END = "\u2514";
         String ZED = "\u2500";
         if (pref.isEmpty()) {  // the first paragraph was a converter header, nothing before it
            if (!suff.isEmpty() && !((SectionHeader) suff.get(0)).getSectionName().equals(name)) {
                // add an extra converter header, representing this file (different name)
                SectionHeader h = new SectionHeader(ZED +"section " + name + " " + END,
                        name + ".z", -1);
                h.setSectionName(name);
                ps.add(0, h);
            }
        } else if (pref.stream().anyMatch(p->(p instanceof Formal))) { // there is at least one formal not in a converter
            SectionHeader h = new SectionHeader(ZED +"section " + name + " parents standard_toolkit "+ END,
                    name + ".z", -1);
            h.setSectionName(name);
            h.getParents().add("standard_toolkit");
            ps.add(0, h);
        } else {  // everything is in sections except some initial informals
            String sname = name + "_informal";
            SectionHeader h = new SectionHeader(ZED + "section " + sname + " "+ END,
                    "<none>", -1);
            h.setSectionName(sname);
            ps.add(0, h);
        }
        return ps;
    }

    private List<Section> filenameToSections(String name) throws IOException {
        List<Section> sections = new ArrayList<>();
        List<Paragraph> ps = addHeader(name);
        if (ps.size() == 0)
            return sections;
        Section s = null;
        for (Paragraph p : ps) {
            if (p instanceof SectionHeader) {
                if (s != null)
                    sections.add(s);
                s = new Section();
            }
            assert s != null;
            s.getParagraphs().add(p);
        }
        if (s.getParagraphs().size() > 0)
            sections.add(s);
        return sections;
    }


    private Set<Section> readSpec(Set<String> names, Set<Section> sections) throws IOException {
        Set<Section> ss = new HashSet<>();
        if (sections != null)
            ss.addAll(sections);
        if (names.isEmpty())
            return ss;

        Set<Section> ss2 = new HashSet<>();
        for (String n : names) {
            Set<Section> f2s = new HashSet<>(filenameToSections(n));
            ss2.addAll(f2s);
        }

        //prepare first param
        Set<String> firstParam = sectionsToParents(ss2);
        List<String> snames = ss.stream().map(Section::getName).collect(Collectors.toList());
        firstParam.removeAll(snames);
        firstParam.removeAll(names);

        //second param
        ss2.addAll(ss);

        return readSpec(firstParam, ss2);
    }

    private List<Section> orderSections(Set<Section> sections) throws SectionDependencyException {

        List<Section>  sorted = new ArrayList<>();

        boolean done = false;
        while (!done) {
            Optional<Section> n = sections.stream().filter(s->s.mark==Section.Mark.NONE).findFirst();
            if (n.isPresent()) {
                n.get().visit(sections, sorted);
            } else {
                done = true;
            }
        }
        Optional<Section> prelude = sorted.stream().filter(s->s.getName().equals("prelude"))
                .findFirst();
        prelude.ifPresent(section -> {
            sorted.remove(section);
            sorted.add(0, section);
        });
        return sorted;
    }

    private void sortSections(String name) throws IOException, SectionDependencyException {
        Set<String> names = new HashSet<>();
        names.add(name);
        names.add("prelude");
        Set<Section> sectionSet = readSpec(names, null);
        sections = orderSections(sectionSet);
    }

    private void expandDefinitions() {
        Map<String, Map<String, String> > defsBySection = new HashMap<>();
        Map<String, Map<String, String> > cumDefs = new HashMap<>();
        // first collect definitions
        for (Section s : sections) {
            s.collectDefinitions();
            defsBySection.put(s.getName(), s.getDefinitions());
            cumDefs.put(s.getName(), s.getDefinitions());
        }
        // now cascade down
        for (Section s : sections) {
            Map<String, String> cumDef = cumDefs.get(s.getName());
            cumDef.putAll(defsBySection.get("prelude"));
            for (String p : s.getParents()) {
                if (cumDefs.containsKey(p))
                    cumDef.putAll(cumDefs.get(p));
            }
            cumDefs.put(s.getName(), cumDef);

            s.expandDefinitions(cumDef);
        }
    }
}
