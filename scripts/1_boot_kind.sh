#!/usr/bin/env bash

kindCmd=$1
if [ -z "$1" ]
then
    kindCmd=kind
fi

echo "Cleaning up old test artifacts"
$kindCmd delete cluster > /dev/null 2>&1 || true

echo "Creating test cluster"
$kindCmd create cluster

configPath=$($kindCmd get kubeconfig-path)
echo "export KUBECONFIG=$configPath"
