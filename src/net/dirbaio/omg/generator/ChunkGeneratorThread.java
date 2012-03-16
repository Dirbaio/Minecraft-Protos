
package net.dirbaio.omg.generator;

import java.awt.Point;
import net.dirbaio.omg.Chunk;

public class ChunkGeneratorThread extends Thread
{

    WorldGenerator gen;
    int generatedChunks = 0;

    int num;
    public ChunkGeneratorThread(WorldGenerator gen, int num)
    {
        super("Generator thread "+num);
        this.num = num;
        this.gen = gen;
    }

    @Override
    public void run()
    {
        System.out.println("Start Gen Thread"+num);
        try {
            while (true) {
                Point p = null;
                
                while(p == null)
                {
                    p = (Point) gen.opQueues[0].poll();
                    Thread.sleep(40);
                    if(gen.stop) break;
                }
                if(gen.stop) break;

                Chunk c = new Chunk(p.x+gen.xMin, p.y+gen.zMin);
				int px = p.x*16 + gen.xMin*16;
				int pz = p.y*16 + gen.zMin*16;
				
				c.blocks = gen.mainFunc.getBlockData(px, pz, 16, 16);

				gen.generatedChunk(c, p.x, p.y);
                generatedChunks++;
            }
        } catch (Exception e) {
            System.err.println("GenThread "+num+" has stopped because of an error");
            e.printStackTrace();
            System.err.println("Aborting generation!");
        }
        System.out.println("Stop Gen Thread "+num+": "+generatedChunks+" chunks generated.");
    }
}
