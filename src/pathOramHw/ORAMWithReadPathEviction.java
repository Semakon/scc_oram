package pathOramHw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

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

		// Set storage capacity
		this.storage.setCapacity(getNumBuckets());

		// populate position map with random variables
		rand_gen.setBound(getNumLeaves());
		for (int i = 0; i < num_blocks; i++) {
			this.positionMap[i] = rand_gen.getRandomLeaf();
		}

		// populate storage with buckets
		for (int i = 0; i < getNumBuckets(); i++) {
			for (int j = 0; j < bucket_size; j++) {
				this.storage.WriteBucket(i, new Bucket());
			}
		}

		// debug
		System.out.println("Number of Blocks:\t" + getNumBlocks());
		System.out.println("Number of Buckets:\t" + getNumBuckets());
		System.out.println("Number of Leaves:\t" + getNumLeaves());
		System.out.println("Number of Levels:\t" + getNumLevels());

		System.out.println(Arrays.toString(getPositionMap()));
		System.out.println();
	}

	@Override
	public byte[] access(Operation op, int a, byte[] newdata) {
		// Remap block
		int x = positionMap[a];
		positionMap[a] = rand_gen.getRandomLeaf();

		// Read path
		for (int l = 0; l <= getNumLevels(); l++) {
			// Get bucket from path at level l
			Bucket bucket = this.storage.ReadBucket(P(x, l));
			for (int i = 0; i < bucket.getBlocks().size(); i++) {
				Block b = bucket.getBlocks().get(i);
				if (b.index > -1) {
					// Not a dummy block, so add to stash
					this.stash.add(b);
				}
				// Remove block from bucket
				if (!bucket.removeBlock(b)) {
					throw new RuntimeException("Could not remove block '"
							+ b.index + "' from bucket");
				}
			}
		}

		// Update block
		Block block = null;
		for (Block b : this.stash) {
			if (b.index == a) {
				// block with index a is in the stash
				block = b;
				break;
			}
		}

		// Initialize data to 0
		byte[] data = new byte[32];
		if (block != null) {
			// get data from block a if it is in the stash
			data = block.data;
		}

		// When performing a write
		if (op == Operation.WRITE) {
			if (block != null) {
				// if block a was in the stash, set its data to newdata
				block.data = newdata;
			} else {
				// if block a was not in the stash, create it with newdata as its data
				// and add to the stash (at beginning)
				block = new Block(a, newdata);
				this.stash.add(0, block);
			}
		}

		// Write path
		for (int l = getNumLevels(); l >= 0; l--) {
			// Find all blocks in the stash that cross paths with x at l
			ArrayList<Block> stash_prime = new ArrayList<>();
			for (Block b : this.stash) {
				if (P(x, l) == P(positionMap[b.index], l)) {
					stash_prime.add(b);
				}
				if (stash_prime.size() >= bucket_size) {
					// bucket full, stop adding blocks
					break;
				}
			}
			// Remove the blocks of S' from S
			for (Block b : stash_prime) {
				this.stash.remove(b);
			}
			// Write bucket to storage
			this.storage.WriteBucket(P(x, l), new Bucket(stash_prime));
		}
		return data;
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

//	public ArrayList<Integer> P(int x) {
//		ArrayList<Integer> path = new ArrayList<>();
//
//		int numBuckets = getNumBuckets();
//		int parent = getNumLeaves();
//		int currentNode = x;
//		int[] parentIds = new int[numBuckets - 1];
//
//		// Initialize the array of parent ids
//		for (int i = 0; i < numBuckets - 2; i += 2) {
//			// Set the parent of the two child nodes
//			parentIds[i] = parent;
//			parentIds[i + 1] = parent;
//
//			parent++;
//		}
//
//		// Add the leaf node as the first entry to the path
//		path.add(currentNode);
//
//		// Loop through the parentIds array to iteratively add the parents of the nodes to the path
//		while (currentNode < numBuckets - 1) {
//			int currentParent = parentIds[currentNode];
//			path.add(currentParent);
//
//			currentNode = currentParent;
//		}
//		return path;
//	}

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
