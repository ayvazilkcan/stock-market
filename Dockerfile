FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} stock-market.jar

ENTRYPOINT ["java", "-jar", "/stock-market.jar"]