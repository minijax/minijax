#!/bin/bash
mvn -o -Dmaven.javadoc.skip=true pre-site site site:stage

