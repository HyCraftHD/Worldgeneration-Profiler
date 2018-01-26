package net.hycrafthd.worldgenerationprofiler;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions("net.hycrafthd.worldgenerationprofiler.asm")
@Name(value = "Worldgeneration Profiler Plugin")
public class WorldGenerationProfilerPlugin implements IFMLLoadingPlugin {
	
	private static Logger logger = LogManager.getLogger("Worldgeneration Profiler Plugin");
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] { "net.hycrafthd.worldgenerationprofiler.asm.transformer.Transformer" };
	}
	
	@Override
	public String getModContainerClass() {
		return "net.hycrafthd.worldgenerationprofiler.Container";
	}
	
	@Override
	public String getSetupClass() {
		return "net.hycrafthd.worldgenerationprofiler.Config";
	}
	
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
	}
	
	public static Logger getLogger() {
		return logger;
	}
	
}
