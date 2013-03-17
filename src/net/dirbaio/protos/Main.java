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

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import net.dirbaio.protos.editor.EditorWindow;
import net.dirbaio.protos.editor.MainWindow;
import net.dirbaio.protos.editor.Project;
import net.dirbaio.protos.editor.ProjectEditorTop;
import net.dirbaio.protos.functions.*;
import net.dirbaio.protos.generator.DaniChunkOutput;
import net.dirbaio.protos.generator.WorldGenerator;
import net.dirbaio.protos.previewer.WorldPreviewer;

public class Main extends JFrame
{

//    public static String worldPath = "C:\\Users\\Dario\\AppData\\Roaming\\.minecraft\\saves\\World1\\";
//    public static String worldPath = "C:\\Users\\dirbaio.dirbaio-win7\\AppData\\Roaming\\.minecraft\\saves\\";
    public static String worldPath = "./generated-map/";

    private static FunctionTerrain epicIslands()
    {
        Function3D terrainMap = new Add3D(
                new SimplexNoise3D(270, 1050, 280, -60, 130),
                new SimplexNoise3D(370, 2000, 420, -140, 250));
        terrainMap = new Add3D(terrainMap,
                new SimplexNoise3D(170, 750, 120, -20, 20));
        terrainMap = new Add3D(terrainMap,
                new SimplexNoise3D(20, 600, 23, -10, 10));
        terrainMap = new Add3D(terrainMap,
                new SimplexNoise3D(5, 1700, 6, -5, 5));

        Function2D seaheight = new SimplexNoise2D(30, 40, 50, 55);
        Function3D seavolume = new Substract3D(seaheight, new GetYCoord());
        Function2D landheight = new SimplexNoise2D(70, 70, 80, 160);
        landheight = new HeightmapErosion(landheight);
        Function3D landvolume = new Substract3D(landheight, new GetYCoord());
        Function2D beachheight = new Constant(63);
        Function3D beachvolume = new Substract3D(beachheight, new GetYCoord());

        Function3D terrainMap2 = new Add3D(
                new Add3D(
                    new SimplexNoise3D(170, 10000, 220, -170, 110),
                    new SimplexNoise3D(70, 10000, 80, -50, 50)),
                terrainMap);
        
        Function3D f = new Interpolate3D(seavolume, beachvolume, terrainMap2);
//		Function3D f = seavolume;
        f = new Interpolate3D(f, landvolume, terrainMap);

        //		f = landvolume;
        f = new Add3D(
                new SimplexNoise3D(20, 10, 20, -1, 3),
                f);

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

        FunctionTerrain tf = new TerrainVolume(f, (short) 1);
//		tf = new HeightmapTerrain(new Constant2D(66), (short)1);

        tf = new TerrainOverlay(tf, (short) 3, (short) 1, 4);
        tf = new BeachOverlay(tf, (short) 12, (short) 0, 5, 62, 66);
        tf = new Sea(tf, (short) 9, 64);
        tf = new TerrainOverlay(tf, (short) 2, (short) 3, 1);
        
        Function3D caveDistr = new SimplexNoise3D(160, 180, 130, -11.8, 5.4);
        caveDistr = new Min3D(
                caveDistr,
                new Constant(0));

        Function3D caves = new SimplexNoise3D(30, 13, 30, -1, 0.6);
        caves = new Add3D(
                caves,
                caveDistr);

        caves = new Limiter3D(caves, 30, 50);
        FunctionTerrain cavesTerrain = new TerrainVolume(caves, (short) 1);

        tf = new TerrainCut(tf, cavesTerrain);
        tf = new BedrockLayer(tf);
        tf = new Sea(tf, (short) 11, 10);     
        
        return tf;
    }
    
    private static FunctionTerrain floatingIslands()
    {
        Function3D f = new SimplexNoise3D(50, 50, 50, -200, 200);
        f = new Add3D(f, new SimplexNoise3D(15, 15, 15, -50, 50));
//        f = new Add3D(f, new PerlinNoise3D(500, 1000, 500, -70, 70));
        f = new Substract3D(f, new Substract3D(new GetYCoord(), new Constant(64)));
        f = new Substract3D(
                f, 
                new Multiply3D(
                    new Min3D(
                        new Constant(0), 
                        new Substract3D(new GetYCoord(), new Constant(64))), 
                    new Constant(10)));

        FunctionTerrain t = new TerrainVolume(f, (short)1);
        t = new TerrainOverlay(t, (short) 3, (short) 1, 4);
        t = new BeachOverlay(t, (short) 12, (short) 0, 8, 90, 100);
        t = new Sea(t, (short) 9, 92);
        t = new TerrainOverlay(t, (short) 2, (short) 3, 1);

        Function3D caveDistr = new SimplexNoise3D(160, 180, 130, -8, 9);
        caveDistr = new Min3D(
                caveDistr,
                new Constant(0));

        Function3D caves = new SimplexNoise3D(30, 13, 30, -1, 0.6);
        caves = new Add3D(
                caves,
                caveDistr);

        caves = new Limiter3D(caves, 0, 60);
        FunctionTerrain cavesTerrain = new TerrainVolume(caves, (short) 1);

        t = new TerrainCut(t, cavesTerrain);
        t = new BedrockLayer(t);
        t = new Sea(t, (short) 11, 10);     
        
        return t;
    }

    public static void main2(String[] args) throws FileNotFoundException, IOException
    {
        Function2D f = new VoronoiNoise2D(50, 50, 1, 2, VoronoiNoise2D.DistanceMetric.Quadratic, 1);
        new ImageViewer(f.render(0, 0, 512, 512, -1, 1)).setVisible(true);
    }
    
    
    public static void main_dani(String[] args) throws FileNotFoundException, IOException
    {
        Function3D f = new SimplexNoise3D(50, 50, 50, -200, 200);
        f = new Add3D(f, new SimplexNoise3D(15, 15, 15, -50, 50));
//        f = new Add3D(f, new PerlinNoise3D(500, 1000, 500, -70, 70));
        f = new Substract3D(f, new Substract3D(new GetYCoord(), new Constant(64)));
        f = new Substract3D(
                f, 
                new Multiply3D(
                    new Min3D(
                        new Constant(0), 
                        new Substract3D(new GetYCoord(), new Constant(64))), 
                    new Constant(10)));

        FunctionTerrain t = new TerrainVolume(f, (short)1);
        
        WorldGenerator wg = new WorldGenerator(t);
        int s = 8;
        if(false) //Set to true to enable preview
        {
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
            wg.addChunkOutput(new DaniChunkOutput("out.bin"));
        }
        wg.setSize(-s, -s, s*2, s*2);
        
        wg.run();
    }
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        Images.init();
     
        new MainWindow().setVisible(true);

        /*
        WorldGenerator wg = new WorldGenerator(floatingIslands());
        int s = 64;
        if(true)
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
        
        wg.run();*/
    }
}
