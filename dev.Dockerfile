FROM amazoncorretto:21-alpine-jdk

WORKDIR /tmp
COPY ./servinator-plugin/ .

RUN mkdir app/run \
  && echo 'eula=true' > app/run/eula.txt

ENV PLUGIN_DELAY=10
ENV ECS_EMABLED=true
ENV ECS_CLUSTER_ARN=sample_cluster_arn
ENV ECS_SERVICE_ARN=sample_service_arn

RUN ./gradlew shadowJar
ENTRYPOINT ["./gradlew", "runServer"]
