#!/bin/bash
set -e

mkdir -p lib

echo "⬇Downloading Dependencies..."

echo "- sqlite-jdbc..."
curl -L -s -o lib/sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.42.0.0/sqlite-jdbc-3.42.0.0.jar

echo "- kotlinx-cli..."
curl -L -s -o lib/kotlinx-cli.jar https://repo1.maven.org/maven2/org/jetbrains/kotlinx/kotlinx-cli-jvm/0.3.5/kotlinx-cli-jvm-0.3.5.jar

echo "- junit-platform..."
curl -L -s -o lib/junit-platform-console-standalone.jar https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.0/junit-platform-console-standalone-1.10.0.jar

if [ ! -d "lib/kotlinc" ]; then
    echo "⬇Downloading Kotlin Compiler (portable)..."
    curl -L -s -o lib/kotlin-compiler.zip https://github.com/JetBrains/kotlin/releases/download/v1.9.22/kotlin-compiler-1.9.22.zip

    echo "Unzipping compiler..."
    unzip -q lib/kotlin-compiler.zip -d lib/
    rm lib/kotlin-compiler.zip
    echo "Kotlin compiler installed in lib/kotlinc"
else
    echo "Kotlin compiler already exists."
fi

echo "Everything should be in lib/ now"