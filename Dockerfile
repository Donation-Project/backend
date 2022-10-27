FROM adoptopenjdk/openjdk11
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","-Duser.timezone=Asia/Seoul","/app.jar"]
