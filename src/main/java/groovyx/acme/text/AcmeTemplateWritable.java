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

import groovy.lang.Binding;
import groovy.lang.Script;
import groovy.lang.Writable;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.CharBuffer;
import java.util.Map;


class AcmeTemplateWritable implements Writable {

    Script script;
    Map bindMap;

    public AcmeTemplateWritable(Script scr, Map map) {
        script = scr;
        bindMap = map;
    }

    @Override
    public Writer writeTo(Writer writer) throws IOException {
        CharBuffer template = (CharBuffer) bindMap.get("template");
        bindMap.put("out", writer);
        script.setBinding(new Binding(bindMap));
        try {
            script.run();
        }catch(Throwable t){
            int line = 1;
            int pos = template.position();
            for(int i=0;i<pos;i++)if(template.get(i)=='\n')line++;
            throw new RuntimeException("Error in template at line "+line+": " + t, t);
        }
        return writer;
    }

    @Override
    public String toString() {
        try {
            return this.writeTo(new StringWriter()).toString();
        } catch (IOException e) {
            throw new RuntimeException(e.toString(),e);
        }
    }
}