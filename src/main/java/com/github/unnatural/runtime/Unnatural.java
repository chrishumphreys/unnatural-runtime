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

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoField.*;

@Slf4j
public class Unnatural {
    private static final String USERNAME = "TestUser";
    private static final String PROGRAM = "TestProgram";
    private static final Pattern ARRAY_ATTRIBUTE = Pattern.compile("(?<name>\\S+)\\[(?<array>\\d+),(?<type>.*)\\]");

    private static final Map<String, AdabasStore<?>> stores = new HashMap<>();
    private static final String DATE_MASK = "YYYYMMDD";
    private static final String ALTERNATIVE_DATE_MASK = "DDMMYYYY";

    private static final DateTimeFormatter STANDARD_NATURAL_TIME_STR = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4)
            .appendValue(MONTH_OF_YEAR, 2)
            .appendValue(DAY_OF_MONTH, 2)
            .appendValue(HOUR_OF_DAY, 2)
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendValue(SECOND_OF_MINUTE, 2).toFormatter();
    private static final Pattern NUMERIC_RANGE_MASK_PATTERN = Pattern.compile("\\d+:\\d+");
    public static final int NATURAL_MIN_YEAR = 1582;
    public static final int NATURAL_MAX_YEAR = 2699;

    public static BigDecimal truncate(int numDigitsRightOfPoint, BigDecimal source) {
        return assign(numDigitsRightOfPoint, source, RoundingMode.DOWN);
    }

    public static BigDecimal round(int numDigitsRightOfPoint, BigDecimal source) {
        return assign(numDigitsRightOfPoint, source, RoundingMode.HALF_UP);
    }

    private static BigDecimal assign(int numDigitsRightOfPoint, BigDecimal source, RoundingMode roundingMode) {
        return source.setScale(numDigitsRightOfPoint, roundingMode);
    }

    public static String truncate(int length, String source) {
        return source.substring(0, Math.min(source.length(), length));
    }

    public static String rightJustify(int length, String source) {
        String src = source.length() > length ? source.substring(0, length) : source;
        String pad = " ".repeat(length - src.length());
        return pad + src;
    }

    public static void setKey(PFKey pfKey, SetKeyCommand command) {
        log.info("setKey {} {}", pfKey.name(), command.name());
    }

    public static void inputFromScreen(NaturalObject data, NaturalMap mapDefinition, InputText inputText, String... attributeMappings) {
        /*System.out.println(screen.getScreenRender());
        Arrays.stream(outputs).forEach(System.out::println);

        for (String attribute : inputVarNames) {
            Scanner scanner = new Scanner(System.in);
            Matcher matcher = ARRAY_ATTRIBUTE.matcher(attribute);
            if (matcher.matches()) {
                attribute = matcher.group("name");
                int number = Integer.parseInt(matcher.group("array"));
                String clazz = matcher.group("type");
                List<String> userInput = new ArrayList<>();
                for (int arrayPart = 0; arrayPart < number; arrayPart++) {
                    System.out.print(attribute + ": ");
                    userInput.add(scanner.next());
                }
                try {
                    screen.setAttribute(attribute, userInput, Class.forName(clazz));
                } catch (ClassNotFoundException e) {
                    throw new NaturalException(e.getMessage());
                }
            } else {
                System.out.print(attribute + ": ");
                String userString = scanner.next();
                screen.setAttribute(attribute, userString);
            }
        }
*/
    }

    public static String inputPFKey() {
        return "";
    }

    @Deprecated
    public static void exit() {
        throw new NaturalExitedException("Natural program exited");
    }

    public static BigDecimal mapPos(Object action) {
        return new BigDecimal(0);
    }

    public static String formatDate(String formatStr, LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(convertNaturalDateFormat(formatStr)));

    }

    public static String formatDateDefault(LocalDate date) {
        return null;
    }

    public static String formatDate(String formatStr, LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(convertNaturalDateFormat(formatStr)));
    }

    // There is a problem with this function, it is called when wanting dates or times (not timestamps)
    // and no way of knowing which is wanted
    @Deprecated
    public static String formatDateDefault(LocalDateTime auditTime) {
        return null;
    }

    @Deprecated
    public static <T> List<T> execQuery(AdabasOperation adabasOperation, String view, String where, int count) {
        return Collections.emptyList();
    }

    @Deprecated
    public static <T> List<T> execQuery(AdabasOperation adabasOperation, String view, String where, int count, Object... params) {
        return Collections.emptyList();
    }

    @Deprecated
    public static int execCountQuery(AdabasOperation adabasOperation, String view, String where) {
        return 0;
    }

    @Deprecated
    public static <T> T getByISN(String viewName, BigDecimal isn) {
        return null;
    }

    @Deprecated
    public static <T> T getByISN(String viewName, int isn) {
        return null;
    }

    @Deprecated
    public static <T> void update(T updateViewGetForModify) {
    }

    public static void endTransaction() {

    }

    public static boolean inputModified(String attributeName) {
        return false;
    }

    @Deprecated
    public static <T> List<T> readBy(String viewName, String where) {
        return Collections.emptyList();
    }

    @Deprecated
    public static <T> List<T> readBy(String viewName, String where, Object... params) {
        return Collections.emptyList();
    }

    @Deprecated
    public static String programName() {
        return PROGRAM;
    }

    @Deprecated
    public static String userName() {
        return USERNAME;
    }

    public static <T> void delete(String tableName, T updateViewGetForPurge) {
    }

    @Deprecated
    public static void reset(Object attribute) {

    }

    @Deprecated
    public static int store(String viewName, AdabasRecord updateView) {
        return 0;
    }

    public static <T extends AdabasRecord> void createStore(String tableName, Class<T> clazz) {
        stores.put(tableName, new AdabasStore<T>());
    }

    public static String messageFor(String number) {
        return "message";
    }

    public static boolean matchesMask(String inputStr, Mask mask) {
        if (inputStr == null) {
            return false;
        }
        if (mask.isDynamicMask()) {
            throw new NaturalException("Unsupported dynamic mask");
        }
        if (isNumericRangeMask(mask)) {
            return strMatchesNumericRange(inputStr, mask);
        } else {
            // any other mask, check the characters
            String regexStr = convertMaskToRegex(mask);
            boolean valid = Pattern.matches(regexStr, inputStr);
            // finally check for any date components in the string
            return valid && validateForDateComponents(mask, inputStr);
        }
    }

    private static boolean validateForDateComponents(Mask mask, String inputStr) {
        Optional<DateMaskComponent> day = extractOptionalMaskComponent("DD", mask, inputStr, 1, 31);
        Optional<DateMaskComponent> month = extractOptionalMaskComponent("MM", mask, inputStr, 1, 12);
        Optional<DateMaskComponent> yearWithCentury = extractOptionalMaskComponent("YYYY", mask, inputStr, NATURAL_MIN_YEAR, NATURAL_MAX_YEAR);
        Optional<DateMaskComponent> yearWithoutCentury = extractOptionalMaskComponent("YY", mask, inputStr, 0, 99);

        boolean validValuesIfPresent =  day.map(DateMaskComponent::valid).orElse(true)
                && month.map(DateMaskComponent::valid).orElse(true)
                && yearWithCentury.map(DateMaskComponent::valid).orElse(true)
                && yearWithoutCentury.map(DateMaskComponent::valid).orElse(true);

        if (!validValuesIfPresent) {
            return false;
        }

        if (day.isPresent()) {
            // check date
            int dayValue = day.get().value();
            int monthValue = month.map(DateMaskComponent::value).orElse(getCurrentMonth());
            int yearValue = yearWithCentury.map(DateMaskComponent::value).orElse(yearWithoutCentury.map(DateMaskComponent::value).orElse(getCurrentYear()));
            if (yearValue < 100) {
                yearValue += getCurrentCentury();
            }
            try {
                LocalDate.of(yearValue, monthValue, dayValue);
                return true;
            } catch (DateTimeException e) {
                return false;
            }
        }
        return true;
    }

    private static int getCurrentCentury() {
        return LocalDate.now().getYear() % 100;
    }

    private static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    private static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();

    }

    private record DateMaskComponent(boolean valid, int value) { }

    private static Optional<DateMaskComponent> extractOptionalMaskComponent(String maskExpr, Mask mask, String inputStr, int minValue, int maxValue) {
        String maskStr = mask.getConstant();
        try {
            int start = maskStr.indexOf(maskExpr);
            if (start > -1) {
                int number = Integer.parseInt(inputStr.substring(start, start + maskExpr.length()));
                boolean valid = number >= minValue && number <= maxValue;
                return Optional.of(new DateMaskComponent(valid, number));
            } else {
                return Optional.empty();
            }
        } catch (NumberFormatException e){
            return Optional.of(new DateMaskComponent(false, 0));

        }
    }

    private static boolean strMatchesNumericRange(String inputStr, Mask mask) {
        String maskStr = mask.getConstant();
        String[] parts = maskStr.split(":");
        BigInteger from = new BigInteger(parts[0]);
        BigInteger to = new BigInteger(parts[1]);
        BigInteger value = new BigInteger(inputStr);
        return (value.compareTo(from) > -1) && (value.compareTo(to) < 1);
    }

    private static boolean isNumericRangeMask(Mask mask) {
        String naturalMaskStr = mask.getConstant();
        return NUMERIC_RANGE_MASK_PATTERN.matcher(naturalMaskStr).matches();
    }

    private static String convertMaskToRegex(Mask maskStr) {
        StringBuilder result = new StringBuilder();
        String naturalMaskStr = maskStr.getConstant();
        boolean inLiteral = false;
        for (int pos = 0; pos < naturalMaskStr.length(); pos++) {
            char character = naturalMaskStr.charAt(pos);
            if (inLiteral) {
                if (character == '\'') {
                    inLiteral = false;
                } else if (character == '\\') {
                    throw new NaturalException("Currently unsupported mask value " + character);
                } else {
                    result.append(character);
                }
            } else {
                if (character == 'U') {
                    result.append("[A-Z]");
                } else if (character == 'N') {
                    result.append("\\d");
                } else if (character == 'Y') {
                    result.append("\\d");  // TODO not technically true - should be validating this is a valid year digit
                } else if (character == 'M') {
                    result.append("\\d");  // TODO not technically true - should be validating this is a valid month digit
                } else if (character == 'D') {
                    result.append("\\d");  // TODO not technically true - should be validating this is a valid day digit
                } else if (character == 'C') {
                    result.append("[A-Za-z\\s\\d]");
                } else if (character == '/') {
                    result.append("/");
                } else if (character == '0') {
                    result.append("0");
                } else if (character == '1' || character == '2' || character == '3' || character == '4' ||
                        character == '5' || character == '6' || character == '7' || character == '8' || character == '9') {
                    result.append("[0-9]");
                } else if (character == 'P') {
                    result.append("[\\w\\s]");
                } else if (character == '\'') {
                    inLiteral = true;
                } else {
                    throw new NaturalException("Currently unsupported mask value " + character);
                }
            }
        }
        // ignore any trailing chars in input
        result.append(".*");
        return result.toString();
    }

    public static LocalDate parseDate(String mask, String dateStr) {
        if (DATE_MASK.equals(mask)) {
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
        } else if (ALTERNATIVE_DATE_MASK.equals(mask)) {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("ddMMyyyy"));
        } else {
            throw new NaturalException("Currently unsupported mask value " + mask);
        }
    }

    @Deprecated
    public static int programLevelNumber() {
        return 0;
    }

    @Deprecated
    public static String userId() {
        return null;
    }

    public static <T extends NaturalObject> List<T> indexRead(Class<T> clazz, String vehicle, String s, String s1, IndexReadDescriptor descriptorsAll) {
        return null;
    }

    public static String hexToString(LocalDateTime now, int i) {
        return null;
    }

    public static String libraryId() {
        return null;
    }

    public static int languageCode() {
        return 0;
    }

    public static int countMatches(String eText, String s) {
        return 0;
    }

    @Deprecated
    public static String device() {
        return "";
    }

    public static UnnaturalRange strIntRange(String number, String number1) {
        return null;
    }

    public static String repeatToLength(String str, int times) {
        return str.repeat(times);
    }

    public static String repeatToLength(int s, int i) {
        return null;
    }

    /*
   should return -1 for no match
   index (from 0) for match
    */
    public static int findIndexOfMatchInStringArray(String[] strArray, String toFind, int matchNumber) {
        return findIndexOfMatchInStringArray(strArray, Function.identity(), toFind, matchNumber);
    }

    /*
    should return -1 for no match
    index (from 0) for match
     */
    public static <T> int findIndexOfMatchInStringArray(T[] array, Function<T, String> accessor, String toFind, int matchNumber) {
        int matchesFound = 0;
        for(int index = 0; index < array.length; index++) {
            String value = accessor.apply(array[index]);
            if (value == null) {
                if (toFind == null) {
                    matchesFound++;
                }
            } else if (value.equals(toFind)){
                matchesFound++;
            }
            if (matchesFound == matchNumber) {
                return index;
            }
        }
        return -1;
    }

    public static void replaceInStringArray(String[] strArray, String toFind, String toReplace) {
    }

    public static <T> void replaceInStringArray(T[] strArray, Function<T, String> accessor, String toFind, String toReplace) {
    }

    public static boolean canBeConvertedTo(String rc, Type type) {
        return false;
    }

    public static UnnaturalRange intRange(int startInclusive, int endInclusive) {
        return new UnnaturalRange(startInclusive, endInclusive);
    }

    public static boolean inRange(BigDecimal number, UnnaturalRange range) {
        return false;
    }

    public static boolean inRange(int number, UnnaturalRange range) {
        return range.withinRange(number);
    }

    public static boolean inRange(String string, UnnaturalRange range) {
        return false;
    }

    public static void setControl(String n) {

    }

    public static void reset(Object[] array, ArrayIndexRange all, String field) {

    }

    public static void setArrayValues(Object[] array, ArrayIndexRange range, Object[] value) {
        int destStart = range.isAll() ? 0 : range.getStart();
        int destEnd = range.isAll() ? array.length - 1 : range.getEnd();
        for (int index = destStart, sourceIndex = 0; index <= destEnd; index++, sourceIndex++) {
            array[index] = value[sourceIndex];
        }
    }

    public static <T> void setArrayValues(T[] array, ArrayIndexRange range, T value) {
    }

    public static void setArrayValues(boolean[] array, ArrayIndexRange range, boolean value) {
    }

    public static void setArrayValues(int[] array, ArrayIndexRange range, int[] values) {
    }

    public static void setArrayValues(int[] array, ArrayIndexRange range, int value) {
    }

    public static int getInputStackSize() {
        return 0;
    }

    public static Object[] readArrayOfStrings(ArrayIndexRange range) {
        return new Object[0];
    }

    public static String setSubstring(String string, int startInclusive, int endExclusive, String value) {
        int numberToReplace = endExclusive - startInclusive;
        if (value.length() > numberToReplace) {
            // truncate supplied string to supplied length if greater
            value = value.substring(0, numberToReplace);
        } else if (value.length() < numberToReplace) {
            // pad with space if supplied string is smaller than area
            String pad = " ".repeat(numberToReplace - value.length());
            value += pad;
        }

        return string.substring(0, startInclusive) + value + string.substring(endExclusive);
    }

    public static void setSubstring(NaturalObject hasAsStringSetter, int start, int end,  String value) {
    }

    @Deprecated
    public static String initProgramName() {
        return null;
    }

    public static String readStringInput() {
        return null;
    }

    public static int findPositionInArrayOfStrings(String[] array, String str) {
        return 0;
    }

    public static boolean matchesMask(BigDecimal number, Mask mask) {
        return false;
    }

    public static boolean matchesMask(int number, Mask mask) {
        return matchesMask(Integer.toString(number), mask);
    }

    public static BigDecimal consoleReadNumeric() {
        return null;
    }

    public static int lineNumber() {
        return 0;
    }

    public static void setWhOn() {

    }

    public static void setWhOff() {
    }

    public static LocalDateTime parseDateTime(String naturalDateFormatStr, String dateTimeStr) {
        if (naturalDateFormatStr.contains("mm") && naturalDateFormatStr.contains("HH")) {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(convertNaturalDateFormat(naturalDateFormatStr)));
        } else {
            return LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern(convertNaturalDateFormat(naturalDateFormatStr))).atStartOfDay();
        }
    }

    // Format LocalDateTime as a natural string yyyymmddHHMMSS
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(STANDARD_NATURAL_TIME_STR);
    }

    @Deprecated
    public static void moveByName(NaturalObject src, NaturalObject dest) {
        Field[] srcFields = src.getClass().getDeclaredFields();
        Arrays.stream(srcFields).forEach((srcField) ->
                getField(dest.getClass(), srcField.getName()).ifPresent(
                    (destField -> copyValue(srcField, src, destField, dest)))
        );
    }

    @Deprecated
    private static void copyValue(Field srcField, NaturalObject src, Field destField, NaturalObject dest) {
        try {
            srcField.setAccessible(true);
            destField.setAccessible(true);
            destField.set(dest, srcField.get(src));
        } catch (IllegalAccessException e) {
            log.warn("Unable to copy UnaturalField {} on class {} to class {} - {}", srcField.getName(),
                    src.getClass().getName(), dest.getClass().getName(), e.getMessage());
        }
    }

    private static boolean classHasField(Class<? extends NaturalObject> clazz, String fieldName) {
        return Arrays.stream(clazz.getDeclaredFields()).anyMatch(f -> f.getName().equals(fieldName));
    }

    static Optional<Field> getField(Class<? extends NaturalObject> clazz, String fieldName) {
        if (classHasField(clazz, fieldName)){
            try {
                return Optional.of(clazz.getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public static void translateStringInverted(String ssReference, String[] trans) {

    }

    public static void pushToInputStack(Object... string) {

    }

    public static void resetInitial(Object duplicate) {

    }

    public static BigDecimal currentDateAsBigDecimal(String format) {
        return null;
    }

    public static boolean anyTrue(int[] intArray, Function<Integer, Boolean> condition) {
        return false;
    }

    public static boolean anyTrue(boolean[] boolArray, Function<Boolean, Boolean> condition) {
        return false;
    }

    public static boolean anyTrue(boolean[] boolArray) {
        return false;
    }

    public static <T> boolean anyTrue(T[] array, Function<T, Boolean> condition) {
        return Arrays.stream(array).anyMatch(condition::apply);
    }

    public static int currentDateAsint(String format) {
        return Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern(convertNaturalDateFormat(format))));
    }

    public static int readIntegerInput() {
        return 0;
    }

    public static String blankString(int length) {
        return null;
    }

    public static NaturalProgram programByName(String fetchProg) {
        return null;
    }

    public static void readGroupInput(NaturalObject naturalObject) {

    }

    public static void markCurrentTransactionForRollback() {
    }

    public static boolean onlyOneSelected(String[] slice) {
        return false;
    }

    public static boolean readBoolean() {
        return false;
    }

    @Deprecated
    public static int getErrorNumber() {
        return 0;
    }

    public static void setErrorNumber(int errorNum) {

    }

    public static String currentDateAsString(String format) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(convertNaturalDateFormat(format)));
    }

    private static String convertNaturalDateFormat(String format) {
        return format.replaceAll("DD", "dd")
                .replaceAll("YY", "yy");
    }

    @Deprecated
    public static String hostname() {
        return "";
    }

    @Deprecated
    public static int getErrorLine() {
        return 0;
    }

    public static void fillCharArray(String[] charArray, String string) {
        fillStringArray(charArray, string, 1);
    }
    public static void fillStringArray(String[] stringArray, String string, int numCharsPerArrayItem) {
        Arrays.fill(stringArray, "");
        int endlength = Math.min(string.length(), stringArray.length * numCharsPerArrayItem);
        for (int pos = 0; pos < endlength; pos += numCharsPerArrayItem) {
            int index = pos / numCharsPerArrayItem;
            stringArray[index] = string.substring(pos, Math.min(pos + numCharsPerArrayItem, string.length()));
        }
    }

    public static String unpackCharArray(String[] charArray) {
        StringBuilder b = new StringBuilder();
        for (int c = 0; c < charArray.length; c++) {
            b.append(charArray[c]);
        }
        return b.toString();

    }

    /*
    convert supplied integer to a string, right aligned, padded with zeros up to specified numDigits
     */
    public static String formatIntegerToString(int integer, int numDigits) {
        return String.format("%0" + numDigits + "d", integer);
    }

    // convert supplied bigdecimal to natural string representation (no decimal point, right aligned, zero padded)
    public static String formatNaturalNumericToString(BigDecimal number, int numberOfDigitsBeforeDecimalPlace, int numDecimalPlaces) {
        if (numDecimalPlaces > 0) {
            BigDecimal bd = number.multiply(new BigDecimal((int)Math.pow(10, numDecimalPlaces))).setScale(0, RoundingMode.HALF_UP);
            String str = bd.toString();
            int targetLength = numberOfDigitsBeforeDecimalPlace + numDecimalPlaces;
            return "0".repeat(targetLength - str.length()) + str;
        } else {
            String str = number.toString();
            return "0".repeat(numberOfDigitsBeforeDecimalPlace - str.length()) + str;
        }
    }

    // A natural numeric converted to string does not include a deimal place
    // that needs to be readded according to the format string
    public static BigDecimal parseNaturalNumericString(String naturalNumericString, int numberOfDigitsBeforeDecimalPlace, int numDecimalPlaces){
        boolean hasDecimalPlaces = numDecimalPlaces > 0;
        String decimalPart = hasDecimalPlaces ? "." + naturalNumericString.substring(numberOfDigitsBeforeDecimalPlace) : "";
        return new BigDecimal(naturalNumericString.substring(0, numberOfDigitsBeforeDecimalPlace) + decimalPart);
    }

    public static String formatNaturalNumericToString(int number, int numberOfDigitsBeforeDecimalPlace, int numDecimalPlaces) {
        return formatIntegerToString(number, numberOfDigitsBeforeDecimalPlace);
    }

    private static int numberOfDigitsBeforeDecimalPlaceFromFormatString(String naturalFormatString) {
        return Integer.parseInt(naturalFormatString.substring(1, naturalFormatString.indexOf(".")));
    }

    private static boolean hasDecimalPlaces(String naturalFormatStr) {
        return naturalFormatStr.contains(".");
    }

    private static int numberOfDecimalPlacesFromFormatString(String naturalFormatStr) {
        return Integer.parseInt(naturalFormatStr.substring(naturalFormatStr.indexOf(".")+1));
    }

    public static DateComponents numberToDateComponents(int nDateN) {
        int day = nDateN % 100;
        int month = (nDateN % 10000) / 100;
        int year = nDateN / 10000;
        return new DateComponents(day, month, year);
    }

    public static String dateComponentsToString(DateComponents dateComponents) {
        return Unnatural.formatIntegerToString(dateComponents.year(), 4) +
                Unnatural.formatIntegerToString(dateComponents.month(), 2) +
                Unnatural.formatIntegerToString(dateComponents.day(), 2);
    }

    public static int dateComponentsToInteger(DateComponents dateComponents) {
        return dateComponents.year() * 10000 + dateComponents.month() * 100 +
                dateComponents.day();
    }

    public static void chunkStringIntoParts(String str, int chunkLength, String[] parts) {
        int numChunksRequired = (int)Math.ceil((float) str.length() / chunkLength);
        if (numChunksRequired > parts.length) {
            throw new NaturalException("Supplied string of length " + str.length() + " cannot be chunked into " + parts.length + " of " + chunkLength);
        }
        int numChunks = (int)Math.min(parts.length, Math.ceil((float)str.length() / chunkLength));
        for (int chunk = 0; chunk < numChunks; chunk++) {
            parts[chunk] = str.substring(chunk * chunkLength, Math.min(str.length(), (chunk + 1) * chunkLength));
        }
    }

    public static String padRightAlignedString(String strValue, int desiredLength) {
        return padString(strValue, desiredLength, true);
    }

    private static String padString(String strValue, int desiredLength, boolean rightAligned) {
        if (strValue == null) {
            return " ".repeat(desiredLength);
        }
        if (strValue.length() < desiredLength) {
            if (rightAligned) {
                return " ".repeat(desiredLength - strValue.length()) + strValue;
            } else {
                return strValue + " ".repeat(desiredLength - strValue.length());
            }
        } else if (strValue.length() > desiredLength) {
            return strValue.substring(0, desiredLength);
        } else {
            return strValue;
        }
    }

    public static String padLeftAlignedString(String strValue, int desiredLength) {
        return padString(strValue, desiredLength, false);
    }

    public static String applyStringEditMaskForTarget(String editMaskStr, String strValue) {
        // Partial implementation of edit masks
        // For now support edit masks of type XXXX'/'XXXX where X copies the character and '/' asserts and drops the / character
        // if field is longer than mask, truncate field
        // if mask is longer than field ignore
        if (!editMaskStr.replaceAll("X", "").replaceAll("'/'", "").isEmpty()) {
            throw new NaturalException("Unsupported String edit mask: " + editMaskStr);
        }
        String updatedEditMask = editMaskStr.replaceAll("'", "");
        StringBuilder stringBuilder = new StringBuilder();
        for (int ch = 0; ch < strValue.length() && ch < updatedEditMask.length(); ch++) {
            char maskChar = updatedEditMask.charAt(ch);
            char strChar = strValue.charAt(ch);
            if (maskChar == 'X') {
                stringBuilder.append(strChar);
            } else if (maskChar == '/' && strChar != '/') {
                throw new NaturalException("Input does not match edit mask");
            }
        }
        return stringBuilder.toString();
    }

    public static String applyStringEditMaskForSource(String editMaskStr, String strValue) {
        // Partial implementation of edit masks
        // For now support edit masks of type XXXX'/'XXXX where X copies the character and '/' inserts the / character
        // if field is longer than mask, truncate field
        // if mask is longer than field ignore
        if (!editMaskStr.replaceAll("X", "").replaceAll("'/'", "").isEmpty()) {
            throw new NaturalException("Unsupported String edit mask: " + editMaskStr);
        }
        String updatedEditMask = editMaskStr.replaceAll("'", "");
        StringBuilder stringBuilder = new StringBuilder();
        int strValueIndex = 0;
        for (int maskIndex = 0; maskIndex < updatedEditMask.length() && strValueIndex < strValue.length(); maskIndex++) {
            char maskChar = updatedEditMask.charAt(maskIndex);
            if (maskChar == 'X') {
                char strChar = strValue.charAt(strValueIndex++);
                stringBuilder.append(strChar);
            } else if (maskChar == '/') {
                stringBuilder.append("/");
            }
        }
        return stringBuilder.toString();
    }


    public enum Type {
        NUMERIC,
        INTEGER
    }

    @Deprecated
    public static Printer getPrinterById(String id) {
        return new RecordingPrinter();
    }

    public static String encodeTimeAsNaturalString(LocalDateTime localDateTime) {
        // Natural encoded times are 7 bytes
        long millis = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime();
        // convert 8 byte long to 4 character (two byte) unicode string and pad to 7 bytes
        return "-T" + new UnicodeBinaryEncode().encodeLong(millis) + "-";
    }

    public static LocalDateTime decodeNaturalStringToTime(String naturalStringEncodedTime) {
        // extract the 4 char encoded long string from padded string and decode
        if (naturalStringEncodedTime.length() == 7 &&
                naturalStringEncodedTime.startsWith("-T") && naturalStringEncodedTime.endsWith("-")) {
            long millis = new UnicodeBinaryEncode().decodeLong(naturalStringEncodedTime.substring(2, 6));
            return LocalDateTime.ofInstant(new Date(millis).toInstant(), ZoneId.systemDefault());
        } else {
            throw new NaturalException("Invalid natural encoded time string");
        }
    }

    public static long currentTimeAsMillis() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()).getTime();
    }

    public static String trimRight(String value){
        if (value == null) {
            return null;
        }
        int i = value.length() - 1;
        while (i >= 0 && Character.isWhitespace(value.charAt(i))) {
            i--;
        }
        return value.substring(0, i + 1);
    }
}
