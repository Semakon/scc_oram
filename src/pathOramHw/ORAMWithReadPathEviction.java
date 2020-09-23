package pathOramHw;

import java.util.ArrayList;
import java.util.List;

/*
 * Name: Martijn de Vries, Dennis Cai
 * StudentID: s1549405, s1592041
 */

public class ORAMWithReadPathEviction implements ORAMInterface {
	
	/**
	 * TODO add necessary variables
	 */
	private UntrustedStorageInterface storage;
	private RandForORAMInterface rand_gen;
	int bucket_size;
	int num_blocks;
	int[] positionMap;

	List<Block> stash = new ArrayList<>();
	
	public ORAMWithReadPathEviction(UntrustedStorageInterface storage,
									RandForORAMInterface rand_gen, int bucket_size, int num_blocks) {
		this.storage = storage;
		this.rand_gen = rand_gen;
		this.bucket_size = bucket_size;
		this.num_blocks = num_blocks;
		this.positionMap = new int[num_blocks];
	}

	@Override
	public byte[] access(Operation op, int blockIndex, byte[] newdata) {
		// TODO Must complete this method for submission
		return null;
	}


	@Override
	public int P(int leaf, int level) {

		return 0;
	}


	@Override
	public int[] getPositionMap() {
		// TODO Must complete this method for submission
		return null;
	}


	@Override
	public ArrayList<Block> getStash() {
		// TODO Must complete this method for submission
		return null;
	}


	@Override
	public int getStashSize() {
		// TODO Must complete this method for submission
		return 0;
	}

	@Override
	public int getNumLeaves() {
		// TODO Must complete this method for submission
		return 0;
	}


	@Override
	public int getNumLevels() {
		// TODO Must complete this method for submission
		return 0;
	}


	@Override
	public int getNumBlocks() {
		// TODO Must complete this method for submission
		return 0;
	}


	@Override
	public int getNumBuckets() {
		// TODO Must complete this method for submission
		return 0;
	}


	
}
