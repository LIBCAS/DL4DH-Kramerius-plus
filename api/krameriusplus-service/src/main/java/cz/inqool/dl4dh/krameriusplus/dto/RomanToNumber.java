package cz.inqool.dl4dh.krameriusplus.dto;

/**
 * @author Norbert Bodnar
 */
public class RomanToNumber {
    // This function returns a value of a Roman symbol
    public static int value(char symbol) {
        switch (symbol) {
            case 'I':
                return 1;
            case 'V':
                return 5;
            case 'X':
                return 10;
            case 'L':
                return 50;
            case 'C':
                return 100;
            case 'D':
                return 500;
            case 'M':
                return 1000;
            default:
                throw new IllegalArgumentException("Not a roman character");
        }
    }

    // Finds decimal value of a given roman numeral
    public static int romanToDecimal(String romanNumber) {
        // Initialize result
        int result = 0;

        for (int i = 0; i < romanNumber.length(); i++) {
            // Getting value of symbol s[i]
            int currentSymbol = value(romanNumber.charAt(i));

            // Getting value of symbol s[i+1]
            if (i + 1 < romanNumber.length()) {
                int nextSymbol = value(romanNumber.charAt(i + 1));

                // Comparing both values
                if (currentSymbol >= nextSymbol) {
                    // Value of current symbol is greater or equal to the next symbol
                    result = result + currentSymbol;
                } else {
                    // Value of current symbol is less than the next symbol
                    result = result + nextSymbol - currentSymbol;
                    i++;
                }
            } else {
                result = result + currentSymbol;
            }
        }

        return result;
    }
}
