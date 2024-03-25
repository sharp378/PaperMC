#!/bin/sh

cp -f /tmp/server/* $PWD

[ ! -d "$PWD/plugins" ] && mkdir $PWD/plugins
cp -f /tmp/plugins/*.jar $PWD/plugins

java -jar server.jar --nogui "$@"
