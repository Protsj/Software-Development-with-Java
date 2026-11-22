#!/usr/bin/env sh
set -eu

cd "$(dirname "$0")"

# Optional override: PR4_JAVA_HOME=/path/to/jdk ./run.sh
if [ -n "${PR4_JAVA_HOME:-}" ]; then
    export JAVA_HOME="$PR4_JAVA_HOME"
fi

if [ -n "${JAVA_HOME:-}" ] && [ ! -x "$JAVA_HOME/bin/java" ]; then
    echo "[ERROR] Java was not found at '$JAVA_HOME/bin/java'." >&2
    echo "Set PR4_JAVA_HOME to your JDK 27+ folder and try again." >&2
    exit 1
fi

# ./run.sh        -> interactive CLI
# ./run.sh demo   -> original non-interactive demo
EXTRA_ARGS=""
for arg in "$@"; do
    case "$arg" in
        demo|--demo) EXTRA_ARGS="-Dexec.args=--demo" ;;
        *) EXTRA_ARGS="$EXTRA_ARGS $arg" ;;
    esac
done

echo "Starting PR4 Order Management..."
# shellcheck disable=SC2086
exec ./mvnw -B -pl Module/Order/app -am package exec:java $EXTRA_ARGS
