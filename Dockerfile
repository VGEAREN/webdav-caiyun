FROM maven:3.6.3-jdk-11 AS maven
USER root
COPY ./ /tmp/code
RUN cd /tmp/code && mvn clean package -Dmaven.test.skip=true -Dmaven.javadoc.skip=true


FROM openjdk:11-jdk-oracle
COPY --from=maven /tmp/code/target/*.jar /caiyun-webdav.jar
EXPOSE 8080
ENV JAVA_OPTS="-Xmx512m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /caiyun-webdav.jar"]
