/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ch.n1b.worldedit.schematic.schematic;

import ch.n1b.vector.Vec3D;
import ch.n1b.worldedit.jnbt.ByteArrayTag;
import ch.n1b.worldedit.jnbt.CompoundTag;
import ch.n1b.worldedit.jnbt.IntTag;
import ch.n1b.worldedit.jnbt.ListTag;
import ch.n1b.worldedit.jnbt.LongArrayTag;
import ch.n1b.worldedit.jnbt.LongTag;
import ch.n1b.worldedit.jnbt.NBTConstants;
import ch.n1b.worldedit.jnbt.NBTInputStream;
import ch.n1b.worldedit.jnbt.NBTOutputStream;
import ch.n1b.worldedit.jnbt.NamedTag;
import ch.n1b.worldedit.jnbt.ShortTag;
import ch.n1b.worldedit.jnbt.StringTag;
import ch.n1b.worldedit.jnbt.Tag;
import ch.n1b.worldedit.schematic.block.BaseBlock;
import ch.n1b.worldedit.schematic.block.Block;
import ch.n1b.worldedit.schematic.data.DataException;
import ch.n1b.worldedit.schematic.vector.BlockVector;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

public class litematicaSchematicFormat extends SchematicFormat {

    private static BaseBlock[] Palette = new BaseBlock[0];
    private static String[] Palettenames = new String[0];

    private static final int MAX_SIZE = Short.MAX_VALUE - Short.MIN_VALUE;

    protected litematicaSchematicFormat() {
        super("litematica", "litematic", "lit");
    }

    public Cuboid load(InputStream stream) throws IOException, DataException {
        //TODO
        return null;
    }

    @Override
    public Cuboid load(File file) throws IOException, DataException {
        return load(new FileInputStream(file));
    }

    @Override
    public void save(Cuboid clipboard, File file) throws IOException, DataException {
        int width = clipboard.getWidth();
        int height = clipboard.getHeight();
        int length = clipboard.getLength();
        int tv = width * height * length;

        if (width > MAX_SIZE) {
            throw new DataException("Width of region too large for a .schematic");
        }
        if (height > MAX_SIZE) {
            throw new DataException("Height of region too large for a .schematic");
        }
        if (length > MAX_SIZE) {
            throw new DataException("Length of region too large for a .schematic");
        }

        int tb = 0;

        //SAVE BEGIN!
        HashMap<String, Tag> schematic = new HashMap<>();
        HashMap<String, Tag> Regions = new HashMap<>();
        HashMap<String, Tag> region = new HashMap<>();
        HashMap<String, Tag> pos = new HashMap<>();
        HashMap<String, Tag> Metadata = new HashMap<>();
        HashMap<String, Tag> s = new HashMap<>();

        ArrayList plt = new ArrayList(); //Palette data object
        HashMap<String, Tag> air = new HashMap<>();
        air.put("Name", new StringTag(Palettenames[0])); // add air as the first element
        plt.add(new CompoundTag(air));
        HashMap<Integer, String> plts = new HashMap<>(); //keep track of blocks we allready added to the Palette
        HashMap<String, Integer> plti = new HashMap<>();
        int ctr = 0;
        for (int x = 0; x < width; ++x) { //compute the palete, so we can properly compute nbits
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    BaseBlock block = clipboard.data[x][y][z];
                    for (int i = 0; i < Palette.length; ++i) {
                        if (clipboard.data[x][y][z].isAir()) {
                            continue;
                        }
                        if ((block.getId() == Palette[i].getId()) && (block.getData() == Palette[i].getData())) {
                            tb++;
                            if (!plts.containsValue(Palettenames[i])) {
                                plti.put(Palettenames[i], ctr);
                                plts.put(ctr, Palettenames[i]);
                                ctr++;
                                HashMap<String, Tag> newentry = new HashMap<>();
                                newentry.put("Name", new StringTag(Palettenames[i]));
                                plt.add(new CompoundTag(newentry));
                            }
                            break;
                        }
                    }
                }
            }
        }
        int nbits = Math.max(2, Integer.SIZE - Integer.numberOfLeadingZeros(plt.size() - 1));
        long maxEntryValue = (1L << nbits) - 1L;
        long[] data = new long[(int) Math.ceil(nbits * tv / 64) + 1];
        ArrayList<Tag> tileEntities = new ArrayList<>();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    BaseBlock block = clipboard.data[x][y][z];
                    int value = 0;
                    if (block.isAir()) {
                        value = 0; // null is air
                    } else {
                        for (int i = 0; i < Palette.length; ++i) {
                            if ((block.getId() == Palette[i].getId()) && (block.getData() == Palette[i].getData())) {
                                value = plti.get(Palettenames[i]) + 1;
                                break;
                            }
                        }
                    }
                    //coppypaste mistery code directly from litematica
                    long index = ((long) y * ((long) width * (long) length)) + (long) z * (long) width + (long) x;
                    long startOffset = index * (long) nbits;
                    int startArrIndex = (int) (startOffset >> 6); // startOffset / 64
                    int endArrIndex = (int) (((index + 1L) * (long) nbits - 1L) >> 6);
                    int startBitOffset = (int) (startOffset & 0x3F); // startOffset % 64
                    data[startArrIndex] = data[startArrIndex] & ~(maxEntryValue << startBitOffset) | ((long) value & maxEntryValue) << startBitOffset;
                    if (startArrIndex != endArrIndex) {
                        int endOffset = 64 - startBitOffset;
                        int j1 = nbits - endOffset;
                        data[endArrIndex] = data[endArrIndex] >>> j1 << j1 | ((long) value & maxEntryValue) >> endOffset;
                    }
                    //end coppypaste mistery code

                    // Get the list of key/values from the block
                    CompoundTag rawTag = block.getNbtData();
                    if (rawTag != null) {
                        Map<String, Tag> values = new HashMap<>();
                        for (Entry<String, Tag> entry : rawTag.getValue().entrySet()) {
                            values.put(entry.getKey(), entry.getValue());
                        }

                        values.put("id", new StringTag(block.getNbtId()));
                        values.put("x", new IntTag(x));
                        values.put("y", new IntTag(y));
                        values.put("z", new IntTag(z));

                        CompoundTag tileEntityTag = new CompoundTag(values);
                        tileEntities.add(tileEntityTag);
                    }
                }
            }
        }
        // Build and output
        region.put("BlockStates", new LongArrayTag(data));
        region.put("BlockStatePalette", new ListTag(CompoundTag.class, plt));
        region.put("Entities", new ListTag(CompoundTag.class, new ArrayList<Tag>()));
        region.put("TileEntities", new ListTag(CompoundTag.class, tileEntities));
        region.put("PendingBlockTicks", new ListTag(CompoundTag.class, new ArrayList<Tag>()));
        region.put("PendingFluidTicks", new ListTag(CompoundTag.class, new ArrayList<Tag>()));
        pos.put("x", new IntTag(0));
        pos.put("y", new IntTag(0));
        pos.put("z", new IntTag(0));
        region.put("Position", new CompoundTag(pos));
        s.put("x", new IntTag(clipboard.getWidth()));
        s.put("y", new IntTag(clipboard.getHeight()));
        s.put("z", new IntTag(clipboard.getLength()));
        region.put("Size", new CompoundTag(s));
        Long t = System.currentTimeMillis(); //TODO: get proper time?
        Metadata.put("TotalBlocks", new IntTag(tb));
        Metadata.put("TotalVolume", new IntTag(tv));
        Metadata.put("EnclosingSize", new CompoundTag(s));
        Metadata.put("Author", new StringTag("libschem"));
        Metadata.put("Description", new StringTag(""));
        Metadata.put("Name", new StringTag(file.getName()));
        Metadata.put("TimeCreated", new LongTag(t));
        Metadata.put("TimeModified", new LongTag(t));
        //main file:
        schematic.put("Version", new IntTag(4));
        schematic.put("MinecraftDataVersion", new IntTag(1631));
        schematic.put("Metadata", new CompoundTag(Metadata));
        Regions.put("Master", new CompoundTag(region));
        schematic.put("Regions", new CompoundTag(Regions));

        CompoundTag schematicTag = new CompoundTag(schematic);
        try (NBTOutputStream stream = new NBTOutputStream(
                new GZIPOutputStream(
                        new FileOutputStream(
                                file.getCanonicalFile()
                        )
                )
        )) {
            stream.writeNamedTag("", schematicTag);
        }
    }

    @Override
    public boolean isOfFormat(File file) {
        //TODO
        return false;
    }

//    /**
//     * Get child tag of a NBT structure.
//     *
//     * @param items The parent tag map
//     * @param key The name of the tag to get
//     * @param expected The expected type of the tag
//     * @return child tag casted to the expected type
//     * @throws DataException if the tag does not exist or the tag is not of the
//     * expected type
//     */
//    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key,
//            Class<T> expected) throws DataException {
//
//        if (!items.containsKey(key)) {
//            throw new DataException("Schematic file is missing a \"" + key + "\" tag");
//        }
//        Tag tag = items.get(key);
//        if (!expected.isInstance(tag)) {
//            throw new DataException(
//                    key + " tag is not of tag type " + expected.getName());
//        }
//        return expected.cast(tag);
//    }
    static { //trolololol parsing JSON without a lib
        try {
            String mc = "minecraft:";
            InputStream str = litematicaSchematicFormat.class.getResourceAsStream("block_ids.json.z");
            byte[] compressed = new byte[str.available()];
            str.read(compressed);
            InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(compressed));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[5];
            int rlen;
            while ((rlen = iis.read(buf)) != -1) {
                baos.write(Arrays.copyOf(buf, rlen));
            }
            String[] fString = new String(baos.toByteArray()).split("\n");
            for (int i = 0; i < fString.length; i++) {
                if (fString[i].contains("\": {")) {
                    String[] newnames = new String[Palettenames.length + 1];
                    BaseBlock[] newPalette = new BaseBlock[Palette.length + 1];
                    System.arraycopy(Palettenames, 0, newnames, 0, Palettenames.length);
                    System.arraycopy(Palette, 0, newPalette, 0, Palette.length);
                    newnames[Palettenames.length] = mc + fString[i].split("\"")[1];
                    newPalette[Palette.length] = new BaseBlock(
                            Short.parseShort(fString[i + 1].split(": ")[1].replace(",", "")),
                            Short.parseShort(fString[i + 2].split(": ")[1].replace(",", ""))
                    );
                    Palettenames = newnames;
                    Palette = newPalette;
                    i += 4;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
