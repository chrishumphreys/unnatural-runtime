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

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UnnaturalArraySlicerTest {

    @Test
    void canSliceAnIntArray() {
        int[] myInts = new int[]{1,2,3,4,5,6,7,8,9,10};

        int[] slice = UnnaturalArraySlicer.slice(myInts, ArrayIndexRange.range(0, 5));

        assertEquals(6, slice.length);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, slice);
    }

    @Test
    void canSliceAnBigDecimalArray() {
        BigDecimal[] myNumbers = new BigDecimal[]{bd(1), bd(2), bd(3), bd(4),
                bd(5), bd(6), bd(7), bd(8), bd(9), bd(10)};

        BigDecimal[] slice = UnnaturalArraySlicer.slice(myNumbers, ArrayIndexRange.range(0, 5));

        assertEquals(6, slice.length);
        assertArrayEquals(new BigDecimal[]{bd(1), bd(2), bd(3), bd(4),
                bd(5), bd(6)}, slice);
    }

    @Test
    void canSliceAMultiDimensionIntArrayWithFirstDimensionAll() {
        // this is an array of five items in it, each item is an array of 10 ints
        int[][] myInts = new int[][]{new int[]{1,2,3,4,5,6,7,8,9,10}, new int[]{11,12,13,14,15,16,17,18,19,20},
                new int[]{21,22,23,24,25,26,27,28,29,30}, new int[]{31,32,33,34,35,36,37,38,39,40},
                new int[]{41,42,43,44,45,46,47,48,49,50}};


        assertEquals(5, myInts.length);
        assertEquals(10, myInts[0].length);

        // take all 5 items in the first dimensions, where each item has been spliced to 0:3 inclusive items form the 10 available
        int[][] slice = UnnaturalArraySlicer.slice(myInts, ArrayIndexRange.ALL, ArrayIndexRange.range(0, 3));

        assertEquals(5, slice.length);
        assertEquals(4, slice[0].length);

        assertArrayEquals(new int[]{1, 2, 3, 4}, slice[0]);
        assertArrayEquals(new int[]{11, 12, 13, 14}, slice[1]);
        assertArrayEquals(new int[]{21, 22, 23, 24}, slice[2]);
        assertArrayEquals(new int[]{31, 32, 33, 34}, slice[3]);
        assertArrayEquals(new int[]{41, 42, 43, 44}, slice[4]);
    }

    @Test
    void canSliceAMultiDimensionIntArray() {
        // this is an array of five items in it, each item is an array of 10 ints
        int[][] myInts = new int[][]{new int[]{1,2,3,4,5,6,7,8,9,10}, new int[]{11,12,13,14,15,16,17,18,19,20},
                new int[]{21,22,23,24,25,26,27,28,29,30}, new int[]{31,32,33,34,35,36,37,38,39,40},
                new int[]{41,42,43,44,45,46,47,48,49,50}};

        assertEquals(5, myInts.length);
        assertEquals(10, myInts[0].length);

        // take 4 items (1:3) items in the first dimensions, where each item has been spliced to 1:4 items form the 10 available
        int[][] slice = UnnaturalArraySlicer.slice(myInts, ArrayIndexRange.range(1, 3), ArrayIndexRange.range(1, 4));

        assertEquals(3, slice.length);
        assertEquals(4, slice[0].length);

        assertArrayEquals(new int[]{12, 13, 14, 15}, slice[0]);
        assertArrayEquals(new int[]{22, 23, 24, 25}, slice[1]);
        assertArrayEquals(new int[]{32, 33, 34, 35}, slice[2]);
    }

    @Test
    void canSliceAMultiDimensionBigDecimalArrayWithFirstDimensionAll() {
        // this is an array of five items in it, each item is an array of 10 ints
        BigDecimal[][] myNumbers = new BigDecimal[][] {
                new BigDecimal[]{bd(1), bd(2), bd(3), bd(4), bd(5), bd(6), bd(7), bd(8), bd(9), bd(10)},
                new BigDecimal[]{bd(11), bd(12), bd(13), bd(14), bd(15), bd(16), bd(17), bd(18), bd(19), bd(20)},
                new BigDecimal[]{bd(21), bd(22), bd(23), bd(24), bd(25), bd(26), bd(27), bd(28), bd(29), bd(30)},
                new BigDecimal[]{bd(31), bd(32), bd(33), bd(34), bd(35), bd(36), bd(37), bd(38), bd(39), bd(40)},
                new BigDecimal[]{bd(41), bd(42), bd(43), bd(44), bd(45), bd(46), bd(47), bd(48), bd(49), bd(50)}};

        assertEquals(5, myNumbers.length);
        assertEquals(10, myNumbers[0].length);

        // take all 5 items in the first dimensions, where each item has been spliced to 0:3 items form the 10 available
        BigDecimal[][] slice = UnnaturalArraySlicer.slice(myNumbers, ArrayIndexRange.ALL, ArrayIndexRange.range(0, 3));

        assertEquals(5, slice.length);
        assertEquals(4, slice[0].length);

        assertArrayEquals(new BigDecimal[]{bd(1), bd(2), bd(3), bd(4)}, slice[0]);
        assertArrayEquals(new BigDecimal[]{bd(11), bd(12), bd(13), bd(14)}, slice[1]);
        assertArrayEquals(new BigDecimal[]{bd(21), bd(22), bd(23), bd(24)}, slice[2]);
        assertArrayEquals(new BigDecimal[]{bd(31), bd(32), bd(33), bd(34)}, slice[3]);
        assertArrayEquals(new BigDecimal[]{bd(41), bd(42), bd(43), bd(44)}, slice[4]);
    }

    @Test
    void canSliceAMultiDimensionBigDecimalArray() {
        // this is an array of five items in it, each item is an array of 10 ints
        BigDecimal[][] myNumbers = new BigDecimal[][] {
                new BigDecimal[]{bd(1), bd(2), bd(3), bd(4), bd(5), bd(6), bd(7), bd(8), bd(9), bd(10)},
                new BigDecimal[]{bd(11), bd(12), bd(13), bd(14), bd(15), bd(16), bd(17), bd(18), bd(19), bd(20)},
                new BigDecimal[]{bd(21), bd(22), bd(23), bd(24), bd(25), bd(26), bd(27), bd(28), bd(29), bd(30)},
                new BigDecimal[]{bd(31), bd(32), bd(33), bd(34), bd(35), bd(36), bd(37), bd(38), bd(39), bd(40)},
                new BigDecimal[]{bd(41), bd(42), bd(43), bd(44), bd(45), bd(46), bd(47), bd(48), bd(49), bd(50)}};

        assertEquals(5, myNumbers.length);
        assertEquals(10, myNumbers[0].length);

        // take 4 items (1:3) items in the first dimensions, where each item has been spliced to 1:4 items form the 10 available
        BigDecimal[][] slice = UnnaturalArraySlicer.slice(myNumbers, ArrayIndexRange.range(1, 3), ArrayIndexRange.range(1, 4));

        assertEquals(3, slice.length);
        assertEquals(4, slice[0].length);

        assertArrayEquals(new BigDecimal[]{bd(12), bd(13), bd(14), bd(15)}, slice[0]);
        assertArrayEquals(new BigDecimal[]{bd(22), bd(23), bd(24), bd(25)}, slice[1]);
        assertArrayEquals(new BigDecimal[]{bd(32), bd(33), bd(34), bd(35)}, slice[2]);
    }

    private static BigDecimal bd(int val) {
        return new BigDecimal(val);
    }
}