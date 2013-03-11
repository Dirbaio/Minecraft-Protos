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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class ImageViewer extends JFrame
{
    Image ii;

    public ImageViewer(Image i)
    {
        super("MCMap");
        ii = i;
        setSize(800, 500);
        add(new JScrollPane(new ImagePreviewerControl()), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    class ImagePreviewerControl extends JComponent
    {
        public ImagePreviewerControl()
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