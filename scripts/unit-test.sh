#!/bin/bash
set -e
cd "$(dirname "$0")/.."

echo "Running Unit Tests..."

MVN="./mvnw"
if [ ! -x "$MVN" ]; then
  MVN="mvn"
fi

$MVN test
echo "Unit Tests Completed."