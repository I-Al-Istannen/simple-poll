#!/usr/bin/env bash

# fail on first error
set -e

cd "/home/simplepoll/"

ls -lah

if [[ -d .docker ]]; then
    echo "Docker folder found"
else
    echo "Docker folder does not exist"
    exit 1
fi

if [[ -f Dockerfile ]]; then
    echo "Dockerfile found"
else
    echo "Dockerfile not found"
    exit 1
fi

cp Dockerfile .docker

cd .docker

docker build -t simplepoll:latest .

# Clean up old images
sudo docker image prune --filter "until=10m" --filter "label=simplepoll" -f
