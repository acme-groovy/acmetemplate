package groovyx.acme.text

@groovy.transform.CompileStatic
class AcmeTemplateWritable implements Writable {

    Script script;
    Map bindMap;

    public AcmeTemplateWritable(Script scr, Map map) {
        script = scr;
        bindMap = map;
    }

    @Override
    Writer writeTo(Writer writer) throws IOException {
        //Appendable out  = bindMap.get("out");
        //if(out==null)out=new StringWriter();
        //if ( !(out instanceof java.io.Writer) && !(out instanceof java.io.PrintStream) ){ throw new RuntimeException("Binding parameter `out` should be instance of java.io.Writer or java.io.PrintStream"); }
        bindMap.put("out", writer);
        script.setBinding(new Binding(bindMap));
        try {
            script.run();
        } catch (Throwable e) {
            //println "Failed script:\n$scriptText";
            throw e;
        }
        return writer;
    }

    @Override
    String toString() {
        return this.writeTo(new StringWriter()).toString()
    }
}