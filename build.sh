#!/bin/bash

echo '  ___  __      __  _                   _______   _____                           _____    _____   ___     _____                   _                  '
echo ' |  _| \ \    / / (_)                 |__   __| |  __ \      /\         /\      / ____|  / ____| |_  |   |  __ \                 | |                 '
echo ' | |    \ \  / /   _  __   __   ___      | |    | |__) |    /  \       /  \    | (___   | (___     | |   | |  | |   ___   _ __   | |   ___    _   _  '
echo ' | |     \ \/ /   | | \ \ / /  / _ \     | |    |  _  /    / /\ \     / /\ \    \___ \   \___ \    | |   | |  | |  / _ \ |  _ \  | |  / _ \  | | | | '
echo ' | |      \  /    | |  \ V /  | (_) |    | |    | | \ \   / ____ \   / ____ \   ____) |  ____) |   | |   | |__| | |  __/ | |_) | | | | (_) | | |_| | '
echo ' | |_      \/     |_|   \_/    \___/     |_|    |_|  \_\ /_/    \_\ /_/    \_\ |_____/  |_____/   _| |   |_____/   \___| | .__/  |_|  \___/   \__, | '
echo ' |___|                                                                                           |___|                   | |                   __/ | '
echo '                                                                                                                         |_|                  |___/  '
echo '2018-07-23 v.0.0.2                                                                                                                                   '

git checkout TRAASS-16.V2
git pull --rebase

cd microservices/vt-ms-soluction
mvn clean install

cd ../../..

mkdir deploy

cp bpm-telecommunications-traass-vivo/microservices/vt-ms-soluction/vt-user-ms-rest-server/target/vt-user-ms-rest-server-0.0.1.war deploy/
cp bpm-telecommunications-traass-vivo/microservices/vt-ms-soluction/vt-solicitation-ms-rest-server/target/vt-solicitation-ms-rest-server-0.0.1.war deploy/

echo $PWD
cd bpm-telecommunications-traass-vivo/
git checkout TRAASS-20
git pull --rebase

echo $PWD
cd portal/customer-portal-vivo-angular/

npm install

#ng build --base-href /portal_vivo/
ng build

cd ../../..

cp -rf bpm-telecommunications-traass-vivo/portal/customer-portal-vivo-angular/dist deploy/

cd deploy
zip -r dist.zip dist

#nohup java -jar vt-user-ms-rest-server-0.0.1.war > vt-user-ms-rest-server.log &
#nohup java -jar vt-solicitation-ms-rest-server-0.0.1.war > vt-solicitation-ms-rest-server.log &
#mover dist para diretorio do nginx
