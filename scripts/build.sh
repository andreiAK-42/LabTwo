#!/bin/bash
set -e
cd "$(dirname "$0")/.."

echo "Building project..."
mkdir -p build/libs

if command -v cygpath &> /dev/null; then
    # Windows (Git Bash)
    ROOT=$(cygpath -w "$(pwd)")
    SEP=";"
else
    # Linux (GitHub Actions / Docker)
    ROOT="$(pwd)"
    SEP=":"
fi

SRC="$ROOT/src"
OUT="$ROOT/build/libs/app.jar"

CLI="$ROOT/lib/kotlinx-cli.jar"
SQL="$ROOT/lib/sqlite-jdbc.jar"
JUNIT="$ROOT/lib/junit-platform-console-standalone.jar"

COMPILER_JAR="$ROOT/lib/kotlinc/lib/kotlin-compiler.jar"

CP="$CLI$SEP$SQL$SEP$JUNIT"

echo "Using compiler: $COMPILER_JAR"

java -jar "$COMPILER_JAR" "$SRC" -include-runtime -d "$OUT" -cp "$CP"

echo "Build complete: build/libs/app.jar"