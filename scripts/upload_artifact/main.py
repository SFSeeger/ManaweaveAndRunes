import os
import json
import requests

def main():
    # Environment variables (passed from GitHub Actions)
    modrinth_api_key = os.environ["MODRINTH_API_KEY"]
    project_id = os.environ["MODRINTH_PROJECT_ID"]
    tag_ref = os.environ["TAG_REF"]
    version_type = os.environ.get("VERSION_TYPE", "release")
    changelog = os.environ.get("CHANGELOG", "")
    url = os.environ.get("MODRINTH_API_URL", "")

    jar_path = f"build/libs/manaweave_and_runes-{tag_ref}.jar"

    # Construct payload (same as your JSON_PAYLOAD)
    payload = {
        "project_id": project_id,
        "loaders": ["neoforge"],
        "game_versions": ["1.21.1"],
        "name": f"Manaweave and Runes {tag_ref}",
        "version_number": tag_ref,
        "file_parts": ["jar"],
        "primary_file": "jar",
        "changelog": changelog,
        "dependencies": [
            {"project_id": "8BmcQJ2H", "dependency_type": "required"},
            {"project_id": "nU0bVIaL", "dependency_type": "required"},
        ],
        "version_type": version_type,
        "featured": version_type == "release",
        "status": "listed",
    }

    # Prepare multipart request
    files = {
        "data": ("data.json", json.dumps(payload), "application/json"),
        "jar": (os.path.basename(jar_path), open(jar_path, "rb")),
    }
    print(json.dumps(payload, indent=2))

    headers = {
        "Authorization": modrinth_api_key,
        "User-Agent": "SFSeeger/manaweave-and-runes",
    }

    response = requests.post(
        url,
        headers=headers,
        files=files,
    )

    # Debug output
    print("Status:", response.status_code)
    print("Response:", response.text)

    if response.status_code >= 400:
        raise Exception(f"Upload failed with error {response.status_code}: {response.text}")

if __name__ == "__main__":
    main()