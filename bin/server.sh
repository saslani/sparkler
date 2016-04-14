#!/usr/bin/env bash

set -eu

CWD="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

java -cp "$CWD/../lib/*" \
     -Dlog4j.configurationFile="file://$CWD/../config/log4j2.xml" \
     -Dlog4j.debug \
     com.testedminds.sparkler.Server
