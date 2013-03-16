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

public class ConstantTerrain extends FunctionTerrain
{

	public short block = 1;

	public ConstantTerrain()
	{
	}

	public ConstantTerrain(short block)
	{
		this.block = block;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] res = new short[sx*sz*Chunk.MAP_HEIGHT];
		
		int i = 0;
		for(int y = 0; y < Chunk.MAP_HEIGHT-1; y++)
			for(int x = 0; x < sx; x++)
				for(int z = 0; z < sz; z++)
                    res[i++] = block;

		return res;
	}
}
