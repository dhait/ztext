# Ztext
## A text-based markup for Z notation

Z notation is a language for writing formal specifications of computer systems (see the article [Z_notation](https://en.wikipedia.org/wiki/Z_notation) in Wikipedia).
The Z notation has been standardized as [ISO/IEC 13568:2002](http://www.iso.ch/iso/en/CatalogueDetailPage.CatalogueDetail?CSNUMBER=21573) and is publically available.

Z notation requires an extensive set of UNICODE symbols for its rendering.  Because of this, a markup language is the preferred method of writing Z specifications.  The most common markup language for Z specification is LaTex.

Ztext is an alternative markup scheme for Z notation, which like LaTex uses text-based symbols for representing Z notation symbols, but does not require LaTex.  The Ztext markup language is intended for rendering in HTML, either independently or for inclusion in Markdown.  The HTML rendering can be converted to PDF by any one of a number of HTML to PDF converters (such as wkhtmltopdf).

# Table of contents
* [Language elements](#language-elements)
* [Language structure](#language-structure)
  * [Comments](#comments)
  * [Sections](#sections)
  * [Paragraphs](#paragraphs) 
  * [Definitions](#definitions)
  * [Tags](#tags)
* [Predefined symbols](#predefined-symbols)

## Language elements

To use Ztext to create a Z language specifications, you can use standard text characters to denote elements of the Z language.  Schema, axioms, and other Z language structures are specified by using keywords defined in Ztext.  In addition, special Unicode characters which are used within the Z language but which are not on the standard keyboard can be defined using a special **define** directive and referenced within the Ztext document.  Characters which are defined in this way always begin with a backslash (\\).

A Ztext document contains only elements of the Z language; it cannot contain "informal" (non-Z) text unless it is part of a comment (prefixed or surrounded by comment characters).

## Language structure

The Z notation consists of a series of paragraphs, each paragraph beginning with an optional section header.  The section header, in addition to providing the name of the section, also lists any of the section's dependencies.  The details of sections and paragraphs are described in the Z specification; here we only present their syntax in ZText.

### Comments
Comments in Ztext can be either surrounded by comment characters ("/*" and "*/"), or on a single line prefixed by the comment prefix "//".

```Z
/* This is a comment in Ztext */
zed [APPLE] end
// This is also a comment
```

### Sections
A Z notation section begins with the **section** keyword:

```Z
section mysection end
section yoursection parents mysection, standard_toolkit end
```

The **section** keyword has an optional "parents" clause, which lists the parent sections of the declared section.

### Paragraphs

A Z notation paragraph can be one of the following:
1. Given type
2. Axiomatic description 
3. Schema definition
4. Horizontal description
5. Free type
6. Conjecture
7. Operator template

Each of these is described below.

#### Given Type
A given type is specified using the **zed** keyword:
```Z
zed [CHAR,DATE] end
```
#### Axiomatic Description
An axiomatic description is specified using the **axiom** keyword:
```Z
axiom limit : \nat
where
limit \leq 65535
end
```
A generic axiomatic description uses the following syntax:

```Z
axiom [X,Y]
    first : X \cross Y \func Y
where
    \forall x : X, y : Y @ first(x,y) = x
end
```

#### Schema Definition
A schema definition is specified using the **schema** keyword:
```Z
schema Aleph
    x,y : \int
where
    x \lt y
end
 ```
 
A generic schema definition uses the following syntax:
```Z
schema State[X]
    state : X
end
```

#### Horizontal description
A horizontal description is specified as follows:
```Z
zed
    RAddBirthday == (AddBirthday \and Success) \or AlreadyKnown
end
```
#### Free type
A free type is specified as follows:

```Z

zed
    REPORT ::= ok | already_known | not_known
end
```
A free type may have an injected expression:
```Z
zed
    TREE ::= tip | fork  \nat \cross TREE \cross TREE
end
```

#### Conjecture
A conjecture is specified as follows:

```Z
zed conjecture
    \forall code : CODES @ code > 3
end
```

A conjecture may also be generic by appending [*parameter*] after the **conjecture** keyword.

#### Operator template

A relation, function, or generic operator template can be specified as follows:
```Z
zed function 50 leftassoc (_ \oplus _) end
```
Functions and relations which are infix require a precedence and an associative setting.

The function is defined by a generic axiom:
```Z
axiom [X,Y]
_ \oplus _ : ( X \rel Y ) \cross ( X \rel Y)
where
\forall r,s : X \rel Y @ r \oplus s = ((dom s) \ndres r) \cup s
end
```

### Definitions

A definition statement is used to define a new Unicode character in Ztext 
(This is analogous to the ***Zchar*** directive used within the LaTeX based markup).  A definition statement is specified as follows:

```Z
define \in \u2208 end
```
The defined value must begin with a backslash (\\) character.  The value to which it corresponds may be any string, and may include a Unicode character, which is specified by the characters \\u followed by 4 hexadecimal digits.

### Tags

A tag statement is used to tag a specific paragraph, so that it can be assigned a reference (for inclusion in an external markdown file, for example). A tag is specified as follows:

```Z
tag 3 end
```

The tag must be an integer greater than or equal to zero.  A tag has no effect on the Z specification in which it is included, and is not interpreted.

## Predefined symbols
The following Z notation symbols are predefined in the standard_toolkit:

```Z
section number_toolkit end

define \num \u2124  end
define \negate \u002d end
define \leq \u2264 end
define \geq \u2265 end

section set_toolkit end

define \rel      \u2194 end
define \fun      \u2192 end
define \neq      \u2260 end
define \notin    \u2209 end
define \emptyset \u2205 end
define \subseteq \u2286 end
define \subset   \u2282 end
define \cup      \u222A end
define \cap      \u2229 end
define \symdiff  \u2296 end
define \bigcup   \u22C3 end
define \bigcap   \u22C2 end
define \finset   \ud835\udd32
end

section relation_toolkit parents set_toolkit end

define \mapsto \u21a6 end
define \comp \u2a3e end
define \circ \u2218 end
define \dres \u25c1 end
define \rres \u25b7 end
define \ndres \u2a64 end
define \nrres \u2a65 end
define \inv \u223c end
define \limg \u2987 end
define \rimg \u2988 end
define \oplus \u2295 end
define \plus + end
define \star * end

section function_toolkit parents relation_toolkit end

define \pfun \u21f8 end
define \pinj \u2914 end
define \inj \u21a3 end
define \psurj \u2900 end
define \surj \u21a0 end
define \bij \u21f8 end
define \finj \u2915 end

section sequence_toolkit parents function_toolkit, number_toolkit end

define \upto \u002e\u002e end
define \langle \u3008 end
define \rangle \u3009 end
define \cat \u2040 end
define \extract \u21bf end
define \filter \u21be end
define \dcat \u2040 end
```

