package introduction;

public class MathUtils {

    private static final int FIRST_PRIME_NUMBER = 2;
    private static final int DEC_DIVISOR_BASE_MODULO = 10;

    /**
     * Returns the greatest common divider of given two numbers
     *
     * @param firstNumber  - positive number
     * @param secondNumber - positive number
     * @return greatest common divider of two numbers
     */
    public int getGreatestCommonDivider(int firstNumber, int secondNumber) {
        if (secondNumber == 0) {
            return Math.abs(firstNumber);
        }
        return getGreatestCommonDivider(secondNumber, firstNumber % secondNumber);
    }

    /**
     * Returns sum of digits of the given number
     *
     * @param number - positive number
     * @return the sum of digits of the given number
     */
    public int getSumOfDigits(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Negative number.");
        }

        int result = 0;
        int i = 1;
        do {
            result += number % Math.pow(DEC_DIVISOR_BASE_MODULO, i) / Math.pow(DEC_DIVISOR_BASE_MODULO, i - 1);
        } while (number % Math.pow(DEC_DIVISOR_BASE_MODULO, i++) != number);
        return result;
    }

    /**
     * Checks if the given number is prime or not
     *
     * @param number
     * @return true - if number is prime, if not return false
     */
    public boolean isPrime(int number) {
        number = Math.abs(number);
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns sum of row: 1! - 2! + 3! - 4! + 5! - ... + n!
     *
     * @param n - positive number
     */
    public int getSumOfRow(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Negative row length.");
        }

        long result = 0;
        if (n == 0) {
            return (int) result;
        }
        result = 1;
        for (int i = 2; i < n; i = i + 2) {
            result += factorial(i) * i;
        }
        return (int) (n % 2 == 0 ? result - factorial(n) : result);
    }

    /**
     * Returns Fibonacci series of a specified length
     *
     * @param length - the length of the Fibonacci series
     * @return array filled with Fibonacci series
     */
    public int[] getFibonacciSeries(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Negative series length.");
        }

        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = (i < 2) ? 1 : (result[i - 1] + result[i - 2]);
        }
        return result;
    }

    /**
     * Returns array with prime numbers
     *
     * @param length - the length of prime numbers series
     * @return array filled with prime numbers
     */
    public int[] getPrimeSeries(int length) {
        if (length < 0){
            throw new IllegalArgumentException("Negative series length.");
        }

        int[] result = new int[length];
        if (length == 0) {
            return result;
        }
        result[0] = FIRST_PRIME_NUMBER;
        int currentPrime = FIRST_PRIME_NUMBER + 1;
        int i = 1;
        while (i < length) {
            for (int j = 0; currentPrime % result[j] != 0; j++) {
                if (result[j] > Math.sqrt(currentPrime) || result[j + 1] == 0) {
                    result[i++] = currentPrime;
                    break;
                }
            }
            currentPrime++;
        }
        return result;
    }

    /**
     * Calculates factorial value of an integer.
     *
     * @param n - non-negative integer
     * @return factorial of n
     */
    private long factorial(int n) {
        if(n < 0){
            throw new IllegalArgumentException("Impossible to get factorial from negative number.");
        }

        long result = 1L;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}