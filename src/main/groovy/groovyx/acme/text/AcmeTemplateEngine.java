package groovyx.acme.text;

import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.codehaus.groovy.control.CompilationFailedException;
import java.io.IOException;
import java.io.Reader;
import org.codehaus.groovy.runtime.IOGroovyMethods;

//@groovy.transform.CompileStatic
public class AcmeTemplateEngine extends TemplateEngine {
    String mode = "&";


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
