#!/bin/bash
set -e
cd "$(dirname "$0")/.."

MVN="./mvnw"
if [ ! -x "$MVN" ]; then
  MVN="mvn"
fi

echo "Running Maven tests..."
$MVN test