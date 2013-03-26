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
package net.dirbaio.protos.previewer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JComponent;
import net.dirbaio.protos.functions.Biome;
import net.dirbaio.protos.functions.BiomeFunction;

public class BiomePreviewer extends JComponent implements MouseListener, MouseMotionListener
{

    private static final int CHUNK_SIZE = 256;
    private BiomeFunction func;
    private int xPos, zPos;
    private ChunkArray chunks;
    private WorkerThread[] workerThreads;
    private BiomeChunk beingPaintedChunk = new BiomeChunk();
    LinkedBlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();

    public BiomePreviewer(BiomeFunction func)
    {
        this.func = func;
        chunks = new ChunkArray();
        addMouseListener(this);
        addMouseMotionListener(this);

        int threadCt = Runtime.getRuntime().availableProcessors();
        workerThreads = new WorkerThread[threadCt];
        for (int i = 0; i < threadCt; i++)
        {
            workerThreads[i] = new WorkerThread();
            workerThreads[i].start();
        }

        //TODO/IMPORTANT: Halt the threads on closing. 
    }
    
    public void stopThreads()
    {
        for(WorkerThread t : workerThreads)
            t.halt();
    }
    
    public static Biome[][] dataToBiome(int[] data, int sx, int sz)
    {
        Biome[][] biomes = new Biome[sx][sz];
        for (int x = 0; x < sx; x++)
            for (int z = 0; z < sz; z++)
            {
                int v = data[x + z * sx];
                biomes[x][z] = v >= 0 ? Biome.biomeList[v] : Biome.undefined;
            }
        return biomes;
    }

    private static int divideDown(int a, int b)
    {
        if(a >= 0) 
            return a/b;
        else
            return (a+1)/b-1;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        int xMin = divideDown(xPos, CHUNK_SIZE);
        int xMax = divideDown(xPos + getWidth() + CHUNK_SIZE, CHUNK_SIZE);
        int zMin = divideDown(zPos, CHUNK_SIZE);
        int zMax = divideDown(zPos + getHeight() + CHUNK_SIZE, CHUNK_SIZE);

        chunks.move(xMin, zMin, xMax - xMin + 1, zMax - zMin + 1);

        for (int x = xMin; x <= xMax; x++)
            for (int z = zMin; z <= zMax; z++)
            {
                BiomeChunk c = chunks.getChunk(x, z);
                if (c != null)
                    g.drawImage(c.img, x * CHUNK_SIZE - xPos, z * CHUNK_SIZE - zPos, null);
            }
        
        if(in && !down)
        {
            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
            
            g.setColor(new Color(0, 0, 0, 128));
            g.fillRect(mouseX+10, mouseZ+10, 100, 50);
            g.setColor(Color.WHITE);
            Biome b = null;
            int cx = divideDown(xPos+mouseX, CHUNK_SIZE);
            int cz = divideDown(zPos+mouseZ, CHUNK_SIZE);
            BiomeChunk c = chunks.getChunk(cx, cz);
            if(c != null && c.biomes != null)
                b = c.biomes[(xPos+mouseX)- cx*CHUNK_SIZE][(zPos+mouseZ)- cz*CHUNK_SIZE];
            String s = "???";
            if(b != null)
                s = b.name;
            g.drawString(s, mouseX+16, mouseZ+30);
            g.drawString((mouseX + xPos) + ", " + (mouseZ + zPos), mouseX+16, mouseZ+50);
        }
    }
    boolean down = false;
    boolean in = false;
    int downX = 0;
    int downZ = 0;
    int speed = 2;
    
    int mouseX = 0;
    int mouseZ = 0;

    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        down = true;
        in = true;
        downX = xPos + e.getX() * speed;
        downZ = zPos + e.getY() * speed;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        down = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        in = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        in = false;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        in = true;
        mouseX = e.getX();
        mouseZ = e.getY();
        xPos = downX - e.getX() * speed;
        zPos = downZ - e.getY() * speed;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        in = true;
        mouseX = e.getX();
        mouseZ = e.getY();
        repaint();
    }

    class Task
    {

        int px, pz;

        public Task(int px, int pz)
        {
            this.px = px;
            this.pz = pz;
        }

        @Override
        public String toString()
        {
            return "Task(" + px + ", " + pz + ")";
        }
    }

    class WorkerThread extends Thread
    {

        public WorkerThread()
        {
            super("BiomePreviewer");
        }

        boolean running = true;

        @Override
        public void run()
        {
            while (running)
                try
                {
                    Task t = taskQueue.take();
                    if (chunks.isTaskStillValid(t))
                    {
                        chunks.setChunk(t.px, t.pz, chunks.makeChunk(t.px, t.pz));
                        repaint();
                    }
                } catch (InterruptedException ex)
                {
                }
        }

        public void halt()
        {
            running = false;
            interrupt();
        }
    }

    class ChunkArray
    {

        BiomeChunk[][] chunks;
        int sx, sz;
        int px, pz;

        void move(int npx, int npz, int nsx, int nsz)
        {
            if (npx == px && npz == pz && nsx == sx && nsz == sz)
                return;

            BiomeChunk[][] nchunks = new BiomeChunk[nsx][nsz];

            if (chunks != null)
            {
                int xMin = max(npx, px);
                int xMax = min(npx + nsx, px + sx);
                int zMin = max(npz, pz);
                int zMax = min(npz + nsz, pz + sz);

                for (int x = xMin; x < xMax; x++)
                    for (int z = zMin; z < zMax; z++)
                        nchunks[x - npx][z - npz] = chunks[x - px][z - pz];
            }


            px = npx;
            pz = npz;
            sx = nsx;
            sz = nsz;
            chunks = nchunks;

            for (int x = npx; x < npx + nsx; x++)
                for (int z = npz; z < npz + nsz; z++)
                    if (nchunks[x - npx][z - npz] == null)
                    {
                        nchunks[x - npx][z - npz] = beingPaintedChunk;
                        taskQueue.add(new Task(x, z));
                    }
        }

        boolean isTaskStillValid(Task t)
        {
            if (t.px < px || t.px >= px + sx)
                return false;
            if (t.pz < pz || t.pz >= pz + sz)
                return false;
            return getChunk(t.px, t.pz) == beingPaintedChunk;
        }

        BiomeChunk makeChunk(int x, int z)
        {
            int[] data = func.getBiomeData(x * CHUNK_SIZE, z * CHUNK_SIZE, CHUNK_SIZE, CHUNK_SIZE);
            return new BiomeChunk(dataToBiome(data, CHUNK_SIZE, CHUNK_SIZE));
        }

        BiomeChunk getChunk(int x, int z)
        {
            if (x < px || x >= px + sx)
                return null;
            if (z < pz || z >= pz + sz)
                return null;
            return chunks[x - px][z - pz];
        }

        void setChunk(int x, int z, BiomeChunk c)
        {
            if (x < px || x >= px + sx)
                return;
            if (z < pz || z >= pz + sz)
                return;
            chunks[x - px][z - pz] = c;
        }

        int max(int a, int b)
        {
            return a > b ? a : b;
        }

        int min(int a, int b)
        {
            return a < b ? a : b;
        }
    }

    class BiomeChunk
    {

        Biome[][] biomes;
        BufferedImage img;

        public BiomeChunk()
        {
        }

        public BiomeChunk(Biome[][] biomes)
        {
            this.biomes = biomes;
            img = new BufferedImage(CHUNK_SIZE, CHUNK_SIZE, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < CHUNK_SIZE; x++)
                for (int z = 0; z < CHUNK_SIZE; z++)
                    img.setRGB(x, z, biomes[x][z].color.getRGB());
        }
    }
}
