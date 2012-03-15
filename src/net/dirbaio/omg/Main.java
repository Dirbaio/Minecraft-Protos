package net.dirbaio.omg;

import net.dirbaio.omg.functions.volume.*;
import net.dirbaio.omg.functions.plane.*;
import net.dirbaio.omg.functions.terrain.*;

import java.io.*;
import javax.swing.JFrame;
import net.dirbaio.omg.functions.*;
import net.dirbaio.omg.generator.*;

public class Main extends JFrame
{

//    public static String worldPath = "C:\\Users\\Dario\\AppData\\Roaming\\.minecraft\\saves\\World1\\";
//    public static String worldPath = "C:\\Users\\Dario\\Application Data\\.minecraft\\saves\\World1\\";
    public static String worldPath = "/home/dirbaio/.minecraft/saves/test/";

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
	
		Function3D terrainMap = new MathFunction3D(
				new PerlinNoise3D(200, 2600, 130, -100, 300),
				new PerlinNoise3D(100, 250, 60, -50, 50),
				MathFunction3D.FUNC_ADD
				);
		
		terrainMap = new MathFunction3D(terrainMap, 
				new PerlinNoise3D(20, 300, 23, -20, 20),
				MathFunction3D.FUNC_ADD
				);
		terrainMap = new MathFunction3D(terrainMap, 
				new PerlinNoise3D(5, 1000, 6, -8, 8),
				MathFunction3D.FUNC_ADD
				);
		
		Function2D seaheight = new PerlinNoise2D(30, 40, 50, 40);
		Function3D seavolume = new HeightFunction(seaheight);
		Function2D landheight = new PerlinNoise2D(70, 70, 70, 90);
		Function3D landvolume = new HeightFunction(landheight);
		Function2D beachheight = new Constant2D(62);
		Function3D beachvolume = new HeightFunction(beachheight);
		
		Function3D terrainMap2 = new MathFunction3D(
				new MathFunction3D(
					new PerlinNoise3D(170, 10000, 220, -140, 130),
					new PerlinNoise3D(70, 10000, 80, -50, 70),
					MathFunction3D.FUNC_ADD),
				terrainMap,
				MathFunction3D.FUNC_ADD);
		
		Function3D f = new Interpolate3D(seavolume, beachvolume, terrainMap2);
//		Function3D f = seavolume;
		f = new Interpolate3D(f, landvolume,  terrainMap);
		
		f = new MathFunction3D(
				new PerlinNoise3D(20, 10, 20, -1, 3), 
				f, 
				MathFunction3D.FUNC_SUB);
		
		Function3D theOverhangs = new OverhangFunction(80, 34);
		theOverhangs = new MathFunction3D(
				theOverhangs, 
				new MathFunction3D(
					new MathFunction3D(
						new PerlinNoise3D(80, 80, 50, -1, 1), 
						new PerlinNoise3D(40, 50, 40, -0.5, 0.5), 
						MathFunction3D.FUNC_ADD),
					new Constant3D(0), 
					MathFunction3D.FUNC_MAX),
				MathFunction3D.FUNC_MUL);
		
		f = new MathFunction3D(theOverhangs, f, MathFunction3D.FUNC_ADD);
//		f = new MathFunction3D(new Constant3D(0), f, MathFunction3D.FUNC_SUB);

		FunctionTerrain tf = new Terrain3D(f, (short)1);
		
		tf = new TerrainOverlay(tf, (short)3, (short)1, 4);
		tf = new BeachOverlay(tf, (short)12, (short)0, 5, 62, 65);
		tf = new SeaFunction(tf, (short)8, 64);
		tf = new TerrainOverlay(tf, (short)2, (short)3, 1);
		
		
		WorldGenerator wg = new WorldGenerator(worldPath);
		wg.mainFunc = tf;
		wg.generate();
    }
}