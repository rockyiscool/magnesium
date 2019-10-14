#!/usr/bin/env bash

_NAMESPACE="itest"
_YAML_FILE="e2e/src/test/resources/k8s/magnesium.yaml"
_FULL_IMAGE="com.rc\/magnesium:DEV"
_YAML_TEMP="$YAML_FILE.temp"

echo "Delete the test namespace"
kubectl delete ns/$_NAMESPACE

echo "Rollback the image placeholder"
sed 's/'$_FULL_IMAGE'/@IMAGE/g' $_YAML_FILE > $_YAML_TEMP
cat $_YAML_TEMP > $_YAML_FILE
rm -rf $_YAML_TEMP
