package net.hycrafthd.worldgenerationprofiler.asm.transformer;

import java.io.*;
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
				InputStream input = (getClass().getClassLoader().getResourceAsStream(version.getFileName()));
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				
				final byte[] buffer = new byte[8024];
				int n = 0;
				long count = 0;
				while (-1 != (n = input.read(buffer))) {
					output.write(buffer, 0, n);
					count += n;
				}
				
				bytes = output.toByteArray();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return bytes;
		}
		return helper.transform(bytes, transformedName);
	}
	
}
