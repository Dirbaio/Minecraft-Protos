package net.dirbaio.omg;

import java.awt.BorderLayout;
import net.dirbaio.omg.functions.volume.*;
import net.dirbaio.omg.functions.plane.*;
import net.dirbaio.omg.functions.terrain.*;

import java.io.*;
import javax.swing.JFrame;
import net.dirbaio.omg.functions.*;
import net.dirbaio.omg.generator.*;
import net.dirbaio.omg.previewer.WorldPreviewer;
import sun.misc.FloatingDecimal;

public class Main extends JFrame
{

//    public static String worldPath = "C:\\Users\\Dario\\AppData\\Roaming\\.minecraft\\saves\\World1\\";
//    public static String worldPath = "C:\\Users\\dirbaio.dirbaio-win7\\AppData\\Roaming\\.minecraft\\saves\\";
    public static String worldPath = "./generated-map/";

    private static FunctionTerrain epicIslands()
    {
        Function3D terrainMap = new MathFunction3D(
                new PerlinNoise3D(270, 450, 280, -60, 130),
                new PerlinNoise3D(370, 150, 420, -140, 250),
                MathFunction3D.FUNC_ADD);
        terrainMap = new MathFunction3D(
                terrainMap,
                new PerlinNoise3D(170, 250, 120, -20, 20),
                MathFunction3D.FUNC_ADD);
        terrainMap = new MathFunction3D(terrainMap,
                new PerlinNoise3D(20, 300, 23, -10, 10),
                MathFunction3D.FUNC_ADD);
        terrainMap = new MathFunction3D(terrainMap,
                new PerlinNoise3D(5, 1000, 6, -5, 5),
                MathFunction3D.FUNC_ADD);

        Function2D seaheight = new PerlinNoise2D(30, 40, 50, 55);
        Function3D seavolume = new HeightFunction(seaheight);
        Function2D landheight = new PerlinNoise2D(70, 70, 80, 160);
        landheight = new HeightmapErosion(landheight);
        Function3D landvolume = new HeightFunction(landheight);
        Function2D beachheight = new Constant2D(63);
        Function3D beachvolume = new HeightFunction(beachheight);

        Function3D terrainMap2 = new MathFunction3D(
                new MathFunction3D(
                new PerlinNoise3D(170, 10000, 220, -170, 110),
                new PerlinNoise3D(70, 10000, 80, -50, 50),
                MathFunction3D.FUNC_ADD),
                terrainMap,
                MathFunction3D.FUNC_ADD);

        Function3D f = new Interpolate3D(seavolume, beachvolume, terrainMap2);
//		Function3D f = seavolume;
        f = new Interpolate3D(f, landvolume, terrainMap);

        //		f = landvolume;
        f = new MathFunction3D(
                new PerlinNoise3D(20, 10, 20, -1, 3),
                f,
                MathFunction3D.FUNC_SUB);

        /*
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
		
         f = new MathFunction3D(theOverhangs, f, MathFunction3D.FUNC_ADD);*/
//		f = new MathFunction3D(new Constant3D(0), f, MathFunction3D.FUNC_SUB);
//		new ImageViewer(new Slice2D(f, 64).renderChunks(-8, -8, 16, 16, -200, 200)).setVisible(true);
//        if(true) return;

        FunctionTerrain tf = new Terrain3D(f, (short) 1);

//		tf = new HeightmapTerrain(new Constant2D(66), (short)1);

        tf = new TerrainOverlay(tf, (short) 3, (short) 1, 4);
        tf = new BeachOverlay(tf, (short) 12, (short) 0, 5, 62, 66);
        tf = new SeaFunction(tf, (short) 9, 64);
        tf = new TerrainOverlay(tf, (short) 2, (short) 3, 1);

        Function3D caveDistr = new PerlinNoise3D(160, 180, 130, -11.8, 5.4);
        caveDistr = new MathFunction3D(
                caveDistr,
                new Constant3D(0),
                MathFunction3D.FUNC_MIN);

        Function3D caves = new PerlinNoise3D(30, 13, 30, -1, 0.6);
        caves = new MathFunction3D(
                caves,
                caveDistr,
                MathFunction3D.FUNC_ADD);

        caves = new Limiter3D(caves, 30, 50);
        FunctionTerrain cavesTerrain = new Terrain3D(caves, (short) 1);

        tf = new TerrainJoin(tf, cavesTerrain, true);
        tf = new BedrockLayer(tf);
        tf = new SeaFunction(tf, (short) 11, 10);     
        
        return tf;
    }
    
    private static FunctionTerrain floatingIslands()
    {
        Function3D f = new PerlinNoise3D(50, 50, 50, -200, 200);
        f = new MathFunction3D(f, new PerlinNoise3D(15, 15, 15, -50, 50), MathFunction3D.FUNC_ADD);
//        f = new MathFunction3D(f, new PerlinNoise3D(500, 1000, 500, -70, 70), MathFunction3D.FUNC_ADD);
        f = new MathFunction3D(f, new HeightFunction(new Constant2D(64)), MathFunction3D.FUNC_SUB);
        f = new MathFunction3D(
                f, 
                new MathFunction3D(
                    new MathFunction3D(
                        new Constant3D(0), 
                        new HeightFunction(new Constant2D(64)),
                        MathFunction3D.FUNC_MIN), 
                    new Constant3D(10),
                    MathFunction3D.FUNC_MUL),
                MathFunction3D.FUNC_SUB);

        FunctionTerrain t = new Terrain3D(f, (short)1);
        t = new TerrainOverlay(t, (short) 3, (short) 1, 4);
        t = new BeachOverlay(t, (short) 12, (short) 0, 8, 90, 100);
        t = new SeaFunction(t, (short) 9, 92);
        t = new TerrainOverlay(t, (short) 2, (short) 3, 1);

        Function3D caveDistr = new PerlinNoise3D(160, 180, 130, -8, 9);
        caveDistr = new MathFunction3D(
                caveDistr,
                new Constant3D(0),
                MathFunction3D.FUNC_MIN);

        Function3D caves = new PerlinNoise3D(30, 13, 30, -1, 0.6);
        caves = new MathFunction3D(
                caves,
                caveDistr,
                MathFunction3D.FUNC_ADD);

        caves = new Limiter3D(caves, 0, 60);
        FunctionTerrain cavesTerrain = new Terrain3D(caves, (short) 1);

        t = new TerrainJoin(t, cavesTerrain, true);
        t = new BedrockLayer(t);
        t = new SeaFunction(t, (short) 11, 10);     
        
        return t;
    }
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        //Function3D terrainMap = new Simple2DTo3D(
        //        new ImageFunction2D("height.png"));

        
        WorldGenerator wg = new WorldGenerator();
        wg.mainFunc = floatingIslands();

        int s = 64;
        if(false)
        {
            s = 8;
            WorldPreviewer wp = new WorldPreviewer();
            wg.addChunkOutput(wp);

            JFrame fr = new JFrame("Minecraft Protos Previewer");
            fr.setSize(500, 500);
            fr.add(wp, BorderLayout.CENTER);
            fr.setVisible(true);
            fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        else
        {
            wg.addChunkOutput(new DiskChunkOutput(new File(worldPath)));
        }
        wg.setSize(-s, -s, s*2, s*2);
        
        wg.generate();
    }
}
