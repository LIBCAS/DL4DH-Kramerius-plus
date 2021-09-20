#!/bin/sh

cd /usr/src
java -jar -Xmx512m api.jar > api.stdout 2> api.stderr

# Keep docker running, when java was failed.
tail -f /etc/issue
