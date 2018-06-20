package net.hycrafthd.worldgenerationprofiler.profiler;

import java.util.HashMap;

import org.apache.logging.log4j.Level;

import net.hycrafthd.worldgenerationprofiler.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IWorldGenerator;

public class Profiler {
	
	private static HashMap<World, HashMap<ChunkQueues, Long>> chunks = new HashMap<>();
	
	public static void startChunk(World world, int chunkX, int chunkZ) {
		chunks.putIfAbsent(world, new HashMap<>());
		chunks.get(world).put(new ChunkQueues(chunkX, chunkZ), System.currentTimeMillis());
		WorldGenerationProfilerPlugin.getLogger().log(Config.onlydisplayrelevant ? Level.DEBUG : Level.INFO, "[General] Started Generation for Chunk " + chunkX + " - " + chunkZ);
	}
	
	public static void endChunk(World world, int chunkX, int chunkZ) {
		if (chunks.get(world) == null) {
			return;
		}
		long time = chunks.get(world).remove(new ChunkQueues(chunkX, chunkZ));
		time = System.currentTimeMillis() - time;
		WorldGenerationProfilerPlugin.getLogger().log(Config.onlydisplayrelevant ? Level.DEBUG : Level.INFO, "[General] Finished Generation for Chunk " + chunkX + " - " + chunkZ + " -> Took about " + time + "ms");
		if (time > Config.warn_whole) {
			WorldGenerationProfilerPlugin.getLogger().warn("[General] Chunk (" + chunkX + " - " + chunkZ + ") at (" + chunkX * 16 + " - " + chunkZ * 16 + ")" + " took too long to generate (" + time + "ms)");
		}
	}
	
	private static HashMap<World, HashMap<ModChunkQueues, Long>> modchunks = new HashMap<>();
	
	public static void startChunkMod(IWorldGenerator generator, World world, int chunkX, int chunkZ) {
		String classname = generator.getClass().getName();
		modchunks.putIfAbsent(world, new HashMap<>());
		modchunks.get(world).put(new ModChunkQueues(classname, chunkX, chunkZ), System.currentTimeMillis());
		WorldGenerationProfilerPlugin.getLogger().log(Config.onlydisplayrelevant ? Level.DEBUG : Level.INFO, "[" + classname + "] Started Generation for Chunk " + chunkX + " - " + chunkZ);
	}
	
	public static void endChunkMod(IWorldGenerator generator, World world, int chunkX, int chunkZ) {
		String classname = generator.getClass().getName();
		if (modchunks.get(world) == null) {
			return;
		}
		long time = modchunks.get(world).remove(new ModChunkQueues(classname, chunkX, chunkZ));
		time = System.currentTimeMillis() - time;
		WorldGenerationProfilerPlugin.getLogger().log(Config.onlydisplayrelevant ? Level.DEBUG : Level.INFO, "[" + classname + "] Finished Generation for Chunk " + chunkX + " - " + chunkZ + " -> Took about " + time + "ms");
		if (time > Config.warn_mod) {
			WorldGenerationProfilerPlugin.getLogger().warn("[" + classname + "] Chunk (" + chunkX + " - " + chunkZ + ") at (" + chunkX * 16 + " - " + chunkZ * 16 + ")" + " took too long to generate (" + time + "ms)");
		}
	}
}
