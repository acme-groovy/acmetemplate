# AcmeTemplateEngine
Processes template source files substituting variables and expressions into placeholders in a template source text to produce the desired output.
The template engine uses JSP style <% %> script and <%= %> expression syntax or GString style expressions.

## Grab it

```groovy
@Grab(group='acme.groovy', module='acmetemplate', version='20200328', transitive=false)
import groovyx.acme.text.AcmeTemplateEngine
```


## The goal
Parse large templates without double character escaping with a possibility to choose a kind of expressions: JSP style or GString or both of them.



## Difference from standard groovy.text.TemplateEngine
- template does not need to use escaping for characters like `'$'`, `'\'`, `'%'`
- can parse large templates
- thread safe: the parsed template could be used in multiple threads
- can be chosen the mode of parsing: using JSP like template or GString like template or both
- keeps in result the original line endings (LF or CRLF) you used in the template
- JSP-like code injections suppresses following EOL to keep templates more structured (see examples below)

## Details
public class AcmeTemplateEngine
extends TemplateEngine

Very similar to SimpleTemplateEngine but better:)

## Examples

### Simple example of using with template in string
```groovy
def te = new AcmeTemplateEngine()
def t = te.createTemplate("myParm1 = <%=myParm1%>; myParm2 = ${myParm2}")
def wr = t.make(myParm1: i, myParm2: 'sss').toString()
assert wr=='myParm1 = 111; myParm2 = sss'
```
### Setting a mode of parsing
```groovy
def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_SH);
def t = te.createTemplate('myParm1 = <%=myParm1%>; myParm2 = ${myParm2}');
def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
assert wr=='myParm1 = <%=myParm1%>; myParm2 = sss'
```
### Parsing from a file
```groovy
def te = new AcmeTemplateEngine()
def t = te.createTemplate(new File("filename.txt"))
def wr = t.make(Par1: 111, Par2: 'sss').toString()
assert wr=='myParm1 = 111; myParm2 = sss'
```
### The end of line after JSP code injection
the end of line is ignored after JSP code injection 
```groovy
def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
def t = te.createTemplate('<% out.print("hello") %>\n <%= name %>')
def wr = t.make(name:'world').toString()
assert wr=='hello world'
```
however kept after interpolation
```groovy
def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
def t = te.createTemplate('<%= "hello" %>\n <%= name %>')
def wr = t.make(name:'world').toString()
assert wr=='hello\n world'
```
the goal to keep templates more structured when code injection used
```groovy
def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
def t = te.createTemplate('''NAMES:
<% names.eachWithIndex{n,i-> %>
 <%= i %> - <%= n %>
<% } %>
''')
def wr = t.make(names: ['John','Paul','Jones'] ).toString()
new File('delme').setText(wr,"UTF-8")
assert wr=='NAMES:\n 0 - John\n 1 - Paul\n 2 - Jones\n'
```