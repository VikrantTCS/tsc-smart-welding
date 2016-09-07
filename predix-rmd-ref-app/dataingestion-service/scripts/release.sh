#! /bin/sh
cp config/application-template.properties config/application.properties 
cp manifest.yml.template manifest.yml
sed -i 's/..\/..\/..\/..\/pages\/adoption/http:\/\/predixdev.github.io/g' README.md
