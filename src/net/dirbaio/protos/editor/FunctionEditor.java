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
import java.awt.event.*;
import javax.swing.*;
import net.dirbaio.protos.Images;
import net.dirbaio.protos.editor.properties.PropertyEditor;
import net.dirbaio.protos.functions.*;
import net.dirbaio.protos.previewer.BiomePreviewerFrame;

public class FunctionEditor extends JPanel implements MouseListener, MouseMotionListener
{
    Function f;
    private String functionName;
    public ProjectEditor pe;
    
    boolean canBeSelected;
    boolean hasChildEdited;
    boolean isProcessed;
    PropertyEditor ed;

    public FunctionEditor(final Function f, final ProjectEditor pe)
    {
        this.f = f;
        this.pe = pe;

        functionName = f.getClass().getSimpleName();

        final Color col = getColorForClass(f.getClass());
        ImageIcon icon = getIconForClass(f.getClass());
        JLabel title = new JLabel(unCamelCase(functionName), icon, JLabel.LEADING);
        title.setBorder(BorderFactory.createEmptyBorder(1,6,1,0));
        title.addMouseListener(this);
        title.addMouseMotionListener(this);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
        buttonsPanel.setOpaque(false);
        JIconButton deleteButton = new JIconButton(Images.delete);
        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                pe.deleteFunction(f);
            }
        });
        
        if(f.getClass() != Output.class)
            buttonsPanel.add(deleteButton);

        
        JIconButton previewButton = new JIconButton(Images.preview);
        previewButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                doPreview();
            }
        });
        buttonsPanel.add(previewButton);
        
        JPanel titlePanel = new JPanel(new BorderLayout()){

            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, col, 0, getHeight(), Color.WHITE));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            
        };
        
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(buttonsPanel, BorderLayout.EAST);
        
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = c.ipady = 10;
        add(titlePanel, c);

        ed = new PropertyEditor(f, this);
        c.gridy = 1;
        c.ipadx = c.ipady = 0;
        add(ed, c);

        setLocation(f.xPos, f.yPos);
        setBorder(BorderFactory.createLineBorder(new Color(col.getRed()/2, col.getGreen()/2, col.getBlue()/2)));
        setBackground(Color.WHITE);

        setSize(getPreferredSize());
        setOpaque(true);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    public void doPreview()
    {
        if(f instanceof BiomeFunction)
            new BiomePreviewerFrame((BiomeFunction)f).setVisible(true);
    }

    public static String unCamelCase(String s)
    {
        String res = "";
        char last = '_';
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            char next = i==s.length()-1?' ':s.charAt(i+1);
            if(last == '_')
                res += Character.toUpperCase(c);
            else
            {
                if(Character.isUpperCase(c) && Character.isUpperCase(next))
                    res += " " + c;
                else if(Character.isDigit(c) || Character.isUpperCase(c) && !Character.isDigit(last))
                    res += " " + Character.toLowerCase(c);
                else
                    res += c;
            }
            
            last = c;
        }
        return res;
    }
    
    boolean down = false;
    int downX, downY;
    boolean moved;
    @Override
    public void mousePressed(MouseEvent e)
    {
        down = true;
        moved = false;
        downX = f.xPos - e.getXOnScreen();
        downY = f.yPos - e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        down = false;
        if(moved)
            pe.doNormalize();
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
        setPos(downX + e.getXOnScreen(), downY + e.getYOnScreen());
        moved = true;
    }

    public void setPos(int x, int y)
    {
        f.xPos = x;
        f.yPos = y;
        setLocation(f.xPos, f.yPos);
        pe.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }

    public static Color getColorForClass(Class c)
    {
        if(FunctionTerrain.class.isAssignableFrom(c))
            return new Color(100, 200, 60);
        if(Function2D.class.isAssignableFrom(c))
            return new Color(255, 128, 0);
        if(Function3D.class.isAssignableFrom(c))
            return new Color(30, 150, 200);

        return new Color(160, 150, 170);
    }
    
    public static ImageIcon getIconForClass(Class c)
    {
        if(FunctionTerrain.class.isAssignableFrom(c))
            return Images.functionterrain;
        if(Function2D.class.isAssignableFrom(c))
            return Images.function2d;
        if(Function3D.class.isAssignableFrom(c))
            return Images.function3d;

        return null;
    }
    
    public static String getNameForClass(Class c)
    {
        if(FunctionTerrain.class.isAssignableFrom(c))
            return "Terrain";
        if(Function2D.class.isAssignableFrom(c))
            return "2D function";
        if(Function3D.class.isAssignableFrom(c))
            return "3D function";

        return null;
    }
}
