FROM adoptopenjdk/openjdk14:latest
COPY target/sekre-0.0.1-SNAPSHOT.jar sekre.jar
CMD ["java", "-jar", "sekre.jar"]