# ztext
Java library for parsing and rendering [Ztext](docs/index.md), a LaTeX-free markup language for Z specifications.

It provides classes for parsing Ztext to an [Antlr](http://www.antlr.org) (4.7) abstract syntax tree (AST), and rendering to HTML.

[![Build Status](https://travis-ci.org/dhait/ztext.svg?branch=master)](https://travis-ci.org/dhait/ztext)

## Example
An example of the output of the Ztext library, based on Spivey's "Birthday Book", can be found here: [BirthdayBook](https://cdn.rawgit.com/dhait/ztext/master/docs/birthday.html).

For the HTML code: [birthday.html](docs/birthday.html).

For an example of the Ztext which generated it, see [BirthdayBook.ztx](docs/birthday.md).


## Usage

The Ztext library parses a Ztext markup file to create an Abstract Syntax Tree (AST).  The AST is then passed to an HTML renderer, which creates and returns HTML to display the Z notation elements.

All ZText files must have the suffix **.zxt**.  Ztext files which contain definitions and Z notation contained in the Z Specification *"prelude"* section and the Z Standard Toolkit are incorporated into the library by default. 

The **tag** directive allows a block of Z notation to be labeled with an integer tag.  This tag can be used after rendering to associate the corresponding block of HTML to the tag.  This is useful when including the rendered Z notation as part of a larger HTML template.
 
Here is a very simple example of a Ztext file:


```Z
/* basic.zxt */
/* A given type definition */
tag 1  
zed
    [STRING]
end
  
/* a simple schema */ 
tag 2  
schema
  name : STRING
  age  : \nat
where
  age > 18
end
```

#### Parsing Ztext

In order to locate Ztext files and their dependencies (i.e., parent sections), we need to tell the Ztext library what search path to use for Ztext files.  Search paths can contain both file directories and Java resource paths.

```Java
import org.optionmetrics.ztext.TextParser;
import org.optionmetrics.ztext.SearchPath;
    
class MyApp {    
    
    public static void main() {
    
        TextParser parser = new TextParser();
        parser.getSearchPath().addItem(SearchPath.SourceType.DIRECTORY, "/path/to/my/ztx_files");
``` 

Once the search path has been set up, we can parse the Ztext file.  The "*.zxt*" suffix is omitted when specifying the file:
```Java
        Node specification = parser.parse("basic");

```

#### Rendering Ztext to HTML

The AST contained in the `Node` object is rendered to HTML using the `HTMLRenderer` class.  The *render* method returns a mapping from tags to HTML segments.  If no tags have been included in the Ztext markup file, the map contains only one entry (corresponding to the tag -1):
```Java
        HtmlRenderer renderer = new HtmlRenderer();
        Map<Integer, String> html = renderer.render(specification);
        
        ...
        
        /* let's print the HTML blocks */
        
        html.forEach((k,v)->System.out.println(v));
    }
```

#### API documentation

Javadocs are available online [here](docs/apidocs/index.html).

## HTML Styles

The following CSS classes are used in the rendering of the HTML for the Z specifications:

**z-text** -- used for non-schema and non-axiom Z notation paragraphs
```html
<div class="z-text">
[NAME, ZIPCODE]
</div>
```
**z-axiom** -- used for Z axiom paragraphs.  The **z-decl** and **z-pred** classes are used to mark the declaration and predicate portions of the axiom.
```html
<div class="z-axiom">
    <div class="z-decl">
        x : X
    </div>
    <div class="z-pred">
        x > 17
    </div>
</div>
``` 

**z-schema** -- used for Z schemas (again along with the **z-decl** and **z-pred** classes).
```html
<div class="z-schema">
    <div class="z-name" >
        BirthdayBook
    </div>
    <div class="z-decl">
        known : ℙ NAME <br/>
        birthday : NAME ⇸ DATE
    </div>
    <div class="z-pred">
        known = dom birthday
    </div>
</div>
``` 

A basic stylesheet which renders the Z notation properly on a browser can be found at [default.css](docs/default.css).

## Langage Specification
For a description of the Ztext markup language, please see the [Ztext](docs/index.md) documentation.

## Roadmap
* User specification of HTML classes
* Z type checking

## Contributing

Pull requests are welcome. Please add tests and document clearly.  Opening an issue describing the problem or feature addressed by the request would be appreciated.

## Acknowledgements

The Ztext library uses Antlr4 (www.antlr4.org) for Z language parsing.

A special thank you to Adam Taylor for the Antlr4 Z Language Lexer and Parser (https://github.com/antlr/grammars-v4/tree/master/z).

## License

Copyright (C) 2017 by David J Hait.
Portions copyrighted by their contributors and governed by their respective licenses.

Licensed under the 3-Clause BSD license.  Please see [LICENSE](LICENSE) file.


