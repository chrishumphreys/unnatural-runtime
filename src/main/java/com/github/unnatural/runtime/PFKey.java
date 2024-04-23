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

import java.util.Arrays;
import java.util.Optional;

public enum PFKey {
    PF1("PF1"), PF2("PF2"), PF3("PF3"), PF4("PF4"),
    PF5("PF5"), PF6("PF6"), PF7("PF7"), PF8("PF8"),
    PF9("PF9"), PF10("PF10"), PF11("PF11"), PF12("PF2");

    private final String naturalValue;

    PFKey(String naturalValue) {
        this.naturalValue = naturalValue;
    }

    public static PFKey fromNatural(String naturalValue) {
        Optional<PFKey> key = Arrays.stream(PFKey.values()).filter(p -> p.naturalValue.equals(naturalValue)).findFirst();
        if (key.isEmpty()) {
            throw new NaturalException("Invalid PFKey value " + naturalValue);
        }
        return key.get();
    }

    public String toJava() {
        return "PFKey." + this.name();
    }
}