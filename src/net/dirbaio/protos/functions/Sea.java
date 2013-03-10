package net.dirbaio.protos.functions;

import net.dirbaio.protos.functions.FunctionTerrain;

public class Sea extends FunctionTerrain
{
	short block;
	int height;
	FunctionTerrain base;

    public Sea()
    {
    }

	public Sea(FunctionTerrain base, short block, int height)
	{
		this.base = base;
		this.block = block;
		this.height = height;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] r = base.getTerrainData(px, pz, sx, sz);
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
