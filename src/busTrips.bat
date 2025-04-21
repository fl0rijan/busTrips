@echo off
javac -cp ".;..\lib\*;" .\busTrips.java
java -Dorg.slf4j.simpleLogger.defaultLogLevel=off -cp ".;..\lib\*;" .\busTrips.java %*