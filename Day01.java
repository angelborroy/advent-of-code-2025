import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Day 1: Secret Entrance
 *
 * Part 1: Count times dial ends at position 0 after rotations
 * Part 2: Count times dial passes through position 0 during rotations
 */
public class Day01 {
    
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day01.txt"));
        
        long start = System.nanoTime();
        int part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;
        
        start = System.nanoTime();
        int part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;
        
        System.out.println("=== Day 01: Secret Entrance ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }
    
    static int solvePart1(List<String> lines) {
        int pos = 50, count = 0;
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            char dir = line.charAt(0);
            int dist = Integer.parseInt(line.substring(1));
            
            pos = dir == 'L' ? mod(pos - dist, 100) : (pos + dist) % 100;
            if (pos == 0) count++;
        }
        
        return count;
    }
    
    static int solvePart2(List<String> lines) {
        int pos = 50, count = 0;
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            char dir = line.charAt(0);
            int dist = Integer.parseInt(line.substring(1));
            
            for (int i = 0; i < dist; i++) {
                pos = dir == 'L' ? (pos == 0 ? 99 : pos - 1) : (pos == 99 ? 0 : pos + 1);
                if (pos == 0) count++;
            }
        }
        
        return count;
    }
    
    static int mod(int a, int b) {
        return ((a % b) + b) % b;
    }
    
    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}
