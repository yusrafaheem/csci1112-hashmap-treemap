# CSCI 1112 HashMap and TreeMap

This project implements a Map interface two ways: a hash table using separate chaining, and a binary search tree using lazy deletion. Both are profiled against a dataset of about 200 university records to compare how many key comparisons each structure needs for inserts and lookups. It was written for GWU CSCI 1112 in Spring 2025.

## What I wrote

I implemented HashMap.java and TreeMap.java, the two concrete classes behind the Map interface. HashMap hashes each key into a bucket and resolves collisions with a linked list, walking the chain to put, get, or delete a key while counting comparisons along the way. TreeMap is a binary search tree keyed lexicographically on the key string, where deletion is lazy: a deleted node is flagged rather than physically removed, so put and get treat a flagged node as absent while the tree shape underneath it stays intact. If the tree is configured to rebalance, it rebuilds itself into a balanced shape after every insertion, discarding flagged nodes in the process, using the balancing utilities that were already provided. Both classes were found as unimplemented stubs in the assignment starter files, so I wrote the put, get, delete, and clear logic for each and verified them against the course's own unit tests before uploading.

## Provided scaffolding (not my code)

Map.java, KVP.java, ListNode.java, TreeNode.java, MapReader.java, Utilities.java, Analysis.java, UnitTests.java, and the mapdata file were provided by the course. I did not write these files, though TreeMap's balancing utilities and TreeNode were also provided already-written.

## Running it

UnitTests.main loads mapdata and runs both TreeMap and HashMap through a battery of correctness tests, reporting a pass or fail summary for each. Analysis.main inserts and searches the full dataset in both a balanced and an unbalanced binary tree, and in hash tables of several bucket sizes, printing the comparison counts so their performance can be compared.
