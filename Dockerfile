
FROM maven:latest

LABEL authors="komeetta"

WORKDIR /app

COPY . /app/

RUN mvn clean package -DskipTests

# Run the application
CMD ["java", "-jar", "target/inventory-management-system-1.0-SNAPSHOT.jar"]

