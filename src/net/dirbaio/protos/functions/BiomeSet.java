package net.dirbaio.protos.functions;

import java.util.ArrayList;

/**
 * Represents a biome set, to which a biome can or can't belong. It contains an
 * {@code invert} variable so it's easy to construct sets such as "All but this
 * biome".
 * 
 * @author dirbaio
 */
public class BiomeSet
{
    public final ArrayList<Integer> biomes = new ArrayList<>();
    public boolean invert;

    /**
     * Creates a BiomeSet that contains no biomes.
     */
    public BiomeSet()
    {
    }

    /**
     * Creates a BiomeSet that contains none or all biomes depending on the
     * {@code invert} parameter.
     * @param invert True if set contains all biomes, false otherwise.
     */
    public BiomeSet(boolean invert)
    {
        this.invert = invert;
    }

    /**
     * Creates a BiomeSet that contains the biome or all but the biome
     * @param biome
     * @param invert 
     */
    public BiomeSet(int biome, boolean invert)
    {
        biomes.add(biome);
        this.invert = invert;
    }
    
    /**
     * Creates a BiomeSet that contains the specified biome
     * @param biome
     */
    public BiomeSet(int biome)
    {
        biomes.add(biome);
        this.invert = false;
    }
    
    /**
     * Checks if the set contains the biome.
     * @param biome the biome
     * @return whether the set contains the biome.
     */
    public boolean contains(int biome)
    {
        //TODO optimize
        return invert != biomes.contains(biome);
    }
}
