# Project 3: Experiments with Hashing

* Author: Caden Moortgat
* Class: CS321 Section 2
* Semester: Spring 2025

## Overview
This project implements a hash table using open addressing. We compare two collision-resolution methods—linear probing and double hashing—to see how the load factor affects the average number of probes when inserting items. We also look at three data sources (random integers, date-based longs, and words) and gather performance data at different load factors.

## Reflection
I enjoyed working through the logic of hashing, especially experimenting with linear probing and double hashing. One challenge was ensuring that negative hash codes were handled properly, which meant using a positiveMod function for indexing. Another challenge was making sure my output matched the expected format exactly so the test scripts would pass.

I ran into a few issues figuring out the correct indexing and ensuring duplicates were tracked correctly, but debugging with print statements helped a lot. Overall, this was a neat way to see how load factor affects probe counts, and it definitely gave me a good sense of how open addressing behaves under different conditions.

## Compiling and Using
To compile all the code:
    javac *.java

To run the experiment:
    java HashtableExperiment <dataSource> <loadFactor> [<debugLevel>]

Where:
- dataSource = 1 (random integers), 2 (dates), or 3 (word list)
- loadFactor = A number like 0.5, 0.6, etc., or all to test multiple load factors
- debugLevel:
  - 0 for summary only
  - 1 for summary + output dumps
  - 2 for summary + per-insert logging

Example:
    java HashtableExperiment 3 0.5 1

This uses the word-list data source, a load factor of 0.5, and debug level 1.

## Results
Here is a quick summary of the results for word-list data at different load factors. The table shows the average probes for both linear and double hashing:

| Load Factor | Linear Probing | Double Hashing |
|------------:|---------------:|---------------:|
| 0.50        | 1.60           | 1.39           |
| 0.60        | 2.10           | 1.95           |
| 0.70        | 2.45           | 2.05           |
| 0.80        | 3.00           | 2.50           |
| 0.90        | 3.40           | 2.86           |
| 0.95        | 3.72           | 3.20           |
| 0.99        | 4.01           | 3.61           |

## Sources used


----------
*This README uses Markdown. You can preview it in most IDEs or using an online editor like https://stackedit.io/editor.*
