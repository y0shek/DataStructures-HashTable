import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class implements a hashtable that using chaining for collision handling.
 * The chains are implemented using LinkedLists.  When a hashtable is created, 
 * its initial size and maximum load factor are specified. The hashtable can 
 * hold arbitrarily many items and resizes itself whenever it reaches 
 * its maximum load factor. Note that this hashtable allows duplicate entries.
 * 
 * @author Joshua Kellerman, CS-367
 * @credits CS-367 Staff
 */
public class HashTable<T> {
	private LinkedList<T>[] dataTable;
	private double maxLoadFactor; //the loadfactor
	private int numItems; // items tracker
	private int tableSize; //the tablesize
	private int longest; //the longest LinkedList<T> length
	private int empty; // the number of empty buckets
	/**
	 * Constructs an empty hashtable with the given initial size and maximum '
	 * load factor. The load factor should be a real 
	 * number greater than 0.0 (not a percentage).  For example, to create a 
	 * hash table with an initial size of 10 and a load factor of 0.85, one 
	 * would use:
	 * <dir><tt>HashTable ht = new HashTable(10, 0.85);</tt></dir>
	 *
	 * @param initSize The initial size of the hashtable.  If the size is less
	 * than or equal to 0, an IllegalArgumentException is thrown.
	 * @param loadFactor The load factor expressed as a real number.  If the
	 * load factor is less than or equal to 0.0, an IllegalArgumentException is
	 * thrown.
	 **/
	public HashTable(int initSize, double loadFactor) throws IllegalArgumentException{
		if(loadFactor <= 0.0){ 	//check to make sure inputs are valid
			throw new IllegalArgumentException("Illegal loadFactor size.");
		}
		if(initSize <= 0){
			throw new IllegalArgumentException("Illegal table size.");
		}
		maxLoadFactor = loadFactor; //set constructor params
		numItems = 0;
		tableSize = initSize;
		dataTable = (LinkedList<T>[])(new LinkedList[initSize]);
	}
	/**
	 * Determines if the given item is in the hashtable and returns it if 
	 * present.  If more than one copy of the item is in the hashtable, the 
	 * first copy encountered is returned.
	 *
	 * @param item the item to search for in the hashtable
	 * @return the item if it is found and null if not found
	 **/
	public T lookup(T item) {
		int hash = item.hashCode()%tableSize; //get the hash for the item
		if(dataTable[hash] == null){ //check for null in array
			return null;
		}
		Iterator<T> itr = dataTable[hash].iterator(); // if its not null
		while (itr.hasNext()){ //iterate through the LinkedList
			T temp = itr.next();
			if (temp.equals(item)){
				return temp; //return the object
			}
		}
		return null; //otherwise return null
	}

	/**
	 * Inserts the given item into the hash table.  
	 * 
	 * If the load factor of the hashtable after the insert would exceed 
	 * (not equal) the maximum load factor (given in the constructor), then the 
	 * hashtable is resized.
	 * 
	 * When resizing, to make sure the size of the table is good, the new size 
	 * is always 2 x <i>old size</i> + 1.  For example, size 101 would become 
	 * 203.  (This  guarantees that it will be an odd size.)
	 * 
	 * <p>Note that duplicates <b>are</b> allowed.</p>
	 *
	 * @param item the item to add to the hashtable
	 **/
	public void insert(T item) {
		numItems ++; //update items
		if(numItems *1.0 / tableSize > maxLoadFactor){ //if expand is needed
			tableSize = (tableSize*2) +1; //update the tablesize
			LinkedList<T>[] tempTable =  //make a new LinkedList to be temp
					(LinkedList<T>[])(new LinkedList[tableSize]);
			for (int i = 0; i < dataTable.length; i ++){ //copy over previous array
				if(dataTable[i] != null){ //as long as a space is not null
					for(T curr : dataTable[i]){
						int hash = curr.hashCode()%tableSize; //rehash
						if(tempTable[hash] == null){ //if current space is empty
							tempTable[hash] = new LinkedList<T>(); //new linkedlist
							tempTable[hash].add(curr); //add it
						} else {
							tempTable[hash].add(0, curr); //add the item to front
						}//end else						  //of LinkedList
					} //end for
				} // end if
			} //end for
			dataTable = tempTable;
		} // end expansion if;
		int hashCode = item.hashCode()%tableSize; //hash the item
		if(dataTable[hashCode] == null){ //if the spot has no linkedlist
			dataTable[hashCode] = new LinkedList<T>(); //make one
			dataTable[hashCode].add(item); //add item

		}else{ //if it does already have a linkedlist
			dataTable[hashCode].add(0,item); //add item
		}
	}
	/**
	 * Removes and returns the given item from the hashtable.  If the item is 
	 * not in the hashtable, <tt>null</tt> is returned.  If more than one copy 
	 * of the item is in the hashtable, only the first copy encountered is 
	 * removed and returned.
	 *
	 * @param item the item to delete in the hashtable
	 * @return the removed item if it was found and null if not found
	 **/
	public T delete(T item) {
		int hash = item.hashCode()%tableSize; //get hash code
		if(dataTable[hash] == null) return null; //if empty, return null
		Iterator<T> itr = dataTable[hash].iterator(); //make an iterator
		while(itr.hasNext()){ //go through the LinkedList
			if(itr.next().equals(item)){ //compare
				dataTable[hash].remove(item); //remove
				numItems --; //decriment numItems
				return item;
			} //if
		} //end while
		return null; //if item is not found
	}//end delete()

	/**
	 * Prints statistics about the hashtable to the PrintStream supplied.
	 * The statistics displayed are: 
	 * <ul>
	 * <li>the current table size
	 * <li>the number of items currently in the table 
	 * <li>the current load factor
	 * <li>the length of the largest chain
	 * <li>the number of chains of length 0
	 * <li>the average length of the chains of length > 0
	 * </ul>
	 *
	 * @param out the place to print all the output
	 **/
	public void displayStats(PrintStream out) { 
		empty = 0;
		for(int i = 0; i < dataTable.length; i ++){  //count longest
			if(dataTable[i] != null){				 //LinkedList
				if(dataTable[i].size() > longest){	 //in the database.
					longest = dataTable[i].size();
				} //end 2nd if
			} //end 1st if
			if (dataTable[i] == null || dataTable[i].size() == 0){
				empty ++;							 //count number of empty
			} //end if								 //and 0-length chains
		} //end for

		double average = (numItems *1.0/(tableSize-empty)); //print out stats
		out.println("\n******** Hashtable Statistics ********");
		out.println();
		out.println("Current Table Size:        " + tableSize);
		out.println("# of Items in Table:       " + numItems);
		out.println("Current LoadFactor:        " + (numItems *1.0 / tableSize));
		out.println("Longest Chain Length:      " + longest);
		out.println("# of 0-Length Chains:      " + empty);
		out.println("Avg (non-0) chain length:  " + average);
	}
}