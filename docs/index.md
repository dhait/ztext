# Ztext
## A text-based markup for Z notation

Z notation is a language for writing formal specifications of computer systems (see the article [Z_notation](https://en.wikipedia.org/wiki/Z_notation) in Wikipedia).
The Z notation has been standardized as [ISO/IEC 13568:2002](http://www.iso.ch/iso/en/CatalogueDetailPage.CatalogueDetail?CSNUMBER=21573) and is publically available.

Z notation requires an extensive set of UNICODE symbols for its rendering.  Because of this, a markup language is the preferred method of writing Z specifications.  The most common markup language for Z specification is LaTex.

Ztext is an alternative markup scheme for Z notation, which like LaTex uses text-based symbols for representing Z notation symbols, but does not require LaTex.  The Ztext markup language is intended for rendering in HTML, either independently or for inclusion in Markdown.  The HTML rendering can be converted to PDF by any one of a number of HTML to PDF converters (such as wkhtmltopdf).

# Table of contents
* [Language elements](#language-elements)
* [Language structure](#language-structure)
  * [Sections](#sections)
  * [Paragraphs](#paragraphs) 
  * [Definitions](#definitions)
  * [Tags](#tags)
* [Predefined symbols](#predefined-symbols)

## Language elements


## Language structure

The Z notation consists of a series of paragraphs, each paragraph beginning with an optional section header.  The section header, in addition to providing the name of the section, also lists any of the section's dependencies.  The details of sections and paragraphs are described in the Z specification; here we only present their syntax in ZText.

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
tag 3
```

The tag must be an integer greater than or equal to zero.  A tag has no effect on the Z specification in which it is included, and is not interpreted.

## Predefined symbols
The following symbols are predefined in the standard_toolkit:



