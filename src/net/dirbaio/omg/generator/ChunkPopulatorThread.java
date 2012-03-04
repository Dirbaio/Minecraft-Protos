
package net.dirbaio.omg.generator;

import java.awt.Point;
import net.dirbaio.omg.Chunk;

public class ChunkPopulatorThread extends Thread
{

    WorldGenerator gen;

    int generatedChunks = 0;

    int num;
    public ChunkPopulatorThread(WorldGenerator gen, int num)
    {
        super("Populator thread "+num);
        this.num = num;
        this.gen = gen;

    }

    @Override
    public void run()
    {
        System.out.println("Start Pop Thread"+num);
        try {
            while (true) {
                Point p = null;

                while(p == null)
                {
                    p = (Point) gen.opQueues[1].poll();
                    Thread.sleep(40);
                    if(gen.stop) break;
                }
                if(gen.stop) break;

//                System.out.println("Pop "+p.x+" "+p.y);
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
                generatedChunks++;
            }
        } catch (Exception e) {
            System.err.println("Pop Thread "+num+" has stopped because of an error");
            e.printStackTrace();
            System.err.println("Aborting generation!");
        }
        System.out.println("Stop Pop Thread "+num+": "+generatedChunks+" chunks populated.");
    }
}
