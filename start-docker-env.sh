#until we're missing docker-compose we can use this file
docker stop retrospecto
docker rm $(docker ps -aq)
docker run -d -p 8098:8098 --name retrospecto retrospecto-backend