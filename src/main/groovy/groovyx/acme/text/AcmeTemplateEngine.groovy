package groovyx.acme.text

import groovy.text.Template
import groovy.text.TemplateEngine
import org.codehaus.groovy.control.CompilationFailedException

@groovy.transform.CompileStatic
public class AcmeTemplateEngine extends TemplateEngine {

    @Override
    Template createTemplate(Reader reader) throws CompilationFailedException, ClassNotFoundException, IOException {
        return new AcmeTemplate(reader.getText(), '<');
    }
}
