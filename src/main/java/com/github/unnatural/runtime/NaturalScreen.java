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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public abstract class NaturalScreen implements NaturalObject {
    public abstract String getScreenRender();

    public void setAttribute(String attributeName, String valueAsString) {
        try {
            Field declaredField = this.getClass().getDeclaredField(attributeName);
            String setterName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            if (declaredField.getType().equals(String.class)) {
                Method accessor = this.getClass().getMethod(setterName, String.class);
                accessor.invoke(this, valueAsString);
            } else if (declaredField.getType().equals(BigDecimal.class)) {
                Method accessor = this.getClass().getMethod(setterName, BigDecimal.class);
                accessor.invoke(this, new BigDecimal(valueAsString));
            } else {
                throw new NaturalException("Invalid type in NaturalScreen.setAttribute");
            }
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new NaturalException(e.getMessage());
        }
    }

    public <T> void setAttribute(String attributeName, List<String> valuesAsString, Class<T> clazz) {
        try {
            Field declaredField = this.getClass().getDeclaredField(attributeName);
            String setterName = "set" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Method accessor = this.getClass().getMethod(setterName, List.class);
            if (declaredField.getType().equals(List.class)) {
                if (clazz.equals(String.class)) {
                    accessor.invoke(this, valuesAsString);
                } else if (clazz.equals(BigDecimal.class)) {
                    accessor.invoke(this, valuesAsString.stream().map(BigDecimal::new).collect(Collectors.toList()));
                } else {
                    throw new NaturalException("Invalid class type in NaturalScreen.setAttribute");
                }
            } else {
                throw new NaturalException("Attribute not a list in NaturalScreen.setAttribute");
            }
        } catch (NoSuchFieldException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new NaturalException(e.getMessage());
        }
    }
}
