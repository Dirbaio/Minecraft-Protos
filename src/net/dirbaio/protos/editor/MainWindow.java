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
package net.dirbaio.protos.editor;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import net.dirbaio.protos.functions.BiomeFunction;
import net.dirbaio.protos.functions.GenLayerAddIsland;
import net.dirbaio.protos.functions.GenLayerAddMushroomIsland;
import net.dirbaio.protos.functions.GenLayerAddSnow;
import net.dirbaio.protos.functions.GenLayerBiome;
import net.dirbaio.protos.functions.GenLayerFuzzyZoom;
import net.dirbaio.protos.functions.GenLayerHills;
import net.dirbaio.protos.functions.GenLayerIsland;
import net.dirbaio.protos.functions.GenLayerRiver;
import net.dirbaio.protos.functions.GenLayerRiverInit;
import net.dirbaio.protos.functions.GenLayerRiverMix;
import net.dirbaio.protos.functions.GenLayerShore;
import net.dirbaio.protos.functions.GenLayerSmooth;
import net.dirbaio.protos.functions.GenLayerSwampRivers;
import net.dirbaio.protos.functions.GenLayerVoronoiZoom;
import net.dirbaio.protos.functions.GenLayerZoom;
import net.dirbaio.protos.functions.Output;
import net.dirbaio.protos.generator.ChunkOutput;
import net.dirbaio.protos.generator.DaniChunkOutput;
import net.dirbaio.protos.generator.DiskChunkOutput;
import net.dirbaio.protos.generator.WorldGenerator;
import net.dirbaio.protos.previewer.WorldPreviewer;

public class MainWindow extends JFrame implements ActionListener
{

    JButton previewButton;
    JButton generateButton;
    JButton generateDaniButton;
    EditorWindow ed;
    Project p;

    public static BiomeFunction getBiome()
    {

        GenLayerIsland var3 = new GenLayerIsland();
        GenLayerFuzzyZoom var9 = new GenLayerFuzzyZoom(var3);
        GenLayerAddIsland var10 = new GenLayerAddIsland(var9);
        GenLayerZoom var11 = new GenLayerZoom(var10);
        var10 = new GenLayerAddIsland(var11);
        GenLayerAddSnow var12 = new GenLayerAddSnow(var10);
        var11 = new GenLayerZoom(var12);
        var10 = new GenLayerAddIsland(var11);
        var11 = new GenLayerZoom(var10);
        var10 = new GenLayerAddIsland(var11);
        GenLayerAddMushroomIsland var16 = new GenLayerAddMushroomIsland(var10);

        int zoomCt = 4; //6 = Large biomes
        BiomeFunction var5 = GenLayerZoom.multiZoom(var16, 0);
        GenLayerRiverInit var13 = new GenLayerRiverInit(var5);
        var5 = GenLayerZoom.multiZoom(var13, zoomCt + 2);
        GenLayerRiver var14 = new GenLayerRiver(var5);
        GenLayerSmooth var15 = new GenLayerSmooth(var14);
        BiomeFunction var6 = GenLayerZoom.multiZoom(var16, 0);
        GenLayerBiome var17 = new GenLayerBiome(var6);
        var6 = GenLayerZoom.multiZoom(var17, 2);
        BiomeFunction var18 = new GenLayerHills(var6);

        for (int var7 = 0; var7 < zoomCt; ++var7)
        {
            var18 = new GenLayerZoom(var18);

            if (var7 == 0)
                var18 = new GenLayerAddIsland(var18);

            if (var7 == 1)
                var18 = new GenLayerShore(var18);

            if (var7 == 1)
                var18 = new GenLayerSwampRivers(var18);
        }

        GenLayerSmooth var19 = new GenLayerSmooth(var18);
        GenLayerRiverMix var20 = new GenLayerRiverMix(var19, var15);
        GenLayerVoronoiZoom var8 = new GenLayerVoronoiZoom(var20);

        BiomeFunction[] r =
        {
            var20, var8, var20
        };
        return var20;
    }

    public MainWindow() throws HeadlessException
    {
        super("Protos Editor");
        JToolBar tb = new JToolBar();
        add(tb, BorderLayout.NORTH);

        Output out = new Output();
        out.biome = getBiome();
        p = new Project(out);

        ed = new EditorWindow(p);
        add(ed, BorderLayout.CENTER);

        tb.add(previewButton = new JButton("Preview"));
        previewButton.addActionListener(this);
        tb.add(generateButton = new JButton("Generate"));
        generateButton.addActionListener(this);
        tb.add(generateDaniButton = new JButton("Towerthousand Generation™"));
        generateDaniButton.addActionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(getPreferredSize());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object s = e.getSource();
        if (s == previewButton)
        {
            WorldGenerator wg = new WorldGenerator(p.getOutput().output);

            WorldPreviewer wp = new WorldPreviewer();
            wg.addChunkOutput(wp);

            JFrame fr = new JFrame("Minecraft Protos Previewer");
            fr.setSize(500, 500);
            fr.add(wp, BorderLayout.CENTER);
            fr.setVisible(true);

            //Don't remove this or very bad things will happen
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            wg.runInThread();
        } else if (s == generateButton)
        {
            WorldGenerator wg = new WorldGenerator(p.getOutput().output);

            DiskChunkOutput out = new DiskChunkOutput(new File("./generated-map/"));
            wg.addChunkOutput(out);

            ProgressFrame pf = new ProgressFrame();
            pf.setVisible(true);
            wg.addProgressListener(pf);

            wg.runInThread();
        } else if (s == generateDaniButton)
        {
            WorldGenerator wg = new WorldGenerator(p.getOutput().output);

            ChunkOutput out = new DaniChunkOutput("out.bin");
            wg.addChunkOutput(out);

            ProgressFrame pf = new ProgressFrame();
            pf.setVisible(true);
            wg.addProgressListener(pf);

            wg.runInThread();
        }
    }
}
