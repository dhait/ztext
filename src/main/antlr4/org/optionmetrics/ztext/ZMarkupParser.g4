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
    | TAG NUMBER                                        #Tag
    ;

zexpr : (TEXT | NAME | NUMBER | DEFSYM | COMMA | UNICODE | LBRACKET | RBRACKET ) ;

sname : NAME;

gen: (LBRACKET NAME (COMMA NAME)* RBRACKET) ;

sparents : sname (COMMA sname)* ;

