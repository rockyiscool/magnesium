#!/usr/bin/env bash

function is_pod_ready() {
  namespace=$1
  pod=$2

  [[ "$(kubectl -n "$namespace" get po "$pod" -o 'jsonpath={.status.conditions[?(@.type=="Ready")].status}')" == 'True' ]]
}

function pods_ready() {
  local pod

  [[ "$pods" == 0 ]] && return 0

  for pod in $pods
  do
    is_pod_ready "$namespace" "$pod" || return 1
  done

  return 0
}

function wait_until_pods_ready() {
  namespace=$1
  timeout=300
  interval=8

  for ((i=0; i<$timeout; i+=$interval))
  do
    pods="$(kubectl -n "$namespace" get po -o 'jsonpath={.items[*].metadata.name}')"
    if pods_ready $namespace $pods
    then
      printf "All pods are ready! \360\237\230\201\n"
      return 0
    fi

    printf 'Waiting for pods to be ready... \360\237\230\264\n'
    sleep "$interval"
  done

  printf "Timeout! All pods are not ready yet. \342\230\271\n"
  return 1
}

namespace="itest"
k8sDir="e2e/src/test/resources/k8s"

echo "Create a namespace - $namespace"
kubectl create namespace $namespace

echo "Apply all the yamls in k8s directory"
kubectl -n $namespace apply -f $k8sDir

echo "Waiting for all pods ready"
wait_until_pods_ready $namespace
