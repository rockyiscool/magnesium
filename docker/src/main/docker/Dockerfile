FROM openjdk:11.0.2-jdk

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

ENV JAVA_OPTS=""
ENV SPRING_PROFILE="default"
ENV SPRING_CONFIG_URI=""

ENTRYPOINT exec java $JAVA_OPTS \
 -Djava.security.egd=file:/dev/./urandom \
 -Dspring.profiles.active=$SPRING_PROFILE \
 -Dconfig.server.uri=$SPRING_CONFIG_URI \
 -jar app.jar