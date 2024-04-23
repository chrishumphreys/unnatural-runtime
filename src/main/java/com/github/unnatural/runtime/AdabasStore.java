/*
Unnatural support library

Copyright 2024 Chris Humphreys, https://github.com/chrishumphreys/unnatural-runtime

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the “Software”), to deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.github.unnatural.runtime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdabasStore<T extends AdabasRecord> {

    private final Map<Integer, T> recordsByIsn = new HashMap<>();

    private Integer maxIsn = 0;

    public void update(T updateViewGetForModify) {
        if (recordsByIsn.containsKey(updateViewGetForModify.getIsn())) {
            recordsByIsn.put(updateViewGetForModify.getIsn(), updateViewGetForModify);
        } else {
            throw new NaturalException("Record not found with ISN " + updateViewGetForModify.getIsn());
        }
    }

    public T getByISN(BigDecimal isn) {
        T result = recordsByIsn.get(isn);
        if (result == null) {
            throw new NaturalException("Record not found with ISN " + isn);
        }
        return result;
    }

    public List<T> execQuery(AdabasOperation adabasOperation, String view, String where, int count) {
        return Collections.emptyList();
    }

    public int execCountQuery(AdabasOperation adabasOperation, String view, String where) {
        return 0;
    }

    public List<T> readBy(String viewName, String where) {
        return Collections.emptyList();
    }

    public void delete(T updateViewGetForPurge) {
        recordsByIsn.remove(updateViewGetForPurge.getIsn());
    }

    public int store(String viewName, T updateView) {
        maxIsn += 1;
        recordsByIsn.put(maxIsn, updateView);
        return maxIsn;
    }
}
