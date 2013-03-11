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
	public short block;

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
		double[][][] data = field.get3DData(px, 0, pz, sx, Chunk.MAP_HEIGHT, sz);
		
		int i = 0;
		for(int y = 0; y < Chunk.MAP_HEIGHT-1; y++)
			for(int x = 0; x < sx; x++)
				for(int z = 0; z < sz; z++)
				{
					double h = data[x][y][z];
					if(h > 0)
						res[i] = block;
					
					i++;
				}

		return res;
	}

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		field.prepare(x, z, sx, sz);
	}
}
