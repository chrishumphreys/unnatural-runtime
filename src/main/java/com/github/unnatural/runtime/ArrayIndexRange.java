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

import lombok.Getter;

public class ArrayIndexRange {
    public static final ArrayIndexRange ALL = new ArrayIndexRange(null, null, true);
    @Getter
    private final Integer start;
    @Getter
    private final Integer end;
    private final boolean all;

    private ArrayIndexRange(Integer start, Integer end, boolean all) {
        this.start = start;
        this.end = end;
        this.all = all;
    }

    public static ArrayIndexRange value(int index) {
        return new ArrayIndexRange(index, null, false);
    }

    public static ArrayIndexRange range(Integer start, Integer end) {
        // inclusive start and end
        return new ArrayIndexRange(start, end, false);
    }

    public boolean isAll() {
        return all;
    }

    public int length() {
        return end - start + 1;  //end is inclusive
    }
}
