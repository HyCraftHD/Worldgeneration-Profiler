package net.hycrafthd.worldgenerationprofiler.asm;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;

import java.io.*;
import java.util.*;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

import net.hycrafthd.worldgenerationprofiler.WorldGenerationProfilerPlugin;

public class VisitorHelper {
	
	private Map<String, MinecraftClassVisitor> visitors;
	
	public VisitorHelper(HashMap<String, MinecraftClassVisitor> visitor) {
		this.visitors = visitor;
	}
	
	public byte[] transform(byte[] bytes, String name) {
		if (visitors.containsKey(name)) {
			WorldGenerationProfilerPlugin.getLogger().info("Trying to patch class " + name);
			
			ClassNode node = new ClassNode();
			ClassReader reader = new ClassReader(bytes);
			
			reader.accept(node, EXPAND_FRAMES);
			
			MinecraftClassVisitor visitor = visitors.get(name);
			try {
				visitor.transform(node);
			} catch (Throwable th) {
				WorldGenerationProfilerPlugin.getLogger().error("Error while trying to patch class " + name, th);
			}
			
			ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES);
			node.accept(writer);
			
			WorldGenerationProfilerPlugin.getLogger().info("Patched class successfully " + name);
			
			// ------------------
			// ONLY DEBUG
			try {
				FileOutputStream stream = new FileOutputStream(new File("GameRegistry.class"));
				stream.write(writer.toByteArray());
				stream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// ------------------
			
			return writer.toByteArray();
		}
		return bytes;
	}
	
}
