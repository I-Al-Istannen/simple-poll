FROM openjdk:14-jdk-slim
LABEL simplepoll="true"

RUN useradd -m simplepoll

EXPOSE 9001

USER simplepoll

COPY config.yml /home/simplepoll/config.yml
COPY SimplePoll.jar /home/simplepoll/SimplePoll.jar

WORKDIR /home/simplepoll

ENTRYPOINT ["java", "-jar", "SimplePoll.jar", "server", "config.yml"]
