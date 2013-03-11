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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.dirbaio.protos.functions.Function;
import net.dirbaio.protos.functions.Function2D;
import net.dirbaio.protos.functions.Function3D;
import net.dirbaio.protos.functions.FunctionTerrain;

public class FunctionEditor extends JPanel implements MouseListener, MouseMotionListener
{
    Function f;
    private String functionName;
    ProjectEditor pe;

    public FunctionEditor(Function f, ProjectEditor pe)
    {
        this.f = f;
        this.pe = pe;

        functionName = f.getClass().getSimpleName();
        loadProperties();

        this.setLayout(new GridBagLayout());

        JLabel title = new JLabel(unCamelCase(functionName));
        title.setBackground(getColorForClass(f.getClass()));
        title.setOpaque(true);
        title.addMouseListener(this);
        title.addMouseMotionListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.ipadx = c.ipady = 10;
        add(title, c);

        int y = 1;
        for(Property p : properties)
        {
            JLabel label = new JLabel(p.name);
            JComponent comp = p.component;
            label.setLabelFor(comp);

            c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(3, 3, 3, 3);
            c.gridx = 0;
            c.gridy = y++;
            add(label, c);
            c.gridx = 1;
            c.weightx = 1;
            add(comp, c);
        }
        //===========

        setLocation(f.xPos, f.yPos);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.YELLOW);

        setSize(getPreferredSize());
        setOpaque(true);
    }
    List<Property> properties = new ArrayList<>();

    class Property
    {

        Field field;
        String name;
        JComponent component;
        int index;

        public Property(Field f, String name, int index)
        {
            this.field = f;
            this.name = name;
            this.index = index;
            this.component = createComponent();
            component.setPreferredSize(new Dimension(100, component.getPreferredSize().height));
        }

        public Object getValue()
        {
            try
            {
                return field.get(f);
            } catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        public void setValue(Object val)
        {
            try
            {
                field.set(f, val);
            } catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        private JComponent createComponent()
        {
            Class type = field.getType();
            if(Function.class.isAssignableFrom(type))
            {
                JButton res = new JButton("Select...");
                final Property self = this;
                res.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(pe.editedProperty != null)
                            pe.editedProperty.endFunctionEdition();
                        pe.editedProperty = self;
                        pe.mx = f.xPos;
                        pe.my = f.yPos;
                        pe.repaint();
                        JButton but = (JButton) component;
                        but.setText("...");
                    }
                });
                return res;
            }
            else
            {
                String val = getValue().toString();
                JTextField res = new JTextField(val);
                res.getDocument().addDocumentListener(new DocumentListener()
                {
                    @Override
                    public void changedUpdate(DocumentEvent e)
                    {
                        modified();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e)
                    {
                        modified();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e)
                    {
                        modified();
                    }
                });
                return res;
            }
        }

        public void endFunctionEdition()
        {
            JButton but = (JButton) component;
            but.setText("Select...");
        }
        Border savedBorder;
        boolean hasSavedBorder;

        private void modified()
        {
            JTextField tf = (JTextField) component;
            String text = tf.getText();
            Object val = null;
            try
            {
                val = fromString(text);
            } catch(Exception ex)
            {
            }

            if(val != null)
            {
                setValue(val);
                if(hasSavedBorder)
                    tf.setBorder(savedBorder);
            }
            else
            {
                if(!hasSavedBorder)
                {
                    savedBorder = tf.getBorder();
                    hasSavedBorder = true;
                }
                tf.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }
        }

        private Object fromString(String val)
        {
            Class type = field.getType();
            if(type == String.class)
                return val;
            if(type == int.class)
                return Integer.parseInt(val);
            if(type == short.class)
                return Short.parseShort(val);
            if(type == double.class)
                return Double.parseDouble(val);

            return null;
        }
    }

    private String unCamelCase(String s)
    {
        String res = "";
        boolean first = true;
        boolean last = true;
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(first)
                res += Character.toUpperCase(c);
            else
            {
                if(Character.isUpperCase(c) || Character.isDigit(c) && !last)
                {
                    last = true;
                    res += " " + Character.toLowerCase(c);
                }
                else
                {
                    last = false;
                    res += c;
                }
            }

            first = false;
        }
        return res;
    }

    private void loadProperties()
    {
        Class c = f.getClass();
        Field[] fields = c.getFields();
        int i = 0;
        for(Field fi : fields)
        {
            if(fi.getName().equals("xPos") || fi.getName().equals("yPos"))
                continue;
            properties.add(new Property(fi, unCamelCase(fi.getName()), i++));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(pe.editedProperty != null)
            pe.selectFunction(this);
    }
    boolean down = false;
    int downX, downY;

    @Override
    public void mousePressed(MouseEvent e)
    {
        down = true;
        downX = f.xPos - e.getXOnScreen();
        downY = f.yPos - e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        down = false;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        pe.hovered = this;
        if(pe.editedProperty != null)
            pe.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        pe.hovered = null;
        if(pe.editedProperty != null)
            pe.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        setPos(downX + e.getXOnScreen(), downY + e.getYOnScreen());
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
            return new Color(220, 150, 60);
        if(Function3D.class.isAssignableFrom(c))
            return new Color(30, 150, 200);

        return new Color(160, 150, 170);
    }
}