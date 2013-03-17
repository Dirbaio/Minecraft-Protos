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

import net.dirbaio.protos.Chunk;

public class TerrainVolume extends FunctionTerrain
{

	public Function3D field;
	public short block = 1;

	public TerrainVolume()
	{
	}

	public TerrainVolume(Function3D field, short block)
	{
		this.field = field;
		this.block = block;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] res = new short[sx*sz*Chunk.MAP_HEIGHT];
		double[] data = field.get3DData(px, 0, pz, sx, Chunk.MAP_HEIGHT, sz);
		
        int s = sx*Chunk.MAP_HEIGHT*sz;
        for(int i = 0; i < s; i++)
            res[i] = data[i]>0?block:0;

		return res;
	}

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		field.prepare(x, z, sx, sz);
	}
}
