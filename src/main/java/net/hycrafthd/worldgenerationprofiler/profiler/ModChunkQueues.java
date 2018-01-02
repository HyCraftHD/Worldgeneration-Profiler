package net.hycrafthd.worldgenerationprofiler.profiler;

public class ModChunkQueues extends ChunkQueues {
	
	protected String classname;
	
	public ModChunkQueues(String classname, int chunkX, int chunkZ) {
		super(chunkX, chunkZ);
		this.classname = classname;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ModChunkQueues)) {
			return false;
		}
		ModChunkQueues other = (ModChunkQueues) obj;
		return other.classname == this.classname && other.chunkX == this.chunkX && other.chunkZ == this.chunkZ;
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		hash *= 31 + classname.hashCode();
		hash *= 31 + this.chunkX;
		hash *= 31 + this.chunkZ;
		return hash;
	}
	
}
