#!/usr/bin/env bash

export IMAGE=$1
echo "Using image: $IMAGE"
docker compose -f docker-compose.yaml up --detach
echo "success"