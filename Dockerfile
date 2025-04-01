FROM amazoncorretto:17-alpine-jdk

# Create a directory for the application
WORKDIR /app

# Copy everything from the current directory to /app in the container
COPY . .

# Build the application
RUN ./gradlew build -x test

# Expose the application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "./build/libs/arr-all.jar"]