#!/bin/bash

set -e

MVN="./mvnw"
if [ ! -x "$MVN" ]; then
  MVN="mvn"
fi

echo "=== Запуск тестов через Maven ==="
$MVN test