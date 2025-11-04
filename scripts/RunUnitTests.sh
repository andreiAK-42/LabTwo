#!/bin/sh

echo "Running Unit Tests via Gradle..."

cd "$(dirname "$0")/.."

./gradlew test

if [ $? -eq 0 ]; then
  echo "-------------------------------------"
  echo "Unit tests completed successfully!"
  echo "HTML report is available at: file://$(pwd)/build/reports/tests/test/index.html"
  echo "-------------------------------------"
else
  echo "-------------------------------------"
  echo "Unit tests failed. Check the console output for details."
  echo "-------------------------------------"
  exit 1
fi