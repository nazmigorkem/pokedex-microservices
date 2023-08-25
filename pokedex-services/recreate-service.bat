@echo off

if "%1"=="" (
    echo "Please provide service name"
    exit 1
)

set service_name=%1

docker-compose stop %service_name% && docker-compose build %service_name% && echo y | docker image prune -a  && docker-compose up -d --no-deps %service_name%