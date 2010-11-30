#!/bin/bash

rhino="./js.jar"
jagged="./build"

if [[ $# == 0 ]]; then 
  echo "Usage: $0 file.js [file2.js [file3.js [...]]]";
fi

scan=$@

# Remove BOM
perl -pi -e 's/\xEF\xBB\xBF//g' $scan
# Fix bad 'float' declaration
perl -pi -e 's/float:o/"float":o/g' $scan


# Scan all JS files for syntax errors.
java -classpath $rhino:$jagged Jagged $scan
