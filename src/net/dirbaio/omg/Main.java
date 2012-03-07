/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
		WorldGenerator wg = new WorldGenerator(worldPath);
		
		Function2D heightmap = new PerlinNoise2D(20, 20, 50, 70);
		Function3D f = new HeightFunction(heightmap);
		f = new MathFunction3D(f, new PerlinNoise3D(10, 5, 10, -15, 20), MathFunction3D.FUNC_ADD);

		FunctionTerrain tf = new Terrain3D(f, (short)1);
		
		tf = new TerrainOverlay(tf, (short)3, (short)1, 4);
		tf = new TerrainOverlay(tf, (short)2, (short)3, 1);
		wg.mainFunc = tf;
		wg.generate();
    }
}
