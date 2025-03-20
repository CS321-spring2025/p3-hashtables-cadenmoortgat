/**
 * Wraps a key (generic Object), plus a frequency count and the probe count
 * used when this object was first inserted into the hashtable.
 */
public class HashObject {
    private Object key;      // The actual data/key
    private int frequency;   // How many times this key has been repeated
    private int probeCount;  // Number of probes to insert (first time)

    /**
     * Constructor initializes with a given key.
     * Frequency is set to 1 by default, since we have it at least once.
     * Probe count is initially 0, and can be updated by the hashtable.
     */
    public HashObject(Object key) {
        this.key = key;
        this.frequency = 1;
        this.probeCount = 0;
    }

    /**
     * Return the wrapped key object.
     */
    public Object getKey() {
        return key;
    }

    /**
     * Increase frequency by 1 if we find a duplicate key.
     */
    public void incrementFrequency() {
        frequency++;
    }

    /**
     * Return the frequency of this key.
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Set how many probes it took to insert this item the first time.
     */
    public void setProbeCount(int pCount) {
        this.probeCount = pCount;
    }

    /**
     * Return how many probes were used to insert this item the first time.
     */
    public int getProbeCount() {
        return this.probeCount;
    }

    /**
     * Two HashObjects are equal if their keys are equal.
     * We delegate to the key's .equals() method.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof HashObject)) {
            return false;
        }
        HashObject other = (HashObject) obj;
        return this.key.equals(other.key);
    }

    /**
     * We display in the format: "key freq probeCount" for debug/dump usage.
     * Example: "personnel 10 2"
     */
    @Override
    public String toString() {
        return key.toString() + " " + frequency + " " + probeCount;
    }
}
