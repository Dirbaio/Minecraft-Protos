package net.dirbaio.protos.functions;

import java.awt.Color;

public class Biome
{
    public static final Biome[] biomeList = new Biome[256];

    public static final Biome ocean = (new Biome(0)).setColor(new Color(112)).setBiomeName("Ocean");
    public static final Biome plains = (new Biome(1)).setColor(new Color(9286496)).setBiomeName("Plains");
    public static final Biome desert = (new Biome(2)).setColor(new Color(16421912)).setBiomeName("Desert");
    public static final Biome extremeHills = (new Biome(3)).setColor(new Color(6316128)).setBiomeName("Extreme Hills");
    public static final Biome forest = (new Biome(4)).setColor(new Color(353825)).setBiomeName("Forest");
    public static final Biome taiga = (new Biome(5)).setColor(new Color(747097)).setBiomeName("Taiga");
    public static final Biome swampland = (new Biome(6)).setColor(new Color(522674)).setBiomeName("Swampland");
    public static final Biome river = (new Biome(7)).setColor(new Color(255)).setBiomeName("River");
    public static final Biome frozenOcean = (new Biome(10)).setColor(new Color(9474208)).setBiomeName("FrozenOcean");
    public static final Biome frozenRiver = (new Biome(11)).setColor(new Color(10526975)).setBiomeName("FrozenRiver");
    public static final Biome icePlains = (new Biome(12)).setColor(new Color(16777215)).setBiomeName("Ice Plains");
    public static final Biome iceMountains = (new Biome(13)).setColor(new Color(10526880)).setBiomeName("Ice Mountains");
    public static final Biome mushroomIsland = (new Biome(14)).setColor(new Color(16711935)).setBiomeName("MushroomIsland");
    public static final Biome mushroomIslandShore = (new Biome(15)).setColor(new Color(10486015)).setBiomeName("MushroomIslandShore");
    public static final Biome beach = (new Biome(16)).setColor(new Color(16440917)).setBiomeName("Beach");
    public static final Biome desertHills = (new Biome(17)).setColor(new Color(13786898)).setBiomeName("DesertHills");
    public static final Biome forestHills = (new Biome(18)).setColor(new Color(2250012)).setBiomeName("ForestHills");
    public static final Biome taigaHills = (new Biome(19)).setColor(new Color(1456435)).setBiomeName("TaigaHills");
    public static final Biome extremeHillsEdge = (new Biome(20)).setColor(new Color(7501978)).setBiomeName("Extreme Hills Edge");
    public static final Biome jungle = (new Biome(21)).setColor(new Color(5470985)).setBiomeName("Jungle");
    public static final Biome jungleHills = (new Biome(22)).setColor(new Color(2900485)).setBiomeName("JungleHills");

    public static final Biome hell = (new Biome(8)).setColor(new Color(16711680)).setBiomeName("Hell");
    public static final Biome sky = (new Biome(9)).setColor(new Color(8421631)).setBiomeName("Sky");

    public static final Biome undefined = (new Biome(255)).setColor(new Color(0)).setBiomeName("Undefined Biome");

    public String name;
    public Color color;

    public float temperature;
    public float rainfall;
    public final int biomeID;

    protected Biome(int id)
    {
        
        this.temperature = 0.5F;
        this.rainfall = 0.5F;
        this.biomeID = id;
        biomeList[id] = this;
    }
        
    protected Biome setBiomeName(String par1Str)
    {
        this.name = par1Str;
        return this;
    }
        
    protected Biome setColor(Color par1)
    {
        this.color = par1;
        return this;
    }
}
