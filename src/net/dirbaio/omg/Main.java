/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.dirbaio.omg;

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
		wg.mainFunc = new HeightmapTerrain(
				new PerlinNoise2D(50, 50, 40, 80), (short)1);
		wg.generate();
    }
}
