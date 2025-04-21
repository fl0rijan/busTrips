## Bus trips
args: buxTrips.java <stop_id> <count of how many arrival_times to show for each route> <absolute|relative>

javac -cp ".;..\lib\*;" .\busTrips.java
java -Dorg.slf4j.simpleLogger.defaultLogLevel=off -cp ".;..\lib\*;" .\busTrips.java 5 5 absolute


run in cmd: \src> .\compile.bat
