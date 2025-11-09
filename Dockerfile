FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/restaurantManager-service-1.0.jar app.jar
EXPOSE ${HOST_PORT:-9010}
CMD ["sh", "-c", "java -jar /app/app.jar --server.port=${HOST_PORT:-9010}"]