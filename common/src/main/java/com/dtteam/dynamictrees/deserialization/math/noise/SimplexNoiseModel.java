package com.dtteam.dynamictrees.deserialization.math.noise;

import net.minecraft.world.level.levelgen.synth.SimplexNoise;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? } else {
/*import net.minecraft.world.level.levelgen.RandomSource;
*///? }

public class SimplexNoiseModel implements NoiseModel {
	
	private final SimplexNoise noise;
	
	public SimplexNoiseModel(RandomSource randomSource) {
		this.noise = new SimplexNoise(randomSource);
	}
	
	@Override
	public double sample(double x, double y, double z) {
		return noise.getValue(x, y, z);
	}

}
