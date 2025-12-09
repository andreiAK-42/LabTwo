#!/bin/sh

set -e

JAR_NAME="labtwo-1.0.0.jar"
MVN="./mvnw"

if [ ! -x "$MVN" ]; then
  MVN="mvn"
fi

echo "=== Сборка LabTwo (Maven) ==="
$MVN -q clean package -DskipTests

echo "=== Введите аргументы ==="
read -p "Логин: " login
read -s -p "Пароль: " password
echo
read -p "Действие (read/write/run): " action
read -p "Путь до ресурса через '.': " resource
read -p "Объём ресурса: " volume

echo "=== Запуск программы ==="
java -jar "target/$JAR_NAME" --login "$login" --password "$password" --action "$action" --resource "$resource" --volume "$volume"
