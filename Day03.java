import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Day 03: Adapter Array (Battery Joltage)
 *
 * Part 1: Find max 2-digit joltage from each bank by selecting 2 batteries
 * Part 2: Find max 12-digit joltage from each bank by selecting 12 batteries
 */
public class Day03 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day03.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 03: Adapter Array ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        long total = 0;
        for (String bank : lines) {
            total += maxJoltage(bank, 2);
        }
        return total;
    }

    static long solvePart2(List<String> lines) {
        long total = 0;
        for (String bank : lines) {
            total += maxJoltage(bank, 12);
        }
        return total;
    }

    /**
     * Find the maximum k-digit number by selecting exactly k digits from the bank
     * while maintaining their original order.
     *
     * Uses a greedy approach: at each step, pick the largest digit possible
     * while ensuring enough digits remain to complete the selection.
     */
    static long maxJoltage(String bank, int k) {
        int n = bank.length();
        long result = 0;
        int start = 0;

        for (int i = 0; i < k; i++) {
            int remainingToPick = k - i;
            int end = n - remainingToPick + 1;

            char bestDigit = '0';
            int bestPos = start;

            for (int p = start; p < end; p++) {
                if (bank.charAt(p) > bestDigit) {
                    bestDigit = bank.charAt(p);
                    bestPos = p;
                }
            }

            result = result * 10 + (bestDigit - '0');
            start = bestPos + 1;
        }

        return result;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}