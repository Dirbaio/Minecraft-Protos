/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.dirbaio.omg;

/**
 *
 * @author Dario
 */
public class Blocks
{
    public static Object[][] blockColours = {

            { "0 - Air", 			0, 		0, 		0, 		0 },	// air
            { "1 - Rock",			120, 	120, 	120, 	255 }, 	// rock
            { "2 - Grass",			103, 	166, 	43, 	255 },	// grass
            { "3 - Dirt",			120, 	100, 	50, 	255 }, 	// dirt
            { "4 - Stone",			125, 	125, 	109, 	255 }, 	// stone
            { "5 - Wood",			150, 	120, 	75, 	255 },	// wood
            { "6 - Shrub",			91, 	111, 	36, 	255 },	// shrub
            { "7 - Blackrock",		64, 	64, 	80, 	255 },	// blackrock
            { "8 - Water",			44, 	120, 	255, 	90 },	// water
            { "9 - Still Water",	35, 	120, 	255, 	90 },	// waterstill

            { "10 - Lava",				250, 	185, 	15,		255 },	// lava 10
            { "11 - Still Lava",		240, 	170, 	15,		255 },	// lavastill
            { "12 - Sand",				228, 	224, 	179, 	255 },	// sand
            { "13 - Gravel",			133, 	124, 	123, 	255 },	// gravel
            { "14 - Gold Rock",			139, 	130, 	110, 	255 },	// goldrock
            { "15 - Iron Rock",			134, 	124, 	118, 	255 },	// ironrock
            { "16 - Coal",				70, 	70, 	71, 	255 },	// coal
            { "17 - Trunk",				103, 	78, 	50, 	255 },	// trunk
            { "18 - Leaf",				90, 	160, 	80, 	220 },	// leaf
            { "19 - Sponge",			160, 	160, 	50, 	250 },	// sponge

            { "20 - Glass",				230, 	230, 	230, 	40 },	// glass 20
            { "21 - Red",				255, 	0, 		0,		255 },	// red
            { "22 - Orange",			255, 	200, 	0,		255 },	// orange
            { "23 - Yellow",			255, 	255, 	0,		255 },	// yellow
            { "24 - Light Green",		100, 	200, 	150,	255 },	// lightgreen
            { "25 - Green",				0, 		255, 	0,		255 },	// green
            { "26 - Aqua Green",		0, 		255, 	150,	255 },	// aquagreen
            { "27 - Cyan",				30, 	200, 	150,	255 },	// cyan
            { "28 - Light Blue",		100, 	100, 	255,	255 },	// lightblue
            { "29 - Blue",				0, 		0, 		255,	255 },	// blue

            { "30 - Purple",			200, 	50, 	200,	255 },	// purple 30
            { "31 - Light Purple",		220, 	80, 	240,	255 },	// lightpurple
            { "32 - Pink",				255, 	200, 	200,	255 },	// pink
            { "33 - Dark Pink",			200, 	130, 	130,	255 },	// darkpink
            { "34 - Dark Grey",			100, 	100, 	100,	255 },	// dark grey
            { "35 - Light Grey",		200, 	200, 	200,	255 },	// light grey
            { "36 - White",				0, 		0, 		0,		255 },	// white
            { "37 - Yellow Flower",		230, 	230, 	100, 	100 },	// yellowflower
            { "38 - Red Flower",		230, 	100, 	100, 	100 },	// redflower
            { "39 - Mushroom",			143, 	114, 	95, 	100 },	// mushroom

            { "40 - Red Mushroom",		200, 	60, 	60, 	100 },		// redmushroom 40
            { "41 - Gold Solid",		210, 	200, 	70, 	255 },		// goldsolid
            { "42 - Iron",				182, 	182, 	182, 	255 },		// iron
            { "43 - Staircase full",	126, 	126, 	126, 	255 },		// staircasefull
            { "44 - Staircase step",	139, 	139, 	139, 	255 },		// staircasestep
            { "45 - Brick",				144, 	100, 	100, 	255 },		// brick
            { "46 - TNT",				100, 	45, 	30, 	255 },		// tnt
            { "47 - Bookcase",			126, 	100, 	60, 	255 },		// bookcase
            { "48 - Stonevine",			80, 	90, 	80, 	255 },		// stonevine
            { "49 - Obsidian",			15, 	15, 	25, 	255 },		// obsidian

            { "50 - Torch",			255, 	255, 	25, 	255 },		// torch 50
            { "51 - Fire",			230, 	170, 	23, 	200 },		// fire
            { "52 - Mob Spawner",	210, 	100, 	210, 	255 },		// mob spawner
            { "53 - Wooden Stairs",	134, 	110, 	70, 	255 },		// wooden stairs
            { "54 - Chest",			115, 	80, 	30, 	255 },		// chest
            { "55 - Redstone Wire",	150, 	30, 	30, 	255 },		// redstone wire
            { "56 - Diamond Ore",	115, 	125, 	145, 	255 },		// diamond ore
            { "57 - Diamond Block",	85, 	170, 	170, 	255 },		// diamond block
            { "58 - Workbench",		108, 	70, 	40, 	255 },		// workbench
            { "59 - Crops",			106, 	150, 	110, 	255 },		// crops

            { "60 - Soil",					90, 	60, 	30, 	255 },		// soil 60
            { "61 - Furnace",				90, 	90, 	90, 	255 },		// furnace
            { "62 - Burning Furnace",		90, 	90, 	90, 	255 },		// burning furnace
            { "63 - Sign Post",				150, 	130, 	80, 	255 },		// Sign Post
            { "64 - Wooden Door",			140, 	100, 	50, 	255 },		// Wooden Door
            { "65 - Ladder",				130, 	115, 	90, 	255 },		// Ladder
            { "66 - Minecart Tracks",		130, 	120, 	100, 	255 },		// Minecart Tracks
            { "67 - Cobblestone Stairs",	76, 	76, 	76, 	255 },		// Cobblestone Stairs
            { "68 - Wall Sign",				156, 	126, 	77, 	255 },		// Wall Sign
            { "69 - Lever",					118, 	109, 	96, 	255 },		// Lever

            { "70 - Stone Pressure Plate",		119, 	119, 	119, 	255 },		// Stone Pressure Plate 70
            { "71 - Iron Door",					168, 	168, 	168, 	255 },		// Iron Door
            { "72 - Wooden Pressure Plate",		144, 	117, 	73, 	255 },		// Wooden Pressure Plate
            { "73 - Redstone Ore",				99, 	90, 	90, 	255 },		// Redstone Ore
            { "74 - Glowing Redstone Ore",		99, 	90, 	90, 	255 },		// Glowing Redstone Ore
            { "75 - Redstone Torch Off",		117, 	100, 	90, 	255 },		// Redstone torch off
            { "76 - Redstone Torch On",			175, 	100, 	80, 	255 },		// Redstone torch on
            { "77 - Stone Button",				100, 	100, 	100, 	255 },		// Stone Button
            { "78 - Snow",						230, 	244, 	244, 	255 },		// Snow
            { "79 - Ice",						120, 	150, 	227, 	130 },		// Ice

            { "80 - Snow Block",		190, 	200, 	200, 	255 },		// Snow Block 80
            { "81 - Cactus",			12, 	90, 	20, 	255 },		// Cactus
            { "82 - Clay",				124, 	124, 	140, 	255 },		// Clay
            { "83 - Reed",				143, 	165, 	120, 	255 },		// Reed
            { "84 - Jukebox",			100, 	65, 	50, 	255 },		// Jukebox
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85
            { "85 - Fence",				110, 	100, 	75, 	255 },		// Fence 85

    };

    public static byte[] blockOpacity;
    public static byte[] blockLightEmission;

    static
    {
        blockOpacity = new byte[256];
        for(int i = 0; i < 256; i++) blockOpacity[i] = 15; //Completely opaque
        blockLightEmission = new byte[256];
        for(int i = 0; i < 256; i++) blockLightEmission[i] = 0;

        blockOpacity[0] = 0;
        blockOpacity[6] = 0;
        blockOpacity[8] = 3;
        blockOpacity[9] = 3;
        blockOpacity[10] = 0;
        blockOpacity[11] = 0;
        blockOpacity[20] = 0;
        blockOpacity[37] = 0;
        blockOpacity[38] = 0;
        blockOpacity[39] = 0;
        blockOpacity[40] = 0;
        blockOpacity[50] = 0;
        blockOpacity[52] = 0;
        blockOpacity[55] = 0;
        blockOpacity[59] = 0;
        blockOpacity[63] = 0;
        blockOpacity[64] = 0;
        blockOpacity[65] = 0;
        blockOpacity[66] = 0;
        blockOpacity[67] = 0;
        blockOpacity[68] = 0;
        blockOpacity[69] = 0;
        blockOpacity[70] = 0;
        blockOpacity[71] = 0;
        blockOpacity[72] = 0;
        blockOpacity[75] = 0;
        blockOpacity[76] = 0;
        blockOpacity[77] = 0;
        blockOpacity[78] = 0;
        blockOpacity[79] = 3;
        blockOpacity[83] = 0;
        blockOpacity[85] = 0;
        blockOpacity[90] = 0;
        blockOpacity[18] = 1; //Tree leaves? Weird ?
        
        blockLightEmission[51] = 15;
        blockLightEmission[91] = 15;
        blockLightEmission[10] = 15;
        blockLightEmission[11] = 15;
        blockLightEmission[89] = 15;
        blockLightEmission[50] = 14;
        blockLightEmission[62] = 13;
        blockLightEmission[90] = 11;
        blockLightEmission[74] = 9;
        blockLightEmission[76] = 7;
        blockLightEmission[39] = 1; //????
    }

    public static boolean isSolidBlock(int id)
    {
        return blockOpacity[id] == 15;
    }
	
	public static byte getBlockLightEmission(int b)
	{
		return blockLightEmission[b & 0xFF];
	}
	public static byte getBlockOpacity(int b)
	{
		return blockOpacity[b & 0xFF];
	}
}
