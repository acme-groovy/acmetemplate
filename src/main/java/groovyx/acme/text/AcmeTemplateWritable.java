package groovyx.acme.text;

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.lang.Writable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

//@groovy.transform.CompileStatic
class AcmeTemplateWritable implements Writable {

    Script script; /**Script returns result text*/
    Map bindMap; /**Parameters to fill in placeholders*/

    public AcmeTemplateWritable(Script scr, Map map) {
        script = scr;
        bindMap = map;
    }

    @Override
    public Writer writeTo(Writer writer) throws IOException {
        bindMap.put("out", writer);
        script.setBinding(new Binding(bindMap));
        script.run();
        return writer;
    }

    @Override
    public String toString() {
        try {
            return this.writeTo(new StringWriter()).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}