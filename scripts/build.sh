#!/bin/bash
set -e
cd "$(dirname "$0")/.."

MVN="./mvnw"
if [ ! -x "$MVN" ]; then
  MVN="mvn"
fi

echo "Building project with Maven..."
$MVN clean package -DskipTests
echo "Build complete: target/labtwo-1.0.0.jar"