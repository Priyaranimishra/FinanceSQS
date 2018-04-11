#!/bin/bash
file="/var/lib/tomcat8/webapps/petclinic.war"
if [ -f $file ] ; then
	sudo rm -rf $file
fi