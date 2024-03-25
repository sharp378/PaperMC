echo $(curl -s "https://api.github.com/repos/sharp378/Servinator/releases/latest" | jq -r '.tag_name')
