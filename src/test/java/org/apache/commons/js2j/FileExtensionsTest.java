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

public class FileExtensionsTest extends TestScript {

    public void setUp() throws Exception {
        super.setUp();
        run("file = java.io.File.createTempFile('test', '.tmp');");
    }
    
    public void testAppend() throws Exception {
        run("file.append('this is a test');");
        run("txt = new java.io.BufferedReader(new java.io.FileReader(file)).readLine();");
        
        test("txt", "this is a test");
    }
    
    public void testCopy() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('this is a test');");
        run("writer.close();");
        run("tfile = file.copy(java.io.File.createTempFile('test', '.tmp').toString());");
        run("txt = new java.io.BufferedReader(new java.io.FileReader(tfile)).readLine();");
        
        test("txt", "this is a test");

        run("tfile = file.copy(java.io.File.createTempFile('test', '.tmp').toString());");
        run("txt = new java.io.BufferedReader(new java.io.FileReader(tfile)).readLine();");
        test("txt", "this is a test");
        
        run("tfile = file.copy(new String(java.io.File.createTempFile('test', '.tmp').toString()));");
        run("txt = new java.io.BufferedReader(new java.io.FileReader(tfile)).readLine();");
        test("txt", "this is a test");
    }
    
    public void testReplace() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('this is a test');");
        run("writer.close();");
        run("tfile = file.replace(/this/, 'foo');");
        run("txt = new java.io.BufferedReader(new java.io.FileReader(tfile)).readLine();");
        
        test("txt", "foo is a test");
        
        run("tfile = file.replace('foo', 'this');");
        run("txt = new java.io.BufferedReader(new java.io.FileReader(tfile)).readLine();");
        
        test("txt", "this is a test");
    }
    
    public void testSearch() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('this is a test');");
        run("writer.close();");
        run("ret = file.search(/this/);");
        
        test("ret", "0");
        test("file.search(/asdf/)", "-1");
    }
    
    public void testGetText() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('foo\\nbar');");
        run("writer.close();");
        run("txt = file.getText();");
        
        test("txt", "foo\nbar");
    }
    
    public void testGetLines() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('foo\\nbar');");
        run("writer.close();");
        run("txt = file.getLines();");
        
        test("txt.length", "2.0");
        test("txt[1]", "bar");
    }

    public void testRemove() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('foo\\nbar');");
        run("writer.close();");
        run("ret = file.remove();");
        
        test("ret", "true");
        test("!file.exists();", "true");
    }

    public void testEachLine() throws Exception {
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('foo\\nbar');");
        run("writer.close();");
        run("foo = false;");
        run("bar = false;");
        run("file.eachLine(function(line) {eval(line+' = true');});");
        
        test("foo == true && bar == true;", "true");
    }
}
