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

parser grammar ZMarkupParser;
options { tokenVocab=ZMarkupLexer; }

specification
	: paragraph* EOF
	;

paragraph
    : DEFINE DEFSYM zexpr* END                          #Define
    | ZED zexpr* END                                    #ZedParagraph
    | AXIOM gen? zexpr* (WHERE zexpr*)? END             #AxiomParagraph
    | SCHEMA sname gen? zexpr* (WHERE zexpr*)? END      #SchemaParagraph
    | SECTION sname (PARENTS sparents)? END             #SectionHeader
    | TAG NUMBER END                                    #Tag
    | IGNORE+                                           #Informal
    ;

zexpr : (TEXT | NAME | NUMBER | DEFSYM | COMMA | UNICODE | LBRACKET | RBRACKET ) ;

sname : NAME;

gen: (LBRACKET NAME (COMMA NAME)* RBRACKET) ;

sparents : sname (COMMA sname)* ;

