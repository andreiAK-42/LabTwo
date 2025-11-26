#!/bin/bash

set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

JAR_VERSION="-1.0"
JAR_FILE="build/libs/LabTwo${JAR_VERSION}.jar"
TOTAL_TESTS=0
PASSED_TESTS=0

run_test() {
    local test_name="$1"
    local expected_code="$2"
    local args="$3"

    ((TOTAL_TESTS++))

    echo -n "Test $TOTAL_TESTS: $test_name... "

    local output
    output=$(java -jar "$JAR_FILE" $args 2>&1)
    local exit_code=$?

    local actual_code
    actual_code=$(echo "$output" | grep -oE "кодом: [0-9]+" | grep -oE "[0-9]+" | tail -1)

    if [ "$actual_code" = "$expected_code" ]; then
        echo -e "${GREEN}OK${NC}"
        ((PASSED_TESTS++))
    else
        echo -e "${RED}FAIL${NC}"
        echo "  Expected: $expected_code"
        echo "  Actual: $actual_code"
        echo "  Output: $output"
    fi
}


main() {
    echo -e "${YELLOW}=== LabTwo Тесты кода ответа ===${NC}"


    echo "JAR file: $JAR_FILE"
    echo

    run_test "Incorrect login" "1" "--login wronguser --password pass --action read --resource res1 --volume 10"

    run_test "Incorrect password" "2" "--login test --password wrongpass --action read --resource res1 --volume 10"

    run_test "Resource not found" "3" "--login test --password test --action read --resource wrongresource --volume 10"

    run_test "Volume error" "4" "--login test --password test --action read --resource res1 --volume 999"

    run_test "Invalid action" "5" "--login test --password test --action invalidaction --resource res1 --volume 10"

    run_test "No access" "6" "--login noaccessuser --password test --action write --resource res1 --volume 10"

    run_test "Success READ" "7" "--login test --password test --action read --resource res1 --volume 10"

    run_test "Success WRITE" "7" "--login test --password test --action write --resource res1 --volume 10"

    run_test "Success RUN" "7" "--login test --password test --action run --resource res1 --volume 10"

    run_test "Help command" "0" "--help"

    echo
    echo -e "${YELLOW}=== Результаты ===${NC}"
    echo "Всего тестов: $TOTAL_TESTS"
    echo -e "Пройдены: ${GREEN}$PASSED_TESTS${NC}"
    echo -e "Провалены: ${RED}$((TOTAL_TESTS - PASSED_TESTS))${NC}"
}

main