FROM alpine:3.9
MAINTAINER sekan@inqool.cz

ADD run.sh /run.sh
RUN chmod +x /run.sh

RUN apk add --update apache2 apache2-proxy \
    && rm -rf /var/cache/apk/*

COPY build /var/www/localhost/htdocs

ENTRYPOINT ["/run.sh"]
