FROM openjdk:8-jre-alpine

LABEL maintainer=sekan@inqool.cz

COPY target/*.jar /usr/src/kramerius.jar
COPY run.sh /run.sh

RUN chmod +x /run.sh && apk add --update ttf-dejavu

ENTRYPOINT ["/run.sh"]
