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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import net.dirbaio.protos.functions.Function;
import net.dirbaio.protos.functions.Output;

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
        doAutoLayout();
        doNormalize();
    }
    
    private void doNormalize()
    {
        int xMin = 0;
        int yMin = 0;
        for(FunctionEditor e : editors)
        {
            if(e.getX() < xMin)
                xMin = e.getX();
            if(e.getY() < yMin)
                yMin = e.getY();
        }
        
        for(FunctionEditor e : editors)
            e.setPos(e.getX()-xMin, e.getY()-yMin);
        int xMax = 0;
        int yMax = 0;

        for(FunctionEditor e : editors)
        {
            if(e.getX()+e.getWidth() > xMax)
                xMax = e.getX() + e.getWidth();
            if(e.getY()+e.getHeight() > yMax)
                yMax = e.getY() + e.getHeight();
        }
        
        setSize(xMax, yMax);
        setPreferredSize(new Dimension(xMax, yMax));
    }
    
    private void doAutoLayout()
    {
        FunctionEditor first = null;
        for(FunctionEditor e : editors)
            if(e.f instanceof Output)
                first = e;
        
        doAutoLayout(first, 1200, 50);
    }
    
    private FunctionEditor[] getChildren(FunctionEditor e)
    {
        ArrayList<FunctionEditor> l = new ArrayList<>();
        for(FunctionEditor.Property p : e.properties)
        {
            Object val = p.getValue();
            if(val != null && val instanceof Function)
                l.add(editorsByFunction.get(val));
        }
        
        return l.toArray(new FunctionEditor[0]);
    }
    
    private int getHeight(FunctionEditor e)
    {
        int r = -10;
        for(FunctionEditor e2 : getChildren(e))
            r += getHeight(e2) + 10;
        int r2 = e.getHeight() + 10;
        
        return r2 > r ? r2 : r;
    }
    
    private void doAutoLayout(FunctionEditor e, int x, int y)
    {
        e.setPos(x, y);
        
        for(FunctionEditor e2 : getChildren(e))
        {
            doAutoLayout(e2, x-e2.getWidth()-30, y);
            y += getHeight(e2);
        }
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

        Graphics2D g2 = (Graphics2D) g;
        for(FunctionEditor ed : editors)
            for(FunctionEditor.Property p : ed.properties)
            {
                if(Function.class.isAssignableFrom(p.field.getType()))
                {
                    g.setColor(FunctionEditor.getColorForClass(p.field.getType()));
                    int x = ed.f.xPos;
                    int y = ed.f.yPos+p.component.getY()+p.component.getHeight()/2;
                    if(p == editedProperty)
                    {
                        if(hovered != null)
                        {
                            int x2 = hovered.f.xPos+hovered.getWidth();
                            int y2 = hovered.f.yPos+10;
                            drawConnection(g2, x2, y2, x, y, p.index);
                        }
                        else
                            drawConnection(g2, mx, my, x, y, p.index);
                    }
                    else
                    {
                        Function f2 = (Function) p.getValue();
                        if(f2 != null)
                        {
                            FunctionEditor e2 = editorsByFunction.get(f2);
                            int x2 = f2.xPos+e2.getWidth();
                            int y2 = f2.yPos+10;
                            drawConnection(g2, x2, y2, x, y, p.index);
                        }
                    }
                }
            }
    }
    
    private void drawConnection(Graphics2D g, int x, int y, int x2, int y2, int index)
    {
        Stroke s = g.getStroke();
        g.setStroke(new BasicStroke(2));
        //TODO Improve
        g.drawLine(x, y, x+8, y);
        x += 8;
        g.drawLine(x2, y2, x2-8, y2);
        x2 -= 8;
        
        g.drawLine(x, y, x2, y2);
        
        g.setStroke(s);
        /*
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
        }*/
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
        editedProperty.endFunctionEdition();
        editedProperty = null;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(editedProperty != null)
        {
            editedProperty.endFunctionEdition();
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
