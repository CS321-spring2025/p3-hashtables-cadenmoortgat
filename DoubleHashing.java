/**
 * Subclass of Hashtable that uses double hashing for collision resolution.
 */
public class DoubleHashing extends Hashtable {

    public DoubleHashing(int size) {
        super(size);
    }

    /**
     * For double hashing, the step size on the i-th attempt is i * h2.
     * Where h2 = 1 + positiveMod(keyHash, tableSize - 2).
     */
    @Override
    protected int stepSize(int keyHash, int i) {
        int h2 = 1 + positiveMod(keyHash, this.tableSize - 2);
        return i * h2;
    }
}
