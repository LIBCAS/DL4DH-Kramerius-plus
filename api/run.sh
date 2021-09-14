#!/bin/sh

cd /usr/src
java -jar -Xmx256m kramerius.jar > kramerius.stdout 2> kramerius.stderr

# Keep docker running, when java was failed.
tail -f /etc/issue
