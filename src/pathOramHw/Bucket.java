package pathOramHw;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

/*
 * Name: Martijn de Vries, Dennis Cai
 * StudentID: s1549405, s1592041
 */

public class Bucket {
	private static boolean is_init = false;
	private static int max_size_Z = -1;

	private ArrayList<Block> blocks;
	private int realSize;
	
	public Bucket() {
		if (!is_init) {
			throw new RuntimeException("Please set bucket size before creating a bucket");
		}

		// Fill bucket with dummy blocks
		this.blocks = new ArrayList<>();
		for (int i = 0; i < max_size_Z; i++) {
			this.blocks.add(new Block());
		}

		// real size is initially 0 (bucket contains only dummy blocks)
		this.realSize = 0;
	}

	public Bucket(ArrayList<Block> blocks) {
		// Create bucket from list of blocks (no dummy blocks)
		this.blocks = blocks;
		this.realSize = blocks.size();
	}
	
	// Copy constructor
	public Bucket(Bucket other) {
		if (other == null) {
			throw new RuntimeException("the other bucket is not malloced.");
		}
		this.blocks = other.blocks;
		this.realSize = other.realSize;
	}
	
	//Implement and add your own methods.
	public Block getBlockByKey(int key) {
		for (Block b : this.blocks) {
			if (b.index == key) {
				return b;
			}
		}
		return null;
	}

	public void addBlock(Block new_blk) {
		if (this.blocks.size() < max_size_Z) {
			// add block to bucket
			this.blocks.add(new_blk);

			// Increase real size if block is not a dummy block
			if (new_blk.index > -1) {
				this.realSize++;
			}
		} else {
			// Bucket full
			throw new RuntimeException("Bucket is already at max size 'Z'");
		}
	}

	public boolean removeBlock(Block rm_blk) {
		// remove block with same index as rm_blck
		Iterator<Block> iter = this.blocks.iterator();
		while (iter.hasNext()) {
			Block b = iter.next();
			if (b.index == rm_blk.index) {
				// Decrease real size if block was not a dummy block
				if (b.index > -1) {
					this.realSize--;
				}

				// Remove block from bucket
				iter.remove();
				return true;
			}
		}
		return false;
	}

	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}

	public void setBlocks(ArrayList<Block> new_blocks) {
		this.blocks = new_blocks;
	}

	public int getRealSize() {
		return this.realSize;
	}

	public void setRealSize(int new_real_size) {
		this.realSize = new_real_size;
	}

	public static void resetState() {
		is_init = false;
	}

	public static void setMaxSize(int maximumSize) {
		if (is_init) {
			throw new RuntimeException("Max Bucket Size was already set");
		}
		max_size_Z = maximumSize;
		is_init = true;
	}

}