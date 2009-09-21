/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.commons.js2j;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *  Base class for function extensions.  A function extension is a function
 *  is added to an existing Java object at the Rhino level.
 */
public abstract class ExtensionFunction extends ScriptableObject implements Function {
        
    protected Object target;    
    protected Scriptable wrapper;
        
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) {
        try {
            // Unwrap args and convert to Java types as appropriate
            for (int x=0; x<args.length; x++) {
                if (args[x] instanceof NativeJavaObject) {
                    args[x] = ((NativeJavaObject)args[x]).unwrap();
                } else if (args[x] instanceof IdScriptableObject) {
                    if ("String".equals(((IdScriptableObject)args[x]).getClassName())) {
                        args[x] = args[x].toString();
                    }
                }
            }
            
            // Execute the function call
            Object o = execute(cx, scope, thisObj, args);
            
            // Wrap the result as necessary
            if (o instanceof Scriptable) {
                return o;
            } else if (o == target) {
                return wrapper;
            } else {
                // Need to wrap the object before we return it.
                scope = ScriptableObject.getTopLevelScope(scope);
                Class type = Object.class;
                if (o != null) {
                    type = o.getClass();
                } 
                return cx.getWrapFactory().wrap(cx, scope, o, type);
            }
        } catch (Exception ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
    }
    
    public abstract Object execute(Context cx, Scriptable scope, Scriptable thisObj, java.lang.Object[] args) throws Exception;
     
    public Scriptable construct(Context cx, Scriptable scope, java.lang.Object[] args) {
        return null;
    }
    
    public String getClassName() {
        return getClass().getName();
    }
    
    public void setTarget(Object target) {
        this.target = target;   
    }
    
    public void setWrapper(Scriptable wrapper) {
        this.wrapper = wrapper;   
    }
}
