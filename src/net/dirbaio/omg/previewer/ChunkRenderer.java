package net.dirbaio.omg.previewer;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import net.dirbaio.omg.Chunk;

public class ChunkRenderer
{

    public static Object[][] blockColours =
    {
        {
            "0 - Air", 0, 0, 0, 0
        }, // air

        {
            "1 - Rock", 120, 120, 120, 255
        }, // rock

        {
            "2 - Grass", 103, 166, 43, 255
        }, // grass

        {
            "3 - Dirt", 120, 100, 50, 255
        }, // dirt

        {
            "4 - Stone", 125, 125, 109, 255
        }, // stone

        {
            "5 - Wood", 150, 120, 75, 255
        }, // wood

        {
            "6 - Shrub", 91, 111, 36, 255
        }, // shrub

        {
            "7 - Blackrock", 64, 64, 80, 255
        }, // blackrock

        {
            "8 - Water", 44, 120, 255, 24
        }, // water

        {
            "9 - Still Water", 35, 120, 255, 24
        }, // waterstill

        {
            "10 - Lava", 250, 185, 15, 255
        }, // lava 10

        {
            "11 - Still Lava", 240, 170, 15, 255
        }, // lavastill

        {
            "12 - Sand", 228, 224, 179, 255
        }, // sand

        {
            "13 - Gravel", 133, 124, 123, 255
        }, // gravel

        {
            "14 - Gold Rock", 139, 130, 110, 255
        }, // goldrock

        {
            "15 - Iron Rock", 134, 124, 118, 255
        }, // ironrock

        {
            "16 - Coal", 70, 70, 71, 255
        }, // coal

        {
            "17 - Trunk", 103, 78, 50, 255
        }, // trunk

        {
            "18 - Leaf", 90, 160, 80, 220
        }, // leaf

        {
            "19 - Sponge", 160, 160, 50, 250
        }, // sponge

        {
            "20 - Glass", 230, 230, 230, 40
        }, // glass 20

        {
            "21 - Red", 255, 0, 0, 255
        }, // red

        {
            "22 - Orange", 255, 200, 0, 255
        }, // orange

        {
            "23 - Yellow", 255, 255, 0, 255
        }, // yellow

        {
            "24 - Light Green", 100, 200, 150, 255
        }, // lightgreen

        {
            "25 - Green", 0, 255, 0, 255
        }, // green

        {
            "26 - Aqua Green", 0, 255, 150, 255
        }, // aquagreen

        {
            "27 - Cyan", 30, 200, 150, 255
        }, // cyan

        {
            "28 - Light Blue", 100, 100, 255, 255
        }, // lightblue

        {
            "29 - Blue", 0, 0, 255, 255
        }, // blue

        {
            "30 - Purple", 200, 50, 200, 255
        }, // purple 30

        {
            "31 - Light Purple", 220, 80, 240, 255
        }, // lightpurple

        {
            "32 - Pink", 255, 200, 200, 255
        }, // pink

        {
            "33 - Dark Pink", 200, 130, 130, 255
        }, // darkpink

        {
            "34 - Dark Grey", 100, 100, 100, 255
        }, // dark grey

        {
            "35 - Light Grey", 200, 200, 200, 255
        }, // light grey

        {
            "36 - White", 0, 0, 0, 255
        }, // white

        {
            "37 - Yellow Flower", 230, 230, 100, 100
        }, // yellowflower

        {
            "38 - Red Flower", 230, 100, 100, 100
        }, // redflower

        {
            "39 - Mushroom", 143, 114, 95, 100
        }, // mushroom

        {
            "40 - Red Mushroom", 200, 60, 60, 100
        }, // redmushroom 40

        {
            "41 - Gold Solid", 210, 200, 70, 255
        }, // goldsolid

        {
            "42 - Iron", 182, 182, 182, 255
        }, // iron

        {
            "43 - Staircase full", 126, 126, 126, 255
        }, // staircasefull

        {
            "44 - Staircase step", 139, 139, 139, 255
        }, // staircasestep

        {
            "45 - Brick", 144, 100, 100, 255
        }, // brick

        {
            "46 - TNT", 100, 45, 30, 255
        }, // tnt

        {
            "47 - Bookcase", 126, 100, 60, 255
        }, // bookcase

        {
            "48 - Stonevine", 80, 90, 80, 255
        }, // stonevine

        {
            "49 - Obsidian", 15, 15, 25, 255
        }, // obsidian

        {
            "50 - Torch", 255, 255, 25, 255
        }, // torch 50

        {
            "51 - Fire", 230, 170, 23, 200
        }, // fire

        {
            "52 - Mob Spawner", 210, 100, 210, 255
        }, // mob spawner

        {
            "53 - Wooden Stairs", 134, 110, 70, 255
        }, // wooden stairs

        {
            "54 - Chest", 115, 80, 30, 255
        }, // chest

        {
            "55 - Redstone Wire", 150, 30, 30, 255
        }, // redstone wire

        {
            "56 - Diamond Ore", 115, 125, 145, 255
        }, // diamond ore

        {
            "57 - Diamond Block", 85, 170, 170, 255
        }, // diamond block

        {
            "58 - Workbench", 108, 70, 40, 255
        }, // workbench

        {
            "59 - Crops", 106, 150, 110, 255
        }, // crops

        {
            "60 - Soil", 90, 60, 30, 255
        }, // soil 60

        {
            "61 - Furnace", 90, 90, 90, 255
        }, // furnace

        {
            "62 - Burning Furnace", 90, 90, 90, 255
        }, // burning furnace

        {
            "63 - Sign Post", 150, 130, 80, 255
        }, // Sign Post

        {
            "64 - Wooden Door", 140, 100, 50, 255
        }, // Wooden Door

        {
            "65 - Ladder", 130, 115, 90, 255
        }, // Ladder

        {
            "66 - Minecart Tracks", 130, 120, 100, 255
        }, // Minecart Tracks

        {
            "67 - Cobblestone Stairs", 76, 76, 76, 255
        }, // Cobblestone Stairs

        {
            "68 - Wall Sign", 156, 126, 77, 255
        }, // Wall Sign

        {
            "69 - Lever", 118, 109, 96, 255
        }, // Lever

        {
            "70 - Stone Pressure Plate", 119, 119, 119, 255
        }, // Stone Pressure Plate 70

        {
            "71 - Iron Door", 168, 168, 168, 255
        }, // Iron Door

        {
            "72 - Wooden Pressure Plate", 144, 117, 73, 255
        }, // Wooden Pressure Plate

        {
            "73 - Redstone Ore", 99, 90, 90, 255
        }, // Redstone Ore

        {
            "74 - Glowing Redstone Ore", 99, 90, 90, 255
        }, // Glowing Redstone Ore

        {
            "75 - Redstone Torch Off", 117, 100, 90, 255
        }, // Redstone torch off

        {
            "76 - Redstone Torch On", 175, 100, 80, 255
        }, // Redstone torch on

        {
            "77 - Stone Button", 100, 100, 100, 255
        }, // Stone Button

        {
            "78 - Snow", 230, 244, 244, 255
        }, // Snow

        {
            "79 - Ice", 120, 150, 227, 130
        }, // Ice

        {
            "80 - Snow Block", 190, 200, 200, 255
        }, // Snow Block 80

        {
            "81 - Cactus", 12, 90, 20, 255
        }, // Cactus

        {
            "82 - Clay", 124, 124, 140, 255
        }, // Clay

        {
            "83 - Reed", 143, 165, 120, 255
        }, // Reed

        {
            "84 - Jukebox", 100, 65, 50, 255
        }, // Jukebox

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        }, // Fence 85

        {
            "85 - Fence", 110, 100, 75, 255
        },		// Fence 85
    };

    static byte max(byte a, byte b)
    {
        return a > b ? a : b;
    }

    public static long renderChunk(BufferedImage i, Chunk c, int xt, int yt)
    {
        long start = System.nanoTime();

        ArrayList<Block> l = new ArrayList<Block>();

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

                            credit *= 255 - (Integer) blockColours[block][4];
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

                        credit *= 255 - (Integer) blockColours[block][4];
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

            int r = (Integer) blockColours[bl.b][1];
            int g = (Integer) blockColours[bl.b][2];
            int b = (Integer) blockColours[bl.b][3];
            int a = (Integer) blockColours[bl.b][4];

            short sblock = bl.b;
            if (sblock == 2)
                sblock = 3;
            int sr = (Integer) blockColours[sblock][1];
            int sg = (Integer) blockColours[sblock][2];
            int sb = (Integer) blockColours[sblock][3];
            int sa = (Integer) blockColours[sblock][4];

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

        ArrayList<Block> l = new ArrayList<Block>();

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

                        credit *= 255 - (Integer) blockColours[block][4];
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

            int r = (Integer) blockColours[bl.b][1];
            int g = (Integer) blockColours[bl.b][2];
            int b = (Integer) blockColours[bl.b][3];
            int a = (Integer) blockColours[bl.b][4];

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
        int dstb = (c >> 0) & 0xFF;
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