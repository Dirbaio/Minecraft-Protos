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

package net.dirbaio.protos.generator;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import net.dirbaio.protos.Chunk;

public class DaniChunkOutput implements ChunkOutput
{
    byte[] data;
    int xSize, zSize, xMin, zMin;
    String path;

    public DaniChunkOutput(String path)
    {
        this.path = path;
    }
    
    @Override
    public void chunkDone(Chunk c)
    {
        int i = 0;
        for(int y = 0; y < Chunk.MAP_HEIGHT; y++)
			for(int x = 0; x < 16; x++)
				for(int z = 0; z < 16; z++)
				{
					byte b = (byte) c.blocks[i++];
                    data[y*(xSize*zSize*256) + (x+(c.xPos-xMin)*16)*(zSize*16) + (z+(c.zPos-zMin)*16)] = b;
				}
    }

    @Override
    public void generationStarted(int xMin, int zMin, int xSize, int zSize)
    {
        this.xMin = xMin;
        this.xSize = xSize;
        this.zMin = zMin;
        this.zSize = zSize;
        data = new byte[xSize*zSize*256*Chunk.MAP_HEIGHT];
    }

    @Override
    public void generationFinished()
    {
        try
        {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(path));
            out.writeInt(xSize*16);
            out.writeInt(Chunk.MAP_HEIGHT);
            out.writeInt(zSize*16);
            out.write(data);
            out.close();
        } catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
}
