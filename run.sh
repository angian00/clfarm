#!/bin/sh

#QUARTZ_DIR=~/tools/quartz-2.2.1
#CLASSPATH=./classes:$QUARTZ_DIR/"lib/*"
CLASSPATH=./classes
MAIN_CLASS=ag.clfarm.Launcher

java -classpath $CLASSPATH $MAIN_CLASS

