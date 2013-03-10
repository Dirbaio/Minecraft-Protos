package net.dirbaio.protos.functions;

import net.dirbaio.protos.Chunk;
import net.dirbaio.protos.functions.Function3D;
import net.dirbaio.protos.functions.FunctionTerrain;

public class ConstantTerrain extends FunctionTerrain
{

	short block;

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
