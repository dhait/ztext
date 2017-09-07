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

lexer grammar ZMarkupLexer;

COMMENT : '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN);

ZED : 'zed' ;
WHERE : 'where';
SECTION : 'section' ;
PARENTS : 'parent' 's'? ;
AXIOM : 'axiom' ;
SCHEMA : 'schema' ;
DEFINE : 'define' ;
END : 'end' ;
TAG : 'tag' ;
COMMA : ',';
LBRACKET : '[';
RBRACKET : ']';
UNICODE : ('\\u' HEX HEX HEX HEX)+;
DEFSYM : '\\' ~[ \t\r\n,]+ ;
NAME   : WORD STROKE*;
NUMBER : DIGITS+;
WS : [ \t\r\n] -> channel(HIDDEN);
TEXT : .+?;

fragment
HEX : [a-zA-Z0-9];


fragment
STROKE : ['?!];

fragment
WORDPART : ALPHA ALPHANUM* ;

fragment
WORD : WORDPART ('_' WORDPART)* ;

fragment
ALPHA : [a-zA-Z];

fragment
DIGITS : [0-9];

fragment
ALPHANUM : ALPHA | DIGITS ;