FROM amazoncorretto:21-alpine-jdk

WORKDIR /tmp
COPY ./servinator-plugin/ .

RUN mkdir app/run \
  && echo 'eula=true' > app/run/eula.txt

RUN ./gradlew shadowJar
ENTRYPOINT ["./gradlew", "runServer"]
