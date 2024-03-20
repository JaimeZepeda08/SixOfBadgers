FROM mysql

ENV MYSQL_DATABASE=euchre
ENV MYSQL_ROOT_PASSWORD=supersecure

RUN chown -R mysql:root /var/lib/mysql/

COPY ./euchre_schema.sql /docker-entrypoint-initdb.d

EXPOSE 3306
