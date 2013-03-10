package net.dirbaio.protos.functions;

import net.dirbaio.protos.Chunk;
import net.dirbaio.protos.functions.Function2D;
import net.dirbaio.protos.functions.FunctionTerrain;

public class TerrainHeightmap extends FunctionTerrain
{
	public Function2D heightmap;
	short block;

	public TerrainHeightmap()
	{
	}

	public TerrainHeightmap(Function2D heightmap, short block)
	{
		this.heightmap = heightmap;
		this.block = block;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
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
	

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		heightmap.prepare(x, z, sx, sz);
	}
}
