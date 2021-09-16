#!/bin/sh
if [ -z $API_HOST ]; then 
    API_HOST=127.0.0.1
fi
if [ -z $TEI_HOST ]; then 
    TEI_HOST=127.0.0.1
fi

cat <<EOT >> /etc/apache2/conf.d/proxy-api.conf
ProxyPass "/api" "http://$API_HOST:8080/api"
ProxyPassReverse "/api" "http://$API_HOST:8080/api"
ProxyPass "/tei" "http://$TEI_HOST:5000"
ProxyPassReverse "/tei" "http://$TEI_HOST:5000"
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
