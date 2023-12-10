# Base container with amazon corretto
FROM amazoncorretto:21-alpine

# Copy sabis executable jar. You need to replace the jar from the assets dir after a new build
# copyjars.sh will do it for you
COPY assets/ /

# Fix the timezone (if required)
# RUN apt-get update ;\
#    apt-get install -y tzdata ;\
#    ln -fs /usr/share/zoneinfo/Europe/Berlin /etc/localtime ;\
#    dpkg-reconfigure -f noninteractive tzdata;\

# http port
EXPOSE 8080

WORKDIR /opt/WeatherStation

# Start our app
USER 1000
ENTRYPOINT ["/usr/bin/java", "-jar", "/opt/WeatherStation/WeatherStation.jar"]

