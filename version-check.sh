#!/bin/bash

# Extract the tag from GITHUB_REF
tag_ref=${GITHUB_REF#refs/tags/}

echo "Checking tag: $tag_ref"

# Check if the tag is present in metadata/version_info.json
if grep -q "$tag_ref" metadata/version_info.json; then
    echo "Tag found in metadata/version_info.json"
else
    echo "Error: Tag not found in metadata/version_info.json"
    exit 1
fi

# Extract mod_version from gradle.properties
mod_version=$(grep '^mod_version=' gradle.properties | cut -d '=' -f2 | tr -d '[:space:]')
if [ -z "$mod_version" ]; then
    echo "Error: Failed to extract mod_version from gradle.properties"
    exit 1
fi

echo "Version in gradle.properties: $mod_version"

# Compare versions
if [[ "$tag_ref" == "$mod_version" ]]; then
    echo "Success: Git tag matches mod_version in gradle.properties"
    exit 0
else
    echo "Error: Git tag does not match mod_version"
    exit 1
fi