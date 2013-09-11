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
import java.lang.reflect.Field;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public abstract class TextProperty extends Property implements DocumentListener
{

    private JTextComponent t;
    private Border savedBorder;
    private boolean hasSavedBorder;

    public TextProperty(Object obj, Field field, int index, PropertyEditor ed)
    {
        super(obj, field, index, ed);
    }

    @Override
    public JComponent createComponent()
    {
        if(isTextArea())
            t = new JTextArea();
        else
            t = new JTextField();
        
        Object oval = getValue();
        String val = oval == null ? "" : oval.toString();
        t.setText(val);
        t.getDocument().addDocumentListener(this);

        return t;
    }

    protected boolean isTextArea()
    {
        return false;
    }
    
    protected abstract boolean parseString(String s);

    private void updated()
    {
        boolean ok = parseString(t.getText());

        if (ok)
        {
            if (hasSavedBorder)
                t.setBorder(savedBorder);
        }
        else
        {
            if (!hasSavedBorder)
            {
                savedBorder = t.getBorder();
                hasSavedBorder = true;
            }
            t.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        updated();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        updated();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        updated();
    }
}
