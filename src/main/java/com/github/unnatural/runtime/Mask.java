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

/*
Character 	Meaning
. or ? or _ 	A period, question mark or underscore indicates a single position that is not to be checked.
* or % 	An asterisk or percent mark is used to indicate any number of positions not to be checked.
/ 	A slash (/) is used to check if a value ends with a specific character (or string of characters).

For example, the following condition will be true if there is either an E in the last position of the field, or the last E in the field is followed by nothing but blanks:

IF #FIELD = MASK (*'E'/)

A 	The position is to be checked for an alphabetical character (upper or lower case).
'c'

One or more positions are to be checked for the characters bounded by apostrophes.

Alphanumeric characters with hexadecimal numbers lower than H'40' (blank) are not allowed.

The characters to be checked for are dependent on the TQMARK parameter:

    if TQMARK=ON a quotation mark (“) is checked for an apostrophe (‘)

    if TQMARK=OFF a quotation mark (“) is checked for a quotation mark (“).

If operand1 is in Unicode format, 'c' must contain Unicode characters.
C 	The position is to be checked for an alphabetical character (upper or lower case), a numeric character, or a blank.
DD 	The two positions are to be checked for a valid day notation (01 - 31; dependent on the values of MM and YY/YYYY, if specified; see also Checking Dates).
H 	The position is to be checked for hexadecimal content (A - F, 0 - 9).
JJJ 	The positions are to be checked for a valid Julian Day; that is, the day number in the year (001-366, dependent on the value of YY/YYYY, if specified. See also Checking Dates.)
L 	The position is to be checked for a lower-case alphabetical character (a - z).
MM 	The positions are to be checked for a valid month (01 - 12); see also Checking Dates.
N 	The position is to be checked for a numeric digit.
n... 	One (or more) positions are to be checked for a numeric value in the range 0 - n.
n1-n2 or n1:n2 	The positions are checked for a numeric value in the range n1-n2.

n1 and n2 must be of the same length.
P 	The position is to be checked for a displayable character (U, L, N or S).
S 	The position is to be checked for special characters.
U 	The position is to be checked for an upper-case alphabetical character (A - Z).
X 	The position is to be checked against the equivalent position in the value (operand2) following the mask-definition.

X is not allowed in a variable mask definition, as it makes no sense.
YY 	The two positions are to be checked for a valid year (00 - 99). See also Checking Dates.
YYYY 	The four positions are checked for a valid year (0000 - 2699). Use the COMPOPT option MASKCME=ON to restrict the range of valid years to 1582 - 2699; see also Checking Dates. If the profile parameter MAXYEAR is set to 9999, the upper year limit is 9999.
Z 	The position is to be checked for a character whose left half-byte is hexadecimal A - F, and whose right half-byte is hexadecimal 0 - 9.

This may be used to correctly check for numeric digits in negative numbers. With N (which indicates a position to be checked for a numeric digit), a check for numeric digits in negative numbers leads to incorrect results, because the sign of the number is stored in the last digit of the number, causing that digit to be hexadecimal represented as non-numeric.

Within a mask, use only one Z for each sequence of numeric digits that is checked.
 */
public class Mask {
    private final String constant;
    private final String operand;

    public Mask(String constant, String operand) {
        this.constant = constant;
        this.operand = operand;
    }
    public Mask(String constant) {
        this.constant = constant;
        this.operand = null;
    }

    public String getConstant() {
        return constant;
    }

    public boolean isDynamicMask() {
        return operand != null;
    }
}
