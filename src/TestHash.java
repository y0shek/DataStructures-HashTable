import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * This program tests some of the functionality of the HashTable class.  It 
 * does not completely test the HashTable class.  You should make sure that 
 * you do completely test your HashTable class, either by modifying this file
 * or by writing a different driver.
 * 
 * @author Joshua Kellerman, CS-367
 * @credits CS-367 Staff
 **/
public class TestHash {

	private static HashTable<Entry> table;		//The hashtable
	private static int initSize;				//The initial size for the table
	private static double loadFactor;			//The max load factor
	private static String inputFile;			//The input file of entries
	private static ArrayList<Entry> allEntries; //All the entries from the
												//input file
	// PrintStreams for printing to a file and to the console, respectively.
	private static PrintStream fileOut, sysOut;
	
	/**
	 * Main method to run the HashTable class.
	 *
	 * @param args needs to have 5 values in the array:
	 * @param [0] is the name of the file containing the information to be 
	 *            hashed. The file should contain strings in the form
	 *            name:phone#
	 *            and each line should be stored in an Entry object.
	 * @param [1] maximum load factor to give the the HashTable class.  
	 *            Note it is a fractional amount (e.g., 0.45), not the percent.
	 * @param [2] the initial size of the hashtable to give to HashTable class
	 * @param [3] the type of hash function to use, of: C(static), S(name),
	 * 			  L(phone), or B(both)
	 * @param [4] output file name used when calling the displayStats() method 
	 * 			  of the table and the testTimes() method here
	 */
	public static void main(String [] args) {
		
		// PrintStreams for displayStats() and testTimes() methods.  
		// Set to null since Java complains about uninitialized value.
		fileOut = null;
		sysOut = new PrintStream(System.out);

		try {
			if (args.length != 5) {
				System.err.println("Expected 5 but got " + args.length);
				System.err.println("Arguments expected:");
				System.err.println("  input file name");
				System.err.println("  max load factor (e.g., 0.75)");
				System.err.println("  initial size of hash table");
				System.err.println("  type of hash function");
				System.err.println("  file for output");
				System.exit(1);
			}

			inputFile = args[0];
			loadFactor = Double.parseDouble(args[1]);
			initSize = Integer.parseInt(args[2]);
			char hashType = args[3].charAt(0);
			String outFileName = args[4];

			// Open file for output
			try {
				File outFile = new File(outFileName);
				fileOut = new PrintStream(outFile);
			} catch (IOException ex) {
				System.err.println("Error opening file " + outFileName +
				                   " failed so stopping.  The error was:");
				System.err.println(ex);
				System.exit(99);
			}
			
			// Run tests on each hash function
			switch(hashType) {
			case 'C':
				loadEntries(Entry.STATIC);
				printStats("STATIC");
				break;
			case 'S':
				loadEntries(Entry.STRING);
				printStats("STRING");
				break;
			case 'L':
				loadEntries(Entry.LONG);
				printStats("LONG");
				break;
			case 'B':
				loadEntries(Entry.BOTH);
				printStats("BOTH");
				break;
			default:
				System.err.println("Invalid hash function type. Should be " +
						"C, S, L, or B.");
				return;
			}

		} catch(Throwable ex) {
			System.out.println("TestHash had unexpected and uncaught exception"
					           + " in main");
			ex.printStackTrace();

		} finally {
			// If you don't always close the PrintStream the file may 
			// appear empty.
			fileOut.close();
			//System.out.println("TestHash done");
			sysOut.close();
		}
	}
	
	/**
	 * Prints the stats for this hash function type to both fileOut and sysOut.
	 * @param type
	 */
	private static void printStats(String type) {
		fileOut.println(type + " hashCode: ");
		testTimes(fileOut);
		table.displayStats(fileOut);
		
		// Comment these lines if you don't want to see the 
		// output in the console
		sysOut.println(type + " hashCode: ");
		testTimes(sysOut);
		table.displayStats(sysOut);
	}
	
	/**
	 * Loads the entries from the input file, then inserts half of them
	 * into the table.
	 * @param hashType The type of the hash function to use.
	 * @throws FileNotFoundException if the input file is invalid
	 */
	private static void loadEntries(int hashType) 
			throws FileNotFoundException {
		table = new HashTable<Entry>(initSize, loadFactor);
		
		Scanner fileReader = new Scanner(new File(inputFile));
		allEntries = new ArrayList<Entry>();
		
		while (fileReader.hasNextLine()) {
			String line = fileReader.nextLine();
			String[] tokens = line.split(":");
			Entry newEntry = new Entry(tokens[0], 
					Long.parseLong(tokens[1]), hashType);
			allEntries.add(newEntry);
		}
		
		//Insert half the entries to fill the table
		for (int i=0; i<allEntries.size()/2; i++) {
			table.insert(allEntries.get(i));
		}
	}
	
	
	/**
	 * Prints out the time to insert, lookup, and delete the remaining entries.
	 * Half have been inserted already.
	 * @param out The stream to print to, either fileOut or sysOut.
	 */
	private static void testTimes(PrintStream out){
		long start, end;
		int numItems = allEntries.size() / 2;
    	
		//Time the insertion of the remaining entries
    	start = System.currentTimeMillis();
		for (int i=numItems; i<allEntries.size(); i++) {
			table.insert(allEntries.get(i));
		}
    	end = System.currentTimeMillis();
    	
    	out.println("  Time to insert " + numItems + " items: " 
    			+ (end - start) + " ms");
    	
    	start = System.currentTimeMillis();
    	for (int i=numItems; i<allEntries.size(); i++) {
			table.lookup(allEntries.get(i));
		}
    	end = System.currentTimeMillis();
    	
    	out.println("  Time to lookup " + numItems + " items: " 
    			+ (end - start) + " ms");
    	
    	start = System.currentTimeMillis();
    	for (int i=numItems; i<allEntries.size(); i++) {
			table.delete(allEntries.get(i));
		}
    	end = System.currentTimeMillis();
    	
    	out.println("  Time to delete " + numItems + " items: " 
    			+ (end - start) + " ms");
	}
}