# Build stage
FROM maven:3.9.4-eclipse-temurin-17 as builder
WORKDIR /build
COPY pom.xml .
# copy maven settings if required
COPY src ./src
RUN mvn -q -DskipTests package

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# copy fat jar produced by maven (adjust artifact name if needed)
COPY --from=builder /build/target/*.jar app.jar

# create directory for logs file and make it writable
RUN mkdir -p /data && chmod -R 777 /data

VOLUME [ "/data" ]
ENV JAVA_OPTS=""
EXPOSE 8082
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app/app.jar" ]
