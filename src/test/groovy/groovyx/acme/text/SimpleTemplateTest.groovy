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
package groovyx.acme.text

class SimpleTemplateTest extends GroovyTestCase {
	int runCount = 5000;



	/*
	public void testLoadSimpleEngine(){
		def te = new groovy.text.SimpleTemplateEngine();
		for(int i=0; i<runCount; i++) {
			def t = te.createTemplate("myParm1 = <%=myParm1%>; myParm2 = <%=myParm2%>");
			def wr = t.make(myParm1: i, myParm2: 'sss').toString();
		}
	}
	*/



	/*
	public void testLoadSimpleAllFile() {
		for(int i=0; i<1000; i++) {
			def te = new groovy.text.SimpleTemplateEngine();
			def t = te.createTemplate(new File("C:\\Users\\madch\\work\\projects\\acmetemplate\\src\\test\\groovy\\groovyx\\acme\\text\\small_text.txt"));
			def wr = t.make(Par1: 111, Par2: 'sss', Par3: 222, Par4: 'aaa', Par5: 444, Par6: 'ccc').toString()
		}
	}
	*/


	/*
	public void testLoadSimpleEngineFile() {
		sleep(30000)
		println "Simple\n"
		def te = new groovy.text.SimpleTemplateEngine();
		for(int i=0; i<60000; i++) {
			def t = te.createTemplate(new File("C:\\Users\\madch\\work\\projects\\acmetemplate\\src\\test\\groovy\\groovyx\\acme\\text\\small_text.txt"));
			def wr = t.make(Par1: i, Par2: 'sss', Par3: 222, Par4: 'aaa', Par5: 444, Par6: 'ccc').toString()
		}
	}
	*/

}
