@echo off
java -Xms4G -Xmx16G -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -jar dist\MapArtConverter.jar "1661220070606-0.png" --dither
pause