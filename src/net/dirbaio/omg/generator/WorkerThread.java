
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

	Point p;

	private int getOp()
	{
		//First do the latest operations possible
		//So that queues don't get full at all.
		
		p = null;
		p = (Point) gen.opQueues[2].poll();
		if(p != null)
			return 2;
		p = (Point) gen.opQueues[1].poll();
		if(p != null)
			return 1;
		p = (Point) gen.opQueues[0].poll();
		if(p != null)
			return 0;
		return -1;
	}
	
    @Override
    public void run()
    {
        System.out.println("Start Worker Thread"+num);
        try {
            while (true) {
				int op;
                while((op = getOp()) == -1)
                {
                    Thread.sleep(40);
                    if(gen.stop) break;
                }
                if(gen.stop) break;

				switch(op)
				{
					case 0:
					{
						Chunk c = new Chunk(p.x+gen.xMin, p.y+gen.zMin);
						int px = p.x*16 + gen.xMin*16;
						int pz = p.y*16 + gen.zMin*16;

						c.blocks = gen.mainFunc.getBlockData(px, pz, 16, 16);

						gen.generatedChunk(c, p.x, p.y);
						break;
					}
					case 1:
					{
						Chunk[][] ctx = gen.createAndLockContext(p.x, p.y);
						int i = 0;/*
						for(ChunkPopulator pop : ops)
						{
							i++;
							pop.setContext(ctx, Configuration.curr.seed+i);
							pop.populate();
						}*/
						gen.unlockContext(p.x, p.y);
						gen.getChunk(p.x, p.y).recalcHeightMap();
						gen.setChunkOpDone(p.x, p.y, 1);
						break;
					}
					case 2:
					{
						Chunk[][] ctx = gen.createAndLockContext(p.x, p.y);
						cl.lightChunk(ctx);
						gen.unlockContext(p.x, p.y);

						gen.setChunkOpDone(p.x, p.y, 2);
					}
				}
            }
        } catch (Exception e) {
            System.err.println("Worker Thread "+num+" has stopped because of an error");
            e.printStackTrace();
            System.err.println("Aborting generation!");
        }
        System.out.println("Stop Worker Thread "+num);
    }
}
