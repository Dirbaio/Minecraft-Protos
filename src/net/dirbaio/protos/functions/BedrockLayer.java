package net.dirbaio.protos.functions;

public class BedrockLayer extends FunctionTerrain
{
	public FunctionTerrain base;

    public BedrockLayer()
    {
    }

	public BedrockLayer(FunctionTerrain base)
	{
		this.base = base;
	}

	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] res = base.getTerrainData(px, pz, sx, sz);
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
