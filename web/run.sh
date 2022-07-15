#!/bin/sh
if [ -z $API_HOST ]; then 
    API_HOST=127.0.0.1
fi
if [ -z $TEI_HOST ]; then 
    TEI_HOST=127.0.0.1
fi
if [ -z $MONGODB_HOST ]; then
    MONGODB_HOST=127.0.0.1
fi
if [ -z $MONGODB_EXPRESS_HOST ]; then
    MONGODB_EXPRESS_HOST=127.0.0.1
fi

cat <<EOT >> /etc/apache2/conf.d/proxy-api.conf
ProxyPreserveHost On

# API
ProxyPass "/api" "http://$API_HOST:8080/api"
ProxyPassReverse "/api" "http://$API_HOST:8080/api"

# Swagger
ProxyPass "/swagger" "http://$API_HOST:8080/swagger"
ProxyPassReverse "/swagger" "http://$API_HOST:8080/swagger"
ProxyPass "/swagger-ui" "http://$API_HOST:8080/swagger-ui"
ProxyPassReverse "/swagger-ui" "http://$API_HOST:8080/swagger-ui"
ProxyPass "/v3" "http://$API_HOST:8080/v3"
ProxyPassReverse "/v3" "http://$API_HOST:8080/v3"

# MongoDB
ProxyPass "/db" "http://$MONGODB_EXPRESS_HOST:8081/db"
ProxyPassReverse "/db" "http://$MONGODB_EXPRESS_HOST:8081/db"

# TEI Converter
ProxyPass "/tei" "http://$TEI_HOST:5000/tei"
ProxyPassReverse "/tei" "http://$TEI_HOST:5000/tei"
EOT

cat <<EOT > /etc/apache2/conf.d/rewrite.conf
<Directory "/var/www/localhost/htdocs">
    RewriteEngine on
    RewriteCond %{REQUEST_FILENAME} -f [OR]
    RewriteCond %{REQUEST_FILENAME} -d
    RewriteRule ^ - [L]
    RewriteRule ^ index.html [L]
</Directory>
EOT

sed -i '/LoadModule rewrite_module/s/^#//g' /etc/apache2/httpd.conf

chown -R apache:apache /var/www/localhost
httpd -D FOREGROUND
tail -f /etc/issue
