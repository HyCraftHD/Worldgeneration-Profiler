package net.hycrafthd.worldgenerationprofiler.asm.transformer;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.compress.utils.IOUtils;

import net.hycrafthd.worldgenerationprofiler.asm.*;
import net.hycrafthd.worldgenerationprofiler.asm.visitor.*;
import net.minecraft.launchwrapper.IClassTransformer;

public class Transformer implements IClassTransformer {
	
	private HashMap<String, MinecraftClassVisitor> visitors;
	private VisitorHelper helper;
	
	public Transformer() {
		visitors = new HashMap<>();
		registerNodes();
		helper = new VisitorHelper(visitors);
	}
	
	private void registerNodes() {
		visitors.put("net.minecraftforge.fml.common.registry.GameRegistry", new VisitorGameRegistry());
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if (name.equals("net.hycrafthd.worldgenerationprofiler.generation.CustomWorldGenerator")) {
			GameVersion version = GameVersion.getCurrent();
			try {
				bytes = IOUtils.toByteArray(getClass().getResourceAsStream(version.getFileName()));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return bytes;
		}
		return helper.transform(bytes, transformedName);
	}
	
}
