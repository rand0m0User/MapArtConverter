@echo off
java -Xms4G -Xmx16G -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -jar MapArtConverter.jar "1667166900088999.jpg" --dither --stainedGlass --virt
pause