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
import java.util.Arrays;

public class UnnaturalArraySlicer {

    public static int[] slice(int[] array, ArrayIndexRange range) {
        if (range.isAll()) {
            return array;
        }
        if (range.getEnd() == null) {
            throw new NaturalException("Attempting to slice array using single scalar reference");
        }
        return Arrays.copyOfRange(array, range.getStart(), range.getEnd() + 1);
    }

    public static <T extends Object> T[] slice(T[] array, ArrayIndexRange range) {
        if (range.isAll()) {
            return array;
        }
        if (range.getEnd() == null) {
            throw new NaturalException("Attempting to slice array using single scalar reference");
        }
        return Arrays.copyOfRange(array, range.getStart(), range.getEnd() + 1);
    }

    public static int[][] slice(int[][] array, ArrayIndexRange rangeDimensionOne, ArrayIndexRange rangeDimensionTwo) {
        int newDimensionOneLen = rangeDimensionOne.isAll() ? array.length : rangeDimensionOne.length();
        int newDimensionTwoLen = rangeDimensionTwo.isAll() ? array[0].length : rangeDimensionTwo.length();

        int dimOneStart = rangeDimensionOne.isAll() ? 0 : rangeDimensionOne.getStart();
        int dimOneEnd = rangeDimensionOne.isAll() ? array.length : rangeDimensionOne.getEnd() + 1;

        int[][] copy = new int[newDimensionOneLen][newDimensionTwoLen];

        int newDimOne = 0;
        for (int oldDimOne = dimOneStart; oldDimOne < dimOneEnd; oldDimOne++) {
            int dimTwoStart = 0;
            int dimTwoEnd = array[0].length;

            if (!rangeDimensionTwo.isAll()) {
                dimTwoStart = rangeDimensionTwo.getStart();
                dimTwoEnd = rangeDimensionTwo.getEnd() + 1;
            }
            System.arraycopy(array[oldDimOne], dimTwoStart, copy[newDimOne], 0, dimTwoEnd - dimTwoStart);
            newDimOne++;
        }

        return copy;
    }

    public static BigDecimal[][] slice(BigDecimal[][] array, ArrayIndexRange rangeDimensionOne, ArrayIndexRange rangeDimensionTwo) {
        int newDimensionOneLen = rangeDimensionOne.isAll() ? array.length : rangeDimensionOne.length();
        int newDimensionTwoLen = rangeDimensionTwo.isAll() ? array[0].length : rangeDimensionTwo.length();

        int dimOneStart = rangeDimensionOne.isAll() ? 0 : rangeDimensionOne.getStart();
        int dimOneEnd = rangeDimensionOne.isAll() ? array.length : rangeDimensionOne.getEnd() + 1;

        Class<? extends Object[]> newType = array.getClass();

        BigDecimal[][] copy = new BigDecimal[newDimensionOneLen][newDimensionTwoLen];

        int newDimOne = 0;
        for (int oldDimOne = dimOneStart; oldDimOne < dimOneEnd; oldDimOne++) {

            int dimTwoStart = 0;
            int dimTwoEnd = array[0].length;

            if (!rangeDimensionTwo.isAll()) {
                dimTwoStart = rangeDimensionTwo.getStart();
                dimTwoEnd = rangeDimensionTwo.getEnd() + 1;
            }
            System.arraycopy(array[oldDimOne], dimTwoStart, copy[newDimOne], 0, dimTwoEnd - dimTwoStart);
            newDimOne++;
        }

        return copy;
    }
}
