#!/bin/sh

cp -f ../target/sneo-core*.jar ./sneo-core.jar
mvn -f ../pom.xml -DoverWriteIfNewer=true -DoutputDirectory=bin -Dmdep.stripVersion=true -DincludeScope=compile dependency:copy-dependencies
