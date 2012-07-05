
package net.dirbaio.omg.generator;

import java.awt.Point;
import net.dirbaio.omg.Chunk;

public class WorkerThread extends Thread
{

    WorldGenerator gen;
    ChunkLighter cl = new ChunkLighter();

    int num;
    public WorkerThread(WorldGenerator gen, int num)
    {
        super("Generator thread "+num);
        this.num = num;
        this.gen = gen;
    }

    @Override
    public void run()
    {
        System.out.println("Start Worker Thread"+num);
        try {
            while (true) {
				WorldGenerator.GeneratorTask t = gen.taskQueue.take();
                if(t.op == -1) break;
//				System.out.println(t.x+" "+t.z+" "+t.op);
				switch(t.op)
				{
					case 0:
					{
						Chunk c = new Chunk(t.x+gen.xMin, t.z+gen.zMin);
						int px = t.x*16 + gen.xMin*16;
						int pz = t.z*16 + gen.zMin*16;

						c.blocks = gen.mainFunc.getBlockData(px, pz, 16, 16);

						gen.generatedChunk(c, t.x, t.z);
						break;
					}
					case 1:
					{
						Chunk[][] ctx = gen.createAndLockContext(t.x, t.z, t.op);
						if(ctx != null) 
						{
							int i = 0;/*
							for(ChunkPopulator pop : ops)
							{
								i++;
								pop.setContext(ctx, Configuration.curr.seed+i);
								pop.populate();
							}*/
							gen.unlockContext(t.x, t.z);
							gen.getChunk(t.x, t.z).recalcHeightMap();
							gen.setChunkOpDone(t.x, t.z, 1);
						}
						break;
					}
					case 2:
					{
						Chunk[][] ctx = gen.createAndLockContext(t.x, t.z, t.op);
						if(ctx != null)
						{
							cl.lightChunk(ctx);
							gen.unlockContext(t.x, t.z);

							gen.setChunkOpDone(t.x, t.z, 2);
						}
					}
				}
            }
        } catch (Exception e) {
            System.err.println("Worker Thread "+num+" has stopped because of an error");
            e.printStackTrace();
            System.err.println("Aborting generation!");
			System.exit(1);
        }
        System.out.println("Stop Worker Thread "+num);
    }
}
