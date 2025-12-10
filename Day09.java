import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Day 09: Movie Theater
 *
 * Part 1: Find the largest rectangle with red tiles at opposite corners
 * Part 2: Find the largest rectangle using only red and green tiles (inside polygon)
 */
public class Day09 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day09.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 09: Movie Theater ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        List<int[]> redTiles = parseInput(lines);

        long maxArea = 0;
        for (int i = 0; i < redTiles.size(); i++) {
            int x1 = redTiles.get(i)[0];
            int y1 = redTiles.get(i)[1];
            for (int j = i + 1; j < redTiles.size(); j++) {
                int x2 = redTiles.get(j)[0];
                int y2 = redTiles.get(j)[1];
                long area = (long)(Math.abs(x2 - x1) + 1) * (Math.abs(y2 - y1) + 1);
                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }
        return maxArea;
    }

    static long solvePart2(List<String> lines) {
        List<int[]> redTiles = parseInput(lines);

        // Build horizontal and vertical segments of the boundary
        List<int[]> hSegments = new ArrayList<>(); // {y, xStart, xEnd}
        List<int[]> vSegments = new ArrayList<>(); // {x, yStart, yEnd}

        for (int i = 0; i < redTiles.size(); i++) {
            int x1 = redTiles.get(i)[0];
            int y1 = redTiles.get(i)[1];
            int x2 = redTiles.get((i + 1) % redTiles.size())[0];
            int y2 = redTiles.get((i + 1) % redTiles.size())[1];

            if (y1 == y2) {
                // Horizontal segment
                hSegments.add(new int[]{y1, Math.min(x1, x2), Math.max(x1, x2)});
            } else {
                // Vertical segment
                vSegments.add(new int[]{x1, Math.min(y1, y2), Math.max(y1, y2)});
            }
        }

        long maxArea = 0;
        for (int i = 0; i < redTiles.size(); i++) {
            int x1 = redTiles.get(i)[0];
            int y1 = redTiles.get(i)[1];
            for (int j = i + 1; j < redTiles.size(); j++) {
                int x2 = redTiles.get(j)[0];
                int y2 = redTiles.get(j)[1];

                // Skip degenerate rectangles
                if (x1 == x2 || y1 == y2) continue;

                long area = (long)(Math.abs(x2 - x1) + 1) * (Math.abs(y2 - y1) + 1);

                // Only check if potentially better
                if (area <= maxArea) continue;

                // Check if no boundary segment crosses through the interior
                if (segmentsCrossRectInterior(x1, y1, x2, y2, hSegments, vSegments)) continue;

                // Check if a point in the interior is inside the polygon
                int midX = (x1 + x2) / 2;
                int midY = (y1 + y2) / 2;

                if (!pointInsidePolygon(midX, midY, vSegments)) continue;

                // Valid rectangle found
                maxArea = area;
            }
        }
        return maxArea;
    }

    static boolean segmentsCrossRectInterior(int rx1, int ry1, int rx2, int ry2,
                                             List<int[]> hSegs, List<int[]> vSegs) {
        int minX = Math.min(rx1, rx2);
        int maxX = Math.max(rx1, rx2);
        int minY = Math.min(ry1, ry2);
        int maxY = Math.max(ry1, ry2);

        // Check horizontal segments
        for (int[] seg : hSegs) {
            int segY = seg[0], segX1 = seg[1], segX2 = seg[2];
            // Does this horizontal segment pass through interior?
            if (minY < segY && segY < maxY) {
                // Check if segment x-range overlaps with interior x-range
                if (segX1 < maxX && segX2 > minX) {
                    return true;
                }
            }
        }

        // Check vertical segments
        for (int[] seg : vSegs) {
            int segX = seg[0], segY1 = seg[1], segY2 = seg[2];
            // Does this vertical segment pass through interior?
            if (minX < segX && segX < maxX) {
                // Check if segment y-range overlaps with interior y-range
                if (segY1 < maxY && segY2 > minY) {
                    return true;
                }
            }
        }

        return false;
    }

    static boolean pointInsidePolygon(int px, int py, List<int[]> vSegs) {
        // Ray casting: count vertical crossings to the left
        int crossings = 0;
        for (int[] seg : vSegs) {
            int segX = seg[0], segY1 = seg[1], segY2 = seg[2];
            if (segX < px) { // Segment is to the left
                // Check if horizontal ray at py crosses this vertical segment
                if (segY1 < py && py < segY2) {
                    crossings++;
                }
            }
        }
        return crossings % 2 == 1;
    }

    static List<int[]> parseInput(List<String> lines) {
        List<int[]> tiles = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            tiles.add(new int[]{x, y});
        }
        return tiles;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}