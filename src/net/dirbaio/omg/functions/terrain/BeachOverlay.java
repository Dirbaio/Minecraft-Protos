package net.dirbaio.omg.functions.terrain;

import net.dirbaio.omg.*;
import net.dirbaio.omg.functions.FunctionTerrain;

public class BeachOverlay extends FunctionTerrain
{
	FunctionTerrain base;
	short overlayblock;
	short surfaceblock; //0 means any
	int depth;
	int minheight, maxheight;

    public BeachOverlay()
    {
    }
	
	public BeachOverlay(FunctionTerrain base, short overlayblock, short surfaceblock, int depth, int minheight, int maxheight)
	{
		this.base = base;
		this.overlayblock = overlayblock;
		this.surfaceblock = surfaceblock;
		this.depth = depth;
		this.minheight = minheight;
		this.maxheight = maxheight;
	}
	
	
	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] res = base.getBlockData(px, pz, sx, sz);

		for(int x = 0; x < sx; x++)
			for(int z = 0; z < sz; z++)
			{
				int y;
				int d = depth;
				boolean paint = false;
				for(y = Chunk.MAP_HEIGHT-1; y >= 0; y--)
				{
					int i = z + sz*(x + sx*y);
					if(res[i] == 0)
					{
						d = depth;
						paint = y <= maxheight && y >= minheight;
					}
					else
					{
						if(d > 0 && (surfaceblock == 0 || res[i] == surfaceblock) && paint)
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
