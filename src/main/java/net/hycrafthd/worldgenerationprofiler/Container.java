package net.hycrafthd.worldgenerationprofiler;

import java.net.*;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.*;

public class Container extends DummyModContainer {
	
	public Container() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "worldgenerationprofiler";
		meta.name = "World Generation Profiler";
		meta.version = "@VERSION@";
		meta.url = "https://www.curseforge.com/minecraft/mc-mods/worldgeneration-profiler";
		meta.authorList.add("HyCraftHD");
		meta.description = "Mod which profiles your worldgeneration";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true;
	}
	
	@Override
	public URL getUpdateUrl() {
		try {
			return new URL("https://api.u-team.info/update/worldgenerationprofiler.json");
		} catch (MalformedURLException ex) {
			return null;
		}
	}
	
}
