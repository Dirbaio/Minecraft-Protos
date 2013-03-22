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

import net.dirbaio.protos.Chunk;

public class WorkerThread extends Thread
{

    private WorldGenerator gen;
    private ChunkLighter lighter = new ChunkLighter();
    private int threadId;

    public WorkerThread(WorldGenerator gen, int num)
    {
        super("Generator thread " + num);
        this.threadId = num;
        this.gen = gen;
    }

    @Override
    public void run()
    {
        try
        {
            while (true)
            {
                WorldGenerator.GeneratorTask t = gen.taskQueue.take();
                if (t.op == -1)
                    break;
//				System.out.println(t.x+" "+t.z+" "+t.op);
                switch (t.op)
                {
                    case 0:
                    {
                        Chunk c = new Chunk(t.x + gen.xMin, t.z + gen.zMin);
                        int px = t.x * 16 + gen.xMin * 16;
                        int pz = t.z * 16 + gen.zMin * 16;

                        c.blocks = gen.mainFunc.getTerrainData(px, pz, 16, 16);

                        gen.generatedChunk(c, t.x, t.z);
                        break;
                    }
                    case 1:
                    {
                        Chunk[][] ctx = gen.createAndLockContext(t.x, t.z, t.op);
                        if (ctx != null)
                        {
                            int i = 0;/*
                             * for(ChunkPopulator pop : ops) { i++;
                             * pop.setContext(ctx, Configuration.curr.seed+i);
                             * pop.populate();
                             }
                             */
                            gen.unlockContext(t.x, t.z);
                            gen.getChunk(t.x, t.z).recalcHeightMap();
                            gen.setChunkOpDone(t.x, t.z, 1);
                        }
                        break;
                    }
                    case 2:
                    {
                        Chunk[][] ctx = gen.createAndLockContext(t.x, t.z, t.op);
                        if (ctx != null)
                        {
                            lighter.lightChunk(ctx);
                            gen.unlockContext(t.x, t.z);

                            gen.setChunkOpDone(t.x, t.z, 2);
                        }
                        break;
                    }
                    case 3:
                    {
                        gen.finishChunk(t.x, t.z);
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Worker Thread " + threadId + " has stopped because of an error");
            e.printStackTrace();
            System.err.println("Aborting generation!");
            System.exit(1);
        }
    }
}
