FROM amazoncorretto:21-alpine-jdk as build

ARG version
ARG build

RUN apk update \
  && apk add --no-cache curl git openssl maven
      
WORKDIR /tmp/server

RUN curl -o server.jar "https://api.papermc.io/v2/projects/paper/versions/${version}/builds/${build}/downloads/paper-${version}-${build}.jar" \
  && echo 'eula=true' > eula.txt \
  && chmod 700 server.jar

WORKDIR /tmp/plugins
RUN git clone https://github.com/MatthewDietrich/CloudWatch.git .

# overwrite files to remove unused metrics
COPY ./cloudwatch-plugin/ src/main/java/github/metalshark/cloudwatch/

RUN  mvn --batch-mode --update-snapshots package \
  && chmod 700 target/CloudWatch-1.1.4.jar \
  && mkdir /tmp/server/plugins \
  && mv target/CloudWatch-1.1.4.jar /tmp/server/plugins/



FROM amazoncorretto:21-alpine-jdk as release

RUN adduser --system --disabled-password paper

WORKDIR /home/paper
COPY --from=build --chown=paper /tmp/server .

USER paper
ENTRYPOINT ["java", "-jar", "server.jar", "--nogui"]
