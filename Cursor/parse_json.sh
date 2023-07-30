#!/bin/bash

#  从data.json 中获取 zip 字段

# Read the JSON file
json_data=$(cat data.json)

# Parse the JSON data
name=$(echo ${json_data} | jq -r '.name')
age=$(echo ${json_data} | jq -r '.age')
zip=$(echo ${json_data} | jq -r '.zip')

echo "Name: $name"
echo "Age: $age"
echo "Zip: $zip"



