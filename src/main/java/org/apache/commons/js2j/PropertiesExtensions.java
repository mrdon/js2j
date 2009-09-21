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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Adds various functions to java.util.Properties
 * @targetClass java.util.Properties
 */
public class PropertiesExtensions {


    /**
     *  Loads properties from a String or File.
     *
     *  @funcParams java.io.File file
     *  @funcReturn java.util.Properties
     *  @example props.load("file.properties")
     */
    public static ExtensionFunction load(final Properties props) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -1324529209501778868L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                InputStream in = null;
                if (args[0] instanceof String) {
                    in = new FileInputStream((String)args[0]);
                } else if (args[0] instanceof File) {
                    in = new FileInputStream((File)args[0]);
                } else {
                    in = (InputStream) args[0];
                }
                
                props.load(in);
                return props;
            }
        };
    }
    
    /**
     *  Stores properties into a File.
     *
     *  @funcParams java.io.File file, String header
     *  @funcReturn java.util.Properties
     *  @example props.store("file.properties", "My Header")
     */
    public static ExtensionFunction store(final Properties props) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = 720448872482233210L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                OutputStream in = null;
                if (args[0] instanceof String) {
                    in = new FileOutputStream((String)args[0]);
                } else if (args[0] instanceof File) {
                    in = new FileOutputStream((File)args[0]);
                } else {
                    in = (OutputStream) args[0];
                }
                
                props.store(in, (String)args[1]);
                return props;
            }
        };
    }
}
