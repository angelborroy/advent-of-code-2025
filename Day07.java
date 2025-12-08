import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Day 07: Laboratories
 *
 * Part 1: Count how many times a tachyon beam is split by splitters (^)
 * Part 2: Count total timelines using many-worlds interpretation (each split doubles timelines)
 */
public class Day07 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day07.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 07: Laboratories ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        int rows = lines.size();
        int cols = lines.getFirst().length();

        // Find starting position 'S'
        int startCol = lines.getFirst().indexOf('S');

        // Track active beam positions (using set for merging)
        Set<Integer> activeBeams = new HashSet<>();
        activeBeams.add(startCol);

        long splitCount = 0;

        // Process each row starting from row 1
        for (int row = 1; row < rows; row++) {
            String line = lines.get(row);
            Set<Integer> newBeams = new HashSet<>();

            for (int col : activeBeams) {
                if (col < 0 || col >= cols) {
                    continue;
                }

                char cell = line.charAt(col);

                if (cell == '.') {
                    // Beam passes through
                    newBeams.add(col);
                } else if (cell == '^') {
                    // Beam hits splitter
                    splitCount++;
                    if (col - 1 >= 0) {
                        newBeams.add(col - 1);
                    }
                    if (col + 1 < cols) {
                        newBeams.add(col + 1);
                    }
                }
            }

            activeBeams = newBeams;
            if (activeBeams.isEmpty()) {
                break;
            }
        }

        return splitCount;
    }

    static long solvePart2(List<String> lines) {
        int rows = lines.size();
        int cols = lines.getFirst().length();

        // Find starting position 'S'
        int startCol = lines.getFirst().indexOf('S');

        // Track timeline counts at each position
        Map<Integer, Long> timelineCounts = new HashMap<>();
        timelineCounts.put(startCol, 1L);

        // Process each row starting from row 1
        for (int row = 1; row < rows; row++) {
            String line = lines.get(row);
            Map<Integer, Long> newCounts = new HashMap<>();

            for (Map.Entry<Integer, Long> entry : timelineCounts.entrySet()) {
                int col = entry.getKey();
                long count = entry.getValue();

                if (col < 0 || col >= cols) {
                    continue;
                }

                char cell = line.charAt(col);

                if (cell == '.') {
                    // Beam passes through - timelines continue
                    newCounts.merge(col, count, Long::sum);
                } else if (cell == '^') {
                    // Beam hits splitter - each timeline splits into 2
                    if (col - 1 >= 0) {
                        newCounts.merge(col - 1, count, Long::sum);
                    }
                    if (col + 1 < cols) {
                        newCounts.merge(col + 1, count, Long::sum);
                    }
                }
            }

            timelineCounts = newCounts;
            if (timelineCounts.isEmpty()) {
                break;
            }
        }

        // Sum all timeline counts
        return timelineCounts.values().stream().mapToLong(Long::longValue).sum();
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}