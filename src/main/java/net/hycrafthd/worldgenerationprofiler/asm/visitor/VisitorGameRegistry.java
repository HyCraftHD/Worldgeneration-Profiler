package net.hycrafthd.worldgenerationprofiler.asm.visitor;

import java.util.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import net.hycrafthd.worldgenerationprofiler.WorldGenerationProfilerPlugin;
import net.hycrafthd.worldgenerationprofiler.asm.MinecraftClassVisitor;
import net.hycrafthd.worldgenerationprofiler.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.*;
import net.minecraftforge.fml.common.IWorldGenerator;

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
		
		iterator.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/fml/common/registry/GameRegistry", "sortedGeneratorList", "Ljava/util/List;"));
		iterator.add(new VarInsnNode(ILOAD, 0));
		iterator.add(new VarInsnNode(ILOAD, 1));
		iterator.add(new VarInsnNode(ALOAD, 2));
		iterator.add(new VarInsnNode(ALOAD, 3));
		iterator.add(new VarInsnNode(ALOAD, 4));
		iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/hycrafthd/worldgenerationprofiler/asm/visitor/VisitorGameRegistry", "generate", "(Ljava/util/List;IILnet/minecraft/world/World;Lnet/minecraft/world/chunk/IChunkGenerator;Lnet/minecraft/world/chunk/IChunkProvider;)V", false));
		
		iterator.next();
	}
	
	public static void generate(List<IWorldGenerator> sortedGeneratorList, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		long worldSeed = world.getSeed();
		Random fmlRandom = new Random(worldSeed);
		long xSeed = fmlRandom.nextLong() >> 2 + 1L;
		long zSeed = fmlRandom.nextLong() >> 2 + 1L;
		long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
		
		Profiler.startChunk(world, chunkX, chunkZ);
		for (IWorldGenerator generator : sortedGeneratorList) {
			fmlRandom.setSeed(chunkSeed);
			
			Profiler.startChunkMod(generator, world, chunkX, chunkZ);
			generator.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
			Profiler.endChunkMod(generator, world, chunkX, chunkZ);
		}
		Profiler.endChunk(world, chunkX, chunkZ);
		
	}
}
