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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ProjectEditorTop extends JPanel
{
    final ProjectEditor editor;
    final Project project;
    final OverlayPanel overlay;
    
    public ProjectEditorTop(Project p)
    {

		editor = new ProjectEditor(p, this);
		overlay = new OverlayPanel();

		setLayout(new OverlayLayout());

        this.project = p;

		add(overlay);
		add(editor);
		setComponentZOrder(overlay, 0);
		setComponentZOrder(editor, 1);
        overlay.setVisible(false);
    }

    @Override
    public boolean isOptimizedDrawingEnabled()
    {
        return false;
    }
    
    public class OverlayPanel extends JComponent implements MouseListener, MouseMotionListener
    {

        public OverlayPanel()
        {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }
        
        @Override
        protected void paintComponent(Graphics g)
        {
            g.setColor(new Color(255, 255, 255, 128));
            for(FunctionEditor ed : editor.editors)
                if(!ed.canBeSelected)
                    g.fillRect(ed.getX(), ed.getY(), ed.getWidth(), ed.getHeight());
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
        }

        @Override
        public void mousePressed(MouseEvent ev)
        {
            int x = ev.getX();
            int y = ev.getY();
            
            if(editor.hovered != null)
                editor.selectFunction(editor.hovered);
            else
                editor.endEditProperty();
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
            FunctionEditor old = editor.hovered;
            editor.hovered = null;
            int x = ev.getX();
            int y = ev.getY();
            editor.mx = x;
            editor.my = y;
 
            for(FunctionEditor ed : editor.editors)
                if(ed.canBeSelected && ed.getBounds().contains(x, y))
                    editor.hovered = ed;
 
            editor.repaint();
        }
    }
    
    private class OverlayLayout implements LayoutManager
    {

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
            return new Dimension(100, 100);
        }

        @Override
        public Dimension minimumLayoutSize(Container parent)
        {
			return new Dimension(100, 100);
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
