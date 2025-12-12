import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 12: Christmas Tree Farm
 *
 * Part 1: Count how many regions can fit all requested presents.
 */
public class Day12 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day12.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        System.out.println("=== Day 12: Christmas Tree Farm ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
    }

    static long solvePart1(List<String> lines) {
        List<Integer> shapeAreas = new ArrayList<>();
        long count = 0;
        int i = 0;

        // Parse shapes (lines like "0:" followed by 3 pattern lines)
        while (i < lines.size() && lines.get(i).matches("\\d+:\\s*")) {
            int area = 0;
            for (int k = 1; k <= 3; k++) {
                for (char c : lines.get(i + k).toCharArray()) {
                    if (c == '#') area++;
                }
            }
            shapeAreas.add(area);
            i += 4;
            while (i < lines.size() && lines.get(i).isEmpty()) i++;
        }

        // Parse and evaluate regions (lines like "46x45: 71 55 46 51 56 44")
        while (i < lines.size()) {
            String line = lines.get(i++);
            if (line.isEmpty()) continue;

            String[] parts = line.split("x|:\\s*");
            int w = Integer.parseInt(parts[0]);
            int h = Integer.parseInt(parts[1]);
            String[] counts = parts[2].split("\\s+");

            long need = 0;
            for (int j = 0; j < counts.length; j++) {
                need += (long) Integer.parseInt(counts[j]) * shapeAreas.get(j);
            }
            if ((long) w * h >= need) count++;
        }

        return count;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f us", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }

}
