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

echo "Creating test cluster"
#"${KIND}" create cluster --name "${NAME}" # --config config.yaml
"${KIND}" create cluster # --config config.yaml
#echo "export KUBECONFIG="$("${KIND}" get kubeconfig-path --name "${NAME}")""
echo "export KUBECONFIG="$("${KIND}" get kubeconfig-path)""
