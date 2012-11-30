package net.dirbaio.omg.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import net.dirbaio.omg.Chunk;
import net.dirbaio.omg.functions.FunctionTerrain;

public class WorldGenerator implements Runnable
{

    public int xMin, zMin;
    public int xSize, zSize;
    public int chunkCt;
    public int chunkDone;
    public int chunksToSave;
    public Chunk[][] chunks;
    boolean[][] savedChunks;
    public PriorityBlockingQueue<GeneratorTask> taskQueue;
    public LinkedBlockingQueue<GeneratorTask> lockedTasksQueue;

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
    int numWorkerThreads = 9;
    WorkerThread[] workerThreads;
    ArrayList<ChunkOutput> out = new ArrayList<ChunkOutput>();
    public static final int opCount = 4;
    boolean stop = false;
    public FunctionTerrain mainFunc;
    int ddone[] = new int[5];

    public WorldGenerator()
    {
        xMin = -8;
        zMin = -8;
        xSize = 16;
        zSize = 16;
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
        if (s == 1)
        {
            if (x < xSize && y < zSize)
                addTaskToQueue(x, y, 0);
            return;
        }

        int s2 = s / 2;
        fillGenQueue(x + s2, y + s2, s2);
        fillGenQueue(x, y + s2, s2);
        fillGenQueue(x + s2, y, s2);
        fillGenQueue(x, y, s2);
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
        /*        if(chunks[x][z] == null)
         {
         System.out.println(x+" "+z);
         System.exit(2);
         }*/
        return chunks[x][z];
    }

    public void setChunk(int x, int z, Chunk c)
    {
        chunks[x][z] = c;
    }

    static public boolean deleteDirectory(File path)
    {
        if (path.exists())
        {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++)
                if (files[i].isDirectory())
                    deleteDirectory(files[i]);
                else
                    files[i].delete();
        }
        return (path.delete());
    }

    public void generate() throws IOException
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
        chunkDone = 0;
        chunkCt = xSize * zSize;
        chunksToSave = xSize * zSize;

        mainFunc.prepare(xMin * 16, zMin * 16, xSize * 16, zSize * 16);

        //Now, create all the threads.
        workerThreads = new WorkerThread[numWorkerThreads];
        for (int i = 0; i < numWorkerThreads; i++)
        {
            workerThreads[i] = new WorkerThread(this, i);
            workerThreads[i].start();
        }

        //Report progress.
        while (!stop)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException ex)
            {
            }

            System.out.print((ddone[0] * 100 / chunkCt) + "% ");
            System.out.print((ddone[1] * 100 / chunkCt) + "% ");
            System.out.print((ddone[2] * 100 / chunkCt) + "% ");
            System.out.print((ddone[3] * 100 / chunkCt) + "% ");
            System.out.println();

            boolean done = true;
            for (int i = 0; i < numWorkerThreads; i++)
                if (workerThreads[i].isAlive())
                    done = false;
            if (done)
                break;
        }

        //Stop all threads.
        for (int i = 0; i < numWorkerThreads; i++)
            addTaskToQueue(-1, -1, -1);

        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex)
        {
        }

        int ct = 0;
        for (int x = 0; x < xSize; x++)
            for (int z = 0; z < zSize; z++)
                if (chunks[x][z] != null)
                    ct++;

        System.out.println("Loaded chunks? " + ct);
        
        for(ChunkOutput o : out)
            o.generationFinished();
    }

    //OP MANAGEMENT
    //Returns whether chunk has undergone the op
    boolean isChunkOpDone(int x, int z, int op)
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
    boolean canDoOpToChunk(int x, int z, int op)
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

    public synchronized void setChunkOpDone(int x, int z, int op)
    {
        chunks[x][z].opsDone[op] = true;

        for (int xx = x - 2; xx <= x + 2; xx++)
            for (int zz = z - 2; zz <= z + 2; zz++)
                if (canDoOpToChunk(xx, zz, op + 1))
                    scheduleChunkForOp(xx, zz, op + 1);
    }

    public void scheduleChunkForOp(int x, int z, int op)
    {
        addTaskToQueue(x, z, op);
    }

    public void finishChunk(int xc, int zc)
    {
        chunks[xc][zc].opsDone[3] = true;

        chunksToSave--;
        if (chunksToSave == 0)
            stop = true;

        Chunk c = getChunk(xc, zc);

        for (ChunkOutput o : out)
            o.chunkDone(c);

        chunks[xc][zc] = null;
        savedChunks[xc][zc] = true;
    }

    public synchronized Chunk[][] createAndLockContext(int x, int z, int op)
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

    public synchronized void unlockContext(int x, int z)
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

    public void generatedChunk(Chunk c, int xc, int zc)
    {
        if (chunks[xc][zc] != null)
            throw new RuntimeException("Oh, shit!");

        setChunk(xc, zc, c);
        setChunkOpDone(xc, zc, 0);

        chunkDone++;
    }

    public void stop()
    {
        stop = true;
    }
}
