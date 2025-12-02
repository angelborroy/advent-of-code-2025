import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Day 02: Invalid Product IDs
 *
 * Part 1: Find invalid IDs where a digit sequence is repeated exactly twice
 * Part 2: Find invalid IDs where a digit sequence is repeated at least twice
 */
public class Day02 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day02.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 01: Invalid Product IDs ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        String input = String.join("", lines).trim();
        String[] ranges = input.split(",");

        long totalSum = 0;

        for (String range : ranges) {
            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            for (long num = start; num <= end; num++) {
                if (isInvalidPart1(num)) {
                    totalSum += num;
                }
            }
        }

        return totalSum;
    }

    static long solvePart2(List<String> lines) {
        String input = String.join("", lines).trim();
        String[] ranges = input.split(",");

        long totalSum = 0;

        for (String range : ranges) {
            String[] parts = range.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            for (long num = start; num <= end; num++) {
                if (isInvalidPart2(num)) {
                    totalSum += num;
                }
            }
        }

        return totalSum;
    }

    /**
     * Part 1: Check if a number is formed by repeating a digit sequence exactly twice.
     * Examples: 11 (1 twice), 6464 (64 twice), 123123 (123 twice)
     */
    static boolean isInvalidPart1(long num) {
        String str = String.valueOf(num);
        int len = str.length();

        // Must have even length to be split into two equal halves
        if (len % 2 != 0) {
            return false;
        }

        int halfLen = len / 2;
        String firstHalf = str.substring(0, halfLen);
        String secondHalf = str.substring(halfLen);

        return firstHalf.equals(secondHalf);
    }

    /**
     * Part 2: Check if a number is formed by repeating a digit sequence at least twice.
     * Examples: 111 (1 three times), 565656 (56 three times), 2121212121 (21 five times)
     */
    static boolean isInvalidPart2(long num) {
        String str = String.valueOf(num);
        int len = str.length();

        // Try all possible pattern lengths from 1 to len/2
        // If the pattern repeats at least twice, the pattern length is at most len/2
        for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
            // Check if the length is divisible by the pattern length
            if (len % patternLen == 0) {
                String pattern = str.substring(0, patternLen);
                boolean isRepeating = true;

                // Check if the entire string is made of this pattern repeated
                for (int i = patternLen; i < len; i += patternLen) {
                    String segment = str.substring(i, i + patternLen);
                    if (!segment.equals(pattern)) {
                        isRepeating = false;
                        break;
                    }
                }

                if (isRepeating) {
                    return true;
                }
            }
        }

        return false;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}