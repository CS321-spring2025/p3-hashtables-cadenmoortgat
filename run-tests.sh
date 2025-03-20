#!/bin/sh

function header(){
	output=$1
	for i in {1..80}
	do
		echo -n "-" >> $output
	done
	echo >> $output
}

echo
echo "Compiling the source code"
echo
javac *.java

if ! test -f HashtableExperiment.class
then
	echo
	echo "HashtableExperiment.class not found! Not able to test!! "
	echo
	exit 1
fi

echo
echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
echo "Running tests for word-list for varying load factors"
echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
echo

# Convert test-cases line endings if needed
dos2unix test-cases/* >& /dev/null

# We only have reference dump files for data source = 3 (word-list)
debugLevel=1

# The set of load factors to test
for load in 0.5 0.6 0.7 0.8 0.9 0.95 0.99
do
    echo "Running java HashtableExperiment dataSource = 3 loadFactor = $load with debugLevel=$debugLevel"
    java HashtableExperiment 3 $load $debugLevel >> /dev/null

    # Convert newly created dump files (in case line endings differ)
    dos2unix linear-dump.txt double-dump.txt >& /dev/null

    echo
    # Compare linear-dump.txt
    diff -w -B linear-dump.txt test-cases/word-list-$load-linear-dump.txt > diff-linear-$load.out
    if test "$?" = 0
    then
        echo "Test PASSED for linear probing (dataSource=3, load=$load)"
        /bin/rm -f diff-linear-$load.out
    else
        echo "==> Test FAILED for linear probing (dataSource=3, load=$load)!!"
        echo "       Check the file diff-linear-$load.out for differences"
    fi

    # Compare double-dump.txt
    diff -w -B double-dump.txt test-cases/word-list-$load-double-dump.txt > diff-double-$load.out
    if test "$?" = 0
    then
        echo "Test PASSED for double hashing (dataSource=3, load=$load)"
        /bin/rm -f diff-double-$load.out
    else
        echo "==> Test FAILED for double hashing (dataSource=3, load=$load)!!"
        echo "       Check the file diff-double-$load.out for differences"
    fi

    echo
done

echo "All tests completed."
