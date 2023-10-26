#!/bin/bash

echo "Enter harbor username: "
read username

echo "Enter harbor password:"
read password

HARBOR_DOMAIN=harbor.yellowsunn.com:80

docker login http://$HARBOR_DOMAIN -u: $username -p $password
docker build -t $HARBOR_DOMAIN/archive/eclipse-temurin:17-jre-alpine-pinpoint-2.5.2  ./ --platform=linux/amd64
docker push $HARBOR_DOMAIN/archive/eclipse-temurin:17-jre-alpine-pinpoint-2.5.2
