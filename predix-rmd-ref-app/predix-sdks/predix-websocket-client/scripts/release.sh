#! /usr/bin/sh
rm scripts/testVPC.sh
cp config/application-template.properties config/application.properties 
rm config/application-template.properties
rm config/application-through-proxy.properties
rm config/application.properties.backup
sed -i 's/..\/..\/..\/..\/pages\/adoption/http:\/\/predixdev.github.io/g' README.md
