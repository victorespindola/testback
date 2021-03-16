#!/bin/bash
wait.sh -h db -p 3306 -t 300 --strict -- java -jar ./target/testback-1.0.jar
