package net.dirbaio.protos.functions;

import net.dirbaio.protos.Chunk;

public class TerrainOverlay extends FunctionTerrain
{
	FunctionTerrain base;
	short overlayblock;
	short surfaceblock; //0 means any
	int depth;

    public TerrainOverlay()
    {
    }

    
	public TerrainOverlay(FunctionTerrain base, short overlayblock, short surfaceblock, int depth)
	{
		this.base = base;
		this.overlayblock = overlayblock;
		this.surfaceblock = surfaceblock;
		this.depth = depth;
	}
	
	
	@Override
	public short[] getTerrainData(int px, int pz, int sx, int sz)
	{
		short[] res = base.getTerrainData(px, pz, sx, sz);

		for(int x = 0; x < sx; x++)
			for(int z = 0; z < sz; z++)
			{
				int y;
				int d = depth;

				for(y = Chunk.MAP_HEIGHT-1; y >= 0; y--)
				{
					int i = z + sz*(x + sx*y);
					if(res[i] == 0)
						d = depth;
					else
					{
						if(d > 0 && (surfaceblock == 0 || res[i] == surfaceblock))
							res[i] = overlayblock;
						d--;
					}
				}
			}
		return res;
	}
	
	@Override
	public void prepare(int x, int z, int sx, int sz)
	{
		base.prepare(x, z, sx, sz);
	}
}
