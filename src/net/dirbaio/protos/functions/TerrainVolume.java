package net.dirbaio.protos.functions;

import net.dirbaio.protos.Chunk;
import net.dirbaio.protos.functions.Function3D;
import net.dirbaio.protos.functions.FunctionTerrain;

public class TerrainVolume extends FunctionTerrain
{

	public Function3D field;
	short block;

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
