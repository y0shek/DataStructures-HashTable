/**
 * This class is instantiable and defines the Entry type object, which contains
 * one phone number associated with one name.
 * 
 * Class includes a hashCode method for generating hashCodes to store items
 * in a HashTable.
 * 
 * @author Joshua Kellerman, CS-367
 * @credits CS-367 Staff
 */

public class Entry {
	
	public static final int STATIC = 0;			//int codes for hash types
	public static final int STRING = 1;
	public static final int LONG = 2;
	public static final int BOTH = 3;
	
	private String name;
	private long phone;
	private int hashType;

/**
 * Constructs an Entry object with a given String name, long phone number,
 * and hashType (STATIC - 0, STRING - 1, LONG - 2, BOTH - 3)
 * @param name the name phone the phone number hashType the type of hashing algo
 **/
	public Entry(String name, long phone, int hashType) {
		this.name = name;
		this.phone = phone;
		this.hashType = hashType;
	}
/**
 * Converts object to printable String.
 * @return A string of the name + ":" + the phone number.
 * @Override toString method
 **/
	@Override
	public String toString() {
		return name + ":" + phone;
	}
/**
 * Compares current Entry object to another Entry object
 * @return boolean true or false whether equals
 * @Override equals method
 **/
	@Override
	public boolean equals(Object other) {
		if (other instanceof Entry) {
			Entry that = (Entry) other;
			if (that.name.equals(this.name) && that.phone == this.phone)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a hashCode for this object. You should complete the three
	 * different hash functions marked below.
	 * 
	 * Make note that when you write a hash function, it must always return
	 * the same value for the same object. In other words, you should not use
	 * any randomness to generate a hash code.
	 */
	@Override
	public int hashCode() {
		if (hashType == STRING) {
			return Math.abs(name.hashCode());
			//hashing function for name only
		}
		else if (hashType == LONG) {
			return ((Long)phone).hashCode();
			//hashing function for the phone number only
		}
		else if (hashType == BOTH) {
			return (int)(Math.abs(name.hashCode()-phone^3));
			//hashing function both phone number and name
		}
		else {
			//Fixed hash function
			return 11;
		} //end else
	} //end 
} //end class
