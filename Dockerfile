FROM openjdk:11
VOLUME /tmp
EXPOSE 8092
ADD ./target/ms-bootcoin-purchase-request-0.0.1-SNAPSHOT.jar ms-bootcoin-purchase-request.jar
ENTRYPOINT ["java","-jar","/ms-bootcoin-purchase-request.jar"]