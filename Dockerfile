FROM openjdk:8-jre-alpine
ENV AWS_REGION=us-east-1
ENV AWS_ACCESS_KEY_ID=xxx
ENV AWS_SECRET_ACCESS_KEY=xxx
COPY ./target/odd-command-dal-0.0.1-SNAPSHOT.jar /usr/src/odd-command-dal/
WORKDIR /usr/src/odd-command-dal
EXPOSE 8111
CMD ["java", "-jar", "odd-command-dal-0.0.1-SNAPSHOT.jar"]