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

public class UnicodeBinaryEncode {
    // Encode an 8 byte long as a 4 character unicode string
    public String encodeLong(long longValue) {
        StringBuilder result = new StringBuilder();
        for (int character=0; character < 4; character++) {
            int characterValue = (int)((longValue >> (character * 16)) & 0xffff);
            characterValue += 0x100;  // offset past control characters
            result.append(Character.toChars(characterValue)[0]);
        }
        return result.toString();
    }

    // Decode 4 character unicode string to 8 byte long
    public long decodeLong(String text) {
        if (text.length() != 4) {
            throw new NaturalException("Invalid text - too long - " + text.length());
        }

        long result = 0;
        for (int characterPos = 0; characterPos < 4; characterPos++) {
            char character = text.charAt(characterPos);
            long longValue = character & 0xffff;
            longValue -= 0x100;  // adjust for control chars offset
            long newLong = longValue << (characterPos * 16);
            result |= newLong;
        }
        return result;
    }
}
