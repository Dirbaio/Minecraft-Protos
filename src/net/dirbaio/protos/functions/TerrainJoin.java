package net.dirbaio.protos.functions;

public class TerrainJoin extends FunctionTerrain
{
	FunctionTerrain a, b;
	
	public TerrainJoin()
	{
	}

	public TerrainJoin(FunctionTerrain a, FunctionTerrain b)
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
			if(da[i] == 0)
				da[i] = db[i];

		return da;
	}


	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		a.prepare(x, z, sx, sz);
		b.prepare(x, z, sx, sz);
	}
}
