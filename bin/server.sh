#!/usr/bin/env bash

set -eu

CWD="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ "$#" -ne 3 ]; then
    echo "USAGE: server.sh PORT DB-URL DB-USER"
    exit 1;
fi

java -cp "$CWD/../lib/*" \
     -Dlog4j.configurationFile="file://$CWD/../config/log4j2.xml" \
     -Dlog4j.debug \
     com.testedminds.template.Server \
     --port $1 \
     --url  $2 \
     --user $3
