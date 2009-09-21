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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Adds various functions to java.io.InputStream
 * @targetClass java.io.InputStream
 */
public class InputStreamExtensions {


    /**
     *  Gets the contents of the stream as a String.
     *
     *  @funcParams 
     *  @funcReturn String
     *  @example text = inStream.getText()
     */
    public static ExtensionFunction getText(final InputStream in) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -6815768615295922278L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer answer = new StringBuffer();
                // reading the content of the file within a char buffer allow to keep the correct line endings
                char[] charBuffer = new char[4096];
                int nbCharRead = 0;
                while ((nbCharRead = reader.read(charBuffer)) != -1) {
                    // appends buffer
                    answer.append(charBuffer, 0, nbCharRead);
                }
                reader.close();
                return answer.toString();
            }
        };
    }
}
