# Dockerfile

# Step 1: Base image
FROM openjdk:17-jdk-slim

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy the JAR file (Make sure the JAR file is available in the target directory)
COPY target/bbansrun.jar /app/bbansrun.jar

# Step 4: Expose the application port
EXPOSE 8080

# Step 5: Run the application
CMD ["java", "-jar", "/app/bbansrun.jar"]
