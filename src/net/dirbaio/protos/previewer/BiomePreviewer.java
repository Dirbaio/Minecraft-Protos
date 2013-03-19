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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import net.dirbaio.protos.functions.Biome;

public class BiomePreviewer extends JFrame
{
    BufferedImage ii;
    int sx, sz;
    Biome[][] biomes;
    public BiomePreviewer(Biome[][] biomes)
    {
        super("Protos Biome Previewer");
        this.biomes = biomes;
        sx = biomes.length;
        sz = biomes[0].length;
        
        ii = new BufferedImage(biomes.length, biomes[0].length, BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < sx; x++)
            for(int z = 0; z < sz; z++)
                ii.setRGB(x, z, biomes[x][z].color.getRGB());
        
        setSize(800, 500);
        add(new JScrollPane(new BiomePreviewerControl()), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class BiomePreviewerControl extends JComponent
    {
        public BiomePreviewerControl()
        {
            setSize(ii.getWidth(null), ii.getHeight(null));
            setPreferredSize(new Dimension(ii.getWidth(null), ii.getHeight(null)));
        }

        @Override
        public void paint(Graphics g)
        {
            super.paint(g);
            g.drawImage(ii, 0, 0, null);
        }


    }    
}
