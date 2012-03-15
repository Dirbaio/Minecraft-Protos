package net.dirbaio.omg.functions.terrain;

import net.dirbaio.omg.functions.FunctionTerrain;

public class SeaFunction extends FunctionTerrain
{
	short block;
	int height;
	FunctionTerrain f;

	public SeaFunction(FunctionTerrain f, short block, int height)
	{
		this.f = f;
		this.block = block;
		this.height = height;
	}

	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] r = f.getBlockData(px, pz, sx, sz);
		int m = sx*sz*height;
		for(int i = 0; i < m; i++)
			if(r[i] == (short)0)
				r[i] = block;
		return r;
	}

	
}
