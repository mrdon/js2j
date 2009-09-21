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

public class WrappersTest extends TestScript {

    public void testMapIndex() throws Exception {
        script += "map = new java.util.HashMap();\n";
        script += "map.put(\"foo\", \"bar\");\n";
        run(script);
        
        test("map[\"foo\"]", "bar");
        test("map.foo", "bar");
        test("map.get(\"foo\")", "bar");
    }
    
    public void testMapFuncPropCollide() throws Exception {
        script += "map = new java.util.HashMap();\n";
        script += "map.put(\"foo\", \"bar\");\n";
        script += "map.put(\"size\", \"100\");\n";
        run(script);
        
        test("map.size()", "2");
        test("map.get(\"size\")", "100");
    }
    
    public void testBeanIndex() throws Exception {
        script += "bean = new Packages.org.apache.commons.beanutils.LazyDynaBean();\n";
        script += "bean.set(\"foo\", \"bar\");\n";
        script += "bean[\"jim\"] = \"bar\";\n";
        script += "bean.sara = 'friend';\n";
        run(script);
        
        test("bean['foo']", "bar");
        test("bean.jim", "bar");
        test("bean.get('sara')", "friend");
        
        script =  "jimFound = false;\n";
        script += "for (x in bean) \n";
        script += "if (x == 'jim') jimFound = true;\n";
        run(script);
        test("jimFound", "true");
    }
    
    public void testDynaBeanFuncPropCollide() throws Exception {
        script += "bean = new Packages.org.apache.commons.beanutils.LazyDynaBean();\n";
        script += "bean.set('foo', 'bar');\n";
        script += "bean.set('get', '100');\n";
        run(script);
        
        test("bean.get('foo')", "bar");
        test("bean.get('get')", "100");
    }
    
    public void testListIndex() throws Exception {
        script += "list = new java.util.ArrayList();\n";
        script += "list.add('foo');\n";
        script += "list.add('bar');\n";
        run(script);
        
        test("list[0]", "foo");
    }
    
    public void testListForIn() throws Exception {
        script += "list = new java.util.ArrayList();\n";
        script += "list.add('foo');\n";
        script += "list.add('bar');\n";
        
        script += "pass = 0;\n";
        script += "for (x in list) {\n";
        script += "    if (x == 0) pass += (list[x] == 'foo');\n";
        script += "    if (x == 1) pass += (list[x] == 'bar');\n";
        script += "}";
        run(script);
        
        test("pass", "2.0");
    }
    
}
