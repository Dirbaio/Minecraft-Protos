package net.dirbaio.omg.functions.terrain;

import net.dirbaio.omg.functions.*;

public class BedrockLayer extends FunctionTerrain
{
	FunctionTerrain base;

	public BedrockLayer(FunctionTerrain base)
	{
		this.base = base;
	}

	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] res = base.getBlockData(px, pz, sx, sz);
		for(int i = 0; i < 256; i++)
			res[i] = (short)7;
		return res;
	}

	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		base.prepare(x, z, sx, sz);
	}
	
	
}
