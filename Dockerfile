#Build stage
FROM maven:3.9.8-amazoncorretto-21 AS build

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


#Runtime stage
FROM amazoncorretto:21.0.4
ARG PROFILE=prod

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=${PROFILE}

CMD ["java", "-jar", "app.jar"]

