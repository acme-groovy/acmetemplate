package groovyx.acme.text;

import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.codehaus.groovy.control.CompilationFailedException;
import java.io.IOException;
import java.io.Reader;
import org.codehaus.groovy.runtime.IOGroovyMethods;

//@groovy.transform.CompileStatic
public class AcmeTemplateEngine extends TemplateEngine {

    final static String MODE_ALL="&";
    final static String MODE_JSP="%";
    final static String MODE_SH="$";
    String mode = MODE_ALL;


    AcmeTemplateEngine setMode(String mode){
        this.mode=mode;
        return this;
    }

    @Override
    public Template createTemplate(String templateText) throws CompilationFailedException, ClassNotFoundException, IOException {
        return new AcmeTemplate(templateText, this.mode);
    }

    @Override
    public Template createTemplate(Reader reader) throws CompilationFailedException, ClassNotFoundException, IOException {
        return new AcmeTemplate(IOGroovyMethods.getText(reader), mode);
    }
}
