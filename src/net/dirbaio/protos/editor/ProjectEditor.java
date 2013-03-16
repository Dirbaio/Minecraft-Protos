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
    ProjectEditorTop pet;
    
    public ProjectEditor(Project p, ProjectEditorTop pet)
    {
        this.p = p;
        this.pet = pet;
        
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
    
    void doNormalize()
    {
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMax = Integer.MIN_VALUE;

        for(FunctionEditor e : editors)
        {
            if(e.getX() < xMin)
                xMin = e.getX();
            if(e.getY() < yMin)
                yMin = e.getY();

            if(e.getX()+e.getWidth() > xMax)
                xMax = e.getX() + e.getWidth();
            if(e.getY()+e.getHeight() > yMax)
                yMax = e.getY() + e.getHeight();
        }
        
        int margin = 50;
        
        for(FunctionEditor e : editors)
            e.setPos(e.getX()-xMin+margin, e.getY()-yMin+margin);
        
        Dimension size = new Dimension(xMax-xMin+2*margin, yMax-yMin+2*margin);
        setSize(size);
        setPreferredSize(size);
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
        for(FunctionEditor.Property pr : e.properties)
        {
            Object val = pr.getValue();
            if(val != null && val instanceof Function)
                l.add(editorsByFunction.get((Function)val));
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
    protected void paintComponent(Graphics gr)
    {
        super.paintComponent(gr);
        Graphics2D g2 = (Graphics2D) gr;
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        

        g2.setColor(Color.BLACK);
        if(editedProperty != null)
        {
            if(hovered != null)
            {
                Color c = FunctionEditor.getColorForClass(hovered.f.getClass());
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                r = (r+255)/2;
                g = (g+255)/2;
                b = (b+255)/2;
                g2.setColor(new Color(r, g, b));
                g2.fillRect(hovered.getX()-10, hovered.getY()-10, hovered.getWidth()+20, hovered.getHeight()+20);
            }
            
            FunctionEditor e = editedProperty.ed;
            
            g2.setColor(Color.BLACK);
            g2.drawString("Click a function!", e.getX(), e.getY()+e.getHeight()+20);
        }
        
        for(FunctionEditor ed : editors)
            for(FunctionEditor.Property pr : ed.properties)
            {
                if(Function.class.isAssignableFrom(pr.field.getType()))
                {
                    gr.setColor(FunctionEditor.getColorForClass(pr.field.getType()));
                    int x = ed.f.xPos;
                    int y = ed.f.yPos+pr.component.getY()+pr.component.getHeight()/2;
                    if(pr == editedProperty)
                    {
                        if(hovered != null)
                        {
                            int x2 = hovered.f.xPos+hovered.getWidth();
                            int y2 = hovered.f.yPos+10;
                            drawConnection(g2, x2, y2, x, y);
                        }
                        else
                            drawConnection(g2, mx, my, x, y);
                    }
                    else
                    {
                        Function f2 = (Function) pr.getValue();
                        if(f2 != null)
                        {
                            FunctionEditor e2 = editorsByFunction.get(f2);
                            int x2 = f2.xPos+e2.getWidth();
                            int y2 = f2.yPos+10;
                            drawConnection(g2, x2, y2, x, y);
                        }
                    }
                }
            }
        
    }
    
    private void drawConnection(Graphics2D g, int x, int y, int x2, int y2)
    {
        Stroke st = g.getStroke();
        g.setStroke(new BasicStroke(2));
        Color c = g.getColor();
        g.setColor(Color.BLACK);
        int s = 1;
        x += s; y += s; x2 += s; y2 += s;
        g.drawLine(x-s, y, x+8, y);
        g.drawLine(x2, y2, x2-8, y2);
        g.drawLine(x+8, y, x2-8, y2);
        g.setColor(c);
        x -= s; y -= s; x2 -= s; y2 -= s;
        g.drawLine(x, y, x+8, y);
        g.drawLine(x2, y2, x2-8, y2);
        g.drawLine(x+8, y, x2-8, y2);
        g.setStroke(st);
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

    void deleteFunction(Function f)
    {
        FunctionEditor ed = editorsByFunction.get(f);
        remove(ed);
        editors.remove(ed);
        editProperty(null);
        p.funcs.remove(f);
        
        for(FunctionEditor e : editors)
            for(FunctionEditor.Property pr : e.properties)
                if(pr.getValue() == f)
                    pr.setValue(null);
        repaint();
    }

    void editProperty(FunctionEditor.Property p)
    {
        hovered = null;
        editedProperty = p;
        repaint();
        
        pet.h.setVisible(p != null);
        
        if(p != null)
        {
            for(FunctionEditor e : editors)
            {
                e.isProcessed = false;
                e.hasChildEdited = false;
            }
            
            for(FunctionEditor e : editors)
                if(!e.isProcessed)
                    process(e);
            for(FunctionEditor e : editors)
            {
                e.canBeSelected = p.field.getType().isAssignableFrom(e.f.getClass()) && !e.hasChildEdited;
            }
        }
    }
    
    private void process(FunctionEditor e)
    {
        if(e.isProcessed) return;
        e.isProcessed = true;
        
        if(editedProperty.ed == e)
        {
            e.hasChildEdited = true;
        }

        FunctionEditor[] c = getChildren(e);
        for(FunctionEditor e2 : c)
        {
            process(e2);
            if(e2.hasChildEdited)
                e.hasChildEdited = true;
        }
    }
    
    void selectFunction(FunctionEditor func)
    {
        if(editedProperty == null) return;
        editedProperty.setValue(func.f);
        endEditProperty();
    }

    void endEditProperty()
    {
        editProperty(null);
    }
}
