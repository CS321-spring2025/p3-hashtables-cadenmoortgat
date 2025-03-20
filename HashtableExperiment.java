import java.util.*;
import java.io.*;

/**
 * Driver program that runs experiments with LinearProbing and DoubleHashing.
 *
 * Usage:
 *   java HashtableExperiment <dataSource> <loadFactor|'all'> [<debugLevel>]
 *
 * dataSource: 1 => random int, 2 => date-based long, 3 => word-list
 * loadFactor: e.g. 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.99
 *             or 'all' => run all standard load factors in one go
 * debugLevel: 0 => print summary only (default if omitted)
 *             1 => summary + dump to file at end
 *             2 => summary + print per-insert debugging
 */
public class HashtableExperiment {

    public static void main(String[] args) {

        // Basic usage check
        if (args.length < 2) {
            System.out.println("Usage: java HashtableExperiment <dataSource> <loadFactor|'all'> [<debugLevel>]");
            return;
        }

        // Parse arguments
        int dataSource = Integer.parseInt(args[0]);
        String loadFactorArg = args[1];
        int debugLevel = 0;
        if (args.length > 2) {
            debugLevel = Integer.parseInt(args[2]);
        }

        // If user passes "all" for load factor, loop over a set of load factors.
        if (loadFactorArg.equalsIgnoreCase("all")) {
            double[] loadFactors = {0.50, 0.60, 0.70, 0.80, 0.90, 0.95, 0.99};
            for (double lf : loadFactors) {
                runOneExperiment(dataSource, lf, debugLevel);
                System.out.println(); // blank line between each run
            }
        } else {
            // Single-run case: parse the load factor as a double
            double loadFactor = Double.parseDouble(loadFactorArg);
            runOneExperiment(dataSource, loadFactor, debugLevel);
        }
    }

    /**
     * Helper method that runs the entire hashtable experiment for a single load factor.
     * @param dataSource the data source code (1 = random int, 2 = date, 3 = word-list)
     * @param loadFactor the desired alpha (0 < alpha <= 1)
     * @param debugLevel 0 => summary only, 1 => summary + dump, 2 => verbose per-insert
     */
    private static void runOneExperiment(int dataSource, double loadFactor, int debugLevel) {
        // Generate a suitable twin-prime table size in [95500..96000]
        int tableSize = TwinPrimeGenerator.generateTwinPrime(95500, 96000);
        if (tableSize < 0) {
            System.out.println("No twin prime found in [95500..96000]. Exiting.");
            return;
        }

        // For convenience, n is the number of *unique* items we want inserted
        int n = (int) Math.ceil(loadFactor * tableSize);

        // Create the two hashtables
        LinearProbing linearTable = new LinearProbing(tableSize);
        DoubleHashing doubleTable = new DoubleHashing(tableSize);

        // Output a brief line that shows the found table capacity
        System.out.println("HashtableExperiment: Found a twin prime table capacity: " + tableSize);

        // Print an input summary
        String sourceName = switch (dataSource) {
            case 1 -> "Random-Integer";
            case 2 -> "Date-Value";
            default -> "Word-List";
        };
        System.out.println("HashtableExperiment: Input: " + sourceName + "   Loadfactor: " + loadFactor);

        // Insert data into both tables until each has insertedCount == n
        // They will share the same data in the same order.
        generateAndFill(linearTable, doubleTable, dataSource, n, debugLevel);

        // Print the required summary for each table
        printSummary("Linear Probing", linearTable);
        printSummary("Double Hashing", doubleTable);

        // If debug level 1, we dump tables to files
        if (debugLevel == 1) {
            linearTable.dumpToFile("linear-dump.txt");
            System.out.println("HashtableExperiment: Saved dump of hash table (linear)");
            doubleTable.dumpToFile("double-dump.txt");
            System.out.println("HashtableExperiment: Saved dump of hash table (double)");
        }
    }

    /**
     * Generate data for the chosen dataSource, feeding the same sequence
     * into both hashtables until each one has inserted n distinct items.
     *
     * If debugLevel == 2, we print per-insert info.
     */
    private static void generateAndFill(Hashtable linear, Hashtable dbl, int dataSource, int n, int debugLevel) {
        // For random:
        Random rand = new Random(); // seed not specified

        // For date source:
        long currentTime = System.currentTimeMillis();

        // For word-list:
        Scanner wordScan = null;
        if (dataSource == 3) {
            try {
                wordScan = new Scanner(new File("word-list.txt"));
            } catch (FileNotFoundException e) {
                System.err.println("word-list.txt not found!");
                return;
            }
        }

        // We'll keep going until both have inserted n items
        while (linear.getInsertedCount() < n || dbl.getInsertedCount() < n) {
            Object data = null;

            // Pick next data item from the chosen source
            if (dataSource == 1) {
                // random int
                data = rand.nextInt();
            } else if (dataSource == 2) {
                // date-based long
                currentTime += 1000; // bump by 1 second
                data = new java.util.Date(currentTime);
            } else {
                // word list
                if (!wordScan.hasNextLine()) {
                    // ran out of words
                    break;
                }
                data = wordScan.nextLine();
            }

            // Insert into linear if not done
            if (linear.getInsertedCount() < n) {
                int probesUsed = linear.insert(new HashObject(data));
                if (debugLevel == 2) {
                    if (probesUsed == 0) {
                        System.out.println("[Linear] Duplicate key: " + data);
                    } else if (probesUsed > 0) {
                        System.out.println("[Linear] Inserted key: " + data + " with " + probesUsed + " probes.");
                    }
                }
            }

            // Insert into double if not done
            if (dbl.getInsertedCount() < n) {
                int probesUsed = dbl.insert(new HashObject(data));
                if (debugLevel == 2) {
                    if (probesUsed == 0) {
                        System.out.println("[Double] Duplicate key: " + data);
                    } else if (probesUsed > 0) {
                        System.out.println("[Double] Inserted key: " + data + " with " + probesUsed + " probes.");
                    }
                }
            }
        }

        // Close scanner if used
        if (wordScan != null) {
            wordScan.close();
        }
    }

    /**
     * Prints the summary for a given table, in debug level 0 style.
     */
    private static void printSummary(String label, Hashtable table) {
        System.out.println("\n\tUsing " + label);
        System.out.println("HashtableExperiment: size of hash table is " + table.getTableSize());
        System.out.println("\tInserted " + table.getInsertedCount() + " elements, of which "
                + table.getDuplicatesCount() + " were duplicates");

        double avgProbes = 0;
        if (table.getInsertedCount() > 0) {
            avgProbes = (double) table.getTotalProbes() / table.getInsertedCount();
        }
        System.out.printf("\tAvg. no. of probes = %.2f\n", avgProbes);
    }
}
