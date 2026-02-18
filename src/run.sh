#!/bin/bash
javac -cp ".:../libs/mysql-connector-java-8.0.17.jar" */*.java MainTest.java
java -cp ".:../libs/mysql-connector-java-8.0.17.jar" MainTest