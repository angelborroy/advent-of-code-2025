import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Day 04: Printing Department
 *
 * Part 1: Count rolls of paper accessible by forklift (fewer than 4 adjacent rolls)
 * Part 2: Iteratively remove accessible rolls until none remain, count total removed
 */
public class Day04 {

    // 8 directions: all adjacent positions including diagonals
    private static final int[] DR = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DC = {-1, 0, 1, -1, 1, -1, 0, 1};

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day04.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 04: Printing Department ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();

        char[][] grid = parseGrid(lines);

        int accessibleCount = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == '@' && countAdjacentRolls(grid, r, c) < 4) {
                    accessibleCount++;
                }
            }
        }

        return accessibleCount;
    }

    static long solvePart2(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();

        char[][] grid = parseGrid(lines);

        long totalRemoved = 0;

        while (true) {
            // Find all accessible rolls
            List<int[]> accessible = new ArrayList<>();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == '@' && countAdjacentRolls(grid, r, c) < 4) {
                        accessible.add(new int[]{r, c});
                    }
                }
            }

            if (accessible.isEmpty()) {
                break;
            }

            // Remove all accessible rolls
            for (int[] pos : accessible) {
                grid[pos[0]][pos[1]] = '.';
            }

            totalRemoved += accessible.size();
        }

        return totalRemoved;
    }

    private static char[][] parseGrid(List<String> lines) {
        int rows = lines.size();
        int cols = lines.get(0).length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = lines.get(r).toCharArray();
        }
        return grid;
    }

    private static int countAdjacentRolls(char[][] grid, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        for (int d = 0; d < 8; d++) {
            int nr = r + DR[d];
            int nc = c + DC[d];

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == '@') {
                count++;
            }
        }

        return count;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}