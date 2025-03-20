import java.util.*;

/**
 * Finds a twin prime in the given range [min, max].
 * We return the larger prime of the twin (i.e., if (p-2, p) is a pair, we return p).
 */
public class TwinPrimeGenerator {

    // Public static method that generates and returns the twin prime
    // in the range [min, max]. If none is found, returns -1.
    public static int generateTwinPrime(int min, int max) {
        // Just a simple approach since the range is small (e.g., 95500..96000)
        for (int candidate = min; candidate <= max; candidate++) {
            if (candidate - 2 >= min) {
                if (isPrime(candidate) && isPrime(candidate - 2)) {
                    // Return the larger prime of the pair
                    return candidate;
                }
            }
        }
        // If none found, return -1 or throw an exception if preferred
        return -1;
    }

    // Checks if a number is prime (basic trial division).
    private static boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        if (num == 2 || num == 3) {
            return true;
        }
        if (num % 2 == 0) {
            return false;
        }
        int limit = (int) Math.sqrt(num);
        for (int i = 3; i <= limit; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
