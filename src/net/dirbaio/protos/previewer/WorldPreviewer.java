package net.dirbaio.protos.previewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;
import net.dirbaio.protos.Chunk;
import net.dirbaio.protos.generator.ChunkOutput;

public class WorldPreviewer extends JComponent implements ChunkOutput, MouseListener, MouseMotionListener
{

    private int xSize, zSize;
    private int xMin, zMin;

    private int x0 = 200;
    private int y0 = 200;
    
    Chunk[][] chunks;

    public WorldPreviewer()
    {
        this.setDoubleBuffered(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (chunks == null)
            return;

        for (int x = xSize - 1; x >= 0; x--)
            for (int z = zSize - 1; z >= 0; z--)
            {
                int cx = x0 - x * 32 + z * 32;
                int cy = y0 - x * 16 - z * 16;

                Chunk c = chunks[x][z];
                if (c == null)
                    continue;
                BufferedImage img = c.cachedImg;
                if (img == null)
                    continue;
                g.drawImage(img, cx, cy, null);
            }
    }

    public void mouseClicked(MouseEvent e)
    {
    }
    int downX, downY;

    public void mousePressed(MouseEvent e)
    {
        downX = e.getX();
        downY = e.getY();
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
        x0 -= downX - e.getX();
        y0 -= downY - e.getY();
        downX = e.getX();
        downY = e.getY();
        repaint();
    }

    public void mouseMoved(MouseEvent e)
    {
    }
    
    public void chunkDone(Chunk c)
    {
        chunks[c.xPos-xMin][c.zPos-zMin] = c;
        c.updateMap();
        repaint();
    }
    
    public void generationStarted(int xMin, int zMin, int xSize, int zSize)
    {
        this.xSize = xSize;
        this.zSize = zSize;
        this.xMin = xMin;
        this.zMin = zMin;
        this.chunks = new Chunk[xSize][zSize];
    }

    public void generationFinished()
    {
    }
}
