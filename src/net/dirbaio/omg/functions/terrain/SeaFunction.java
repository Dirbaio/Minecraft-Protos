package net.dirbaio.omg.functions.terrain;

import net.dirbaio.omg.functions.FunctionTerrain;

public class SeaFunction extends FunctionTerrain
{
	short block;
	int height;
	FunctionTerrain base;

    public SeaFunction()
    {
    }

	public SeaFunction(FunctionTerrain base, short block, int height)
	{
		this.base = base;
		this.block = block;
		this.height = height;
	}

	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] r = base.getBlockData(px, pz, sx, sz);
		int m = sx*sz*height;
		for(int i = 0; i < m; i++)
			if(r[i] == (short)0)
				r[i] = block;
		return r;
	}


	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		base.prepare(x, z, sx, sz);
	}
}
