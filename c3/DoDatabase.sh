#!/bin/bash
# Create database zipfile using application version info and push to artifactory
# We expect to run in jenkins workspace with c3 folder directly underneath
# And have execute permissions set on this script i.e. chmod +x DoDatabase.sh which should carry through to jenkins workspace.
#
# WARNING .. Temporary script for sbt builds therefore simplistic and likely duplicated amongst any projects with database
#
# The database zipfile looks like
# <appname>-<appversion>-database.zip
# i.e. c3-3.13-SNAPSHOT-database.zip
#
# We post to artifactory using curl PUT with command that looks like
# curl -u $user:$password -X PUT http://build.3cbeta.co.uk:8080/artifactory/libs-snapshot-local/gov/dwp/carers/cs_2.10/2.8-SNAPSHOT/cg.txt -T cg.txt
#
script=`basename $0`

#####
# Usage
# We expect to be passed url of artefactory server and port.
#####
artifactory=$1
if [ -z "$artifactory" ]
then
	echo "========================================================================="
	echo "FATAL ERROR - Exects argument of artifactory url to be passed"
	echo "i.e. $script http://build.3cbeta.co.uk:8080"
	echo "========================================================================="
	exit 1
fi

#####
# Create database zipfile
# Version info is found in the workspace application-info.conf and contains something like ...
# application.version=3.13-SNAPSHOT
# application.name=c3
#####
# we get called from parent directory so need it find the conf file
parentdir=`dirname $0`
appconffile="$parentdir/conf/application-info.conf"
if [ ! -f $appconffile ]
then
	echo "========================================================================="
	echo "FATAL ERROR - No conf file found $appconffile"
	echo "Current directory is $PWD"
	echo "========================================================================="
	exit 1
fi
appname=`awk -F= '/application.name/{print $2}' $appconffile`
appversion=`awk -F= '/application.version/{print $2}' $appconffile`
echo "Retrieved application.name:$appname and application.version:$appversion from $appconffile"

if [ ! -d "database" ]
then
	echo "========================================================================="
	echo "FATAL ERROR - No database directory found under current directory $PWD"
	echo "========================================================================="
	exit 1
fi
dbzipfile=$appname"-"$appversion"-database.zip"
rm -f $dbzipfile
zip -r $dbzipfile database

if [ ! -f $dbzipfile ]
then
	echo "========================================================================="
	echo "FATAL ERROR - Failed to create dbzipfile $dbzipfile"
	echo "========================================================================="
	exit 1
fi

#####
# Upload zipfile to artifactory. We expect atifactory credentials to be found in  $HOME/.sbt/.credentials
# user=jenkins
# password={DESede}ABCDE+kOdMbdOQv1234567==
#####
credfile="$HOME/.sbt/.credentials"
if [ ! -f $credfile ]
then
	echo "========================================================================="
	echo "FATAL ERROR - No credentials file found $credfile"
	echo "Current directory is $PWD"
	echo "========================================================================="
	exit 1
fi
user=`awk -F= '/user/{print $2}' $credfile`
password=`awk -F= '/password/{print $2}' $credfile`

urlfolder="$artifactory/artifactory/libs-snapshot-local/gov/dwp/carers/"$appname"/"$appversion
curlcmd="curl -u $user:$password -X PUT $urlfolder/$dbzipfile -T $dbzipfile"
$curlcmd
