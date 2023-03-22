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
package ch.n1b.worldedit.jnbt;

import static ch.n1b.worldedit.jnbt.Preconditions.checkNotNull;

/**
 * The {@code TAG_Long_Array} tag.
 */
public final class LongArrayTag extends Tag {

    private final long[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public LongArrayTag(long[] value) {
        super();
        checkNotNull(value);
        this.value = value;
    }

    @Override
    public long[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder longs = new StringBuilder();
        for (long v : value) {
            longs.append(v).append(" ");
        }
        return "TAG_Long_Array(" + longs + ")";
    }

}
