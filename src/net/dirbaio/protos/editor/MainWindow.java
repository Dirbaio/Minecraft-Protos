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

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import net.dirbaio.protos.Main;
import net.dirbaio.protos.functions.Output;
import net.dirbaio.protos.generator.WorldGenerator;
import net.dirbaio.protos.previewer.WorldPreviewer;

public class MainWindow extends JFrame implements ActionListener
{
    JButton previewButton;
    EditorWindow ed;
    Project p;
    
    public MainWindow() throws HeadlessException
    {
        super("Protos Editor");
        JToolBar tb = new JToolBar();
        add(tb, BorderLayout.NORTH);
        
        ed = new EditorWindow(p = new Project());
        add(ed, BorderLayout.CENTER);
        
        tb.add(previewButton = new JButton("Preview"));
        previewButton.addActionListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(getPreferredSize());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == previewButton)
        {
            WorldGenerator wg = new WorldGenerator(p.getOutput().output);
            
            WorldPreviewer wp = new WorldPreviewer();
            wg.addChunkOutput(wp);

            JFrame fr = new JFrame("Minecraft Protos Previewer");
            fr.setSize(500, 500);
            fr.add(wp, BorderLayout.CENTER);
            fr.setVisible(true);

            //Don't remove this or very bad things will happen
            fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
            
            wg.runInThread();
        }
    }

}
