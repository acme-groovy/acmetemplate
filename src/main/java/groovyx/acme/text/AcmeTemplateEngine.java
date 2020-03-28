/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package groovyx.acme.text;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.codehaus.groovy.control.CompilationFailedException;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.codehaus.groovy.runtime.MethodClosure;

/**
 * Processes template source files substituting variables and expressions into placeholders in a template source text to produce the desired output.
 * <br>The template engine uses JSP style &lt;% %&gt; script and &lt;%= %&gt; expression syntax or GString style expressions.
 * <br>Difference from standard groovy.text.TemplateEngine
 * <br>
 * <ul>
 *     <li>template does not need to use screening for characters like {@code '$', '\', '%'}</li>
 *     <li>can parse large templates</li>
 *     <li>thread safe - the method {@code template.make(...)} could be used in parallel threads </li>
 *     <li>can be chosen the mode of parsing: using JSP like template or GString like template or both</li>
 * </ul>
 */
public class AcmeTemplateEngine extends TemplateEngine {

    public final static char MODE_ALL = '&';
    public final static char MODE_JSP = '%';
    public final static char MODE_SH  = '$';
    char mode = MODE_ALL;

    //GroovyShell groovyShell = new GroovyShell();
    ClassLoader cl = AcmeTemplateEngine.class.getClassLoader();

    /**
     * Sets mode of expressions style: JSP style or GString style or both)
     * @param mode use MODE_JSP {@code '%'} for JSP like code injection and interpolation, MODE_SH {@code '$'} for groovy/shell like interpolation, or MODE_ALL {@code '&'} to support both (default)
     * @return returns this template engine
     */
    public AcmeTemplateEngine setMode(char mode) {
        if(mode!=MODE_ALL && mode!=MODE_JSP && mode!=MODE_SH)throw new IllegalArgumentException("Unsupported mode = `"+mode+"`");
        this.mode = mode;
        return this;
    }
    /**
     * Sets mode of expressions style: JSP style or GString style or both). The same as other method but with strings (for groovy)
     * @param mode use {@code "%"} for JSP like code injection and interpolation, {@code "$"} for groovy/shell like interpolation, or {@code "&"} to support both (default)
     * @return returns this template engine
     */
    public AcmeTemplateEngine setMode(String mode) {
        if(mode==null || mode.length()!=1)throw new IllegalArgumentException("Unsupported mode = `"+mode+"`");
        return setMode(mode.charAt(0));
    }

    /**
     * Sets class loader to run template
     * @param cl class loader to be used to find classes
     * @return returns this template engine
     */
    public AcmeTemplateEngine setClassLoader(ClassLoader cl) {
        this.cl = cl;
        return this;
    }

    /**
     * Creating template with input string
     */
    @Override
    public Template createTemplate(String templateText) throws CompilationFailedException, ClassNotFoundException, IOException {
        return new AcmeTemplate(templateText);
    }

    /**
     * Creating template with reader
     */
    @Override
    public Template createTemplate(Reader reader) throws CompilationFailedException, ClassNotFoundException, IOException {
        return new AcmeTemplate(IOGroovyMethods.getText(reader));
    }


    /**
     * Parses the template and creates a result
     */
    class AcmeTemplate implements Template {

        CharBuffer template;
        Class<Script> scriptClass = null;



        public AcmeTemplate(CharSequence templateText) {
            template = CharBuffer.wrap(templateText);
        }


        private Script getScript() throws IllegalAccessException, InstantiationException, IOException {
            if (scriptClass == null) {
                Script script = parse();
                scriptClass = (Class<Script>) script.getClass();
                return script;
            } else return scriptClass.newInstance();
        }



        private final int STATE_DEF = 0;
        private final int STATE_JSP = 0;
        private final int STATE_SH  = 6;
        private Script parse() throws IOException {
            int state = 0;//0 - `<` - 1 - `%` - 2 -'%' - 3 - `>` - 1
            boolean eqFlag = false;
            StringBuffer out = new StringBuffer((int) (template.length() * 0.75));
            char[] cchars = {
                    '<', '%', '%', '>','\r','\n',
                    '$', '{', '}'};

            int index = 0;
            int start = 0; //start of the last block script or default
            out.append("/*autogenerated script start*/\n");
            for (index = 0; index < template.length(); index++) {
                char b = template.get(index);
                switch (state) {
                    case STATE_DEF: //default
                        if (b == cchars[STATE_JSP] && (mode == MODE_JSP || mode == MODE_ALL)) {
                            state = STATE_JSP+1;
                        } else if (b == cchars[STATE_SH] && (mode == MODE_SH || mode == MODE_ALL)) {
                            state = STATE_SH+1;
                        }
                        // else if( b==cchars[4] ){ state=5; } //comment to disable support of ${} expressions
                        break;
                    case STATE_JSP+1: //got `<` from default waiting for `%`
                        if (b == cchars[state]) {
                            state++;
                            if (index - 1 > start) {
                                out.append("\ntemplate.position(" + start + ");");
                                out.append("\nwrite(out, template, " + (index - 1 - start) + ");\n");
                            }
                            start = index + 1;
                        } else {
                            state = STATE_DEF; //fall back state
                        }
                        break;
                    case STATE_JSP+2: //got `%` after `<` : sctipt started from the next byte
                        //println "index=$index state=$state b=`${(char)b}`"
                        if (b == cchars[state]) {
                            state++;
                        } else {
                            if (start == index && b == (char) '=') {
                                out.append("\nwrite(out, ");
                                eqFlag = true;
                            } else {
                                out.append(b);
                            }
                        }
                        break;
                    case STATE_JSP+3: //got `%` from script waiting for `>` to go to default state
                        if (b == cchars[state]) { //got `>` after  `%` -> default state
                            state++;
                            if (index + 3 > start) { //TODO: why?
                                if (eqFlag) {
                                    state = STATE_DEF; //skip next new line char
                                    out.append(" );");
                                }
                                eqFlag = false;
                                //out.append("\ntemplateStream.skip("+(index-start+3)+");");
                            }
                            start = index + 1;
                        } else {
                            out.append('%');
                            out.append((char) b);
                            state--; // = STATE_JSP+2; //fall back state
                        }
                        break;
                    case STATE_JSP+4: //jsp finished waiting potential `\r\n` or `\n` or `\r`
                        if(b == '\r'){
                            start++; //shift start to next char
                            state++; //got `\r` expecting next `\n`
                        }else if(b == '\n'){
                            start++; //shift start to next char
                            state=STATE_DEF;
                        }else{
                            //keep current start, current char will be printed to output
                            state=STATE_DEF;
                        }
                        break;
                    case STATE_JSP+5: //jsp finished waiting potential `\r\n` or `\n` or `\r`
                        if(b == '\r'){ // `\r` 2nd time, so it's a next line
                            //keep current start, current char will be printed to output
                        }else if(b == '\n'){
                            start++; //shift start to next char
                        }else{
                            //keep current start, current char will be printed to output
                        }
                        state=STATE_DEF;
                        break;
                    case STATE_SH+1: //got '$' on previous step. waiting for '{'
                        if (b == cchars[state]) {
                            state++;
                            if (index - 1 > start) {
                                out.append("\ntemplate.position(" + start + ");");
                                out.append("\nwrite(out, template, " + (index - 1 - start) + ");\n");
                            }
                            out.append("\nwrite(out, ");
                            start = index + 1;
                        } else {
                            state = STATE_DEF; //fall back state
                        }
                        break;
                    case STATE_SH+2: //:state `${`  waiting for `}` to go to default state
                        if (b == cchars[state]) {
                            state = STATE_DEF;
                            if (index + 3 > start) {
                                out.append(" );");
                                //out.append("\ntemplateStream.skip("+(index-start+3)+");");
                            }
                            start = index + 1;
                        } else {
                            out.append((char) b);
                        }
                        break;
                }
            }
            if (state == 0) {
                if (index > start) {
                    out.append("\ntemplate.position(" + start + ");");
                    out.append("\nwrite(out, template, " + (index - start) + ");\n");
                }
            } else if(state == STATE_JSP+4 || state == STATE_JSP+5) {
                //nothing to add because that's the last new line after JSP pag close
            } else {
                throw new RuntimeException("Wrong state=" + state + " at the end of the template file. Close all expressions!");
            }
            out.append("\ntemplate.position(0);");
            out.append("\nout.flush();");
            out.append("\n/*autogenerated script end*/");
            template.position(0);
            String scriptText = out.toString();
            //System.out.println (scriptText);
            return new GroovyShell(cl).parse(scriptText, "AcmeTemplate_" + Long.toHexString(template.hashCode()) + ".groovy");
        }


        @Override
        public Writable make() {
            return make(new HashMap());
        }



        public void write(Appendable out, Object data) throws IOException {
            write(out, data, -1);
        }


        public void write(Appendable out, Object data, int chars /*-1: all*/) throws IOException {
            if (data == null || chars == 0) return;
            if (data instanceof CharSequence) {
                CharSequence cs = (CharSequence) data;
                if (chars == -1) chars = cs.length();
                if (chars == 0) return;
                out.append(cs, 0, chars);
                if (cs instanceof CharBuffer) {
                    //if it's a charbuffer move current position
                    ((CharBuffer) cs).position(((CharBuffer) cs).position() + chars);
                }
            } else {
                this.write(out, data.toString(), chars);
            }
        }


        @Override
        public Writable make(Map map) {
            Map bindMap = new LinkedHashMap();
            bindMap.putAll(map);
            bindMap.put("template", template);
            bindMap.put("write", new MethodClosure(this, "write")); //this.&write;
            Writable writable = null;
            try {
                writable = new AcmeTemplateWritable(getScript(), bindMap);
            } catch (Throwable e) {
                if(e instanceof RuntimeException)throw (RuntimeException)e;
                throw new RuntimeException("Error making template: "+e, e);
            }
            return writable;
        }
    }

}
