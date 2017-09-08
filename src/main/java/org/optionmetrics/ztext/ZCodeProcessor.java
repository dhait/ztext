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

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.optionmetrics.ztext.antlr4.ZLexer;
import org.optionmetrics.ztext.antlr4.ZParser;


import java.io.IOException;
import java.io.InputStream;

/**
 * This class is responsible for parsing the Standard Z code file (generated from the Ztext markup)
 * and converting it to an AST.
 */
public class ZCodeProcessor {

    private CommonTokenStream tokens;

    public Node process(InputStream input) throws IOException {
        CharStream stream = CharStreams.fromStream(input);
        return process(stream);
    }

    public Node process(String input) {
        CharStream stream = CharStreams.fromString(input);
        return process(stream);
    }

    /**
     * Parses a stream of characterss from a Standard Z file.
     * @param stream The stream to be parsed
     * @return a Node class referencing the AST
     */
    private Node process(CharStream stream) {
        Node node = null;
        ZLexer lexer = new ZLexer(stream);
        tokens = new CommonTokenStream(lexer);
        ZParser parser = new ZParser(tokens);

        parser.removeErrorListeners();
        ZCodeErrorListener errorListener = new ZCodeErrorListener();
        parser.addErrorListener(errorListener);

        ParserRuleContext tree = parser.specification();

        if (errorListener.getErrorCount() > 0) {
            System.err.println(errorListener.getErrorCount() + " errors in Z code ");
        } else {
            node = new Node(tree, tokens);
        }
        return node;
    }
}
