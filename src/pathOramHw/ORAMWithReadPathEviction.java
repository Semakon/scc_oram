package pathOramHw;

import java.util.ArrayList;
import java.util.Collections;

/*
 * Name: Martijn de Vries, Dennis Cai
 * StudentID: s1549405, s1592041
 */

public class ORAMWithReadPathEviction implements ORAMInterface {

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

		// populate position map with random variables
		rand_gen.setBound(getNumLeaves());
		for (int i = 0; i < num_blocks; i++) {
			this.positionMap[i] = rand_gen.getRandomLeaf();
		}
	}

	@Override
	public byte[] access(Operation op, int blockIndex, byte[] newdata) {

		return null;
	}


	@Override
	public int P(int leaf, int level) {
		ArrayList<Integer> path = P(leaf);

		// reverse path to start from root (level 0)
		Collections.reverse(path);
		return path.get(level);
	}

	/**
	 * Creates a path from the root Bucket to the given leaf <code>x</code>. This methods uses the ID of the Buckets
	 * and assumes the leftmost leaf has ID 0, going from left to right, bottom to top (root Bucket has ID
	 * <code>num_buckets</code> - 1)
	 * @param x ID of leaf node at the end of the path
	 * @return A list of Bucket IDs from the root to <code>x</code>
	 */
	public ArrayList<Integer> P(int x) {
		ArrayList<Integer> path = new ArrayList<>();

		// ID of root bucket is the number of buckets - 1
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

			// If the upper and lower bound are next to each other, then x should be one of the leaves
			if (upperBound - lowerBound <= 1 && (upperBound == x || lowerBound == x)) {
				path.add(x);
				break;
			}

			// Use the level and the left count to determine ID of next Bucket
			path.add(path.get(path.size() - 1) - (int)Math.pow(2, lvl) - lftCnt);
			lvl++;
		}
		// Reverse path so it goes from x to root
		Collections.reverse(path);
		return path;
	}


	@Override
	public int[] getPositionMap() {
		return this.positionMap;
	}


	@Override
	public ArrayList<Block> getStash() {
		return this.stash;
	}


	@Override
	public int getStashSize() {
		return this.stash.size();
	}

	@Override
	public int getNumLeaves() {
		// no leaves = 2^L
		return (int) Math.pow(2, getNumLevels());
	}


	@Override
	public int getNumLevels() {
		// L = log_2(N) (rounded up)
		return (int) Math.ceil(Math.log(this.num_blocks) / Math.log(2));
	}


	@Override
	public int getNumBlocks() {
		// N
		return this.num_blocks;
	}


	@Override
	public int getNumBuckets() {
		// no buckets = 2^(L+1) - 1
		return (int) Math.pow(2, getNumLevels() + 1) - 1;
	}


	
}
