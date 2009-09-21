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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

/**
 * Adds support for extended functions to wrapped Java objects.
 */
public class JavaObjectWrapper extends NativeJavaObject implements Scriptable, Wrapper, Serializable {

    /** Auto-generated serialization id */
    private static final long serialVersionUID = -6540242119193661910L;
    private Map functions;

    public JavaObjectWrapper() {
        super();
    }

    public JavaObjectWrapper(Scriptable scope, Object javaObject, Class staticType, Map functions) {
        super(scope, javaObject, staticType);
        this.functions = functions;
    }

    public Object get(String name, Scriptable start) {
        Method func = (Method)functions.get(name);
        if (func != null) {
            try {
                if (func.getParameterTypes().length == 2) {
                    Object val = func.invoke(null, javaObject, start);
                    Class type = func.getReturnType();
                    start = ScriptableObject.getTopLevelScope(start);
                    Context cx = Context.getCurrentContext();
                    return cx.getWrapFactory().wrap(cx, start, val, type);
                } else {
                    ExtensionFunction f = (ExtensionFunction)func.invoke(null, javaObject);
                    f.setTarget(javaObject);
                    f.setWrapper(this);
                    return f;
                }
            } catch (Exception ex) {
                throw new RuntimeException("Unable to create function "+name+" on "+javaObject, ex);
            }
        } else {
            return super.get(name, start);
        } 
    }
    
    protected Object wrap(Object o, Scriptable start) {
        Class type = Object.class;
        if (o != null) {
            type = o.getClass();
        }
        start = ScriptableObject.getTopLevelScope(start);
        Context cx = Context.getCurrentContext();
        return cx.getWrapFactory().wrap(cx, start, o, type);
    }
}
