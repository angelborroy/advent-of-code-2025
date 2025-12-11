import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.*;

/**
 * Day 10: Factory
 *
 * Part 1: Configure indicator lights using XOR toggles - find minimum button presses (GF(2) linear algebra)
 * Part 2: Configure joltage counters using additive increments - find minimum button presses (ILP)
 */
public class Day10 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/day10.txt"));

        long part1 = 0, part2 = 0;

        long start = System.nanoTime();
        for (String line : lines) {
            Object[] parsed = parse(line);
            int[] target = (int[]) parsed[0];
            int[][] btns = (int[][]) parsed[1];
            part1 += solvePart1(target, btns);
        }
        long time1 = System.nanoTime() - start;

        start = System.nanoTime();
        for (String line : lines) {
            Object[] parsed = parse(line);
            int[][] btns = (int[][]) parsed[1];
            int[] joltage = (int[]) parsed[2];
            part2 += solvePart2(joltage, btns);
        }
        long time2 = System.nanoTime() - start;

        System.out.println("=== Day 10: Factory ===");
        System.out.println("Part 1: " + part1 + " (took " + formatTime(time1) + ")");
        System.out.println("Part 2: " + part2 + " (took " + formatTime(time2) + ")");
    }
    static Object[] parse(String line) {
        // Returns: [target int[], buttons int[][], joltage int[]]
        Matcher m = Pattern.compile("\\[([.#]+)]|\\(([0-9,]+)\\)|\\{([0-9,]+)}").matcher(line);
        int[] target = null, joltage = null;
        List<int[]> buttons = new ArrayList<>();
        while (m.find()) {
            if (m.group(1) != null) { // [.##.]
                String s = m.group(1);
                target = new int[s.length()];
                for (int i = 0; i < s.length(); i++) target[i] = s.charAt(i) == '#' ? 1 : 0;
            } else if (m.group(2) != null) { // (0,1,2)
                buttons.add(Arrays.stream(m.group(2).split(",")).mapToInt(Integer::parseInt).toArray());
            } else { // {3,5,4}
                joltage = Arrays.stream(m.group(3).split(",")).mapToInt(Integer::parseInt).toArray();
            }
        }
        return new Object[]{target, buttons.toArray(new int[0][]), joltage};
    }

    // Part 1: XOR system - brute force all 2^n button combinations
    static int solvePart1(int[] target, int[][] btns) {
        int n = btns.length, lights = target.length, min = Integer.MAX_VALUE;
        for (int mask = 0; mask < (1 << n); mask++) {
            int[] state = new int[lights];
            for (int i = 0; i < n; i++)
                if ((mask & (1 << i)) != 0)
                    for (int idx : btns[i]) if (idx < lights) state[idx] ^= 1;
            if (Arrays.equals(state, target)) min = Math.min(min, Integer.bitCount(mask));
        }
        return min;
    }

    // Part 2: Linear system - Gaussian elimination + search free variables
    static long solvePart2(int[] target, int[][] btns) {
        int n = btns.length, m = target.length;
        double[][] A = new double[m][n + 1];
        for (int i = 0; i < n; i++) for (int idx : btns[i]) if (idx < m) A[idx][i] = 1;
        for (int i = 0; i < m; i++) A[i][n] = target[i];

        // Gaussian elimination
        int[] pivot = new int[m];
        Arrays.fill(pivot, -1);
        boolean[] isPivot = new boolean[n];
        int row = 0;
        for (int col = 0; col < n && row < m; col++) {
            int best = row;
            for (int r = row + 1; r < m; r++) if (Math.abs(A[r][col]) > Math.abs(A[best][col])) best = r;
            if (Math.abs(A[best][col]) < 1e-9) continue;
            double[] tmp = A[row]; A[row] = A[best]; A[best] = tmp;
            pivot[row] = col; isPivot[col] = true;
            double scale = A[row][col];
            for (int c = 0; c <= n; c++) A[row][c] /= scale;
            for (int r = 0; r < m; r++) if (r != row && Math.abs(A[r][col]) > 1e-9) {
                double f = A[r][col];
                for (int c = 0; c <= n; c++) A[r][c] -= f * A[row][c];
            }
            row++;
        }

        // Find free variables and search
        List<Integer> free = new ArrayList<>();
        for (int j = 0; j < n; j++) if (!isPivot[j]) free.add(j);
        int maxV = Math.min(300, free.size() > 3 ? 50 : 500);
        return search(A, pivot, target, btns, n, m, free, new int[free.size()], 0, maxV, Long.MAX_VALUE);
    }

    static long search(double[][] A, int[] pivot, int[] target, int[][] btns, int n, int m,
                       List<Integer> free, int[] vals, int idx, int maxV, long best) {
        if (idx == free.size()) {
            long[] x = new long[n];
            for (int i = 0; i < free.size(); i++) x[free.get(i)] = vals[i];
            for (int r = m - 1; r >= 0; r--) {
                if (pivot[r] < 0) continue;
                double v = A[r][n];
                for (int c = pivot[r] + 1; c < n; c++) v -= A[r][c] * x[c];
                long rv = Math.round(v);
                if (Math.abs(v - rv) > 0.001 || rv < 0) return best;
                x[pivot[r]] = rv;
            }
            // Verify: compute A*x and compare to target
            for (int j = 0; j < m; j++) {
                long sum = 0;
                for (int i = 0; i < n; i++) for (int idx2 : btns[i]) if (idx2 == j) sum += x[i];
                if (sum != target[j]) return best;
            }
            long sum = 0; for (long xi : x) sum += xi;
            return Math.min(best, sum);
        }
        for (int v = 0; v <= maxV; v++) { vals[idx] = v; best = search(A, pivot, target, btns, n, m, free, vals, idx + 1, maxV, best); }
        return best;
    }

    static String formatTime(long nanos) {
        if (nanos < 1_000) return nanos + " ns";
        if (nanos < 1_000_000) return String.format("%.2f μs", nanos / 1_000.0);
        if (nanos < 1_000_000_000) return String.format("%.2f ms", nanos / 1_000_000.0);
        return String.format("%.2f s", nanos / 1_000_000_000.0);
    }
}