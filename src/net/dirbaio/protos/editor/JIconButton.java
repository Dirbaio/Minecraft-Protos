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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class JIconButton extends JComponent implements MouseListener
{
    ImageIcon i;
    ArrayList<ActionListener> listeners = new ArrayList<>();
    
    public JIconButton(ImageIcon i)
    {
        this.i = i;
        addMouseListener(this);
        setSize(new Dimension(i.getIconWidth(), i.getIconHeight()));
        setPreferredSize(new Dimension(i.getIconWidth()+6, i.getIconHeight()));
        setMinimumSize(new Dimension(i.getIconWidth()+6, i.getIconHeight()));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if(hover)
        {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.drawImage(i.getImage(), (getWidth()-i.getIconWidth())/2, (getHeight() - i.getIconHeight())/2, null);
    }
    
    public void addActionListener(ActionListener l)
    {
        listeners.add(l);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        for(ActionListener l : listeners)
            l.actionPerformed(null);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    boolean hover = false;
    @Override
    public void mouseEntered(MouseEvent e)
    {
        hover = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        hover = false;
        repaint();
    }
    
}
