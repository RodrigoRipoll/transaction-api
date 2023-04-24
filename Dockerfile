FROM gradle:7.6.1-jdk17-alpine

RUN mkdir /project
COPY . /project
WORKDIR /project
RUN gradle build -x test
CMD ["java", "-jar", "build/libs/transaction-api-1.0.0-SNAPSHOT.jar"]