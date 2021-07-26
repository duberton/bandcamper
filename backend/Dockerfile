FROM openjdk:11-jre-slim

RUN mkdir -p /app/config
VOLUME /app/config

COPY ./resources/application-docker.conf /app/config
COPY ./build/install/ /app/

WORKDIR /app/bandcamper/bin


EXPOSE 8080:8080
ENTRYPOINT ./bandcamper -config=/app/config/application-docker.conf