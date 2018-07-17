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
	
	public void test1(){
		def tpl = new ReaderTemplate(" myParm1 = <%=myParm1%>; myParm2 = <%=myParm2%>")
		def s = tpl.make( out: new StringWriter(), myParm1: 111, myParm2:'sss' ).toString()
		assert s==" myParm1 = 111; myParm2 = sss";
	}

	public void test2(){
		def te = new groovy.text.SimpleTemplateEngine();
		def t = te.createTemplate("myParm1 = <%=myParm1%>; myParm2 = <%=myParm2%>");
		def wr = t.make(myParm1: 111, myParm2:'sss').toString();
		assert wr=="myParm1 = 111; myParm2 = sss";
	}

	public void test3(){
		def te = new AcmeTemplateEngine();
		def t = te.createTemplate("myParm1 = <%=myParm1%>; myParm2 = <%=myParm2%>");
		def wr = t.make1(myParm1: 111, myParm2:'sss').toString()
		println wr;
	}
}
