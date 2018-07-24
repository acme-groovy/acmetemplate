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

import junit.framework.TestResult;
import junit.framework.TestCase;

class AcmeTemplateTest extends GroovyTestCase {
	int runCount = 50;



	public void testLoadAcmeEngine(){
		def te = new AcmeTemplateEngine();
		for (int i=0; i<runCount; i++) {
			def t = te.createTemplate("myParm1 = <%=myParm1%>; myParm2 = <%=myParm2%>");
			def wr = t.make(myParm1: i, myParm2: 'sss').toString()
		}
	}




	public void testAcmeModeSH(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_SH);
		def t = te.createTemplate('myParm1 = <%=myParm1%>; myParm2 = ${myParm2}');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='myParm1 = <%=myParm1%>; myParm2 = sss'
	}


	public void testAcmeModeJSP(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('myParm1 = <%=myParm1%>; myParm2 = ${myParm2}');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='myParm1 = 111; myParm2 = ${myParm2}'
	}




	public void testLoadAcmeEngineFile() {
		def te = new AcmeTemplateEngine();
		for(int i=0; i<runCount; i++) {
			def t = te.createTemplate(new File("C:\\Users\\madch\\work\\projects\\acmetemplate\\src\\test\\groovy\\groovyx\\acme\\text\\small_text.txt"));
			def wr = t.make(Par1: i, Par2: 'sss', Par3: 222, Par4: 'aaa', Par5: 444, Par6: 'ccc').toString()
		}
	}

	/*
	public void testLoadAcmeEngineThreads(){
		def thread1 = new Thread({
			def te = new AcmeTemplateEngine().setMode(MODE_SH);
			def t = te.createTemplate('myParm1 = <%=myParm1%>; myParm2 = ${myParm2}');
			def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
			assert wr=='myParm1 = <%=myParm1%>; myParm2 = sss'
		})
		def thread2 = new Thread({
			def te = new AcmeTemplateEngine().setMode(MODE_JSP);
			def t = te.createTemplate('myParm1 = <%=myParm1%>; myParm2 = ${myParm2}');
			def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
			assert wr=='myParm1 = 111; myParm2 = ${myParm2}'
		})

	}
	*/


}
