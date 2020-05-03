FROM adoptopenjdk:14-jdk-hotspot
LABEL simplepoll="true"

# We use nginx as a reverse proxy
# We use git for fast cloning
# We use sudo to drop privileges
RUN apt update && apt install nginx -y && apt install sudo -y && apt install git -y

# Preconfigured nginx configs for development on info-tuorien.de
COPY nginx-site /etc/nginx/sites-available
COPY nginx.conf /etc/nginx/nginx.conf

RUN ln -s /etc/nginx/sites-available/nginx-site /etc/nginx/sites-enabled && \
    rm /etc/nginx/sites-enabled/default && \
    useradd simplepoll

EXPOSE 9001

COPY start.sh /home/simplepoll/start.sh
COPY config.yml /home/simplepoll/config.yml
COPY SimplePoll.jar /home/simplepoll/SimplePoll.jar
COPY dist /home/simplepoll/frontend

WORKDIR /home/simplepoll

RUN chown -R simplepoll:simplepoll /home/simplepoll

ENTRYPOINT ["/home/simplepoll/start.sh"]
