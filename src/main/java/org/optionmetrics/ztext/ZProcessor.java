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

import java.io.IOException;

public class ZProcessor {

    private ZMarkupProcessor zMarkupProcessor = new ZMarkupProcessor();

    public ZProcessor() {
        zMarkupProcessor = new ZMarkupProcessor();
        SearchPath searchPath = new SearchPath();
        searchPath.addItem(SearchPath.SourceType.RESOURCE_PATH, "/toolkit");
        zMarkupProcessor.setSearchPath(searchPath);
    }

    public SearchPath getSearchPath() {
        return zMarkupProcessor.getSearchPath();
    }

    public Node parse(String text) throws IOException, SectionDependencyException {
        // phase 1 - parse the markup
        String zcode = zMarkupProcessor.process(text);
        // phase 2 - parse the zcode
        return new Node();
    }


}