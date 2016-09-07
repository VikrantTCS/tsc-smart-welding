#! /usr/bin/sh
cp src/test/resources/application-template.properties src/test/resources/application.properties 
rm src/test/resources/application-restclientonly.properties
sed -i 's/..\/..\/..\/..\/pages\/adoption/http:\/\/predixdev.github.io/g' README.md
