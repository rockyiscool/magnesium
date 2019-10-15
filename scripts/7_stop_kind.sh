#!/usr/bin/env bash

KIND=$1
if [ -z "$1" ]
then
    KIND=kind
fi

#NAME="test-cluster"

echo "Cleaning up old test artifacts"
#"${KIND}" delete cluster --name "${NAME}" > /dev/null 2>&1 || true
"${KIND}" delete cluster > /dev/null 2>&1 || true

echo "export KUBECONFIG="