#!/bin/sh
# Minimal Gradle wrapper launcher — calls the wrapper JAR directly.
# This implementation is intentionally small to avoid issues with complex generated
# scripts in CI/editor environments. It supports passing JVM options via
# JAVA_OPTS or DEFAULT_JVM_OPTS if set.

set -e

DIRNAME="$(cd "$(dirname "$0")" && pwd)"
APP_HOME="$DIRNAME"
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Gradle wrapper JAR not found at $WRAPPER_JAR" >&2
  exit 1
fi

# Choose java command
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

# Allow users to prepend JVM options in DEFAULT_JVM_OPTS or JAVA_OPTS
DEFAULT_JVM_OPTS=${DEFAULT_JVM_OPTS:-}
JAVA_OPTS=${JAVA_OPTS:-}

exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS -jar "$WRAPPER_JAR" "$@"
