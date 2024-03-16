FROM amazoncorretto:21-alpine-jdk

WORKDIR /tmp
COPY ./servinator-plugin/ .

RUN mkdir app/run \
  && echo 'eula=true' > app/run/eula.txt

ENV INTERVAL=1
ENV ECS_EMABLED=false
ENV ECS_CLUSTER_ARN=sample_cluster_arn
ENV ECS_SERVICE_ARN=sample_service_arn

RUN ./gradlew shadowJar --no-daemon
ENTRYPOINT ["./gradlew", "runServer"]
