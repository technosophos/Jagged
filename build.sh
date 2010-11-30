#!/bin/bash

jsjar="./js.jar"

javac -classpath $CLASSPATH:$jsjar -d ./build src/*.java
