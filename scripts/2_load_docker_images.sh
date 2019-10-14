#!/usr/bin/env bash

KIND=$1
if [ -z "$1" ]
then
    KIND=kind
fi

NAME="test-cluster"

echo "Load com.rc/magnesium:DEV"
"${KIND}" load docker-image --name "${NAME}" com.rc/magnesium:DEV
