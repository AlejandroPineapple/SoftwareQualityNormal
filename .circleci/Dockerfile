FROM cimg/openjdk:17.0

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y maven

RUN mvn clean package

EXPOSE 8080

CMD ["java", "-jar", "target/"]