# MapArtConverter
lightweight tool to convert png/jpg images to Minecraft schematics

# arguments
one or all argumants can be used together.
- **"--dither"** self explanitory, uses [Java-Floyd-Steinberg-Dithering](https://github.com/Regarrzo/Java-Floyd-Steinberg-Dithering) to dither the input image, orignal file is not changed
- **"--litematic"** saves a litematica schematic instead of the default schematica/mcedit one 
- **"--stainedGlass"** self explanitory, uses only stained glass (--dither option recomended)
- **"--virt"** can be used to make stained glass windows or walls of memes (taking into account the height limit obviously)

# example

>java -jar "MapArtConverter.jar" "my image.png" --dither
