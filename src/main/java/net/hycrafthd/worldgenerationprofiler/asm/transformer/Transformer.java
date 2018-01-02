package net.hycrafthd.worldgenerationprofiler.asm.transformer;

import java.util.HashMap;

import net.hycrafthd.worldgenerationprofiler.asm.*;
import net.hycrafthd.worldgenerationprofiler.asm.visitor.VisitorGameRegistry;
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
		return helper.transform(bytes, transformedName);
	}

}
