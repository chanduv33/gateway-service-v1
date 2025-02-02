FROM azul/zulu-openjdk-alpine:11.0.4
COPY target/gateway-service-v1-0.0.1-SNAPSHOT.jar gateway-service-v1.jar
ENTRYPOINT ["java","-jar","gateway-service-v1.jar"]