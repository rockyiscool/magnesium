#!/usr/bin/env bash

kindCmd=$1
if [ -z "$1" ]
then
    kindCmd=kind
fi

echo "Load com.rc/magnesium:DEV"
$kindCmd load docker-image com.rc/magnesium:DEV
