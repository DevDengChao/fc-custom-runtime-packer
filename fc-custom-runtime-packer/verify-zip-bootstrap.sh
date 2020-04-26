#!/usr/bin/env bash

cd ../demo/build/libs

unzip -o -d default demo-1.0.0-default.zip
cd default
# start up
nohup ./bootstrap >> ./log.txt &
# wait for application start up
ping -c 5 localhost > /dev/null
# shutdown application
curl -X POST http://localhost:9000/actuator/shutdown
echo

cd ..
unzip -o -d pro demo-1.0.0-pro.zip
cd pro
nohup ./bootstrap >> ./log.txt &
ping -c 5 localhost > /dev/null
curl -X POST http://localhost:9000/actuator/shutdown
echo
