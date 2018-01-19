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

import java.awt.Dimension;
import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.JTextArea;

public abstract class Property
{

    public PropertyEditor propertyEditor;
    public Object obj;
    public Field field;
    public String name;
    public JComponent component;
    private int index;

    public Property(Object obj, Field field, int index, PropertyEditor ed)
    {
        this.propertyEditor = ed;
        this.obj = obj;
        this.field = field;
        this.name = field.getName();
        this.index = index;
    }

    public final String getName()
    {
        return name;
    }

    public final JComponent getComponent()
    {
        if(component == null)
        {
            component = createComponent();
            if (!(component instanceof JTextArea))
                component.setPreferredSize(new Dimension(100, component.getPreferredSize().height));
        }
        return component;
    }

    public final Object getValue()
    {
        try
        {
            return field.get(obj);
        }
        catch (IllegalArgumentException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public final void setValue(Object val)
    {
        try
        {
            field.set(obj, val);
        }
        catch (IllegalArgumentException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    public abstract JComponent createComponent();
}
