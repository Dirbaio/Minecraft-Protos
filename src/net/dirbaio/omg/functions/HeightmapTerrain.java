package net.dirbaio.omg.functions;

import net.dirbaio.omg.*;

public class HeightmapTerrain extends FunctionTerrain
{
	public Function2D heightmap;
	short block;

	public HeightmapTerrain()
	{
	}

	public HeightmapTerrain(Function2D heightmap, short block)
	{
		this.heightmap = heightmap;
		this.block = block;
	}

	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] res = new short[sx*sz*Chunk.MAP_HEIGHT];
		double[][] height = heightmap.get2DData(px, pz, sx, sz);
		
		for(int x = 0; x < sx; x++)
			for(int z = 0; z < sz; z++)
			{
				double h = height[x][z];
				int i = z + x*sz;
				for(int y = 0; y < h && y < Chunk.MAP_HEIGHT; y++)
				{
					res[i] = block;
					i += sx*sz;
				}
			}
		
		return res;
	}
	
}
