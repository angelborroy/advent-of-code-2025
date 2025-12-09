import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Day 08: Playground
 *
 * Part 1: Connect 1000 closest pairs of junction boxes, multiply sizes of 3 largest circuits
 * Part 2: Connect all junction boxes into one circuit, multiply X coordinates of last pair
 */
public class Day08 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day08.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 08: Playground ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static long solvePart1(List<String> lines) {
        int[][] points = parsePoints(lines);
        int n = points.length;

        // Calculate all pairwise distances and sort
        List<long[]> edges = computeEdges(points);

        // Initialize Union-Find
        UnionFind uf = new UnionFind(n);

        // Connect 1000 closest pairs
        for (int i = 0; i < 1000; i++) {
            long[] edge = edges.get(i);
            uf.union((int) edge[1], (int) edge[2]);
        }

        // Count circuit sizes
        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = uf.find(i);
            circuitSizes.merge(root, 1, Integer::sum);
        }

        // Get top 3 sizes
        List<Integer> sizes = new ArrayList<>(circuitSizes.values());
        sizes.sort(Collections.reverseOrder());

        return (long) sizes.get(0) * sizes.get(1) * sizes.get(2);
    }

    static long solvePart2(List<String> lines) {
        int[][] points = parsePoints(lines);
        int n = points.length;

        // Calculate all pairwise distances and sort
        List<long[]> edges = computeEdges(points);

        // Initialize Union-Find
        UnionFind uf = new UnionFind(n);

        // Connect until all in one circuit (need n-1 merges)
        int numCircuits = n;
        int lastA = -1, lastB = -1;

        for (long[] edge : edges) {
            int a = (int) edge[1];
            int b = (int) edge[2];
            if (uf.union(a, b)) {
                numCircuits--;
                lastA = a;
                lastB = b;
                if (numCircuits == 1) {
                    break;
                }
            }
        }

        return (long) points[lastA][0] * points[lastB][0];
    }

    static int[][] parsePoints(List<String> lines) {
        int[][] points = new int[lines.size()][3];
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            points[i][0] = Integer.parseInt(parts[0]);
            points[i][1] = Integer.parseInt(parts[1]);
            points[i][2] = Integer.parseInt(parts[2]);
        }
        return points;
    }

    static List<long[]> computeEdges(int[][] points) {
        int n = points.length;
        List<long[]> edges = new ArrayList<>(n * (n - 1) / 2);

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = points[i][0] - points[j][0];
                long dy = points[i][1] - points[j][1];
                long dz = points[i][2] - points[j][2];
                long distSq = dx * dx + dy * dy + dz * dz;
                edges.add(new long[]{distSq, i, j});
            }
        }

        edges.sort(Comparator.comparingLong(a -> a[0]));
        return edges;
    }

    static class UnionFind {
        int[] parent;
        int[] rank;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return false; // Already in same circuit
            }
            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}