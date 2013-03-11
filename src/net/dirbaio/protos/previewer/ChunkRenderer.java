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

package net.dirbaio.protos.previewer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import net.dirbaio.protos.Blocks;
import net.dirbaio.protos.Chunk;

public class ChunkRenderer
{

    static byte max(byte a, byte b)
    {
        return a > b ? a : b;
    }

    public static long renderChunk(BufferedImage i, Chunk c, int xt, int yt)
    {
        long start = System.nanoTime();

        ArrayList<Block> l = new ArrayList<>();

        int ymax = 254;

        for (int xi = 15; xi >= 0; xi--)
            for (int zi = 15; zi >= 0; zi--)
                for (int yi = 0; yi <= 1; yi++)
                {
                    int x = xi, z = zi, y = ymax + yi;

                    int credit = 255;
                    while (z <= 15 && x <= 15 && y > 0)
                    {
                        // System.out.println(x+" "+y+" "+z);
                        short block = c.blocks[z + (x * 16) + (y * 256)];
                        if (block != 0)
                        {
                            Block nb = new Block();
                            nb.x = x;
                            nb.y = y;
                            nb.z = z;
                            nb.b = block;
                            l.add(nb);

                            credit *= 255 - (Integer) Blocks.blockColors[block][4];
                            credit /= 255;
                            if (credit < 2)
                                break;
                        }

                        x++;
                        y--;
                        z++;
                    }
                }

        for (int xi = 0; xi < 31; xi++)
            for (int yi = ymax - 1; yi >= 0; yi--)
            {
                int x = 0, z = 0, y = yi;
                if (xi < 15) //0--14: X side, 15--30: z side
                    x = 15 - xi;
                else
                    z = 30 - xi;

                int credit = 255;
                while (z <= 15 && x <= 15 && y > 0)
                {
                    // System.out.println(x+" "+y+" "+z);
                    short block = c.blocks[z + (x * 16) + (y * 256)];
                    if (block != 0)
                    {
                        Block nb = new Block();
                        nb.x = x;
                        nb.y = y;
                        nb.z = z;
                        nb.b = block;
                        l.add(nb);

                        credit *= 255 - (Integer) Blocks.blockColors[block][4];
                        credit /= 255;
                        if (credit < 2)
                            break;
                    }

                    x++;
                    y--;
                    z++;
                }
            }

        Collections.sort(l);

        for (Block bl : l)
        {
            int light;
            if (bl.y == 255)
                light = c.getMaxLight(bl.x, bl.y, bl.z) + 5;
            else
                light = c.getMaxLight(bl.x, bl.y + 1, bl.z) + 5;

            if (light < 8)
                light = 8;

            int r = (Integer) Blocks.blockColors[bl.b][1];
            int g = (Integer) Blocks.blockColors[bl.b][2];
            int b = (Integer) Blocks.blockColors[bl.b][3];
            int a = (Integer) Blocks.blockColors[bl.b][4];

            short sblock = bl.b;
            if (sblock == 2)
                sblock = 3;
            int sr = (Integer) Blocks.blockColors[sblock][1];
            int sg = (Integer) Blocks.blockColors[sblock][2];
            int sb = (Integer) Blocks.blockColors[sblock][3];
            int sa = (Integer) Blocks.blockColors[sblock][4];

            int xx = xt - bl.x * 2 + bl.z * 2;
            int yy = yt - bl.x - bl.y * 2 - bl.z;

            try
            {
                i.setRGB(xx - 2, yy, combinecolors(i.getRGB(xx - 2, yy), r, g, b, a, light));
                i.setRGB(xx - 1, yy, combinecolors(i.getRGB(xx - 1, yy), r, g, b, a, light));
                i.setRGB(xx, yy, combinecolors(i.getRGB(xx, yy), r, g, b, a, light));
                i.setRGB(xx + 1, yy, combinecolors(i.getRGB(xx + 1, yy), r, g, b, a, light));

                i.setRGB(xx - 2, yy + 1, combinecolors(i.getRGB(xx - 2, yy + 1), sr, sg, sb, sa, light / 2));
                i.setRGB(xx - 1, yy + 1, combinecolors(i.getRGB(xx - 1, yy + 1), sr, sg, sb, sa, light / 2));
                i.setRGB(xx, yy + 1, combinecolors(i.getRGB(xx, yy + 1), sr, sg, sb, sa, light / 3));
                i.setRGB(xx + 1, yy + 1, combinecolors(i.getRGB(xx + 1, yy + 1), sr, sg, sb, sa, light / 3));

                i.setRGB(xx - 2, yy + 2, combinecolors(i.getRGB(xx - 2, yy + 2), sr, sg, sb, sa, light / 2));
                i.setRGB(xx - 1, yy + 2, combinecolors(i.getRGB(xx - 1, yy + 2), sr, sg, sb, sa, light / 2));
                i.setRGB(xx, yy + 2, combinecolors(i.getRGB(xx, yy + 2), sr, sg, sb, sa, light / 3));
                i.setRGB(xx + 1, yy + 2, combinecolors(i.getRGB(xx + 1, yy + 2), sr, sg, sb, sa, light / 3));
            } catch (RuntimeException ex)
            {
                System.out.println(bl.x + " " + bl.y + " " + bl.z);
                System.out.println(xx + " " + yy);
                throw ex;
            }
        }
        return System.nanoTime() - start;
    }

    public static long renderChunkTop(BufferedImage i, Chunk c, int xt, int yt)
    {
        long start = System.nanoTime();

        ArrayList<Block> l = new ArrayList<>();

        int ymax = 254;

        for (int x = 15; x >= 0; x--)
            for (int z = 15; z >= 0; z--)
            {
                int y = 127;
                int credit = 255;
                while (y > 0)
                {
                    // System.out.println(x+" "+y+" "+z);
                    short block = c.blocks[z + (x * 16) + (y * 256)];
                    if (block != 0)
                    {
                        Block nb = new Block();
                        nb.x = x;
                        nb.y = y;
                        nb.z = z;
                        nb.b = block;
                        l.add(nb);

                        credit *= 255 - (Integer) Blocks.blockColors[block][4];
                        credit /= 255;
                        if (credit < 2)
                            break;
                    }
                    y--;
                }
            }

        Collections.sort(l);

        for (Block bl : l)
        {
            byte light1 = c.blockLight[(bl.y + (bl.z * 128 + (bl.x * 128 * 16))) / 2];
            if (bl.y % 2 != 0)
                light1 = (byte) (light1 >> 4);
            light1 &= 0xF;
            byte light2 = c.skyLight[(bl.y + (bl.z * 128 + (bl.x * 128 * 16))) / 2];
            if (bl.y % 2 != 0)
                light2 = (byte) (light2 >> 4);
            light2 &= 0xF;
            byte light = (byte) (max(light1, light2) + 4);
//            light = 20;
//            if(light1 < light2) light=light2;

            int r = (Integer) Blocks.blockColors[bl.b][1];
            int g = (Integer) Blocks.blockColors[bl.b][2];
            int b = (Integer) Blocks.blockColors[bl.b][3];
            int a = (Integer) Blocks.blockColors[bl.b][4];

            int xx = xt + bl.x;
            int yy = yt + bl.z;

            i.setRGB(xx, yy, combinecolors(i.getRGB(xx, yy), r, g, b, a, light));
        }
        for (int x = 15; x >= 0; x--)
            for (int z = 15; z >= 0; z--)
                for (int y = 0; y < 128; y++)
                {
                    short block = c.blocks[z + (x * 16) + (y * 256)];
                    if (block == 54)
                        i.setRGB(xt + x, yt + z, rgba2i(255, 0, 0, 255));
                }
        return System.nanoTime() - start;
    }

    public static int rgba2i(int r, int g, int b, int a)
    {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int combinecolors(int c, int srcr, int srcg, int srcb, int srca, int light)
    {
        int dstb = (c) & 0xFF;
        int dstg = (c >> 8) & 0xFF;
        int dstr = (c >> 16) & 0xFF;
        int dsta = (c >> 24) & 0xFF;

        srcr = srcr * light / 20;
        srcg = srcg * light / 20;
        srcb = srcb * light / 20;

        int outa = srca + dsta * (255 - srca) / 255;
        int outr = (srcr * srca + dstr * dsta * (255 - srca) / 255) / outa;
        int outg = (srcg * srca + dstg * dsta * (255 - srca) / 255) / outa;
        int outb = (srcb * srca + dstb * dsta * (255 - srca) / 255) / outa;

        return rgba2i(outr, outg, outb, outa);
    }

    public static class Block implements Comparable<Block>
    {

        int x, y, z;
        short b;

        @Override
        public int compareTo(Block o)
        {
            if (y == o.y)
                if (x == o.x)
                    return o.z - z;
                else
                    return o.x - x;
            else
                return y - o.y;
        }
    }
}