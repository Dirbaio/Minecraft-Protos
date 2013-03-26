package net.dirbaio.protos.previewer;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import net.dirbaio.protos.functions.BiomeFunction;

public class BiomePreviewerFrame extends JFrame implements WindowListener
{
    BiomeFunction f;
    BiomePreviewer p;
    public BiomePreviewerFrame(BiomeFunction f) throws HeadlessException
    {
        super("Biome Previewer");
        this.f = f;
        
        p = new BiomePreviewer(f);
        add(p, BorderLayout.CENTER);
        addWindowListener(this);

        //Don't remove this or very bad things will happen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        p.stopThreads();
    }

    @Override
    public void windowIconified(WindowEvent e)
    {
    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
    }

    @Override
    public void windowActivated(WindowEvent e)
    {
    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {
    }
    
}
