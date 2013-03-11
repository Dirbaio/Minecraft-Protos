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

package net.dirbaio.protos.generator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dirbaio.protos.Chunk;
import net.dirbaio.protos.functions.Function;
import net.dirbaio.protos.functions.FunctionTerrain;

public class WorldGenerator implements Runnable
{

    public static final int OP_COUNT = 4;
    private int ddone[] = new int[OP_COUNT+1];
    private Chunk[][] chunks;
    private boolean[][] savedChunks;

    private int chunkCount;
    private int chunksLeft;
    
    private int numWorkerThreads = 1;
    private WorkerThread[] workerThreads;
    
    private ArrayList<ChunkOutput> out = new ArrayList<ChunkOutput>();
    
    int xMin, zMin;
    int xSize, zSize;
    
    PriorityBlockingQueue<GeneratorTask> taskQueue;
    LinkedBlockingQueue<GeneratorTask> lockedTasksQueue;
    
    FunctionTerrain mainFunc;


    public static class GeneratorTask implements Comparable<GeneratorTask>
    {

        int x, z;
        int op;

        public GeneratorTask(int x, int z, int op)
        {
            this.x = x;
            this.z = z;
            this.op = op;
        }

        public int compareTo(GeneratorTask o)
        {
            if (op < o.op)
                return 1;
            if (op > o.op)
                return -1;
            if (z < o.z)
                return -1;
            if (z > o.z)
                return 1;
            if (x < o.x)
                return -1;
            if (x > o.x)
                return 1;
            return 0;
        }
    }
    
    public WorldGenerator(FunctionTerrain f)
    {
        mainFunc = f;
        
        xMin = -8;
        zMin = -8;
        xSize = 16;
        zSize = 16;

        //Autodetect based on number of cores!
        numWorkerThreads = Runtime.getRuntime().availableProcessors();
        
        //Set seed!
        try
        {
            setSeed(f, 92164286);
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void setSeed(Function f, long seed) throws IllegalArgumentException, IllegalAccessException
    {
        f.setRandomSeed(seed);
        Field[] fs = f.getClass().getFields();
        
        for(Field ff : fs)
        {
            if(Function.class.isAssignableFrom(ff.getType()))
            {
                setSeed((Function)ff.get(f), seed*20);
                seed++;
            }
        }
    }
    
    public void setSize(int xMin, int zMin, int xSize, int zSize)
    {
        this.xMin = xMin;
        this.zMin = zMin;
        this.xSize = xSize;
        this.zSize = zSize;
    }

    public void addTaskToQueue(int x, int y, int op)
    {
        taskQueue.add(new GeneratorTask(x, y, op));
        if (op >= 0)
            ddone[op]++;
    }

    private void fillGenQueue(int x, int y, int s)
    {
        if(x >= xSize || y >= zSize)
            return;

        if (s == 1)
            addTaskToQueue(x, y, 0);
        else
        {
            int s2 = s / 2;
            fillGenQueue(x + s2, y + s2, s2);
            fillGenQueue(x, y + s2, s2);
            fillGenQueue(x + s2, y, s2);
            fillGenQueue(x, y, s2);
        }
    }

    public void addChunkOutput(ChunkOutput out)
    {
        this.out.add(out);
    }

    public Chunk getChunk(int x, int z)
    {
        if (x < 0 || x >= xSize)
            return null;
        if (z < 0 || z >= zSize)
            return null;
        
        return chunks[x][z];
    }

    public void setChunk(int x, int z, Chunk c)
    {
        chunks[x][z] = c;
    }


    public void runInThread() throws IOException
    {
        new Thread(this, "Generator reporting thread").start();
    }

    
    public void run()
    {
        //First setup!
        chunks = new Chunk[xSize][zSize];
        savedChunks = new boolean[xSize][zSize];

        for(ChunkOutput o : out)
            o.generationStarted(xMin, zMin, xSize, zSize);

        //Create queue.
        taskQueue = new PriorityBlockingQueue<GeneratorTask>();
        lockedTasksQueue = new LinkedBlockingQueue<GeneratorTask>();

        //Fill it.
        int max = xSize > zSize ? xSize : zSize;
        int p = 1;
        while (p < max)
            p *= 2;

        fillGenQueue(0, 0, p);

        //Some stats.
        chunkCount = xSize * zSize;
        chunksLeft = xSize * zSize;

        //TODO: Make this threaded? How?
        mainFunc.prepare(xMin * 16, zMin * 16, xSize * 16, zSize * 16);

        //Now, create all the threads.
        workerThreads = new WorkerThread[numWorkerThreads];
        for (int i = 0; i < numWorkerThreads; i++)
        {
            workerThreads[i] = new WorkerThread(this, i);
            workerThreads[i].start();
        }

        //Report progress.
        while (chunksLeft != 0)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException ex)
            {
            }

            System.out.print((ddone[0] * 100 / chunkCount) + "% ");
            System.out.print((ddone[1] * 100 / chunkCount) + "% ");
            System.out.print((ddone[2] * 100 / chunkCount) + "% ");
            System.out.print((ddone[3] * 100 / chunkCount) + "% ");
            System.out.println();
        }

        //Stop all threads.
        stop();
        
        for(WorkerThread t : workerThreads)
        {
            try
            {
                t.join();
            }
            catch (InterruptedException ex)
            {}
        }
        
        System.out.println("Finishing...");
        for(ChunkOutput o : out)
            o.generationFinished();

        System.out.println("Generation finished!");
    }

    //OP MANAGEMENT
    //Returns whether chunk has undergone the op
    private boolean isChunkOpDone(int x, int z, int op)
    {
        if (x < 0 || x >= xSize)
            return true;
        if (z < 0 || z >= zSize)
            return true;
        if (savedChunks[x][z])
            return true;
        if (chunks[x][z] == null)
            return false;

        return chunks[x][z].opsDone[op];
    }

    //Return whether all chunks around have undergone the op
    private boolean canDoOpToChunk(int x, int z, int op)
    {
        if (x < 0 || x >= xSize)
            return false;
        if (z < 0 || z >= zSize)
            return false;
        if (chunks[x][z] == null)
            return false;
        if (chunks[x][z].opsDone[op])
            return false;

        for (int xx = x - 2; xx <= x + 2; xx++)
            for (int zz = z - 2; zz <= z + 2; zz++)
                if (!isChunkOpDone(xx, zz, op - 1))
                    return false;

        return true;
    }

    synchronized void setChunkOpDone(int x, int z, int op)
    {
        chunks[x][z].opsDone[op] = true;

        for (int xx = x - 2; xx <= x + 2; xx++)
            for (int zz = z - 2; zz <= z + 2; zz++)
                if (canDoOpToChunk(xx, zz, op + 1))
                    scheduleChunkForOp(xx, zz, op + 1);
    }

    private void scheduleChunkForOp(int x, int z, int op)
    {
        addTaskToQueue(x, z, op);
    }

    void finishChunk(int xc, int zc)
    {
        chunks[xc][zc].opsDone[3] = true;

        chunksLeft--;

        Chunk c = getChunk(xc, zc);

        for (ChunkOutput o : out)
            o.chunkDone(c);

        chunks[xc][zc] = null;
        savedChunks[xc][zc] = true;
    }

    synchronized Chunk[][] createAndLockContext(int x, int z, int op)
    {
        Chunk[][] context = new Chunk[3][3];

        for (int xx = 0; xx < 3; xx++)
            for (int zz = 0; zz < 3; zz++)
                context[xx][zz] = getChunk(x + xx - 1, z + zz - 1);

        boolean locked = false;

        for (int xx = 0; xx < 3; xx++)
            for (int zz = 0; zz < 3; zz++)
                if (context[xx][zz] != null && context[xx][zz].opLock.isLocked())
                    locked = true;

        if (locked)
        {
            lockedTasksQueue.add(new GeneratorTask(x, z, op));
            return null;
        } else
        {
            for (int xx = 0; xx < 3; xx++)
                for (int zz = 0; zz < 3; zz++)
                    if (context[xx][zz] != null)
                        context[xx][zz].opLock.lock();
            return context;
        }
    }

    synchronized void unlockContext(int x, int z)
    {
        for (int xx = 0; xx < 3; xx++)
            for (int zz = 0; zz < 3; zz++)
            {
                Chunk c = getChunk(x + xx - 1, z + zz - 1);
                if (c != null)
                    c.opLock.unlock();
            }
        taskQueue.addAll(lockedTasksQueue);
        lockedTasksQueue.clear();
    }

    void generatedChunk(Chunk c, int xc, int zc)
    {
        if (chunks[xc][zc] != null)
            throw new RuntimeException("Oh, shit!");

        setChunk(xc, zc, c);
        setChunkOpDone(xc, zc, 0);
    }

    /**
     * Stops all threads
     */
    public void stop()
    {
        for (int i = 0; i < numWorkerThreads; i++)
            addTaskToQueue(-1, -1, -1);
    }
}
