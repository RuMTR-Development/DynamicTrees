package com.dtteam.dynamictrees.client;

import com.dtteam.dynamictrees.entity.FallingTreeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

//? if >= 1.19.2 {
import net.minecraft.util.RandomSource;
//? }

public class SoundInstanceHandler {

    private static final Map<Integer, SoundInstance> instances = new HashMap<>();

    public static void playSoundInstance (SoundEvent sound, float pitch, Vec3 pos, FallingTreeEntity entity){
        playSoundInstance(sound, SoundSource.NEUTRAL, 1f, pitch, pos, entity);
    }

    public static void playSoundInstance (SoundEvent sound, SoundSource source, float volume, float pitch, Vec3 pos, FallingTreeEntity entity){
        SoundInstance instance = new SimpleSoundInstance(
                sound,
                source,
                volume,
                pitch,

                //? if >= 1.19.2
                RandomSource.create(),

                pos.x,
                pos.y,
                pos.z
        );

        Minecraft.getInstance().getSoundManager().play(instance);
        instances.put(entity.getId(), instance);
    }

    public static void stopSoundInstance (FallingTreeEntity entity){
        if (entity == null || !instances.containsKey(entity.getId())) return;
        SoundInstance instance = instances.get(entity.getId());
        if (instance == null) return;
        Minecraft.getInstance().getSoundManager().stop(instance);
        instances.remove(entity.getId());
        //we also clean up invalid instances
        //~ if < 1.21.1 '.level()' -> '.level'
        instances.keySet().removeIf((id)->entity.level().getEntity(id) == null);
    }

}
