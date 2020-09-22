/*
 * You are NOT allowed to change this interface.
 * 
 * This is an interface that provides the methods ORAM needs to interact with its (possibly remote) untrusted storage.
 * 
 */

package pathOramHw;

interface UntrustedStorageInterface{
	
	//You are required to call setCapacity BEFORE reading or writing any buckets. You are not allowed to change the capacity after you set it.
	//You should only use ONE UntrustedStorage in your ORAM.
	abstract void setCapacity(int totalNumOfBuckets);
	
	abstract Bucket readBucket(int position);
	
	abstract void writeBucket(int position, Bucket bucket_to_write);
	
}
