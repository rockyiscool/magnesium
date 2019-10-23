#!/usr/bin/env bash

yaml="e2e/src/test/resources/k8s/magnesium.yaml"
image="com.rc\/magnesium:DEV"
temp="$yaml.temp"

echo "Delete the test namespace"
kubectl delete ns/itest

echo "Rollback the image placeholder"
sed 's/'$image'/@IMAGE/g' $yaml > $temp
cat $temp > $yaml
rm -rf $temp
