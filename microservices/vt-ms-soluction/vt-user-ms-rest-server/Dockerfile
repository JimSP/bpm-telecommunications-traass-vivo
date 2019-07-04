FROM jboss/wildfly

ADD vt-user-ms-rest-server-0.0.1.war /opt/jboss/wildfly/standalone/deployments/

ARG PORT=8080
ENV PORT $PORT

EXPOSE $PORT
