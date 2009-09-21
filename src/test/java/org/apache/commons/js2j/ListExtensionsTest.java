/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.js2j;

public class ListExtensionsTest extends TestScript {

    public void setUp() throws Exception {
        super.setUp();
        run("this.list = new java.util.ArrayList();");
        run("this.list.add('foo');");
        run("this.list.add('bar');");
    }

    public void testEach() throws Exception {
        run("foo = false;");
        run("bar = false;");
        run("list.each(function(val) { eval(val+' = true;');});");
        
        test("foo == true && bar == true", "true");
    }

    public void testAsImmutable() throws Exception {
        run("lst = list.asImmutable();");
        run("passed = false;");
        run("try {lst.add('foo'); }catch (e) {passed = true;}");
        
        test("passed", "true");
    }

    public void testPop() throws Exception {
        run("val = list.pop();");
        
        test("val", "bar");
        test("list.size()", "1");
    }

    public void testSort() throws Exception {
        run("list.sort();");
        
        test("list[0]", "bar");
    }

    public void testSortEach() throws Exception {
        script += "list.sortEach(function(val1, val2) {";
        script += "  if (val1 == 'bar') return -1;";
        script += "  else return 1;";
        script += " });";
        run(script);
        
        test("list[0]", "bar");
    }

    public void testLength() throws Exception {
        test("list.length", "2");
    }

    public void testFind() throws Exception {
        run("result = list.find(function(val) { return (val == 'bar' ? val : null);});");
        
        test("result", "bar");
    }

    public void testFindAll() throws Exception {
        run("list.add('bar');");
        run("result = list.findAll(function(val) { return (val == 'bar' ? val : null);});");
        
        test("result.size()", "2");
    }
}
