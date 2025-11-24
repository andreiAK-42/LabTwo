#!/bin/bash
cd "$(dirname "$0")/.."

echo "Running Integration Tests..."

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
CP="$JAR$SEP$CLI$SEP$SQL"

if [ ! -f "build/libs/app.jar" ]; then
    echo "JAR not found. Run bash scripts/build.sh first."
    exit 1
fi

TOTAL=0
PASSED=0

run_test() {
    local name="$1"
    local expected="$2"
    local args="$3"

    ((TOTAL++))
    echo -n "Test $TOTAL: $name... "

    output=$(java -cp "$CP" MainKt $args 2>&1)
    exit_code=$?

    if echo "$output" | grep -q "$expected"; then
        echo "OK"
        ((PASSED++))
    else
        echo "FAIL"
        echo "  Expected to find: '$expected'"
        echo "  Actual output: $output"
        echo "  Exit code: $exit_code"
    fi
}

run_test "Help command" "" "--help"
run_test "Bad Login" "INCORRECT_LOGIN" "--login wrong --password pass --action read --resource A --volume 10"
run_test "Bad Pass" "INCORRECT_PASSWORD" "--login alice --password wrong --action read --resource A --volume 10"
run_test "Bad Resource" "BAD_RESOURCE" "--login alice --password 123456 --action read --resource A.X.Y --volume 10"
run_test "Success Access" "SUCCESS" "--login alice --password 123456 --action read --resource A --volume 10"

echo "--------------------------------"
echo "Result: $PASSED / $TOTAL passed."

if [ "$PASSED" -ne "$TOTAL" ]; then
    exit 1
fi