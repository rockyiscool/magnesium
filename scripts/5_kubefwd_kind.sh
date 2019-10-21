#!/usr/bin/env bash

 _NAMESPACE="$1"

if [ -z "$1" ]
then
    _NAMESPACE="itest"
fi

#sudo kubefwd svc -n ${_NAMESPACE} -c ~/.kube/kind-config-test-cluster
sudo kubefwd svc -n ${_NAMESPACE} -c ~/.kube/kind-config-kind
