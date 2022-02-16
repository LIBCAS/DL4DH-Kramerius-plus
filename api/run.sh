#!/bin/sh

sleep 10

cd /usr/src
java -jar -Xmx1024m api.jar > api.stdout 2> api.stderr

# Keep docker running, when java was failed.
tail -f /etc/issue
