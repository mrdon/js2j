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

import java.util.List;

public class PropertiesExtensionsTest extends TestScript {

    public void testLoad() throws Exception {
        run("file = java.io.File.createTempFile('test', '.tmp');");
        run("writer = new java.io.FileWriter(file);");
        run("writer.write('# some file\\n');");
        run("writer.write('foo=bar\\n');");
        run("writer.close();");
        run("props = new java.util.Properties()");
        run("props.load(file)");
        
        test("props.getProperty('foo')", "bar");
        
        run("props = new java.util.Properties()");
        run("props.load(file.toString())");
        
        test("props.getProperty('foo')", "bar");
        
        test("new java.util.Properties().load(file).foo", "bar");
    }
    
    public void testStore() throws Exception {
        run("file = java.io.File.createTempFile('test', '.tmp');");
        run("props = new java.util.Properties()");
        run("props.setProperty('foo', 'bar')");
        run("props.store(file, 'boo')");
        
        List lines = load(run("file.toString()"));
        
        assertEquals(lines.get(0), "#boo");
        assertEquals(lines.get(2), "foo=bar");
        
        run("props.store(file.toString(), 'boo')");
        
        lines = load(run("file.toString()"));
        
        assertEquals(lines.get(0), "#boo");
        assertEquals(lines.get(2), "foo=bar");
    }
}
