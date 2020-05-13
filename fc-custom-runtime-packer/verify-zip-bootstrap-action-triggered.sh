#!/usr/bin/env bash

pattern=\\d{2}:\\d{2}:\\d{2}\.\\d{9}
previousCreatedTime=$(ls ../demo/build/libs -lc --time-style=full-iso |grep demo-1.0.0-default.zip|grep -o -P ${pattern})
# rw-rw-r-- 1 dengchao dengchao 19314577 2020-04-26 14:48:00.126628582 +0800 demo-1.0.0-default.zip
#                                                   ^----------------^

./../gradlew :demo:zipBootstrap

currentCreatedTime=$(ls ../demo/build/libs -lc --time-style=full-iso |grep demo-1.0.0-default.zip|grep -o -P ${pattern})

if [[ ${previousCreatedTime} == ${currentCreatedTime} ]] ; then
    echo "zipBootstrap task is not re-generated"
    touch ./verify-zip-bootstrap-action-triggered-failed
fi
