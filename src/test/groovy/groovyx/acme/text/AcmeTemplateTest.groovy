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


	public void testAcmeModeJSP2(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('myParm1 = <%= myParm1%1>0?"titanic":"all good"  %>; myParm2 = ${myParm2}');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='myParm1 = all good; myParm2 = ${myParm2}'
	}

	public void testAcmeModeJSP_CRCR(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\r\r789');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456\r789'
	}

	public void testAcmeModeJSP_CRLF(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\r\n789');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456789'
	}

	public void testAcmeModeJSP_LFLF(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\n\n789');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456\n789'
	}

	public void testAcmeModeJSP_CRX(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\r789');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456789'
	}

	public void testAcmeModeJSP_LFX(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\n789');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456789'
	}

	public void testAcmeModeJSP_LFatEOF(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\n');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456'
	}

	public void testAcmeModeJSP_CRatEOF(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('123<% out<<"456" %>\r');
		def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
		assert wr=='123456'
	}

	public void testAcmeModeJSP_LF_1(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('<% out.print("hello") %>\n <%= name %>')
		def wr = t.make(name:'world').toString()
		assert wr=='hello world'
	}

	public void testAcmeModeJSP_LF_2(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('<%= "hello" %>\n <%= name %>')
		def wr = t.make(name:'world').toString()
		assert wr=='hello\n world'
	}

	public void testAcmeModeJSP_LF_3(){
		def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
		def t = te.createTemplate('''NAMES:
<% names.eachWithIndex{n,i-> %>
<%= i %> - <%= n %>
<% } %>
''')
		def wr = t.make(names: ['John','Paul','Jones'] ).toString()
		assert wr=='NAMES:\n0 - John\n1 - Paul\n2 - Jones\n'
	}

	public void testLoadAcmeEngineFile() {
		def te = new AcmeTemplateEngine();
		for(int i=0; i<runCount; i++) {
			def t = te.createTemplate(new File("./src/test/groovy/groovyx/acme/text/small_text.txt"));
			def wr = t.make(Par1: i, Par2: 'sss', Par3: 222, Par4: 'aaa', Par5: 444, Par6: 'ccc').toString()
		}
	}

	public void testFailure(){
		try {
			def te = new AcmeTemplateEngine().setMode(AcmeTemplateEngine.MODE_JSP);
			def t = te.createTemplate('\nmyParm1 = \n<%=myParm1.XYZ()%> <%=new Date()%>   ');
			def wr = t.make(myParm1: 111, myParm2: 'sss').toString()
			assert wr == 'myParm1 = 111; myParm2 = ${myParm2}'
		}catch(Throwable t){
			assert t.getMessage().indexOf("at line 3")>-1
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
