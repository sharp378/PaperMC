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
RUN ./gradlew shadowJar --no-daemon && \
  chmod 700 app/build/libs/app-all.jar \
  && mkdir /tmp/server/plugins \
  && mv app/build/libs/app-all.jar /tmp/server/plugins/Servinator-0.1.0.jar


FROM amazoncorretto:21-alpine-jdk as release

RUN adduser --system --disabled-password paper

WORKDIR /home/paper
COPY --from=build --chown=paper /tmp/server .

ENV PLUGIN_DELAY=5
ENV ECS_ENABLED=false
ENV ECS_CLUSTER_ARN=changeme
ENV ECS_SERVICE_ARN=changeme

USER paper
ENTRYPOINT ["java", "-jar", "server.jar", "--nogui"]
