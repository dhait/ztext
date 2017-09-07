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

import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import org.optionmetrics.ztext.impl.Definition;
import org.optionmetrics.ztext.impl.Formal;
import org.optionmetrics.ztext.impl.SectionHeader;

import java.util.ArrayList;
import java.util.List;

public class ZMarkupTranslator extends ZMarkupParserBaseListener {

    private final String fileName;
    private List<Paragraph> paragraphs = new ArrayList<>();
    private List<String> formals = new ArrayList<>();
    private int currentTag = -1;
    private BufferedTokenStream tokens;
    private TokenStreamRewriter rewriter;

    public ZMarkupTranslator(BufferedTokenStream tokens, String fileName) {
        this.tokens = tokens;
        this.fileName = fileName;
        rewriter = new TokenStreamRewriter(tokens);
    }

    public TokenStreamRewriter getRewriter() {
        return rewriter;
    }
    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    @Override
    public void exitSparents(ZMarkupParser.SparentsContext ctx) {
        formals.clear();
        for (ZMarkupParser.SnameContext sctx : ctx.sname()) {
            formals.add(sctx.getText());
        }
    }

    @Override
    public void exitDefine(ZMarkupParser.DefineContext ctx) {
        Definition definition  = new Definition(fileName, currentTag);
        definition.setKey(ctx.DEFSYM().getText());
        definition.setValue(ctx.zexpr(0).getText());
        paragraphs.add(definition);
    }

    public String convert(String s, boolean generic) {
        String ZED = "\u2500";
        String GEN = "\u2550";
        String AX = "\u2577";
        String SCH = "\u250c";
        String VERT = "|";
        String END = "\u2514";
        String SPOT = "\u2981";

        switch(s) {
            case "@":
                return SPOT;
            case "zed":
                return ZED;
            case "axiom":
                return generic?  AX + GEN : AX;
            case "schema":
                return generic? SCH + GEN : SCH;
            case "where":
                return VERT;
            case "end":
                return END;
            case "section":
                return ZED + "section";
        }
        return s;
    }

    @Override
    public void exitSchemaParagraph(ZMarkupParser.SchemaParagraphContext ctx) {
        String text = buildText(ctx, ctx.gen() != null);
        Formal f = new Formal(text, fileName, currentTag);
        paragraphs.add(f);
    }

    @Override
    public void exitZedParagraph(ZMarkupParser.ZedParagraphContext ctx) {
        String text = buildText(ctx, false);
        Formal f = new Formal(text, fileName, currentTag);
        paragraphs.add(f);
    }

    @Override
    public void exitAxiomParagraph(ZMarkupParser.AxiomParagraphContext ctx) {
        String text = buildText(ctx, ctx.gen() != null);
        Formal f = new Formal(text, fileName, currentTag);
        paragraphs.add(f);
    }

    private String buildText(ParserRuleContext ctx, boolean generic) {
        StringBuilder sb = new StringBuilder();
        Interval interval = ctx.getSourceInterval();
        for (int i = interval.a; i <= interval.b; i++) {
            sb.append(convert(tokens.get(i).getText(), generic));
        }
        return sb.toString();
    }

    @Override
    public void exitSectionHeader(ZMarkupParser.SectionHeaderContext ctx) {
        String text = buildText(ctx, false);
        SectionHeader s = new SectionHeader(text, fileName, currentTag);
        s.setSectionName(ctx.sname().getText());
        s.getParents().addAll(formals);
        paragraphs.add(s);
    }

    @Override
    public void exitTag(ZMarkupParser.TagContext ctx) {
        currentTag = Integer.valueOf(ctx.NUMBER().getText());
        super.exitTag(ctx);
    }

    @Override
    public void exitInformal(ZMarkupParser.InformalContext ctx) {
        System.out.println("Informal");
    }
}
