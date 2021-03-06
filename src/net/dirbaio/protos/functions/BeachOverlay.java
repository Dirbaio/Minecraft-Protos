/*
 * Copyright (C) 2013 dirbaio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.dirbaio.protos.functions;

import net.dirbaio.protos.Chunk;

public class BeachOverlay extends FunctionTerrain
{

    public FunctionTerrain base;
    public short overlayBlock = 12;
    public short surfaceBlock; //0 means any
    public int depth = 3;
    public int minHeight = 62, maxHeight = 66;

    public BeachOverlay()
    {
    }

    public BeachOverlay(FunctionTerrain base, short overlayblock, short surfaceblock, int depth, int minheight, int maxheight)
    {
        this.base = base;
        this.overlayBlock = overlayblock;
        this.surfaceBlock = surfaceblock;
        this.depth = depth;
        this.minHeight = minheight;
        this.maxHeight = maxheight;
    }

    @Override
    public short[] getTerrainData(int px, int pz, int sx, int sz)
    {
        short[] res = base.getTerrainData(px, pz, sx, sz);

        for (int x = 0; x < sx; x++)
            for (int z = 0; z < sz; z++)
            {
                int y;
                int d = depth;
                boolean paint = false;
                for (y = Chunk.MAP_HEIGHT - 1; y >= 0; y--)
                {
                    int i = z + sz * (x + sx * y);
                    if (res[i] == 0)
                    {
                        d = depth;
                        paint = y <= maxHeight && y >= minHeight;
                    }
                    else
                    {
                        if (d > 0 && (surfaceBlock == 0 || res[i] == surfaceBlock) && paint)
                            res[i] = overlayBlock;
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
