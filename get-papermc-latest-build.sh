#!/bin/bash

version="${1}"
response=$(curl -s -X 'GET' "https://api.papermc.io/v2/projects/paper/versions/${version}" -H 'accept: application/json')
builds_raw=$(echo $response | grep -Po '(?<="builds":\[).*(?=\],?)')

if test -z $builds_raw; then
    echo "An error occurred parsing the response:"
    echo "$response"
    exit 1
fi	

readarray -td, builds <<<"$builds_raw,"; unset 'builds[-1]';

latest=${builds[-1]}
for current in ${list[@]}; do
    if [ $current -gt $latest ]; then
        latest=$current
    fi
done

echo "$latest"
