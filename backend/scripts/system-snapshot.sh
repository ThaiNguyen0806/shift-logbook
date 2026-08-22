#!/bin/bash

set -euo pipefail

LOAD=$(uptime | awk -F'load average:' '{print $2}' | xargs)

ERRORS=$(grep -iE "error|fail|critical" /var/log/syslog 2>/dev/null | tail -n 10 || echo "No recent errors found")

SNAPSHOT="CPU load (1/5/15 min): ${LOAD}
Recent system errors:
${ERRORS}"

echo "$SNAPSHOT"