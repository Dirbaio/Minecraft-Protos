package net.dirbaio.protos;

public class BlockType
{
    public byte b;

    public BlockType()
    {
        
    }
    public BlockType(byte b)
    {
        this.b = b;
    }

    public BlockType(int i)
    {
        this.b = (byte)i;
    }
}
