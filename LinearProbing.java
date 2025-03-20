/**
 * Subclass of Hashtable that uses linear probing for collision resolution.
 */
public class LinearProbing extends Hashtable {

    public LinearProbing(int size) {
        super(size);
    }

    /**
     * For linear probing, step size is simply i on the i-th attempt.
     */
    @Override
    protected int stepSize(int keyHash, int i) {
        // linear offset is just i
        return i;
    }
}
