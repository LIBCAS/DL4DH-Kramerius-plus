#!/bin/sh

sleep 15

cd /usr/src
java -jar -Xmx1024m api.jar

# Keep docker running, when java was failed.
tail -f /etc/issue
