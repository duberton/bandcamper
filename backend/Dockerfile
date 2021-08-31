FROM openjdk:11-jre-slim

RUN mkdir -p /app/config
VOLUME /app/config

COPY ./resources/application.conf /app/config
COPY ./build/install/ /app/

WORKDIR /app/bandcamper/bin

RUN chmod +x bandcamper

EXPOSE 8080:8080
ENTRYPOINT ./bandcamper -config=/app/config/application.conf