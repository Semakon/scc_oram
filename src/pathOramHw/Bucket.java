package pathOramHw;

import java.util.ArrayList;

import javax.management.RuntimeErrorException;

/*
 * Name: Martijn de Vries, Dennis Cai
 * StudentID: s1549405, TODO
 */

public class Bucket{
	private static boolean is_init = false;
	private static int max_size_Z = -1;
	
	private ArrayList<Block> blocks;
	private int realSize;
	
	Bucket(){
		if(!is_init)
		{
			throw new RuntimeException("Please set bucket size before creating a bucket");
		}
		//TODO Must complete this method for submission
	}
	
	// Copy constructor
	Bucket(Bucket other) {
		if(other == null)
		{
			throw new RuntimeException("the other bucket is not malloced.");
		}
		//TODO Must complete this method for submission
	}
	
	//Implement and add your own methods.
	Block getBlockByKey(int key) {
		// TODO Must complete this method for submission
		return null;
	}
	
	void addBlock(Block new_blk) {
		if (realSize < max_size_Z) {
			// There is space in the bucket

		} else {
			// There is no space in the bucket
		}
	}
	
	boolean removeBlock(Block rm_blk) {
		// TODO Must complete this method for submission
		return false;
	}
	
	ArrayList<Block> getBlocks() {
		return blocks;
	}
	
	int returnRealSize() {
		return realSize;
	}

	static void resetState() {
		is_init = false;
	}

	static void setMaxSize(int maximumSize) {
		if(is_init)
		{
			throw new RuntimeException("Max Bucket Size was already set");
		}
		max_size_Z = maximumSize;
		is_init = true;
	}

}