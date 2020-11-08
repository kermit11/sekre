FROM adoptopenjdk/openjdk14:alpine-slim
COPY target/sekre-0.0.1-SNAPSHOT.jar sekre.jar
CMD ["java", "-jar", "sekre.jar"]