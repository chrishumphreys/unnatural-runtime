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

public enum SetKeyCommand {
    ALL("ALL"), ON("ON"), OFF("OFF"), NAMED_OFF("NAMED OFF"),
    COMMAND_ON("COMMAND ON"), COMMAND_OFF("COMMAND OFF"), PGM("PGM");

    private final String naturalValue;

    SetKeyCommand(String naturalValue) {
        this.naturalValue = naturalValue;
    }

    public static SetKeyCommand fromNatural(String naturalValue) {
        Optional<SetKeyCommand> key = Arrays.stream(SetKeyCommand.values()).filter(p -> p.naturalValue.equals(naturalValue)).findFirst();
        if (key.isEmpty()) {
            throw new NaturalException("Invalid SetKeyCommand value " + naturalValue);
        }
        return key.get();
    }

    public String toJava() {
        return "SetKeyCommand." + this.name();
    }
}
