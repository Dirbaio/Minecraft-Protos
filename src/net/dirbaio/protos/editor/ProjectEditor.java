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
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import net.dirbaio.protos.functions.Function;

public class ProjectEditor extends JPanel implements MouseMotionListener, MouseListener
{
    Project p;
    List<FunctionEditor> editors = new ArrayList<>();
    Map<Function, FunctionEditor> editorsByFunction = new HashMap<>();
    
    public ProjectEditor(Project p)
    {
        this.p = p;
        
        setLayout(null);
        setBackground(Color.WHITE);
        
        for(Function f : p.funcs)
        {
            FunctionEditor fe = new FunctionEditor(f, this);
            editors.add(fe);
            editorsByFunction.put(f, fe);
            add(fe);
        }
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public boolean isOptimizedDrawingEnabled()
    {
        //Avoid glitches when overlapping panels.
        return false;
    }

    int mx, my;
    FunctionEditor.Property editedProperty = null;
    FunctionEditor hovered = null;
    
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        for(FunctionEditor ed : editors)
            for(FunctionEditor.Property p : ed.properties)
            {
                if(Function.class.isAssignableFrom(p.field.getType()))
                {
                    int x = ed.f.xPos;
                    int y = ed.f.yPos+p.component.getY()+p.component.getHeight()/2;
                    if(p == editedProperty)
                    {
                        if(hovered != null)
                        {
                            int x2 = hovered.f.xPos+hovered.getWidth();
                            int y2 = hovered.f.yPos+10;
                            drawConnection(g, x2, y2, x, y, p.index);
                        }
                        else
                            drawConnection(g, mx, my, x, y, p.index);
                    }
                    else
                    {
                        Function f2 = (Function) p.getValue();
                        if(f2 != null)
                        {
                            FunctionEditor e2 = editorsByFunction.get(f2);
                            int x2 = f2.xPos+e2.getWidth();
                            int y2 = f2.yPos+10;
                            drawConnection(g, x2, y2, x, y, p.index);
                        }
                    }
                }
            }
    }
    
    private void drawConnection(Graphics g, int x, int y, int x2, int y2, int index)
    {
        g.drawLine(x, y, x+5, y);
        x += 5;
        g.drawLine(x2, y2, x2-5, y2);
        x2 -= 5;
        if(x2 < x)
        {
            g.drawLine(x2, y2, x2-3*index, y2);
            x2 -= 3*index;
            g.drawLine(x, y, x, y-20);
            g.drawLine(x, y-20, x2, y-20);
            g.drawLine(x2, y-20, x2, y2);
        }
        else
        {
            g.drawLine(x, y, x, y2);
            g.drawLine(x, y2, x2, y2);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        repaint();
    }

    void selectFunction(FunctionEditor func)
    {
        editedProperty.setValue(func.f);
        editedProperty = null;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(editedProperty != null)
        {
            editedProperty = null;
            repaint();
        }
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
}
