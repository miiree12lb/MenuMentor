#!/usr/bin/env sh
# Compile and run MenuMentor (macOS / Linux)
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -encoding UTF-8 -cp "lib/*" -d out src/*.java
java -cp "out:resources:lib/*" Main
