#!/usr/bin/env bash

set -eu

CWD="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

if [ "$#" -ne 2 ]; then
    echo "USAGE: migrate.sh URL USER"
    exit 1;
fi

java -cp "$CWD/../lib/*" com.testedminds.template.db.Migrate $1 $2
