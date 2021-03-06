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

package net.dirbaio.protos;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import net.dirbaio.protos.previewer.ChunkRenderer;

public class Chunk
{

    public static final int MAP_HEIGHT = 256;
    public int[] heightMap;
    public byte[] blockLight, skyLight;
    public short[] blocks;
    public int xPos, zPos;
    public boolean[] opsDone = new boolean[7];
    public ReentrantLock opLock = new ReentrantLock();
    public BufferedImage cachedImg;

    public Chunk(int x, int y)
    {
        this.xPos = x;
        this.zPos = y;
        heightMap = new int[16 * 16];
        blockLight = new byte[16 * 16 * MAP_HEIGHT / 2];
        skyLight = new byte[16 * 16 * MAP_HEIGHT / 2];
        blocks = new short[16 * 16 * MAP_HEIGHT];
    }

    public short getBlock(int x, int y, int z)
    {
        return blocks[z + x * 16 + y * 256];
    }

    public void setBlock(int x, int y, int z, short b)
    {
        blocks[z + x * 16 + y * 256] = b;
    }

    public void setBlockCheck(int x, int y, int z, byte b)
    {
        if (x < 0 || x >= 16)
            return;
        if (z < 0 || z >= 16)
            return;
        if (y < 0 || y >= MAP_HEIGHT)
            return;

        blocks[z + x * 16 + y * 256] = b;
    }

    public byte getBlockLight(int x, int y, int z)
    {
        int i = z + x * 16 + y * 256;
        byte b = blockLight[i / 2];
        if (i % 2 == 1)
            b = (byte) (b >> 4);
        return (byte) (b & 0xF);
    }

    public void setBlockLight(int x, int y, int z, byte d)
    {
        int i = z + x * 16 + y * 256;
        byte b = blockLight[i / 2];
        if (i % 2 == 1) //higher nibble
            blockLight[i / 2] = (byte) (b & 0x0F | d << 4);
        else
            blockLight[i / 2] = (byte) (b & 0xF0 | d);
    }

    public byte getSkyLight(int x, int y, int z)
    {
        int i = x + z * 16 + y * 256;
        byte b = skyLight[i / 2];
        if (i % 2 == 1)
            b = (byte) (b >> 4);
        return (byte) (b & 0xF);
    }

    public void setSkyLight(int x, int y, int z, byte d)
    {
        int i = x + z * 16 + y * 256;
        byte b = skyLight[i / 2];
        if (i % 2 == 1)
            skyLight[i / 2] = (byte) (b & 0x0F | d << 4);
        else
            skyLight[i / 2] = (byte) (b & 0xF0 | d);
    }

    public byte getMaxLight(int x, int y, int z)
    {
        byte a = getSkyLight(x, y, z);
        byte b = getBlockLight(x, y, z);

        return a > b ? a : b;
    }

    public int getHeight(int x, int z)
    {
        return heightMap[x + z * 16];
    }

    public void setHeight(int x, int z, int h)
    {
        heightMap[x + z * 16] = h;
    }

    /*
    public void drawSphere(int xx, int yy, int zz, int size, short b)
    {
        for (int x = -size; x <= size; x++)
        {
            if (x + xx < 0)
                continue;
            if (x + xx >= 16)
                continue;
            for (int y = -size; y <= size; y++)
            {
                if (y + yy < 0)
                    continue;
                if (y + yy >= Chunk.MAP_HEIGHT)
                    continue;
                for (int z = -size; z <= size; z++)
                {
                    if (z + zz < 0)
                        continue;
                    if (z + zz >= 16)
                        continue;
                    if (x * x + y * y + z * z < size * size)
                        if (getBlock(xx + x, yy + y, zz + z) == 0)
                            setBlock(xx + x, yy + y, zz + z, b);
                }
            }
        }
    }*/

    //TODO: Optimize it as the chunk is modified?
    //Nah, not necessary...
    public void recalcHeightMap()
    {
        for (int x = 0; x < 16; x++)
            for (int z = 0; z < 16; z++)
            {
                int y = MAP_HEIGHT - 1;
                while (Blocks.getBlockOpacity(getBlock(x, y, z)) == 0 && y > 0)
                    y--;
                y++;
                setHeight(x, z, y);
            }
    }

    public void writeTo(File baseFolder) throws IOException
    {
        recalcHeightMap();

        CompoundTag tag = new CompoundTag();
        tag.putInt("xPos", xPos);
        tag.putInt("zPos", zPos);
        tag.putLong("LastUpdate", 0);
        tag.putIntArray("HeightMap", heightMap);
        byte[] biomes = new byte[16 * 16];
        for (int i = 0; i < 256; i++)
            biomes[i] = 4;
        tag.putByteArray("Biomes", biomes);
        tag.putBoolean("TerrainPopulated", false);

        ListTag<CompoundTag> sectionTags = new ListTag<CompoundTag>("Sections");
        for (int yBase = 0; yBase < (MAP_HEIGHT / 16); yBase++)
        {
            // find non-air
            boolean allAir = true;
            for (int x = 0; x < 16 && allAir; x++)
                for (int y = 0; y < 16 && allAir; y++)
                    for (int z = 0; z < 16; z++)
                    {
                        int pos = z + x * 16 + (y + yBase * 16) * 256;
                        int block = blocks[pos];
                        if (block != 0)
                        {
                            allAir = false;
                            break;
                        }
                    }

            if (allAir)
                continue;

            // build section
            byte[] blockData = new byte[16 * 16 * 16];
            DataLayer dataValues = new DataLayer(blockData.length, 4);
            byte[] skyLightData = new byte[16 * 16 * 16 / 2];
            byte[] blockLightData = new byte[16 * 16 * 16 / 2];

            for (int x = 0; x < 16; x++)
                for (int y = 0; y < 16; y++)
                    for (int z = 0; z < 16; z++)
                    {
                        int pos = z + x * 16 + (y + yBase * 16) * 256;
                        int block = blocks[pos] & 0xFFF;

                        blockData[(y << 8) | (z << 4) | x] = (byte) (block & 0xff);
                        dataValues.set(x, y, z, blocks[pos] >> 12);
                    }

            System.arraycopy(this.skyLight, yBase * 2048, skyLightData, 0, 2048);
            System.arraycopy(this.blockLight, yBase * 2048, blockLightData, 0, 2048);

            CompoundTag sectionTag = new CompoundTag();

            sectionTag.putByte("Y", (byte) (yBase & 0xff));
            sectionTag.putByteArray("Blocks", blockData);
            sectionTag.putByteArray("Data", dataValues.data);
            sectionTag.putByteArray("SkyLight", skyLightData);
            sectionTag.putByteArray("BlockLight", blockLightData);

            sectionTags.add(sectionTag);
        }
        tag.put("Sections", sectionTags);

        CompoundTag mainTag = new CompoundTag();
        mainTag.put("Level", tag);
        RegionFile region = RegionFileCache.getRegionFile(baseFolder, xPos, zPos);

        DataOutputStream out = region.getChunkDataOutputStream(xPos & 31, zPos & 31);
        NbtIo.write(mainTag, out);
        out.close();

    }

    public void updateMap()
    {
        cachedImg = new BufferedImage(70, 556, BufferedImage.TYPE_INT_ARGB);
        ChunkRenderer.renderChunk(cachedImg, this, 35, 546);
    }
}
