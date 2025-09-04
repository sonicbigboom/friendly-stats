# ==== Build Stage ====

# Use JDK 21 for gradlew.
FROM eclipse-temurin:21-jdk-alpine AS builder

# Copy the app.
WORKDIR /usr/app/
COPY . . 

# Build the app.  This will build to /usr/app/build/libs/friendly-stats-api.jar
RUN ./gradlew build


# ==== Package Stage ====

# Use JRE 21.
FROM eclipse-temurin:21-jre-alpine

# Copy the built jar file.
RUN mkdir -p /opt/app
COPY --from=builder /usr/app/build/libs/friendly-stats-api.jar /opt/app/friendly-stats-api.jar

# Create the entrypoint.
ENTRYPOINT ["java", "-jar", "/opt/app/friendly-stats-api.jar"]