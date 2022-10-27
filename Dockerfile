FROM adoptopenjdk/openjdk11
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","jar","/app.jar"]