#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p out
find src -name '*.java' -print > fuentes.txt
javac --release 17 -encoding UTF-8 -d out @fuentes.txt
java -cp out filey.Main "$@"
