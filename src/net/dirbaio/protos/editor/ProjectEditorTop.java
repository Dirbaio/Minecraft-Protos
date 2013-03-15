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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ProjectEditorTop extends JPanel
{
    final ProjectEditor e;
    final Project p;
    final HackPanel h;
    
    public ProjectEditorTop(Project p)
    {
        setLayout(new HackLayout(e = new ProjectEditor(p, this)));
        this.p = p;
        GridBagConstraints ct = new GridBagConstraints();
        ct.fill = GridBagConstraints.BOTH;
        ct.weightx = 1;
        ct.weighty = 1;
        add(e, new GridBagConstraints());
        add(h = new HackPanel(), ct);
        setComponentZOrder(h, 0);
        setComponentZOrder(e, 1);
//        h.setVisible(false);
    }

    @Override
    public boolean isOptimizedDrawingEnabled()
    {
        return false;
    }
    
    public class HackPanel extends JComponent implements MouseListener, MouseMotionListener
    {

        @Override
        protected void paintComponent(Graphics g)
        {
            g.setColor(Color.red);
            g.fillRect(0, 0, 100, 100);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
        }

        @Override
        public void mouseMoved(MouseEvent ev)
        {
            FunctionEditor old = e.hovered;
            e.hovered = null;
            int x = ev.getX();
            int y = ev.getY();
            e.mx = x;
            e.my = y;
            for(FunctionEditor ed : e.editors)
            {
                if(ed.getBounds().contains(x, y))
                    e.hovered = ed;
            }
            
            if(e.hovered != old)
                e.repaint();
        }
    }
    
    private class HackLayout implements LayoutManager
    {
        JComponent a;

        public HackLayout(JComponent a)
        {
            this.a = a;
        }
        
        
        @Override
        public void addLayoutComponent(String name, Component comp)
        {
        }

        @Override
        public void removeLayoutComponent(Component comp)
        {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent)
        {
            return a.getPreferredSize();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent)
        {
            return a.getMinimumSize();
        }

        @Override
        public void layoutContainer(Container parent)
        {
            int n = parent.getComponentCount();
            for(int i = 0; i < n; i++)
            {
                Component c = parent.getComponent(i);
                c.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            }
        }
    }
}
