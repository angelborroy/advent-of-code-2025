#!/bin/bash

# Helper script to create a new day's solution from template
# Usage: ./newday.sh <day_number> <title>
# Example: ./newday.sh 2 "Cube Conundrum"

if [ -z "$1" ]; then
    echo "Usage: ./newday.sh <day_number> [title]"
    echo "Example: ./newday.sh 2 'Cube Conundrum'"
    exit 1
fi

DAY=$(printf "%02d" $1)
TITLE=${2:-"TBD"}

echo "Creating Day $DAY: $TITLE"

# Create Java file from template
sed "s/XX/$DAY/g; s/\[Title\]/$TITLE/g" DayXX_Template.java > Day$DAY.java

# Create empty input file
touch inputs/day$DAY.txt

echo "✓ Created Day$DAY.java"
echo "✓ Created inputs/day$DAY.txt"
echo ""
echo "Next steps:"
echo "1. Download your input from https://adventofcode.com/2025/day/$1/input"
echo "2. Save it to inputs/day$DAY.txt"
echo "3. Edit Day$DAY.java and implement your solution"
echo "4. Run: java Day$DAY.java"
