package net.dirbaio.omg.functions;

import net.dirbaio.omg.*;

public class TerrainOverlay extends FunctionTerrain
{
	FunctionTerrain base;
	short overlayblock;
	short surfaceblock; //0 means any
	int depth;

	public TerrainOverlay(FunctionTerrain base, short overlayblock, short surfaceblock, int depth)
	{
		this.base = base;
		this.overlayblock = overlayblock;
		this.surfaceblock = surfaceblock;
		this.depth = depth;
	}
	
	
	@Override
	public short[] getBlockData(int px, int pz, int sx, int sz)
	{
		short[] res = base.getBlockData(px, pz, sx, sz);

		for(int x = 0; x < sx; x++)
			for(int z = 0; z < sz; z++)
			{
				int y;
				//Find top-most non-air block.
				for(y = Chunk.MAP_HEIGHT-1; y >= 0; y--)
				{
					int i = z + sz*(x + sx*y);
					if(res[i] != 0) break;
				}
				
				//No blocks?
				if(y < 0) continue;
				
				//Correct surface block?
				if(surfaceblock != 0 && res[z + sz*(x + sx*y)] != surfaceblock) continue;
				
				//Put overlay
				for(int d = 0; d < depth; d++)
				{
					if(y-d < 0) break;
					res[z + sz*(x + sx*(y-d))] = overlayblock;
				}
			}
		return res;
	}
	
}
