package pathOramHw;

import java.util.ArrayList;

/*
 * Name: Martijn de Vries, Dennis Cai
 * StudentID: s1549405, s1592041
 */

public class Bucket {
	private static boolean is_init = false;
	private static int max_size_Z = -1;

	private int id;
	private ArrayList<Block> blocks;
	private int realSize;
	
	public Bucket(int id) {
		if (!is_init) {
			throw new RuntimeException("Please set bucket size before creating a bucket");
		}
		this.id = id;
		this.blocks = new ArrayList<>();
		this.realSize = 0;
	}
	
	// Copy constructor
	public Bucket(Bucket other) {
		if (other == null) {
			throw new RuntimeException("the other bucket is not malloced.");
		}
		this.id = other.id;
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
			if (new_blk.index > -1) {
				this.realSize++;
			}
		} else {
			// Bucket full
			throw new RuntimeException("Bucket is already at max size Z");
		}
	}

	public boolean removeBlock(Block rm_blk) {
		this.blocks.remove(rm_blk);
		if (rm_blk.index > -1) {
			this.realSize--;
		}
		return true;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int new_id) {
		this.id = new_id;
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