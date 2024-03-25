FROM alpine:3.19 as build

ARG paper_version
ARG paper_build
ARG servinator_version

WORKDIR /tmp/server
ADD "https://api.papermc.io/v2/projects/paper/versions/${paper_version}/builds/${paper_build}/downloads/paper-${paper_version}-${paper_build}.jar" ./server.jar
COPY ./start-server.sh .
RUN echo 'eula=true' > eula.txt

WORKDIR /tmp/plugins
ADD "https://github.com/sharp378/Servinator/releases/download/v${servinator_version}/ServinatorPlugin-${servinator_version}.jar" .

FROM amazoncorretto:21-alpine-jdk as release

LABEL org.opencontainers.image.name="Paper Minecraft Server"
LABEL org.opencontainers.image.authors="sharpscontainer"
LABEL org.opencontainers.image.description="A simple Paper server for Minecraft that self terminates"
LABEL org.opencontainers.image.source="https://github.com/sharp378/PaperMC"

COPY --from=build --chmod=755 /tmp/server /tmp/server
COPY --from=build --chmod=755 /tmp/plugins /tmp/plugins

RUN addgroup --gid 1000 paper \
  && adduser --disabled-password --uid 1000 --ingroup paper paper \
  && chown paper:paper /home/paper

WORKDIR /home/paper
USER paper:paper

VOLUME /home/paper
EXPOSE 25565

ENV SERVINATOR_INTERVAL=5

ENTRYPOINT ["/tmp/server/start-server.sh"]
