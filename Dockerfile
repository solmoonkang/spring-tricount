FROM amazonlinux:latest
MAINTAINER the.eris.net

RUN yum update -y
RUN yum install httpd -y
RUN yum install java-21-amazon-corretto -y
COPY ./index.html /var/www/html/index.html

COPY ./entrypoint.sh /entrypoint.sh
COPY build/libs/*.jar /app.jar

EXPOSE 80

ENTRYPOINT ["sh", "/entrypoint.sh"]