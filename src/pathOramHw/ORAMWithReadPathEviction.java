package pathOramHw;

import java.util.ArrayList;

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
	private int bucket_size;
	private int num_blocks;
	private int[] positionMap;

	private ArrayList<Block> stash = new ArrayList<>();
	
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

	public ArrayList<Integer> P(int x) {
		ArrayList<Integer> path = new ArrayList<>();

		// ID of root bucket is the number of leaves - 1
		path.add(getNumBuckets() - 1);
		int lftCnt = 0;
		int lvl = 0;
		int upperBound = getNumLeaves();
		int lowerBound = 0;

		// Loop until x is in the path
		while (path.get(path.size() - 1) > x) {

			// Determine middle of leaves (to act as new upper or lower bound)
			int mid = ((upperBound - lowerBound) / 2) + lowerBound;
			if (mid > x) {
				// x is on the left of this bucket
				lftCnt++;
				upperBound = mid;
			} else {
				// x is on the right of this bucket
				lowerBound = mid;
			}

			// If the upper and lower bound as next to each other, then x should be one of the leaves
			if (upperBound - lowerBound <= 1 && (upperBound == x || lowerBound == x)) {
				path.add(x);
				break;
			}

			// Use the level and the left count to determine ID of next Bucket
			path.add(path.get(path.size() - 1) - (int)Math.pow(2, lvl) - lftCnt);
			lvl++;
		}
		return path;
	}


	@Override
	public int[] getPositionMap() {
		// TODO Must complete this method for submission
		return null;
	}


	@Override
	public ArrayList<Block> getStash() {
		return this.stash;
	}


	@Override
	public int getStashSize() {
		// TODO Must complete this method for submission
		return 0;
	}

	@Override
	public int getNumLeaves() {
		return (int) Math.pow(2, getNumLevels());
	}


	@Override
	public int getNumLevels() {
		return (int) Math.ceil(Math.log(this.num_blocks) / Math.log(2));
	}


	@Override
	public int getNumBlocks() {
		return this.num_blocks;
	}


	@Override
	public int getNumBuckets() {
		return (int) Math.pow(2, getNumLevels() + 1) - 1;
	}


	
}
