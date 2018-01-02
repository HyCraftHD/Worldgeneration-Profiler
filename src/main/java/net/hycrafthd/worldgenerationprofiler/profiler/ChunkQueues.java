package net.hycrafthd.worldgenerationprofiler.profiler;

public class ChunkQueues {
	
	protected int chunkX, chunkZ;
	
	public ChunkQueues(int chunkX, int chunkZ) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChunkQueues)) {
			return false;
		}
		ChunkQueues other = (ChunkQueues) obj;
		return other.chunkX == this.chunkX && other.chunkZ == this.chunkZ;
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
		hash *= 31 + this.chunkX;
		hash *= 31 + this.chunkZ;
		return hash;
	}
	
}
