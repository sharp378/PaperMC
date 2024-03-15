FROM amazoncorretto:21-alpine-jdk as build

ARG version
ARG build

RUN apk update \
  && apk add --no-cache curl
      
WORKDIR /tmp/server

RUN curl -o server.jar "https://api.papermc.io/v2/projects/paper/versions/${version}/builds/${build}/downloads/paper-${version}-${build}.jar" \
  && echo 'eula=true' > eula.txt \
  && chmod 700 server.jar

WORKDIR /tmp/plugins

COPY ./servinator-plugin/ .
RUN ./gradlew distZip && \
  chmod 700 app/build/libs/app.jar \
  && mkdir /tmp/server/plugins \
  && mv app/build/libs/app.jar /tmp/server/plugins/Servinator-0.1.0.jar


FROM amazoncorretto:21-alpine-jdk as release

RUN adduser --system --disabled-password paper

WORKDIR /home/paper
COPY --from=build --chown=paper /tmp/server .

USER paper
ENTRYPOINT ["java", "-jar", "server.jar", "--nogui"]
