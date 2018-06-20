package net.hycrafthd.worldgenerationprofiler.asm.visitor;

import java.util.ListIterator;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import net.hycrafthd.worldgenerationprofiler.WorldGenerationProfilerPlugin;
import net.hycrafthd.worldgenerationprofiler.asm.MinecraftClassVisitor;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class VisitorGameRegistry extends MinecraftClassVisitor {
	
	@Override
	public void transform(ClassNode node) throws Throwable {
		
		MethodNode methodnode = null;
		for (MethodNode method : node.methods) {
			if (method.name.equals("generateWorld")) {
				methodnode = method;
				break;
			}
		}
		if (methodnode == null) {
			WorldGenerationProfilerPlugin.getLogger().error("Method generateWorld in GameRegistry was not found... This should not happen!?");
			return;
		}
		
		InsnList instructions = methodnode.instructions;
		ListIterator<AbstractInsnNode> iterator = instructions.iterator();
		
		int i = 0;
		boolean remove = true;
		
		while (iterator.hasNext()) {
			AbstractInsnNode insnode = iterator.next();
			if (i > 7 && remove) {
				if (insnode.getOpcode() == RETURN) {
					
					insert(iterator);
					
					remove = false;
					continue;
				}
				iterator.remove();
			}
			i++;
		}
	}
	
	private void insert(ListIterator<AbstractInsnNode> iterator) {
		iterator.previous();
		
		GameVersion version = GameVersion.getCurrent();
		
		if (version == GameVersion.UNSUPPORTED) {
			WorldGenerationProfilerPlugin.getLogger().error("Unsupported version.");
			FMLCommonHandler.instance().exitJava(0, true);
			return;
		}
		
		try {
			LaunchClassLoader loader = (LaunchClassLoader) this.getClass().getClassLoader();
			Class<?> clazz = loader.findClass("net.hycrafthd.worldgenerationprofiler.generation.CustomWorldGenerator");
			WorldGenerationProfilerPlugin.getLogger().info("CustomWorldGenerator loaded in vm successfully for " + version + " (" + clazz.getName() + ")");
		} catch (Exception ex) {
			WorldGenerationProfilerPlugin.getLogger().error("CustomWorldGenerator class could not be loaded to classloader.", ex);
			FMLCommonHandler.instance().exitJava(0, true);
		}
		
		iterator.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/fml/common/registry/GameRegistry", "sortedGeneratorList", "Ljava/util/List;"));
		iterator.add(new VarInsnNode(ILOAD, 0));
		iterator.add(new VarInsnNode(ILOAD, 1));
		iterator.add(new VarInsnNode(ALOAD, 2));
		iterator.add(new VarInsnNode(ALOAD, 3));
		iterator.add(new VarInsnNode(ALOAD, 4));
		iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/hycrafthd/worldgenerationprofiler/generation/CustomWorldGenerator", "generate", version.getDescriptor(), false));
		
		iterator.next();
	}
	
}
