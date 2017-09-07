/*
 * [The "BSD licence"]
 * Copyright (c) 2017 David J Hait
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products
 *      derived from this software without specific prior written permission.
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
 *
 */

package org.optionmetrics.ztext;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.omg.IOP.TAG_CODE_SETS;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ZRendererListenerImpl extends ZParserBaseListener {

    private final CommonTokenStream tokens;
    private Map<Integer, StringBuilder> blockMap = new TreeMap<>();
    private int currentTag = -1;

    public ZRendererListenerImpl(CommonTokenStream tokens) {
        this.tokens = tokens;
    }
    @Override
    public String toString() {
        //return builder.toString();
        return "";
    }

    public Map<Integer, StringBuilder> getBlockMap() {
        return blockMap;
    }

    @Override
    public void exitInformal(ZParser.InformalContext ctx) {
        // informal paragraph
        StringBuilder builder = getBuilder();
        builder.append("<p>");
        for (TerminalNode t : ctx.TEXT()) {
               builder.append(t.getText());
        }
        builder.append("</p>\n");
    }

    @Override
    public void exitGivenTypesParagraph(ZParser.GivenTypesParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitHorizontalDefinitionParagraph(ZParser.HorizontalDefinitionParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitGenericHorizontalDefinitionParagraph(ZParser.GenericHorizontalDefinitionParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitGenericOperatorDefinitionParagraph(ZParser.GenericOperatorDefinitionParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitFreeTypesParagraph(ZParser.FreeTypesParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitConjectureParagraph(ZParser.ConjectureParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitGenericConjectureParagraph(ZParser.GenericConjectureParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitOperatorTemplateParagraph(ZParser.OperatorTemplateParagraphContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitBaseSection(ZParser.BaseSectionContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitInheritingSection(ZParser.InheritingSectionContext ctx) {
        exitZedRule(ctx);
    }

    @Override
    public void exitAxiomaticDescriptionParagraph(ZParser.AxiomaticDescriptionParagraphContext ctx) {
        StringBuilder builder = getBuilder();
        builder.append("<div class=\"z-axiom\">\n");
        if (ctx.schemaText().declPart() != null) {
            builder.append("<div class=\"z-axiom-decl\">\n");
            int start = ctx.schemaText().declPart().start.getTokenIndex();
            int stop = ctx.schemaText().declPart().stop.getTokenIndex();
            appendTokens(start, stop);
            builder.append("</div>\n");
        }
        if (ctx.schemaText().predicate() != null) {
            builder.append("<div class=\"z-axiom-pred\">\n");
            int start = ctx.schemaText().predicate().start.getTokenIndex();
            int stop = ctx.schemaText().predicate().stop.getTokenIndex();
            appendTokens(start, stop);
            builder.append("</div>\n");
        }
        builder.append("</div>\n");
    }

    private StringBuilder getBuilder() {
        StringBuilder builder;
        if (blockMap.containsKey(currentTag)) {
            builder = blockMap.get(currentTag);
        }
        else {
            builder = new StringBuilder();
            blockMap.put(currentTag, builder);
        }
        return builder;
    }

    private void appendTokens(int start, int stop) {
        StringBuilder builder = getBuilder();
        for (int i = start; i <= stop; i++) {
            Token t = tokens.get(i);
            if (t.getText().equals("\n")) {
                builder.append("<br/>\n");
            } else {
                // HACK: to make it look nicer
                builder.append(t.getText()).append(" ");
            }
        }
    }


    public void exitZedRule(ParserRuleContext ctx) {
        StringBuilder builder = getBuilder();
        builder.append("<div class=\"z-text\">\n");
        int start = ctx.start.getTokenIndex();
        int stop = ctx.stop.getTokenIndex();
        for (int i = start+1; i <= stop-1; i++) {
            Token t = tokens.get(i);
            if (t.getText().equals("\n")) {
                builder.append("<br/>\n");
            }
            else if (t.getChannel() != Token.HIDDEN_CHANNEL){
                // HACK: to make it look nicer
                builder.append(t.getText()).append(" ");
            }
        }
        builder.append("</div>\n");
    }

    @Override
    public void exitSchemaDefinitionParagraph(ZParser.SchemaDefinitionParagraphContext ctx) {
        StringBuilder builder = getBuilder();
        builder.append("<div class=\"z-schema\">\n");
        builder.append("\t<div class=\"z-name\" >\n");
        builder.append("\t").append(ctx.NAME().getText()).append("\n");
        builder.append("\t</div>\n");
        if (ctx.schemaText().declPart() != null) {
            builder.append("<div class=\"z-schema-decl\">\n");
            int start = ctx.schemaText().declPart().start.getTokenIndex();
            int stop = ctx.schemaText().declPart().stop.getTokenIndex();
            appendTokens(start, stop);
            builder.append("</div>\n");
        }
        if (ctx.schemaText().predicate() != null) {
            builder.append("<div class=\"z-schema-pred\">\n");
            int start = ctx.schemaText().predicate().start.getTokenIndex();
            int stop = ctx.schemaText().predicate().stop.getTokenIndex();
            appendTokens(start, stop);
            builder.append("</div>\n");
        }
        builder.append("</div>\n");
    }

    @Override
    public void exitAttribute(ZParser.AttributeContext ctx) {
        if (ctx.attType().getText().equals("Tag")) {
            currentTag = Integer.valueOf(ctx.ADIGIT().toString());
        }
        super.exitAttribute(ctx);
    }


}
