mvn clean install -Pdocker

docker buildx build --build-arg="JAR_FILE=target/challenge-0.0.1-SNAPSHOT.jar" -t smart-assets-be-challenge:latest .

docker-compose up --exit-code-from service