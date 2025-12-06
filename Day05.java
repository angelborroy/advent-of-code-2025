import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Day 05: Cafeteria
 *
 * Part 1: Count how many available ingredient IDs are fresh (fall within any range)
 * Part 2: Count total unique ingredient IDs considered fresh by all ranges
 */
public class Day05 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day05.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 05: Cafeteria ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        int blankLineIndex = findBlankLine(lines);
        List<long[]> ranges = parseRanges(lines, blankLineIndex);
        List<Long> ingredientIds = parseIngredientIds(lines, blankLineIndex);

        long freshCount = 0;
        for (long id : ingredientIds) {
            if (isFresh(id, ranges)) {
                freshCount++;
            }
        }
        return freshCount;
    }

    static long solvePart2(List<String> lines) {
        int blankLineIndex = findBlankLine(lines);
        List<long[]> ranges = parseRanges(lines, blankLineIndex);

        // Sort ranges by start value
        ranges.sort(Comparator.comparingLong(a -> a[0]));

        // Merge overlapping ranges
        List<long[]> merged = new ArrayList<>();
        for (long[] range : ranges) {
            if (!merged.isEmpty() && range[0] <= merged.getLast()[1] + 1) {
                // Overlapping or adjacent - extend the previous range
                long[] last = merged.getLast();
                last[1] = Math.max(last[1], range[1]);
            } else {
                // New separate range
                merged.add(new long[]{range[0], range[1]});
            }
        }

        // Count total fresh IDs
        long total = 0;
        for (long[] range : merged) {
            total += range[1] - range[0] + 1;
        }
        return total;
    }

    static int findBlankLine(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    static List<long[]> parseRanges(List<String> lines, int blankLineIndex) {
        List<long[]> ranges = new ArrayList<>();
        for (int i = 0; i < blankLineIndex; i++) {
            String[] parts = lines.get(i).split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new long[]{start, end});
        }
        return ranges;
    }

    static List<Long> parseIngredientIds(List<String> lines, int blankLineIndex) {
        List<Long> ids = new ArrayList<>();
        for (int i = blankLineIndex + 1; i < lines.size(); i++) {
            ids.add(Long.parseLong(lines.get(i)));
        }
        return ids;
    }

    static boolean isFresh(long id, List<long[]> ranges) {
        for (long[] range : ranges) {
            if (id >= range[0] && id <= range[1]) {
                return true;
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