FROM openjdk:11-jre-slim
COPY build/libs/ToGather-0.0.1-SNAPSHOT.jar ToGather-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-DSpring.profiles.active=prod","-jar","ToGather-0.0.1-SNAPSHOT.jar"]
