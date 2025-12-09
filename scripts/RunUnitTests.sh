#!/bin/sh

cd "$(dirname "$0")/.."

MVN="./mvnw"
if [ ! -x "$MVN" ]; then
  MVN="mvn"
fi

echo "Running unit tests via Maven..."
$MVN test