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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JComponent;
import static net.dirbaio.protos.editor.FunctionEditor.getIconForClass;

public class FunctionProperty extends Property implements ActionListener
{

    public FunctionProperty(Object obj, Field field, int index, PropertyEditor ed)
    {
        super(obj, field, index, ed);
    }

    @Override
    public JComponent createComponent()
    {
        JButton res = new JButton(getIconForClass(field.getType()));
        res.addActionListener(this);
        return res;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        propertyEditor.fe.pe.editProperty(this);
    }
}
