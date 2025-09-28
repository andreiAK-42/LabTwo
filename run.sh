#!/bin/sh

set -e

JAR_VERSION="-1.0"
JAR_NAME="LabTwo${JAR_VERSION}.jar"
BUILD_DIR="build/libs"

echo "=== Сборка LabTwo ==="

./gradlew clean
if grep -q "shadow" build.gradle.kts; then
    ./gradlew shadowJar
    cp $(find "$BUILD_DIR" -name "*-all.jar" | head -1) "$BUILD_DIR/$JAR_NAME"
else
    ./gradlew jar
    cp $(find "$BUILD_DIR" -name "*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" | head -1) "$BUILD_DIR/$JAR_NAME"
fi

echo "=== Введите аргументы ==="
read -p "Логин: " login
read -s -p "Пароль: " password
echo
read -p "Действие (read/write/run): " action
read -p "Путь до ресурса через '.': " resource
read -p "Объём ресурса: " volume

echo "=== Запуск программы ==="
java -jar "$BUILD_DIR/$JAR_NAME" --login "$login" --password "$password" --action "$action" --resource "$resource" --volume "$volume"