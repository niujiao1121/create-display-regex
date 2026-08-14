#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$ROOT/.test-classes"
rm -rf "$OUT"
mkdir -p "$OUT"
javac -d "$OUT" \
  "$ROOT/src/main/java/dev/createfly/displayregex/RegexProcessor.java" \
  "$ROOT/scripts/RegexProcessorSelfTest.java"
java -cp "$OUT" RegexProcessorSelfTest
