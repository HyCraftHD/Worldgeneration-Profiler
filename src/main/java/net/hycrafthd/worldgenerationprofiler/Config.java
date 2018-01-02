package net.hycrafthd.worldgenerationprofiler;

import java.io.File;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

public class Config implements IFMLCallHook {
	
	public static boolean onlydisplayrelevant;
	public static int warn_whole, warn_mod;
	
	private Configuration config;
	
	@Override
	public Void call() throws Exception {
		config = new Configuration(new File("config/worldgeneration_profiler.cfg"));
		config.load();
		
		onlydisplayrelevant = config.getBoolean("DisplayRelevant", "Logging", true, "Only display warn messages in console (others might be logged, because they will be sent through debug level)");
		
		warn_whole = config.getInt("WarnWhole", "Warn", 50, 1, 10000, "Time in ms to warn user (whole generation per chunk)");
		warn_mod = config.getInt("WarnMod", "Warn", 20, 1, 10000, "Time in ms to warn user (only mod specific generation per chunk)");
		
		config.save();
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
	}
	
}
