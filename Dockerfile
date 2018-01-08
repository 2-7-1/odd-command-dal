FROM openjdk:8-jre-alpine
ENV AWS_REGION=us-east-1
ENV AWS_ACCESS_KEY_ID=AKIAICUZPQXNZCJNW4WQ
ENV AWS_SECRET_ACCESS_KEY=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
COPY ./target/odd-command-dal-0.0.1-SNAPSHOT.jar /usr/src/odd-command-dal/
WORKDIR /usr/src/odd-command-dal
EXPOSE 8111
CMD ["java", "-jar", "odd-command-dal-0.0.1-SNAPSHOT.jar"]