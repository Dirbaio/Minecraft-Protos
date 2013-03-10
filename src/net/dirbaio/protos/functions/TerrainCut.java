package net.dirbaio.protos.functions;

public class TerrainCut extends FunctionTerrain
{
	FunctionTerrain a, b;
	public TerrainCut()
	{
	}

	public TerrainCut(FunctionTerrain a, FunctionTerrain b)
	{
		this.a = a;
		this.b = b;
	}

	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] da = a.getBlockData(px, pz, sx, sz);
		short[] db = b.getBlockData(px, pz, sx, sz);
		
		int len = da.length;
		
		for(int i = 0; i < len; i++)
			if(db[i] != 0)
				da[i] = 0;

		return da;
	}


	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
	}
}
