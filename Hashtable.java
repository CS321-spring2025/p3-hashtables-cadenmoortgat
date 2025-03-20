import java.io.PrintWriter;
import java.io.FileNotFoundException;

/**
 * Abstract parent class that defines the core logic of an open-addressing hashtable.
 * Subclasses (LinearProbing, DoubleHashing) implement stepSize(...) differently.
 */
public abstract class Hashtable {
    protected HashObject[] table;   // The actual storage array
    protected int tableSize;        // Capacity of the hashtable
    protected long totalProbes;     // Sum of all probes for *new insertions*
    protected int insertedCount;    // How many *unique* keys have been inserted
    protected int duplicatesCount;  // How many times we encountered a duplicate

    /**
     * Create the table with the given capacity.
     */
    public Hashtable(int size) {
        this.tableSize = size;
        this.table = new HashObject[size];
        this.totalProbes = 0;
        this.insertedCount = 0;
        this.duplicatesCount = 0;
    }

    /**
     * We compute (dividend % divisor) in a positive manner (avoid negative remainders).
     */
    protected int positiveMod(int dividend, int divisor) {
        int rem = dividend % divisor;
        if (rem < 0) {
            rem += divisor;
        }
        return rem;
    }

    /**
     * Subclasses must define how the "step size" is computed for the i-th probe
     * since linear vs. double hashing differ.
     *
     * @param keyHash: hashCode of the key
     * @param i: the probe attempt counter (0,1,2,...)
     * @return how far to move from the original h1 index on the i-th attempt
     */
    protected abstract int stepSize(int keyHash, int i);

    /**
     * Insert the given HashObject into the table using open addressing.
     *
     * @param obj the HashObject to insert
     * @return number of probes if new insertion, or 0 if found duplicate
     */
    public int insert(HashObject obj) {
        // Key's hashCode
        int keyHash = obj.getKey().hashCode();
        // Primary hash
        int h1 = positiveMod(keyHash, tableSize);

        // We'll search up to tableSize times in worst case
        for (int i = 0; i < tableSize; i++) {
            // offset for this attempt
            int offset = stepSize(keyHash, i);
            // final index (mod tableSize)
            int index = positiveMod(h1 + offset, tableSize);

            // We count each check of an index as a 'probe'
            // but only if we might insert. If we see it's a duplicate,
            // we do not count it toward totalProbes for new insertion.
            if (table[index] == null) {
                // A free slot! Insert a brand new object
                table[index] = obj;
                // The # of probes for this insertion is i+1
                obj.setProbeCount(i + 1);
                // We track total probes only for a new insertion
                totalProbes += (i + 1);
                insertedCount++;
                return (i + 1);
            } else {
                // If the slot is not null, check if it's a duplicate
                if (table[index].equals(obj)) {
                    // It's a duplicate. Increase frequency.
                    table[index].incrementFrequency();
                    duplicatesCount++;
                    // Return 0 to indicate we did NOT do a new insertion
                    return 0;
                }
                // else keep probing...
            }
        }
        // If we exit the loop, no space found (shouldn't happen if alpha < 1)
        // but just in case, we return -1 or throw an exception
        return -1;
    }

    /**
     * A simple search that returns the HashObject if found, otherwise null.
     * We'll do open addressing search logic.
     */
    public HashObject search(Object key) {
        int keyHash = key.hashCode();
        int h1 = positiveMod(keyHash, tableSize);

        for (int i = 0; i < tableSize; i++) {
            int offset = stepSize(keyHash, i);
            int index = positiveMod(h1 + offset, tableSize);

            if (table[index] == null) {
                // If we hit an empty slot, the item doesn't exist
                return null;
            } else if (table[index].getKey().equals(key)) {
                return table[index];
            }
            // else keep checking
        }
        // Not found
        return null;
    }

    /**
     * Dump the non-null entries of the table to a file.
     * Format: table[index]: toString()
     */
    public void dumpToFile(String fileName) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(fileName);
            for (int i = 0; i < tableSize; i++) {
                if (table[i] != null) {
                    out.println("table[" + i + "]: " + table[i].toString());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not open file " + fileName + " for writing.");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Getter for total number of probes used (for new insertions).
     */
    public long getTotalProbes() {
        return totalProbes;
    }

    /**
     * Getter for how many unique items have actually been inserted (excludes duplicates).
     */
    public int getInsertedCount() {
        return insertedCount;
    }

    /**
     * Getter for how many duplicates have been encountered.
     */
    public int getDuplicatesCount() {
        return duplicatesCount;
    }

    /**
     * Getter for table size (capacity).
     */
    public int getTableSize() {
        return tableSize;
    }
}
