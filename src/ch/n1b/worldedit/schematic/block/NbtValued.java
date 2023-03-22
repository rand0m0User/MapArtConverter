// $Id$
/*
 * This file is a part of WorldEdit.
 * Copyright (c) sk89q <http://www.sk89q.com>
 * Copyright (c) the WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
*/

package ch.n1b.worldedit.schematic.block;

import ch.n1b.worldedit.jnbt.CompoundTag;
import ch.n1b.worldedit.schematic.data.DataException;


/**
 * Indicates an object that contains extra data identified as an NBT structure. This
 * interface is used when saving and loading objects to a serialized format, but may
 * be used in other cases.
 */
public interface NbtValued {
    
    /**
     * Returns whether the block contains NBT data. {@link #getNbtData()} must not return
     * null if this method returns true.
     * 
     * @return true if there is NBT data
     */
    public boolean hasNbtData();

    /**
     * Get the object's NBT data (tile entity data). The returned tag, if modified
     * in any way, should be sent to {@link #setNbtData(CompoundTag)} so that
     * the instance knows of the changes. Making changes without calling
     * {@link #setNbtData(CompoundTag)} could have unintended consequences.
     * </p>
     * {@link #hasNbtData()} must return true if and only if method does not return null.
     * 
     * @return compound tag, or null
     */
    CompoundTag getNbtData();

    /**
     * Set the object's NBT data (tile entity data).
     * 
     * @param nbtData NBT data, or null if no data
     * @throws DataException if possibly the data is invalid
     */
    void setNbtData(CompoundTag nbtData) throws DataException;

}
