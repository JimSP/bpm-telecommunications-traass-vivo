#!/bin/bash

ERROR="Usage: ./docker.sh [command]\n
[command]\n
	\t-b\t--build\t\t: Construir imagem\n
	\t-s\t--start\t\t: Iniciar container\n
	\t-x\t--stop\t\t: Parar container\n
	\t-r\t--remove\t: Remover container"

if [ "$#" -lt 1 ]
then
  echo -e ${ERROR}
  exit -1
fi

case $1 in 
	-b|--build)
    cp target/vt-user-ms-rest-server-0.0.1.war .
    docker build -t vt-user-ms-rest-server-0.0.1 .
    rm ./vt-user-ms-rest-server-0.0.1.war
    read -rsp $'Press enter to continue...\n'
		;;
	-s|--start)
    docker run -d --name vt-user-ms-rest-server-0.0.1 -p 8080:8080 vt-user-ms-rest-server-0.0.1
    read -rsp $'Press enter to continue...\n'
		;;
	-x|--stop)
    docker stop vt-user-ms-rest-server-0.0.1
    read -rsp $'Press enter to continue...\n'
		;;
	-r|--remove)
    docker rm -f vt-user-ms-rest-server-0.0.1
    read -rsp $'Press enter to continue...\n'
		;;
	*)
		echo $ERROR
		;;
esac

