import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Day 06: Trash Compactor
 *
 * Part 1: Read numbers per row (left-to-right problems).
 * Part 2: Read numbers per column (right-to-left cephalopod problems).
 *
 */
public class Day06 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day06.txt"));
        char[][] grid = buildGrid(lines);

        long start = System.nanoTime();
        long part1 = solve(grid, false);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solve(grid, true);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 06: Trash Compactor ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    private static long solve(char[][] grid, boolean byColumn) {
        int h = grid.length;
        long total = 0;

        for (int[] seg : findSegments(grid)) {
            int start = seg[0], end = seg[1];
            List<Long> nums = byColumn
                    ? extractByColumn(grid, start, end, h - 1)
                    : extractByRow(grid, start, end, h - 1);

            if (!nums.isEmpty()) {
                total += compute(nums, findOperator(grid[h - 1], start, end));
            }
        }
        return total;
    }

    private static List<Long> extractByRow(char[][] grid, int start, int end, int rows) {
        List<Long> nums = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            long value = 0;
            boolean found = false;
            for (int c = start; c < end; c++) {
                char ch = grid[r][c];
                if (Character.isDigit(ch)) {
                    found = true;
                    value = value * 10 + (ch - '0');
                } else if (found) break;
            }
            if (found) nums.add(value);
        }
        return nums;
    }

    private static List<Long> extractByColumn(char[][] grid, int start, int end, int rows) {
        List<Long> nums = new ArrayList<>();
        for (int c = start; c < end; c++) {
            long value = 0;
            boolean found = false;
            for (int r = 0; r < rows; r++) {
                char ch = grid[r][c];
                if (Character.isDigit(ch)) {
                    found = true;
                    value = value * 10 + (ch - '0');
                }
            }
            if (found) nums.add(value);
        }
        return nums;
    }

    private static long compute(List<Long> nums, char op) {
        long result = (op == '+') ? 0 : 1;
        for (long v : nums) {
            result = (op == '+') ? result + v : result * v;
        }
        return result;
    }

    private static char[][] buildGrid(List<String> lines) {
        int h = lines.size();
        int w = lines.stream().mapToInt(String::length).max().orElse(0);
        char[][] grid = new char[h][w];
        for (int r = 0; r < h; r++) {
            String line = lines.get(r);
            for (int c = 0; c < w; c++) {
                grid[r][c] = (c < line.length()) ? line.charAt(c) : ' ';
            }
        }
        return grid;
    }

    private static List<int[]> findSegments(char[][] grid) {
        int w = grid[0].length;
        List<int[]> segments = new ArrayList<>();
        int c = 0;
        while (c < w) {
            while (c < w && isEmptyColumn(grid, c)) c++;
            if (c >= w) break;
            int start = c;
            while (c < w && !isEmptyColumn(grid, c)) c++;
            segments.add(new int[]{start, c});
        }
        return segments;
    }

    private static boolean isEmptyColumn(char[][] grid, int c) {
        for (char[] row : grid) {
            if (row[c] != ' ') return false;
        }
        return true;
    }

    private static char findOperator(char[] row, int start, int end) {
        for (int c = start; c < end; c++) {
            if (row[c] == '+') return '+';
        }
        return '*';
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}