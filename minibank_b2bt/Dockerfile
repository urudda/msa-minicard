FROM java:8

# Add Author info
#LABEL maintainer="sharplee7@gmail.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8079

# The application's jar file
ARG JAR_FILE=build/libs/minibank_b2bt-0.0.1-SNAPSHOT.war

# Add the application's jar to the container
ADD ${JAR_FILE} minibank_b2bt.war

# Run the jar file
ENTRYPOINT ["java","-jar","/minibank_b2bt.war"]