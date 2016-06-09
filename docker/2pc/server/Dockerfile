FROM mateuszj92/java_server

EXPOSE 8080:8080
EXPOSE 27017:27017

WORKDIR /usr

CMD mongod & java -jar 2pc-server.jar
