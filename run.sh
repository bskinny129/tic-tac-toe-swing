#!/usr/bin/env bash
# Compiles the game and runs it. The window shows up in the Desktop tab (port 6080).
set -e

cd "$(dirname "$0")"

echo "Compiling..."
mkdir -p out
javac -d out *.java

echo "Starting the game - switch to the Desktop tab to play!"
java -cp out TicTacToeFinished
