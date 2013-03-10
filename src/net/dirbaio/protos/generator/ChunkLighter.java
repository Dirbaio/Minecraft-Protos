package net.dirbaio.protos.generator;

import java.util.LinkedList;
import net.dirbaio.protos.Blocks;
import net.dirbaio.protos.Chunk;

public class ChunkLighter
{
    private static final int xSize = 3;
    private static final int ySize = 3;

    public Chunk[][] chunks;


    public int min(int a, int b)
    {
        return a<b?a:b;
    }

    public int max(int a, int b)
    {
        return a>b?a:b;
    }

    public int getBlock(int x, int y, int z)
    {
        if(chunks[x/16][z/16] == null) return 1;
        return chunks[x/16][z/16].getBlock(x%16, y, z%16);
    }

    public byte getBlockLight(int x, int y, int z)
    {
        if(chunks[x/16][z/16] == null) return 0;
        return chunks[x/16][z/16].getBlockLight(x%16, y, z%16);
    }

    public void setBlockLight(int x, int y, int z, byte d)
    {
        if(chunks[x/16][z/16] == null) return;
//        if(chunks[x/16][z/16].lighted) return;
        chunks[x/16][z/16].setBlockLight(x%16, y, z%16, d);
    }

    public byte getSkyLight(int x, int y, int z)
    {
        if(chunks[x/16][z/16] == null) return 0;
        return chunks[x/16][z/16].getSkyLight(x%16, y, z%16);
    }

    public void setSkyLight(int x, int y, int z, byte d)
    {
        if(chunks[x/16][z/16] == null) return;
//        if(chunks[x/16][z/16].lighted) return;
        chunks[x/16][z/16].setSkyLight(x%16, y, z%16, d);
    }

    public int getHeight(int x, int z)
    {
        if(chunks[x/16][z/16] == null) return 128;
        int res = chunks[x/16][z/16].getHeight(x%16, z%16);
        return res;
    }

    //Java generics suck
    @SuppressWarnings("unchecked")
    public void lightChunk(Chunk[][] cs)
    {
        chunks = cs;

        LinkedList[] ls = new LinkedList[16];
        for(int i = 0; i < 16; i++) ls[i] = new LinkedList();
        
        for(int x = 16; x < 32; x++)
            for(int z = 16; z < 32; z++)
            {
                int bottom = getHeight(x, z);

				for(int i = bottom; i < Chunk.MAP_HEIGHT; i++)
                {
                    setSkyLight(x, i, z, (byte)15);
                    ls[15].add(new Pos(x, i, z, (byte)15));
                }

                /*
                int top = getHeight(x-1, z-1);
                top = max(top, getHeight(x-1, z+1));
                top = max(top, getHeight(x+1, z+1));
                top = max(top, getHeight(x+1, z-1));
                int bottom = getHeight(x, z);
                for(int i = bottom; i <= top; i++)
                    ls[15].add(new Pos(x, i, z, (byte)15));
                if(bottom < 0) bottom = 128;
                if(bottom < 0) throw new RuntimeException("Negative height??");
                for(int i = bottom; i < 128; i++)
                    setSkyLight(x, i, z, (byte)15);*/
            }

        for(int i = 15; i >= 0; i--)
        {
            for(Object o : ls[i])
            {
                Pos p = (Pos) o;
                byte light = getSkyLight(p.x, p.y, p.z);
                propagateSkyLight(p.x+1, p.y, p.z, light, ls);
                propagateSkyLight(p.x-1, p.y, p.z, light, ls);
                propagateSkyLight(p.x, p.y+1, p.z, light, ls);
                propagateSkyLight(p.x, p.y-1, p.z, light, ls);
                propagateSkyLight(p.x, p.y, p.z+1, light, ls);
                propagateSkyLight(p.x, p.y, p.z-1, light, ls);
            }
        }

        ls = new LinkedList[16];
        for(int i = 0; i < 16; i++) ls[i] = new LinkedList();

        for(int x = 16; x < 32; x++)
            for(int z = 16; z < 32; z++)
                for(int y = 0; y < Chunk.MAP_HEIGHT; y++)
                {
                    int b = getBlock(x, y, z);
                    byte lg = Blocks.getBlockLightEmission(b);
                    if(lg != 0)
                    {
//                        System.out.println(x+", "+y+", "+z+", "+b+", "+lg);
                        ls[lg].add(new Pos(x, y, z, lg));
                        setBlockLight(x, y, z, lg);
                    }
                }

        for(int i = 15; i >= 0; i--)
        {
            for(Object o : ls[i])
            {
                Pos p = (Pos) o;
                byte light = getBlockLight(p.x, p.y, p.z);
                propagateBlockLight(p.x+1, p.y, p.z, light, ls);
                propagateBlockLight(p.x-1, p.y, p.z, light, ls);
                propagateBlockLight(p.x, p.y+1, p.z, light, ls);
                propagateBlockLight(p.x, p.y-1, p.z, light, ls);
                propagateBlockLight(p.x, p.y, p.z+1, light, ls);
                propagateBlockLight(p.x, p.y, p.z-1, light, ls);
            }
        }
    }


    private static class Pos implements Comparable<Pos>
    {
        int x, y, z;
        byte light;
        Pos(int x, int y, int z, byte l)
        {
            this.x = x;
            this.y = y;
            this.z = z;
            light = l;
        }

        public int compareTo(Pos o) {
            if(light > o.light) return 1;
            if(light == o.light) return 0;
            if(light < o.light) return -1;
            throw new RuntimeException("Shit");
        }
    }

    @SuppressWarnings("unchecked")
    private void propagateSkyLight(int x, int y, int z, byte light, LinkedList[] ls)
    {
        if(x < 0 || x >= xSize*16) return;
        if(y < 0 || y >= Chunk.MAP_HEIGHT) return;
        if(z < 0 || z >= ySize*16) return;

        byte oldlight = getSkyLight(x, y, z);
		int op = Blocks.getBlockOpacity(getBlock(x, y, z));
		if(op == 0) op = 1;
        light-=op;
        if(oldlight>= light) return;
		
//        System.out.println(oldlight+" -> "+light+"   "+x+", "+y+", "+z);
        setSkyLight(x, y, z, light);
        if(light > 0)
            ls[light].add(new Pos(x, y, z, light));
    }

    @SuppressWarnings("unchecked")
    private void propagateBlockLight(int x, int y, int z, byte light, LinkedList[] ls)
    {
        if(x < 0 || x >= xSize*16) return;
        if(y < 0 || y >= Chunk.MAP_HEIGHT) return;
        if(z < 0 || z >= ySize*16) return;

        byte oldlight = getBlockLight(x, y, z);
		int op = Blocks.getBlockOpacity(getBlock(x, y, z));
		if(op == 0) op = 1;
        light-=op;
        if(oldlight>= light) return;

//        System.out.println(oldlight+" -> "+light+", "+x+", "+y+", "+z);
        setBlockLight(x, y, z, light);
        if(light > 0)
            ls[light].add(new Pos(x, y, z, light));
    }


}
