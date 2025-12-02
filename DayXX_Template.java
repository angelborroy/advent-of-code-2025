import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Day XX: [Title]
 *
 * Part 1: [Brief description]
 * Part 2: [Brief description]
 */
public class DayXX_Template {
    
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/dayXX.txt"));
        
        long start = System.nanoTime();
        long part1 = solvePart1(lines);
        long time1 = System.nanoTime() - start;
        
        start = System.nanoTime();
        long part2 = solvePart2(lines);
        long time2 = System.nanoTime() - start;
        
        System.out.println("=== Day XX: [Title] ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }
    
    static long solvePart1(List<String> lines) {
        // TODO: Implement Part 1
        return 0;
    }
    
    static long solvePart2(List<String> lines) {
        // TODO: Implement Part 2
        return 0;
    }
    
    // Utility methods (add as needed)
    
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
