FROM alpine:3.11
LABEL maintainer=sekan@inqool.cz

COPY run.sh /run.sh
RUN chmod +x /run.sh && \
    apk add --update \
      ca-certificates ca-certificates-cacert \
      openjdk11-jre openjdk11-jre-headless openssl fontconfig ttf-dejavu && \
    rm -rf /var/cache/apk/*

COPY krameriusplus-app/target/krameriusplus-app-*.jar /usr/src/api.jar

ENTRYPOINT ["/run.sh"]
