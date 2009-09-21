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
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Wrapper;

/**  Wrap a java.util.List for JavaScript.  */
public class ScriptableList extends JavaObjectWrapper implements Scriptable, Wrapper, Serializable {

    /** Auto-generated serialization id */
    private static final long serialVersionUID = -2221655313388687110L;
    private List list;


    public ScriptableList() {
    }


    public ScriptableList(List list) {
        this.list = list;
    }


    public ScriptableList(Scriptable scope, Object javaObject, Class staticType, Map funcs) {
        super(scope, javaObject, staticType, funcs);
        if (javaObject instanceof List) {
            this.list = (List) javaObject;
        } else {
            throw new IllegalArgumentException("Passed object " + javaObject + " is not an instance of List");
        }
    }


    public String getClassName() {
        return staticType.toString();
    }


    public boolean has(int index, Scriptable start) {
        // We catch the ArrayIndexOutOfBoundsException because the parser is probably trying to retrieve
        // the value first(reading the statement left to right) then assign the value to that index... 
        // or something like that.
        try {
            return (list.get(index) != null);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }


    public Object get(int index, Scriptable start) {
        return list.get(index);
    }


    public void put(int index, Scriptable start, Object value) {
        int max = index + 1;
        if (max > list.size()) {
            for (int i = list.size(); i < index; i++) {
                list.add(i, null);
            }
            list.add(index, value);
        } else {
            list.set(index, value);
        }
    }


    public void delete(int index) {
        list.remove(index);
    }


    public Object[] getIds() {

        //TODO: optimize this :)
        Integer[] ids = new Integer[list.size()];
        for (int x = 0; x < ids.length; x++) {
            ids[x] = new Integer(x);
        }
        return ids;
    }


    public Object unwrap() {
        return this.list;
    }

}

