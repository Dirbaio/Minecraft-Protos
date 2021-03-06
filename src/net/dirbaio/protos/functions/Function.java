/*
 * Copyright (C) 2013 dirbaio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.dirbaio.protos.functions;

import java.util.Random;

public abstract class Function
{
    public int xPos, yPos;
    public long randomSeedOffset;

    public Function()
    {
        Random r = new Random();
        randomSeedOffset = r.nextLong();
    }
    
	public void prepare(int x, int z, int sx, int sz) {}
    
    protected void setRandomSeed(long seed) {}
    
    public final void doSetRandomSeed(long seed)
    {
        setRandomSeed(seed+randomSeedOffset);
    }
}
