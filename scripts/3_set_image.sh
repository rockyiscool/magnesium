#!/usr/bin/env bash

_DOCKER_REGISTRY=$1
_IMAGE_NAME=$2
_IMAGE_TAG=$3
_FILE=$4
_TEMP="$FILE.temp"

if [ -z "$1" ] && [ -z "$2" ] && [ -z "$3" ] && [ -z "$4" ]
then
    _DOCKER_REGISTRY="com.rc"
    _IMAGE_NAME="magnesium"
    _IMAGE_TAG="DEV"
    _FILE="e2e/src/test/resources/k8s/magnesium.yaml"
fi

_FULL_IMAGE="$_DOCKER_REGISTRY\/$_IMAGE_NAME:$_IMAGE_TAG"

sed 's/@IMAGE/'$_FULL_IMAGE'/g' $_FILE > $_TEMP
cat $_TEMP > $_FILE
rm -rf $_TEMP