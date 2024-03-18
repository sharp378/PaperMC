FROM alpine:3.19 as build

ARG version
ARG build

RUN apk update \
  && apk add --no-cache curl
      
WORKDIR /tmp/server

RUN curl -o server.jar "https://api.papermc.io/v2/projects/paper/versions/${version}/builds/${build}/downloads/paper-${version}-${build}.jar" \
  && echo 'eula=true' > eula.txt \
  && chmod 500 server.jar

WORKDIR /tmp/plugins

RUN url=$(curl -s https://api.github.com/repos/sharp378/Servinator/releases/latest | grep "browser_download_url" | awk -F '"' '{print $4}' | grep '[0-9]\.jar') \
  && curl -LJO "$url" \
  && chmod 500 *.jar \
  && mkdir /tmp/server/plugins \
  && mv *.jar /tmp/server/plugins/


FROM amazoncorretto:21-alpine-jdk as release

LABEL org.opencontainers.image.name="Paper Minecraft Server"
LABEL org.opencontainers.image.authors="sharpscontainer"
LABEL org.opencontainers.image.description="A simple Paper server for Minecraft that self terminates"
LABEL org.opencontainers.image.source="https://github.com/sharp378/PaperMC"

RUN adduser --system --disabled-password paper

WORKDIR /home/paper
COPY --from=build --chown=paper /tmp/server .

ENV SERVINATOR_INTERVAL=5

USER paper
ENTRYPOINT ["java", "-jar", "server.jar", "--nogui"]
