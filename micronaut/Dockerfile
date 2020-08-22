FROM openjdk:14-alpine
COPY build/libs/sse-test-micronaut-*-all.jar sse-test-micronaut.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "sse-test-micronaut.jar"]