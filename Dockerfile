# Step 1: Base image for building the application
FROM gradle:7.6-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Build the application and create the JAR file
RUN gradle build --no-daemon

# Step 2: Base image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/bbansrun-0.0.1-SNAPSHOT.jar /app/bbansrun.jar

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/bbansrun.jar"]
