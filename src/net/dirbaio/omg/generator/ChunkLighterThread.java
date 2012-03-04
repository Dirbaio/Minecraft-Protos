
package net.dirbaio.omg.generator;

import java.awt.Point;
import net.dirbaio.omg.Chunk;

public class ChunkLighterThread extends Thread
{

    WorldGenerator gen;

    ChunkLighter cl = new ChunkLighter();
    int generatedChunks = 0;
    
    int num;
    public ChunkLighterThread(WorldGenerator gen, int num)
    {
        super("Lighter thread "+num);
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
                    p = (Point) gen.opQueues[2].poll();
                    Thread.sleep(40);
                    if(gen.stop) break;
                }
                if(gen.stop) break;

//                System.out.println("Light "+p.x+" "+p.y);

                Chunk[][] ctx = gen.createAndLockContext(p.x, p.y);
                cl.lightChunk(ctx);
                gen.unlockContext(p.x, p.y);
                
                gen.setChunkOpDone(p.x, p.y, 2);
                generatedChunks++;
            }
        } catch (Exception e) {
            System.err.println("Light Thread "+num+" has stopped because of an error");
            e.printStackTrace();
            System.err.println("Aborting generation!");
        }
        System.out.println("Stop Light Thread "+num+": "+generatedChunks+" chunks lighted.");
    }
}
