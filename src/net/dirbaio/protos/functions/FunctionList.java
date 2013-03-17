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

package net.dirbaio.protos.functions;

import java.util.ArrayList;
import java.util.List;
import net.dirbaio.protos.functions.*;

public class FunctionList
{
    public static final List<Class> functions;
    
    static
    {
        functions = new ArrayList<>();
        functions.add(Add2D.class);
        functions.add(Add3D.class);
        functions.add(BeachOverlay.class);
        functions.add(BedrockLayer.class);
        functions.add(Clamp2D.class);
        functions.add(Clamp3D.class);
        functions.add(Constant.class);
        functions.add(ConstantTerrain.class);
        functions.add(Divide2D.class);
        functions.add(Divide3D.class);
        functions.add(GetYCoord.class);
        functions.add(HeightmapErosion.class);
        functions.add(Image2D.class);
        functions.add(Interpolate2D.class);
        functions.add(Interpolate3D.class);
        functions.add(Limiter3D.class);
        functions.add(Max2D.class);
        functions.add(Max3D.class);
        functions.add(Min2D.class);
        functions.add(Min3D.class);
        functions.add(Multiply2D.class);
        functions.add(Multiply3D.class);
        functions.add(Sea.class);
        functions.add(SimplexNoise2D.class);
        functions.add(SimplexNoise3D.class);
        functions.add(Substract2D.class);
        functions.add(Substract3D.class);
        functions.add(TerrainCut.class);
        functions.add(TerrainHeightmap.class);
        functions.add(TerrainJoin.class);
        functions.add(TerrainOverlay.class);
        functions.add(TerrainVolume.class);
        functions.add(VoronoiNoise2D.class);
        
    }
}
