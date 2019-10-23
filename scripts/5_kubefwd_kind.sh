#!/usr/bin/env bash

kubefwdCmd=$1
kubeConfig=$2

if [ -z "$1" ]
then
    kubefwdCmd=kubefwd
fi

if [ -z "$2" ]
then
    kubeconfig=~/.kube/config
fi

$kubefwdCmd svc -n itest -c $kubeConfig
