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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.RegExpProxy;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;

/**
 * Adds various functions to java.io.File
 * @targetClass java.io.File
 */
public class FileExtensions {


    /**
     *  Appends text to the file.
     *
     *  @funcParams String text
     *  @funcReturn java.io.File
     *  @example file.append("added text")
     */
    public static ExtensionFunction append(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -8708037167577801237L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                String text = args[0].toString();
                FileWriter writer = null;
                try {
                    writer = new FileWriter(file, true);
                    writer.write(text);
                } finally {
                    writer.close();
                }
                return file;
            }
        };
    }
    
    /**
     *  Gets the contents of the file as a String.
     *
     *  @funcParams 
     *  @funcReturn String
     *  @example text = file.getText()
     */
    public static ExtensionFunction getText(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = 5225834890780473777L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                return readFile(file);
            }
        };
    }
    
    /**
     *  Copies the contents of the file into a new File.
     *
     *  @funcParams java.io.File targetFile
     *  @funcReturn java.io.File
     *  @example file.copy(new File("newFile.txt"));
     */
    public static ExtensionFunction copy(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -8565740854310175556L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                File tFile = null;
                if (args[0] instanceof File) {
                    tFile = (File) args[0];
                } else {
                    tFile = new File((String)args[0]);
                }
                FileWriter writer = null;
                BufferedReader reader = null;
                
                try {
                    writer = new FileWriter(tFile);
                    reader = new BufferedReader(new FileReader(file));
                    // reading the content of the file within a char buffer allow to keep the correct line endings
                    char[] charBuffer = new char[4096];
                    int nbCharRead = 0;
                    while ((nbCharRead = reader.read(charBuffer)) != -1) {
                        writer.write(charBuffer, 0, nbCharRead);
                    }
                } finally {
                    try {
                        if (writer != null) {
                            writer.close();
                        }
                    } finally {
                        if (reader != null) {
                            reader.close();
                        }
                    }
                }
                return tFile;
            }
        };
    }
    
     /**
     *  Passes each line to the provided function.  The file is opened, and 
     *  interpreted as a text file using the default encoding.  Each line
     *  is read and passed to the provided function.
     *
     *  @funcParams Function func
     *  @funcReturn void
     *  @example file.eachLine(function(line) { print(line) })
     */
    public static ExtensionFunction eachLine(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = 6912551910810089408L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                FileReader freader = null;
                try {
                    freader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(freader);
                    Function func = (Function)args[0];
                    Object[] params = new Object[1];
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        params[0] = line;
                        func.call(cx, scope, thisObj, params);
                    }
                } finally {
                    freader.close();
                }
                return null;
            }
        };
    }
    
    /**
     *  Replaces text in a file.
     *
     *  @funcParams Regexp pattern, String replacementText
     *  @funcReturn java.io.File
     *  @example file.replace(/foo/, "bar")
     */
    public static ExtensionFunction replace(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -785850132775863225L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                // Read original file contents
                String orig = readFile(file);
                
                // Replace context with regexp
                Scriptable l = cx.getWrapFactory().wrapAsJavaObject(cx, scope, orig.toString(), String.class);
                String text = ScriptRuntime.toString(
                        ScriptRuntime.checkRegExpProxy(cx).action(cx, scope, l, args, RegExpProxy.RA_REPLACE));
                
                // Write new contents to original file
                FileWriter writer = null;
                try {
                    writer = new FileWriter(file);
                    writer.write(text);
                } finally {
                    writer.close();
                }
                return file;
            }
        };
    }
    
    /**
     *  Searches a file for a regular expression.
     *
     *  @funcParams Regexp pattern
     *  @funcReturn int
     *  @example file.search(/foo/)
     */
    public static ExtensionFunction search(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -8841229353342589837L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                // Read original file contents
                String orig = readFile(file);
                
                // Replace context with regexp
                Scriptable l = cx.getWrapFactory().wrapAsJavaObject(cx, scope, orig.toString(), String.class);
                return ScriptRuntime.checkRegExpProxy(cx).action(cx, scope, l, args, RegExpProxy.RA_SEARCH);
            }
        };
    }
    
     /**
     *  Collects the contents of the file as an array of lines.  The file is opened, and 
     *  interpreted as a text file using the default encoding.  
     *
     *  @funcParams 
     *  @funcReturn String[]
     *  @example linesArray = file.getLines()
     */
    public static ExtensionFunction getLines(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = -8026495170352394269L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                ArrayList list = new ArrayList();
                FileReader freader = null;
                try {
                    freader = new FileReader(file);
                    BufferedReader reader = new BufferedReader(freader);
                    String line = null;
                    
                    while ((line = reader.readLine()) != null) {
                        list.add(line);
                    }
                } finally {
                    freader.close();
                }
                
                Object[] lines = new Object[list.size()];
                for (int x=0; x<lines.length; x++) {
                    lines[x] = Context.javaToJS(list.get(x), scope);
                }
                return cx.newArray(scope, lines);
            }
        };
    }
    
    /**
     *  Removes a file.  Used to get around the problem of the reserved word 'delete'.
     *
     *  @funcParams 
     *  @funcReturn boolean
     *  @example isRemoved = file.remove()
     */
    public static ExtensionFunction remove(final File file) {
        return new ExtensionFunction() {    
            /** Auto-generated serialization id */
            private static final long serialVersionUID = 7808567817975522748L;

            public Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) 
                    throws IOException {
                
                return Boolean.valueOf(file.delete());
            }
        };
    }
    
    private static String readFile(File file) throws IOException {
        StringBuffer orig = new StringBuffer();
        FileReader freader = null;
        try {
            freader = new FileReader(file);
            BufferedReader reader = new BufferedReader(freader);
            char[] charBuffer = new char[4096];
            int nbCharRead = 0;
            while ((nbCharRead = reader.read(charBuffer)) != -1) {
                orig.append(charBuffer, 0, nbCharRead);
            }
        } finally {
            freader.close();
        }
        return orig.toString();
    }
    
}
