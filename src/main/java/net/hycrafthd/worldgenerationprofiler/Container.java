package net.hycrafthd.worldgenerationprofiler;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.*;

public class Container extends DummyModContainer {
	
	public Container() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "worldgenerationprofiler";
		meta.name = "World Generation Profiler";
		meta.version = "@VERSION@";
		meta.description = "https://www.curseforge.com/minecraft/mc-mods/worldgeneration-profiler";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
	
}
