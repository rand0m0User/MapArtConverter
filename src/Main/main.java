package Main;

import ch.n1b.libschem.LibschemAPI;
import ch.n1b.worldedit.schematic.block.BaseBlock;
import ch.n1b.worldedit.schematic.block.BlockID;
import ch.n1b.worldedit.schematic.schematic.Cuboid;
import ch.n1b.vector.Vec3D;
import ditherer.ColorPalette;
import ditherer.Ditherer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class main {

    public static Cuboid object; //the schematic data object (slow, arrays are faster but would make the code a mess)
    public static BlockColor[] allBlocks = new BlockColor[49]; //default pallete before any options are set

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("USAGE: (java -jar) MapArtConverter.jar <*.png *.jpg *.bmp> <--dither, --stainedGlass, --virt>");
            return;
        }

        boolean dither = false;
        boolean vertical = false;
        boolean litematic = false;
        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "--litematic":
                    litematic = true;
                    break;
                case "--dither":
                    dither = true;
                    break;
                case "--stainedGlass":
                    initStainedGlass();
                    break;
                case "--virt": //used for stained glass windows of memes
                    vertical = true;
                    break;
            }
        }
        try {
            BufferedImage img = ImageIO.read(new File(createCanonicalFile(args[0]).getAbsolutePath()));
            if (dither) {
                System.out.println("dithering image, as you requested...");
                Color[] palette_colors = new Color[allBlocks.length];
                for (int i = 0; i < allBlocks.length; i++) {
                    palette_colors[i] = allBlocks[i].Color();
                }
                Ditherer ditherer = new Ditherer(new ColorPalette(palette_colors));
                ditherer.dither(img);
            }
            if (vertical) {
                object = new Cuboid(new Vec3D(img.getWidth(), img.getHeight(), 1));
            } else {
                object = new Cuboid(new Vec3D(img.getWidth(), 1, img.getHeight()));
            }
            System.out.println("converting '" + args[0] + "' into '" + args[0] + ".schematic'");
            int dif;
            int diftmp;
            int id = 0;
            //main loop that does everything 
            for (int x = 0; x < img.getWidth(); ++x) {
                for (int y = 0; y < img.getHeight(); ++y) {
                    Color c = new Color(img.getRGB(x, y));
                    dif = 255;
                    for (int i = 0; i < allBlocks.length; ++i) {
                        diftmp = allBlocks[i].getDif(c);
                        if (diftmp < dif) {
                            dif = diftmp;
                            id = i;
                        }
                    }
                    if (vertical) {
                        object.setBlock(new Vec3D((img.getWidth() - x) - 1, (img.getHeight() - y) - 1, 0), new BaseBlock(allBlocks[id].getBlock(), allBlocks[id].getMeta()));
                    } else {
                        object.setBlock(new Vec3D(x, 0, y), new BaseBlock(allBlocks[id].getBlock(), allBlocks[id].getMeta()));
                    }
                }
            }
            System.out.println("saving schematic");
            if (litematic) {
                LibschemAPI.saveLitematicaSchematic(createCanonicalFile(args[0] + ".litematic"), object);
            } else {
                LibschemAPI.saveSchematic(createCanonicalFile(args[0] + ".schematic"), object);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static File createCanonicalFile(String path) {
        File f = new File(path);
        try {
            return f.getCanonicalFile();
        } catch (IOException e) {
            return f;
        }
    }

    public static void initStainedGlass() {
        allBlocks = new BlockColor[16];
        allBlocks[0] = new BlockColor(BlockID.STAINED_GLASS_PANE, 202, 208, 209);
        allBlocks[1] = new BlockColor(BlockID.STAINED_GLASS_PANE, 1, 221, 96, 2);
        allBlocks[2] = new BlockColor(BlockID.STAINED_GLASS_PANE, 2, 167, 49, 157);
        allBlocks[3] = new BlockColor(BlockID.STAINED_GLASS_PANE, 3, 35, 135, 196);
        allBlocks[4] = new BlockColor(BlockID.STAINED_GLASS_PANE, 4, 237, 174, 23);
        allBlocks[5] = new BlockColor(BlockID.STAINED_GLASS_PANE, 5, 91, 165, 23);
        allBlocks[6] = new BlockColor(BlockID.STAINED_GLASS_PANE, 6, 210, 99, 240);
        allBlocks[7] = new BlockColor(BlockID.STAINED_GLASS_PANE, 7, 53, 56, 60);
        allBlocks[8] = new BlockColor(BlockID.STAINED_GLASS_PANE, 8, 122, 122, 112);
        allBlocks[9] = new BlockColor(BlockID.STAINED_GLASS_PANE, 9, 21, 117, 133);
        allBlocks[10] = new BlockColor(BlockID.STAINED_GLASS_PANE, 10, 99, 31, 154);
        allBlocks[11] = new BlockColor(BlockID.STAINED_GLASS_PANE, 11, 43, 45, 139);
        allBlocks[12] = new BlockColor(BlockID.STAINED_GLASS_PANE, 12, 95, 59, 31);
        allBlocks[13] = new BlockColor(BlockID.STAINED_GLASS_PANE, 13, 71, 88, 35);
        allBlocks[14] = new BlockColor(BlockID.STAINED_GLASS_PANE, 14, 137, 31, 31);
        allBlocks[15] = new BlockColor(BlockID.STAINED_GLASS_PANE, 15, 8, 10, 15);
    }

    public static void initClayOnly() {
        allBlocks = new BlockColor[17];
        allBlocks[0] = new BlockColor(BlockID.STAINED_CLAY, 195, 167, 152);
        allBlocks[1] = new BlockColor(BlockID.STAINED_CLAY, 1, 151, 79, 76);
        allBlocks[2] = new BlockColor(BlockID.STAINED_CLAY, 2, 140, 82, 103);
        allBlocks[3] = new BlockColor(BlockID.STAINED_CLAY, 3, 106, 102, 129);
        allBlocks[4] = new BlockColor(BlockID.STAINED_CLAY, 4, 174, 124, 34);
        allBlocks[5] = new BlockColor(BlockID.STAINED_CLAY, 5, 96, 110, 51);
        allBlocks[6] = new BlockColor(BlockID.STAINED_CLAY, 6, 151, 74, 74);
        allBlocks[7] = new BlockColor(BlockID.STAINED_CLAY, 7, 53, 39, 34);
        allBlocks[8] = new BlockColor(BlockID.STAINED_CLAY, 8, 126, 100, 92);
        allBlocks[9] = new BlockColor(BlockID.STAINED_CLAY, 9, 81, 85, 86);
        allBlocks[10] = new BlockColor(BlockID.STAINED_CLAY, 10, 110, 66, 81);
        allBlocks[11] = new BlockColor(BlockID.STAINED_CLAY, 11, 69, 56, 86);
        allBlocks[12] = new BlockColor(BlockID.STAINED_CLAY, 12, 72, 48, 34);
        allBlocks[13] = new BlockColor(BlockID.STAINED_CLAY, 13, 71, 78, 40);
        allBlocks[14] = new BlockColor(BlockID.STAINED_CLAY, 14, 134, 57, 44);
        allBlocks[15] = new BlockColor(BlockID.STAINED_CLAY, 15, 35, 22, 16);
        allBlocks[16] = new BlockColor(172, 140, 87, 64);
    }

    static {
        //default block info
        allBlocks[0] = new BlockColor(BlockID.STAINED_CLAY, 195, 167, 152);
        allBlocks[1] = new BlockColor(BlockID.STAINED_CLAY, 1, 151, 79, 76);
        allBlocks[2] = new BlockColor(BlockID.STAINED_CLAY, 2, 140, 82, 103);
        allBlocks[3] = new BlockColor(BlockID.STAINED_CLAY, 3, 106, 102, 129);
        allBlocks[4] = new BlockColor(BlockID.STAINED_CLAY, 4, 174, 124, 34);
        allBlocks[5] = new BlockColor(BlockID.STAINED_CLAY, 5, 96, 110, 51);
        allBlocks[6] = new BlockColor(BlockID.STAINED_CLAY, 6, 151, 74, 74);
        allBlocks[7] = new BlockColor(BlockID.STAINED_CLAY, 7, 53, 39, 34);
        allBlocks[8] = new BlockColor(BlockID.STAINED_CLAY, 8, 126, 100, 92);
        allBlocks[9] = new BlockColor(BlockID.STAINED_CLAY, 9, 81, 85, 86);
        allBlocks[10] = new BlockColor(BlockID.STAINED_CLAY, 10, 110, 66, 81);
        allBlocks[11] = new BlockColor(BlockID.STAINED_CLAY, 11, 69, 56, 86);
        allBlocks[12] = new BlockColor(BlockID.STAINED_CLAY, 12, 72, 48, 34);
        allBlocks[13] = new BlockColor(BlockID.STAINED_CLAY, 13, 71, 78, 40);
        allBlocks[14] = new BlockColor(BlockID.STAINED_CLAY, 14, 134, 57, 44);
        allBlocks[15] = new BlockColor(BlockID.STAINED_CLAY, 15, 35, 22, 16);
        allBlocks[16] = new BlockColor(172, 140, 87, 64);

        allBlocks[17] = new BlockColor(BlockID.CLOTH, 243, 244, 244);
        allBlocks[18] = new BlockColor(BlockID.CLOTH, 1, 244, 145, 28);
        allBlocks[19] = new BlockColor(BlockID.CLOTH, 2, 195, 76, 185);
        allBlocks[20] = new BlockColor(BlockID.CLOTH, 3, 70, 188, 223);
        allBlocks[21] = new BlockColor(BlockID.CLOTH, 4, 248, 206, 50);
        allBlocks[22] = new BlockColor(BlockID.CLOTH, 5, 120, 190, 25);
        allBlocks[23] = new BlockColor(BlockID.CLOTH, 6, 239, 159, 184);
        allBlocks[24] = new BlockColor(BlockID.CLOTH, 7, 67, 74, 76);
        allBlocks[25] = new BlockColor(BlockID.CLOTH, 8, 149, 149, 142);
        allBlocks[26] = new BlockColor(BlockID.CLOTH, 9, 22, 145, 148);
        allBlocks[27] = new BlockColor(BlockID.CLOTH, 10, 129, 46, 175);
        allBlocks[28] = new BlockColor(BlockID.CLOTH, 11, 57, 62, 162);
        allBlocks[29] = new BlockColor(BlockID.CLOTH, 12, 122, 77, 45);
        allBlocks[30] = new BlockColor(BlockID.CLOTH, 13, 89, 177, 23);
        allBlocks[31] = new BlockColor(BlockID.CLOTH, 14, 168, 42, 35);
        allBlocks[32] = new BlockColor(BlockID.CLOTH, 15, 26, 26, 30);

        allBlocks[33] = new BlockColor(BlockID.CONCRETE, 202, 208, 209);
        allBlocks[34] = new BlockColor(BlockID.CONCRETE, 1, 221, 96, 2);
        allBlocks[35] = new BlockColor(BlockID.CONCRETE, 2, 167, 49, 157);
        allBlocks[36] = new BlockColor(BlockID.CONCRETE, 3, 35, 135, 196);
        allBlocks[37] = new BlockColor(BlockID.CONCRETE, 4, 237, 174, 23);
        allBlocks[38] = new BlockColor(BlockID.CONCRETE, 5, 91, 165, 23);
        allBlocks[39] = new BlockColor(BlockID.CONCRETE, 6, 210, 99, 240);
        allBlocks[40] = new BlockColor(BlockID.CONCRETE, 7, 53, 56, 60);
        allBlocks[41] = new BlockColor(BlockID.CONCRETE, 8, 122, 122, 112);
        allBlocks[42] = new BlockColor(BlockID.CONCRETE, 9, 21, 117, 133);
        allBlocks[43] = new BlockColor(BlockID.CONCRETE, 10, 99, 31, 154);
        allBlocks[44] = new BlockColor(BlockID.CONCRETE, 11, 43, 45, 139);
        allBlocks[45] = new BlockColor(BlockID.CONCRETE, 12, 95, 59, 31);
        allBlocks[46] = new BlockColor(BlockID.CONCRETE, 13, 71, 88, 35);
        allBlocks[47] = new BlockColor(BlockID.CONCRETE, 14, 137, 31, 31);
        allBlocks[48] = new BlockColor(BlockID.CONCRETE, 15, 8, 10, 15);
    }
}
