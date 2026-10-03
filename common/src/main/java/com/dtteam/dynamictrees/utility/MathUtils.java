package com.dtteam.dynamictrees.utility;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? }

import java.util.Random; /**
 * Just a few math helper functions.
 *
 * @author ferreusveritas
 */
public class MathUtils {

    /**
     * Selects a random direction weighted from the given probability map.
     *
     * @param random  An instance of RandomSource/Random.
     * @param distMap The probability map.
     * @return A random direction.
     */
    //~ if < 1.19.2 'RandomSource random' -> 'Random random'
    public static int selectRandomFromDistribution(final RandomSource random, final int[] distMap) {
        int distSize = 0;

        for (int j : distMap) {
            distSize += j;
        }

        if (distSize <= 0) {
            //System.err.println("Warning: Zero sized distribution");
            return -1;
        }

        int rnd = random.nextInt(distSize) + 1;

        for (int i = 0; i < 6; i++) {
            if (rnd > distMap[i]) {
                rnd -= distMap[i];
            } else {
                return i;
            }
        }

        return 0;
    }

    public static float shortDegreesDist(float ang1, float ang2) {
        final float max = 360.0f;
        final float da = (ang2 - ang1) % max;
        return 2 * da % max - da;
    }

    public static float angleDegreesInterpolate(float ang1, float ang2, float t) {
        return ang1 + shortDegreesDist(ang1, ang2) * t;
    }

    //~ if < 1.19.2 'RandomSource' -> 'Random'
    public static int randomBetween(RandomSource random, int min, int max) {
        if (min == max) {
            return min;
        }
        return min + random.nextInt(1 + max - min);
    }

}