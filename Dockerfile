FROM fabric8/java-alpine-openjdk8-jre

COPY spring-boot-app/target/spring-boot-app-exec.jar java-run.sh /app/

EXPOSE 8080
EXPOSE 8081

RUN ["chmod", "+x", "/app/java-run.sh"]

WORKDIR /app
CMD ["/app/java-run.sh"]
