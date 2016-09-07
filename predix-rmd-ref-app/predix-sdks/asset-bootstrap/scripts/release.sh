#! /usr/bin/sh
cp config/application-template.properties config/application.properties 
rm config/application-template.properties
sed -i 's/..\/..\/..\/..\/pages\/adoption/http:\/\/predixdev.github.io/g' README.md
