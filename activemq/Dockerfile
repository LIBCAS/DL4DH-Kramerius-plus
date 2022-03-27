FROM rmohr/activemq:5.15.4-alpine

COPY --chown=activemq:activemq conf/ /opt/activemq/conf/
COPY --chown=activemq:activemq data/ /opt/activemq/data/

RUN sed -i 's/-Xmx1G/-Xmx128M/g' bin/env
