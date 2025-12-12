# Advent of Code 2025

My solutions.

## Project Structure

```
aoc2025/
├── Day01.java
├── Day02.java
├── ...
├── Day12.java
├── inputs/
│   ├── day01.txt
│   ├── day02.txt
│   └── ...
└── README.md
```

## Running Solutions

Each day is a standalone Java file that can be run directly:

```bash
java Day01.java
java Day02.java
```

Or compile first for better performance:
```bash
javac Day01.java && java Day01
```

## Setup for a New Day

1. Copy the template:
   ```bash
   cp DayXX_Template.java Day02.java
   ```

2. Update the file:
    - Replace `XX` with the day number (e.g., `02`)
    - Update the title and descriptions
    - Download your input to `inputs/day02.txt`

3. Implement the solutions:
    - Fill in `solvePart1()` method
    - Test with the example from the problem
    - Run Part 1 and submit
    - Fill in `solvePart2()` method
    - Run Part 2 and submit

4. Run:
   ```bash
   java Day02.java
   ```

## Common Patterns

### Input Parsing

```java
// All lines as strings
List<String> lines = Files.readAllLines(Paths.get("inputs/dayXX.txt"));
```

### Modular Arithmetic

```java
// Proper modulo handling negatives
static int mod(int a, int b) {
    return ((a % b) + b) % b;
}

// Circular position updates
pos = dir == 'L' ? mod(pos - dist, 100) : (pos + dist) % 100;
```

### Grid/2D Array Helpers

```java
// 8 directions including diagonals
static final int[] DR = {-1, -1, -1, 0, 0, 1, 1, 1};
static final int[] DC = {-1, 0, 1, -1, 1, -1, 0, 1};

// Count adjacent cells matching condition
int count = 0;
for (int d = 0; d < 8; d++) {
    int nr = r + DR[d], nc = c + DC[d];
    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == '@') {
        count++;
    }
}

// Check if column is empty
static boolean isEmptyColumn(char[][] grid, int c) {
    for (char[] row : grid) if (row[c] != ' ') return false;
    return true;
}
```

### Range Operations

```java
// Sort and merge overlapping ranges
ranges.sort(Comparator.comparingLong(a -> a[0]));
List<long[]> merged = new ArrayList<>();
for (long[] range : ranges) {
    if (!merged.isEmpty() && range[0] <= merged.getLast()[1] + 1) {
        merged.getLast()[1] = Math.max(merged.getLast()[1], range[1]);
    } else {
        merged.add(new long[]{range[0], range[1]});
    }
}

// Check if value falls in any range
static boolean inRange(long val, List<long[]> ranges) {
    for (long[] r : ranges) if (val >= r[0] && val <= r[1]) return true;
    return false;
}
```

### State Tracking with Collections

```java
// Track unique positions with Set
Set<Integer> activePositions = new HashSet<>();
activePositions.add(startPos);

// Track counts per position with Map
Map<Integer, Long> counts = new HashMap<>();
counts.merge(pos, count, Long::sum);  // Add or increment

// Iterative state update pattern
for (int step = 0; step < steps; step++) {
    Set<Integer> next = new HashSet<>();
    for (int pos : current) {
        // Process and add to next
    }
    current = next;
}
```

### Union-Find (Disjoint Set)

```java
// Full implementation with path compression and union by rank
static class UnionFind {
    int[] parent, rank;
    
    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    
    boolean union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return false;
        if (rank[rx] < rank[ry]) parent[rx] = ry;
        else if (rank[rx] > rank[ry]) parent[ry] = rx;
        else { parent[ry] = rx; rank[rx]++; }
        return true;
    }
}
```

### Graph Algorithms

```java
// Adjacency list representation
Map<String, List<String>> graph = new HashMap<>();

// DFS path counting with backtracking
static long countPaths(Graph g, String start, String end, Set<String> visited) {
    if (start.equals(end)) return 1;
    if (visited.contains(start)) return 0;
    visited.add(start);
    long total = 0;
    for (String next : g.getNeighbors(start)) {
        total += countPaths(g, next, end, visited);
    }
    visited.remove(start);  // Backtrack
    return total;
}

// Memoized path counting (when graph is DAG)
static long countPathsDP(Graph g, String start, String end, Map<String, Long> memo) {
    if (memo.containsKey(start)) return memo.get(start);
    if (start.equals(end)) return 1;
    long total = 0;
    for (String next : g.getNeighbors(start)) {
        total += countPathsDP(g, next, end, memo);
    }
    memo.put(start, total);
    return total;
}
```

### Computational Geometry

```java
// Point-in-polygon using ray casting
static boolean pointInPolygon(int px, int py, List<int[]> verticalSegments) {
    int crossings = 0;
    for (int[] seg : verticalSegments) {
        int segX = seg[0], segY1 = seg[1], segY2 = seg[2];
        if (segX < px && segY1 < py && py < segY2) crossings++;
    }
    return crossings % 2 == 1;
}

// Squared distance (avoid sqrt for comparisons)
long dx = p1[0] - p2[0], dy = p1[1] - p2[1], dz = p1[2] - p2[2];
long distSq = dx*dx + dy*dy + dz*dz;
```

### Greedy Selection

```java
// Select k digits from string maintaining order, maximizing value
static long maxKDigits(String s, int k) {
    int n = s.length(), start = 0;
    long result = 0;
    for (int i = 0; i < k; i++) {
        int end = n - (k - i) + 1;
        char best = '0';
        int bestPos = start;
        for (int p = start; p < end; p++) {
            if (s.charAt(p) > best) { best = s.charAt(p); bestPos = p; }
        }
        result = result * 10 + (best - '0');
        start = bestPos + 1;
    }
    return result;
}
```

### String Pattern Detection

```java
// Check if string is pattern repeated exactly twice
static boolean isDoubled(String s) {
    int len = s.length();
    if (len % 2 != 0) return false;
    return s.substring(0, len/2).equals(s.substring(len/2));
}
```

### Linear Algebra (GF(2) and Real)

```java
// Brute force XOR system - try all 2^n combinations
for (int mask = 0; mask < (1 << n); mask++) {
    int[] state = new int[m];
    for (int i = 0; i < n; i++)
        if ((mask & (1 << i)) != 0)
            for (int idx : buttons[i]) state[idx] ^= 1;
    if (Arrays.equals(state, target)) 
        minPresses = Math.min(minPresses, Integer.bitCount(mask));
}
```

## Tips

Problem-Solving Approach
- Read the problem carefully: Part 2 often requires a fundamentally different algorithm, not just parameter changes
- Test with example input first, but watch for edge cases not covered by examples
- Start with a working brute-force solution, then optimize only if needed

Performance Considerations
- Use `long` instead of `int` when numbers might exceed 2 billion (or use it always)
- Squared distances avoid floating-point issues and sqrt overhead
- Memoization turns exponential DFS into polynomial DP when the graph structure allows
- `HashSet` and `HashMap` operations are O(1): use them for lookups and deduplication
- Union-Find with path compression handles millions of operations efficiently

Java-Specific Idioms
- `List.getLast()` (Java 21+) is cleaner than `list.get(list.size()-1)`
- `map.merge(key, value, Long::sum)` handles missing keys elegantly
- `Arrays.stream(arr).mapToInt(Integer::parseInt).toArray()` for quick parsing
- `Comparator.comparingLong(a -> a[0])` for sorting by first element

Common Pitfalls
- Negative modulo: Java's `%` returns negative for negative inputs: use custom `mod()`
- Off-by-one in ranges: Clarify if endpoints are inclusive or exclusive
- Grid bounds: Always check `0 <= r < rows && 0 <= c < cols` before accessing
- Integer overflow: Multiplying two ints can overflow even if result fits in long

Code Organization
- Keep `solvePart1()` and `solvePart2()` separate for clarity
- Extract parsing logic into helper methods
- Use meaningful names: `countAdjacentRolls()` beats `helper()`
- Include timing to identify performance issues early