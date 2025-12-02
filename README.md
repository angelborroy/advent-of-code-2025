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

### Reading Input

```java
// All lines as strings
List<String> lines = Files.readAllLines(Paths.get("inputs/dayXX.txt"));

// Parse as integers
List<Integer> numbers = lines.stream()
    .map(Integer::parseInt)
    .collect(Collectors.toList());

// Read entire file as string
String content = Files.readString(Paths.get("inputs/dayXX.txt"));

// Split by blank lines
String[] blocks = content.split("\n\n");
```

### Common Utilities

```java
// Proper modulo (handles negatives)
static int mod(int a, int b) {
    return ((a % b) + b) % b;
}

// GCD
static long gcd(long a, long b) {
    return b == 0 ? a : gcd(b, a % b);
}

// LCM
static long lcm(long a, long b) {
    return a * b / gcd(a, b);
}

// Parse integers from string
static List<Integer> extractInts(String s) {
    return Pattern.compile("-?\\d+")
        .matcher(s)
        .results()
        .map(m -> Integer.parseInt(m.group()))
        .collect(Collectors.toList());
}
```

### Grid/2D Array Helpers

```java
// 4 directions (up, right, down, left)
static final int[][] DIRS4 = {{-1,0}, {0,1}, {1,0}, {0,-1}};

// 8 directions (includes diagonals)
static final int[][] DIRS8 = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

// Check bounds
static boolean inBounds(int r, int c, int rows, int cols) {
    return r >= 0 && r < rows && c >= 0 && c < cols;
}
```

## Tips

- Start simple, optimize later if needed
- Use meaningful variable names
- Add helper methods as you find patterns
- Test with the example input first
- Keep track of edge cases
- Don't over-engineer early - solve it first, refactor later