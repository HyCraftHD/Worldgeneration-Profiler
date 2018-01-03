package net.hycrafthd.worldgenerationprofiler.asm.visitor;

import net.hycrafthd.worldgenerationprofiler.WorldGenerationProfilerPlugin;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public enum GameVersion {
	
	V1_8("1.8.byte", "1.8-obf.byte", "(Ljava/util/List;IILnet/minecraft/world/World;Lnet/minecraft/world/chunk/IChunkProvider;Lnet/minecraft/world/chunk/IChunkProvider;)V"),
	V1_9_1_11("1.9-1.11.byte", "1.9-1.11-obf.byte", "(Ljava/util/List;IILnet/minecraft/world/World;Lnet/minecraft/world/chunk/IChunkGenerator;Lnet/minecraft/world/chunk/IChunkProvider;)V"),
	V1_12("1.12.byte", "1.12-obf.byte", "(Ljava/util/List;IILnet/minecraft/world/World;Lnet/minecraft/world/gen/IChunkGenerator;Lnet/minecraft/world/chunk/IChunkProvider;)V"),
	UNSUPPORTED("", "", "");
	
	private String file, fileobf, descriptor;
	
	private Boolean obf = !(Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	
	private GameVersion(String file, String fileobf, String descriptor) {
		this.file = file;
		this.fileobf = fileobf;
		this.descriptor = descriptor;
	}
	
	public String getFile() {
		return file;
	}
	
	public String getFileObf() {
		return fileobf;
	}
	
	public String getFileName() {
		String filename = obf ? fileobf : file;
		WorldGenerationProfilerPlugin.getLogger().info("Using " + filename + " for class.");
		return filename;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public static GameVersion getCurrent() {
		String version = ObfuscationReflectionHelper.getPrivateValue(ForgeVersion.class, null, "mcVersion");
		if (version.startsWith("1.8")) {
			return V1_8;
		} else if (version.startsWith("1.9") || version.startsWith("1.10") || version.startsWith("1.11")) {
			return V1_9_1_11;
		} else if (version.startsWith("1.12")) {
			return V1_12;
		}
		
		return UNSUPPORTED;
	}
	
}
