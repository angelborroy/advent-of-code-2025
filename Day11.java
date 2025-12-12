import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Day 11: Reactor
 *
 * Part 1: Count all paths from 'you' to 'out'
 * Part 2: Count paths from 'svr' to 'out' that visit both 'dac' and 'fft'
 */
public class Day11 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day11.txt"));

        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 11: Reactor ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }

    static class Graph {
        Map<String, List<String>> adjacency = new HashMap<>();

        void addEdge(String from, List<String> to) {
            adjacency.put(from, to);
        }

        List<String> getNeighbors(String node) {
            return adjacency.getOrDefault(node, Collections.emptyList());
        }
    }

    static long solvePart1(List<String> lines) {
        Graph graph = parseGraph(lines);
        return countPaths(graph, "you", "out", new HashSet<>());
    }

    static long solvePart2(List<String> lines) {
        Graph graph = parseGraph(lines);

        // Case 1: svr -> dac (without visiting fft) -> fft -> out
        long pathsSvrToDacNoFft = countPathsDP(graph, "svr", "dac", Set.of("fft"), new HashMap<>());
        long pathsDacToFft = countPathsDP(graph, "dac", "fft", Set.of(), new HashMap<>());
        long pathsFftToOut = countPathsDP(graph, "fft", "out", Set.of(), new HashMap<>());
        long case1 = pathsSvrToDacNoFft * pathsDacToFft * pathsFftToOut;

        // Case 2: svr -> fft (without visiting dac) -> dac -> out
        long pathsSvrToFftNoDac = countPathsDP(graph, "svr", "fft", Set.of("dac"), new HashMap<>());
        long pathsFftToDac = countPathsDP(graph, "fft", "dac", Set.of(), new HashMap<>());
        long pathsDacToOut = countPathsDP(graph, "dac", "out", Set.of(), new HashMap<>());
        long case2 = pathsSvrToFftNoDac * pathsFftToDac * pathsDacToOut;

        return case1 + case2;
    }

    /**
     * Parse input into a directed graph
     */
    static Graph parseGraph(List<String> lines) {
        Graph graph = new Graph();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(": ");
            String device = parts[0];

            List<String> outputs = new ArrayList<>();
            if (parts.length > 1) {
                String[] outputArray = parts[1].split(" ");
                outputs = Arrays.asList(outputArray);
            }

            graph.addEdge(device, outputs);
        }

        return graph;
    }

    /**
     * Count all paths from start to end using DFS with backtracking.
     * Used for Part 1.
     */
    static long countPaths(Graph graph, String start, String end, Set<String> visited) {
        // Base case: reached destination
        if (start.equals(end)) {
            return 1;
        }

        // No path if node has no outputs or already visited
        if (visited.contains(start)) {
            return 0;
        }

        // Mark as visited
        visited.add(start);

        // Count paths through all neighbors
        long totalPaths = 0;
        for (String neighbor : graph.getNeighbors(start)) {
            totalPaths += countPaths(graph, neighbor, end, visited);
        }

        // Backtrack
        visited.remove(start);

        return totalPaths;
    }

    /**
     * Count paths from start to end using dynamic programming with memoization.
     * Forbidden nodes cannot be visited.
     * Used for Part 2 optimization.
     */
    static long countPathsDP(Graph graph, String start, String end,
                             Set<String> forbidden, Map<String, Long> memo) {
        // Check memoization cache
        if (memo.containsKey(start)) {
            return memo.get(start);
        }

        // Base case: reached destination
        if (start.equals(end)) {
            return 1;
        }

        // No path if node is forbidden
        if (forbidden.contains(start)) {
            return 0;
        }

        // Count paths through all neighbors
        long total = 0;
        for (String neighbor : graph.getNeighbors(start)) {
            total += countPathsDP(graph, neighbor, end, forbidden, memo);
        }

        // Memoize result
        memo.put(start, total);
        return total;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}