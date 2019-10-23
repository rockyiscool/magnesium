#!/usr/bin/env bash

registry="com.rc"
name="magnesium"
tag="DEV"
file="e2e/src/test/resources/k8s/magnesium.yaml"
temp="$file.temp"

image="$registry\/$name:$tag"

sed 's/@IMAGE/'$image'/g' $file > $temp
cat $temp > $file
rm -rf $temp