#!/usr/bin/env bash

echo "#Starting the container..."
pwd
cd ..
docker load < /home/ec2-user/deploy/groom-test-docker.tar
docker load < /home/ec2-user/deploy/groom-test-docker-latest.tar
docker images
docker run -d -p 80:80 --name groom-test-docker groom-test-docker
#docker save groom-test-docker:latest > groom-test-docker-latest.tar