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

public class UnnaturalMapRenderer {
    private final StringBuilder screen = new StringBuilder();
    private int currentRow = 0;
    private String currentLine = blankLine();

    private String blankLine() {
        return " ".repeat(80);
    }

    private void addCurrentRow() {
        screen.append(currentLine).append("\n");
        currentRow+=1;
        currentLine = blankLine();
    }

    private void advanceToRow(int row) {
        while(row > currentRow) {
            addCurrentRow();
        }
    }

    private void addTextToCurrentRow(String text, int col, int row) {
        String left = currentLine.substring(0, col);
        String right = currentLine.substring(col + text.length());
        currentLine = left + text + right;
    }

    public String asString() {
        return screen.toString();
    }

    public void finish() {
        addCurrentRow();
    }

    private void addDataAreaToCurrentRow(int length, int col, int row) {
        addTextToCurrentRow("_".repeat(length), col, row);
    }


    public void addText(String text, int col, int row) {
        if (row > currentRow) {
            advanceToRow(row);
        }
        addTextToCurrentRow(text, col, row);
    }

    public void addField(String field, int col, int row) {
        if (row > currentRow) {
            advanceToRow(row);
        }
        if (field != null) {
            addTextToCurrentRow(field, col, row);
        }
    }

    public void addField(int field, int col, int row) {
        if (row > currentRow) {
            advanceToRow(row);
        }
        addTextToCurrentRow(Integer.toString(field), col, row);
    }
}
