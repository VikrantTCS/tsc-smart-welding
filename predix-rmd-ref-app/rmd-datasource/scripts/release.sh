#! /bin/sh
cp config/application-template.properties config/application.properties 
sed -i 's/..\/..\/..\/..\/pages\/adoption/http:\/\/predixdev.github.io/g' README.md
