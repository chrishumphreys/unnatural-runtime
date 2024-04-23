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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UnnaturalTest {

    private static final String[] STR_ARRAY = new String[]{"ABC", "DEF", "GHI", "JKL", "ABC", "DEF", null, ""};
    private static final String PRESENT_VALUE = "ABC";
    private static final String MISSING_VALUE = "ZZZ";

    @Test
    void truncateDecimalPlacesBigDecimalDecreasing() {
        BigDecimal result = Unnatural.truncate(1, new BigDecimal("1.36"));
        assertEquals(new BigDecimal("1.3"), result);
    }

    @Test
    void roundDecimalPlacesBigDecimalDecreasing() {
        BigDecimal result = Unnatural.round(1, new BigDecimal("1.36"));
        assertEquals(new BigDecimal("1.4"), result);
    }

    @Test
    void truncateDecimalPlacesBigDecimalIncreasing() {
        BigDecimal result = Unnatural.truncate(2, new BigDecimal("1.3"));
        assertEquals(new BigDecimal("1.30"), result);
    }

    @Test
    void roundDecimalPlacesBigDecimalIncreasing() {
        BigDecimal result = Unnatural.round(2, new BigDecimal("1.3"));
        assertEquals(new BigDecimal("1.30"), result);
    }


    @Test
    void maskAlphaNumeric() {
        assertTrue(Unnatural.matchesMask("11AAA11", new Mask("NNUUUNN")));
        assertTrue(Unnatural.matchesMask("1111", new Mask("NNNN")));
        assertTrue(Unnatural.matchesMask("1111", new Mask("NN")));  // ignores rest of string
        assertTrue(Unnatural.matchesMask("22226666", new Mask("22225555")));
        assertTrue(Unnatural.matchesMask("22226666", new Mask("22225555")));
        assertTrue(Unnatural.matchesMask("2AAAA", new Mask("N")));
        assertTrue(Unnatural.matchesMask("A123", new Mask("U")));
        assertTrue(Unnatural.matchesMask("24/11AA/00/12345678901A", new Mask("YY'/'CCCC'/'NN'/'NNNNNNNNNNNU")));
        assertTrue(Unnatural.matchesMask("24ABC", new Mask("NN'ABC'")));

        assertFalse(Unnatural.matchesMask("1111", new Mask("UUUU")));
        assertFalse(Unnatural.matchesMask("1111", new Mask("NNNNN"))); // mask longer than input
        assertTrue(Unnatural.matchesMask("22226666", new Mask("33335555")));
        assertFalse(Unnatural.matchesMask("A123", new Mask("UU")));
        assertFalse(Unnatural.matchesMask((String)null, new Mask("U")));

    }

    @Test
    void maskNumericRange(){
        assertTrue(Unnatural.matchesMask("2000", new Mask("1000:3000")));
        assertTrue(Unnatural.matchesMask("1000", new Mask("1000:3000")));
        assertTrue(Unnatural.matchesMask("3000", new Mask("1000:3000")));

        assertFalse(Unnatural.matchesMask("5000", new Mask("1000:3000")));
    }

    @Test
    void maskDateStr() {
        assertTrue(Unnatural.matchesMask(20201020, new Mask("YYYYMMDD")));
        assertTrue(Unnatural.matchesMask("0210", new Mask("MMDD")));
        assertTrue(Unnatural.matchesMask("0210", new Mask("MMDD")));

        assertFalse(Unnatural.matchesMask(202020, new Mask("YYYYMMDD")));
        assertFalse(Unnatural.matchesMask(20200231, new Mask("YYYYMMDD")));
        assertFalse(Unnatural.matchesMask("0231", new Mask("MMDD")));
    }

    @Test
    void maskDateComponentsWithinOtherStr(){
        assertTrue(Unnatural.matchesMask("hello 2020 there", new Mask("PPPPPPYYYYPPPPPP")));
        assertTrue(Unnatural.matchesMask("hello 20201002 there", new Mask("PPPPPPYYYYMMDDPPPPPP")));
        assertTrue(Unnatural.matchesMask("20221005123010", new Mask("YYYYMMDD999999")));

        assertFalse(Unnatural.matchesMask("hello 9999 there", new Mask("PPPPPPYYYYPPPPPP")));
        assertFalse(Unnatural.matchesMask("hello 20201032 there", new Mask("PPPPPPYYYYMMDDPPPPPP")));
        assertFalse(Unnatural.matchesMask("20221032123010", new Mask("YYYYMMDD999999")));
    }

    @Test
    void setSubstring() {
        assertEquals("sABrce", Unnatural.setSubstring("source", 1, 3, "AB"));
        assertEquals("sABrce", Unnatural.setSubstring("source", 1, 3, "ABCD"));
        assertEquals("sA rce", Unnatural.setSubstring("source", 1, 3, "A"));
        assertEquals("HELLO/WOR      RE/IS/NOTHING/HERE", Unnatural.setSubstring("HELLO/WORLD/THERE/IS/NOTHING/HERE", 9, 15, " "));
    }

    @Test
    void matchesNumeric() {
        assertTrue(Unnatural.matchesMask(1234, new Mask("NNNN")));
    }

    @Test
    void findFirstIndexOfMatchInStringArrayWhenPresent() {
        int match = Unnatural.findIndexOfMatchInStringArray(STR_ARRAY, PRESENT_VALUE, 1);
        assertEquals(0, match);
    }

    @Test
    void findSecondIndexOfMatchInStringArrayWhenPresent() {
        int match = Unnatural.findIndexOfMatchInStringArray(STR_ARRAY, PRESENT_VALUE, 2);
        assertEquals(4, match);
    }

    @Test
    void findIndexOfMatchInStringArrayWhenMissing() {
        int match = Unnatural.findIndexOfMatchInStringArray(STR_ARRAY, MISSING_VALUE, 1);
        assertEquals(-1, match);
    }

    @Test
    void findFirstIndexOfMatchInObjectArrayWhenPresent() {

        int match = Unnatural.findIndexOfMatchInStringArray(OBJ_ARRAY, ObjWithString::getMyStr, PRESENT_VALUE, 1);
        assertEquals(0, match);
    }

    @Test
    void findSecondIndexOfMatchInObjectArrayWhenPresent() {
        int match = Unnatural.findIndexOfMatchInStringArray(OBJ_ARRAY, ObjWithString::getMyStr, PRESENT_VALUE, 2);
        assertEquals(4, match);
    }

    @Test
    void findIndexOfMatchInObjectArrayWhenMissing() {
        int match = Unnatural.findIndexOfMatchInStringArray(OBJ_ARRAY, ObjWithString::getMyStr, MISSING_VALUE, 1);
        assertEquals(-1, match);
    }

    @Test
    void repeatToLength() {
        assertEquals("ABABAB", Unnatural.repeatToLength("AB", 3));
        assertEquals("ZZZZZZ", Unnatural.repeatToLength("Z", 6));
    }

    @Test
    void fillCharArray() {
        String[] charArray = new String[5];
        Unnatural.fillCharArray(charArray, "ABC");

        assertArrayEquals(new String[]{"A", "B", "C", "", ""}, charArray);
    }

    @Test
    void fillStringArray() {
        String[] charArray = new String[5];
        Unnatural.fillStringArray(charArray, "AABBCCD", 2);

        assertArrayEquals(new String[]{"AA", "BB", "CC", "D", ""}, charArray);
    }

    @Test
    void charArrayToString() {
        String[] charArray = new String[]{"A", "B", "C", " ", " "};
        String result = Unnatural.unpackCharArray(charArray);

        assertEquals("ABC  ", result);
    }

    @Test
    void formatIntegerToString() {
        assertEquals("20201020", Unnatural.formatIntegerToString(20201020, 8));
        assertEquals("0020201020", Unnatural.formatIntegerToString(20201020, 10));
    }

    @Test
    void numberToDateComponents() {
        DateComponents dateComponents = Unnatural.numberToDateComponents(20201020);
        assertEquals(2020, dateComponents.year());
        assertEquals(10, dateComponents.month());
        assertEquals(20, dateComponents.day());
    }

    @Test
    void dateComponentsToString() {
        assertEquals("20202010", Unnatural.dateComponentsToString(new DateComponents(10,20,2020)));
    }

    @Test
    void parseDate() {
        assertEquals(LocalDate.of(2020, 10, 20), Unnatural.parseDate("YYYYMMDD", "20201020"));
        assertEquals(LocalDate.of(2020, 10, 20), Unnatural.parseDate("DDMMYYYY", "20102020"));
    }

    @Test
    void canChunkStringToArrayOfParts() {
        String[] parts = new String[7];
        Unnatural.chunkStringIntoParts("An example string which is short", 5, parts);
        assertEquals("An ex", parts[0]);
        assertEquals("ample", parts[1]);
        assertEquals(" stri", parts[2]);
        assertEquals("ng wh", parts[3]);
        assertEquals("ich i", parts[4]);
        assertEquals("s sho", parts[5]);
        assertEquals("rt", parts[6]);
        String[] parts2 = new String[3];
        Unnatural.chunkStringIntoParts("Very short", 5, parts2);
        assertEquals("Very ", parts2[0]);
        assertEquals("short", parts2[1]);
        assertNull(parts2[2]);
        String[] parts3 = new String[3];
        Unnatural.chunkStringIntoParts("Exactly the right length", 8, parts3);
        assertEquals("Exactly ", parts3[0]);
        assertEquals("the righ", parts3[1]);
        assertEquals("t length", parts3[2]);
        assertNull(parts2[2]);
    }

    @Test
    void chunkStringToArrayOfPartsThrowsExceptionIfStrDoesntFitInArray() {
        String[] parts = new String[7];
        assertThrows(NaturalException.class, ()->
            Unnatural.chunkStringIntoParts("An example string which is too long to fit", 5, parts));
    }


    @Test
    void formatNaturalNumericToString() {
        assertEquals("00042", Unnatural.formatNaturalNumericToString(42, 5,0));
        assertEquals("0012345", Unnatural.formatNaturalNumericToString(new BigDecimal("123.45"), 5,2));
        assertEquals("00000123459800", Unnatural.formatNaturalNumericToString(new BigDecimal("12345.98"), 10,4));
        assertEquals("00000123459877", Unnatural.formatNaturalNumericToString(new BigDecimal("12345.9876543"), 10,4));
    }


    @Test
    void applyStringEditMaskForTarget() {
        assertEquals("Something", Unnatural.applyStringEditMaskForTarget("XXXX'/'XXXXX", "Some/thing"));
        assertEquals("Something", Unnatural.applyStringEditMaskForTarget("XXXX'/'XXXXX", "Some/thing With Trailing"));
        assertEquals("Something too short", Unnatural.applyStringEditMaskForTarget("XXXX'/'XXXXXXXXXXXXXXXXXXXXXXXX", "Some/thing too short"));
    }

    @Test
    void applyStringEditMaskForSource() {
        assertEquals("Some/thing", Unnatural.applyStringEditMaskForSource("XXXX'/'XXXXX", "Something"));
        assertEquals("Some/thing", Unnatural.applyStringEditMaskForSource("XXXX'/'XXXXX", "Something with trailing"));
        assertEquals("Some/thing too short", Unnatural.applyStringEditMaskForSource("XXXX'/'XXXXXXXXXXXXXXXXXXXXXXXX", "Something too short"));
    }


    @Test
    void parseDateTime() {
        assertEquals(LocalDateTime.of(2010,10,4,12,13,0), Unnatural.parseDateTime("/DDLLLYY/HHmm", "/04Oct10/1213"));
        assertEquals(LocalDateTime.of(2700,12, 31,12,0,0), Unnatural.parseDateTime("DDMMYYYYHHmm", "311227001200"));
        assertEquals(LocalDateTime.of(2700,12, 31,0,0,0), Unnatural.parseDateTime("DDMMYYYY", "31122700"));
    }

    @Test
    void formatDate() {
        assertEquals("/04Oct10/1213", Unnatural.formatDate("/DDLLLYY/HHmm", LocalDateTime.of(2010, 10, 4, 12, 13, 5)));
        assertEquals("20101004", Unnatural.formatDate("YYYYMMDD", LocalDate.of(2010, 10, 4)));
        assertEquals("04102010", Unnatural.formatDate("DDMMYYYY", LocalDate.of(2010, 10, 4)));
    }

    @Test
    void setArrayValues() {
        String[] destArray = new String[]{"", "", "", ""};
        String[] sourceArray = new String[]{"A", "B", "C", "D"};

        Unnatural.setArrayValues(destArray, ArrayIndexRange.range(0, 2), sourceArray);
        assertArrayEquals(new String[]{"A", "B", "C", ""}, destArray);
    }

    @Test
    void padLeftAlignedString() {
        assertEquals("HELLO   ", Unnatural.padLeftAlignedString("HELLO", 8));
        assertEquals("        ", Unnatural.padLeftAlignedString("", 8));
        assertEquals("        ", Unnatural.padLeftAlignedString(null, 8));
    }

    @Test
    void padRightAlignedString() {
        assertEquals("   HELLO", Unnatural.padRightAlignedString("HELLO", 8));
        assertEquals("        ", Unnatural.padRightAlignedString("", 8));
        assertEquals("        ", Unnatural.padRightAlignedString(null, 8));
    }

    @Test
    void currentDateAsint() {
        assertDateIntApproxEquivalent(LocalDate.now(), "ddMMyyyy", Unnatural.currentDateAsint("DDMMYYYY"));
    }

    @Test
    void intRange() {
        assertEquals(5, Unnatural.intRange(5, 20).startInclusive());
        assertEquals(20, Unnatural.intRange(5, 20).endInclusive());
    }

    @Test
    void anyTrueForObjectArray() {
        assertTrue(Unnatural.anyTrue(new String[]{"X123", "X456", "A234", "X567"}, s -> s.startsWith("A")));
        assertFalse(Unnatural.anyTrue(new String[]{"X123", "X456", "A234", "X567"}, s -> s.startsWith("B")));
    }

    @Test
    void inRange() {
        assertTrue(Unnatural.inRange(5, Unnatural.intRange(5, 20)));
        assertTrue(Unnatural.inRange(6, Unnatural.intRange(5, 20)));
        assertTrue(Unnatural.inRange(20, Unnatural.intRange(5, 20)));
        assertFalse(Unnatural.inRange(4, Unnatural.intRange(5, 20)));
        assertFalse(Unnatural.inRange(21, Unnatural.intRange(5, 20)));
    }

    @Test
    void trimRight() {
        assertEquals("  Value", Unnatural.trimRight("  Value   "));
        assertEquals("Value", Unnatural.trimRight("Value"));
        assertEquals("", Unnatural.trimRight(""));
        assertNull(Unnatural.trimRight(null));
    }

    private void assertDateIntApproxEquivalent(LocalDate expected, String formatStr, int resultDateInt) {
        // about the same - given that test could be running over a date change
        // check within a day
        int expectedMin = Integer.parseInt(expected.format(DateTimeFormatter.ofPattern(formatStr)));
        int expectedHigh = Integer.parseInt(expected.plusDays(1).format(DateTimeFormatter.ofPattern(formatStr)));
        assertTrue(resultDateInt >= expectedMin && resultDateInt <= expectedHigh);
    }

    private static final ObjWithString[] OBJ_ARRAY =
            Arrays.stream(STR_ARRAY).map(ObjWithString::new).toList()
                    .toArray(new ObjWithString[STR_ARRAY.length]);


    private static class ObjWithString {
        private final String myStr;

        private ObjWithString(String myStr) {
            this.myStr = myStr;
        }

        public String getMyStr() {
            return myStr;
        }
    }
}