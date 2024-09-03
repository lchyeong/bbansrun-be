# Step 1: Base image
FROM openjdk:17-jdk-slim

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy the JAR file
COPY target/bbansrun.jar /app/bbansrun.jar

# Step 4: Copy the .env file
COPY .env /app/.env

# Step 5: Expose the application port
EXPOSE 8080

# Step 6: Run the application
CMD ["java", "-jar", "/app/bbansrun.jar"]
