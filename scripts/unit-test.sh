#!/bin/bash
set -e
cd "$(dirname "$0")/.."

echo "Running Unit Tests..."

if command -v cygpath &> /dev/null; then
    ROOT=$(cygpath -w "$(pwd)")
    SEP=";"
else
    ROOT="$(pwd)"
    SEP=":"
fi

JAR="$ROOT/build/libs/app.jar"
CLI="$ROOT/lib/kotlinx-cli.jar"
SQL="$ROOT/lib/sqlite-jdbc.jar"
JUNIT="$ROOT/lib/junit-platform-console-standalone.jar"
CP="$JAR$SEP$JUNIT$SEP$CLI$SEP$SQL"

java -jar "$JUNIT" \
    --class-path "$CP" \
    --scan-classpath \
    --reports-dir "$ROOT/build/test-reports"

echo "Unit Tests Completed."