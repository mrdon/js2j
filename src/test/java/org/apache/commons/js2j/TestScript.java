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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;

import junit.framework.TestCase;

public abstract class TestScript extends TestCase {

    Context cx = null;
    Scriptable scope = null;
    protected String script = "";

    protected void setUp() throws Exception {
        cx = Context.enter();
        cx.setWrapFactory(new SugarWrapFactory());
        scope = new Global(cx);
    }

    protected void test(String script, String expected) throws Exception {
        Object obj = run(script);
        String val = (obj == null ? null : obj.toString());
        if (!expected.equals(val)) {
            fail("Expression '"+script+"' should have returned '"+expected+"', "+
                 "but instead returned '"+val+"'");
        }
    }

    protected Object run(String script) throws Exception {
        Object ret = cx.evaluateString(scope, script, "test", 1, null);
        if (ret instanceof JavaObjectWrapper) {
            ret = ((JavaObjectWrapper)ret).unwrap();
        }
        return ret;
    }
    
    protected List load(Object object) throws IOException {
        ArrayList list = new ArrayList();
        FileReader freader = null;
        try {
            freader = new FileReader(new File(object.toString()));
            BufferedReader reader = new BufferedReader(freader);
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } finally {
            freader.close();
        }
        return list;
    }

    protected void tearDown() throws Exception {
        Context.exit();
    }

}