FROM amazoncorretto:11

# Add Maintainer Info
LABEL maintainer="cv"
# Add a temporary volume
VOLUME /tmp
# Expose Port 8080
EXPOSE 8080
# Application Jar File
ARG JAR_FILE=../dm-service/target/dm-service-0.0.1-SNAPSHOT.jar
# Add Application Jar File to the Container
ADD ${JAR_FILE} app.jar
# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]