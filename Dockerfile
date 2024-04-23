#
# Build stage
#

FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR usr/src/app

COPY . ./

RUN mvn clean package

#
# Package stage
#

FROM eclipse-temurin:21-jdk-alpine

ARG JAR_NAME="lakeSide-hotel-0.0.1-SNAPSHOT"

WORKDIR /usr/src/app

COPY --from=build /usr/src/app/target/${JAR_NAME}.jar ./app.jar

CMD ["java","-jar", "./app.jar"]
