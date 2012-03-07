package net.dirbaio.omg.functions.terrain;

import net.dirbaio.omg.functions.*;

public class TerrainJoin extends FunctionTerrain
{
	FunctionTerrain a, b;
	boolean cut;
	
	public TerrainJoin()
	{
	}

	public TerrainJoin(FunctionTerrain a, FunctionTerrain b)
	{
		this.a = a;
		this.b = b;
	}

	public TerrainJoin(FunctionTerrain a, FunctionTerrain b, boolean cut)
	{
		this.a = a;
		this.b = b;
		this.cut = cut;
	}
	
	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] da = a.getBlockData(px, pz, sx, sz);
		short[] db = b.getBlockData(px, pz, sx, sz);
		
		int len = da.length;
		
		for(int i = 0; i < len; i++)
			if(db[i] != 0)
				da[i] = cut?0:db[i];

		return da;
	}

}
