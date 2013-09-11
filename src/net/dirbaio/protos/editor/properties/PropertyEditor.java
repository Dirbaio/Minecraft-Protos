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

package net.dirbaio.protos.editor.properties;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.dirbaio.protos.editor.FunctionEditor;
import static net.dirbaio.protos.editor.FunctionEditor.unCamelCase;
import net.dirbaio.protos.editor.ProjectEditor;
import net.dirbaio.protos.functions.Function;


public class PropertyEditor extends JPanel
{
    public List<Property> properties = new ArrayList<>();
    private Object obj;
    public FunctionEditor fe;
    
    public PropertyEditor(Object obj, FunctionEditor fe)
    {
        this.obj = obj;
        this.fe = fe;
        
        loadProperties();
        
        this.setLayout(new GridBagLayout());
        
        int y = 1;
        for(Property p : properties)
        {
            JLabel label = new JLabel(p.getName());
            JComponent comp = p.getComponent();
            label.setLabelFor(comp);
            if(comp instanceof JTextArea)
            {
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.insets = new Insets(3, 3, 3, 3);
                c.gridx = 0;
                c.gridy = y++;
                c.gridwidth = 2;
                c.weightx = 1;
                add(comp, c);
            }
            else
            {
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.HORIZONTAL;
                c.insets = new Insets(3, 3, 3, 3);
                c.gridx = 0;
                c.gridy = y++;
                add(label, c);
                c.gridx = 1;
                c.weightx = 1;
                add(comp, c);
            }
        }
        
        setBackground(Color.WHITE);
    }

    private void loadProperties()
    {
        Class c = obj.getClass();
        Field[] fields = c.getFields();
        int i = 0;
        for(Field fi : fields)
            if(fi.getDeclaringClass() != Function.class)
            {
                Class type = fi.getType();
                if(type == int.class)
                    properties.add(new IntProperty(obj, fi, i++, this));
                else if(type == double.class)
                    properties.add(new DoubleProperty(obj, fi, i++, this));
                else if(type == float.class)
                    properties.add(new FloatProperty(obj, fi, i++, this));
                else if(type == short.class)
                    properties.add(new ShortProperty(obj, fi, i++, this));
                else if(type == byte.class)
                    properties.add(new ByteProperty(obj, fi, i++, this));
                else if(type == boolean.class)
                    properties.add(new BooleanProperty(obj, fi, i++, this));
                else if(type == String.class)
                    properties.add(new StringProperty(obj, fi, i++, this));
                else if(Function.class.isAssignableFrom(type))
                    properties.add(new FunctionProperty(obj, fi, i++, this));
                else
                    properties.add(new ObjectProperty(obj, fi, i++, this));
            }
    }

}
