#/bin/bash

echo "creating build"
cd ../test-common
#mvn clean install -Dmaven.test.skip=true -Dfindbugs.skip=true
cd ../inttest
mvn clean install -Dmaven.test.skip=true -Dfindbugs.skip=true

echo "deleting from tomcat"
rm -rf /usr/local/apache-tomcat-7.0.79/webapps/inttest*

cp target/inttest.war /usr/local/apache-tomcat-7.0.79/webapps

echo "complete"
