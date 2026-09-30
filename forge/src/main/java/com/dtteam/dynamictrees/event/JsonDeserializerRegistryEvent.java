package com.dtteam.dynamictrees.event;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * This event is posted for add-ons to register custom Json object getters at the right time.
 */
public final class JsonDeserializerRegistryEvent extends Event implements IModBusEvent { }