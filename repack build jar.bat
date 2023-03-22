@echo off
color 0a
"C:\Program Files\7-Zip\7z" a -sdel tmp.null build
del tmp.null
del MapArtConverter.jar
cd dist
..\..\..\__NBP_tools_dir__\advzip.exe -z -3 MapArtConverter.jar
move MapArtConverter.jar ..\MapArtConverter.jar
cd..
"C:\Program Files\7-Zip\7z" a -sdel tmp.null dist
del tmp.null