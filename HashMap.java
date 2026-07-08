/*--------------------------------------------------------------------------
GWU CSCI 1112 Spring 2025
author: Yusra Faheem, James Taylor

This class implements the Map interface using a hash table with separate
chaining. Each bucket in the table is the head of a singly linked list of
ListNode objects, and collisions are resolved by appending to that list.
--------------------------------------------------------------------------*/

public class HashMap implements Map {

    // The bucket array. Each slot holds the head of a linked list of
    // key-value pairs that hashed to that slot.
    private final ListNode[] buckets;

    /// Creates an empty hash table with a fixed number of buckets.
    /// @param length the number of buckets to allocate
    public HashMap(int length) {
        this.buckets = new ListNode[length];
    }

    /// Inserts a key-value pair into the table, or updates the value if the
    /// key already exists. Walks the chain in the target bucket comparing
    /// keys; if a match is found the existing node's value is updated in
    /// place (no duplicate is created), otherwise a new node is added to
    /// the front of the chain.
    /// @param key the key to insert or update
    /// @param value the value to associate with the key
    /// @param profile a single-element array; profile[0] is incremented by
    ///        the number of key comparisons performed
    public void put(String key, String value, int[] profile) {
        int index = hash(key);
        ListNode current = buckets[index];
        while (current != null) {
            profile[0]++;
            if (current.getKey().equals(key)) {
                current.updateValue(value);
                return;
            }
            current = current.next;
        }
        ListNode newNode = new ListNode(key, value);
        newNode.next = buckets[index];
        buckets[index] = newNode;
    }

    /// Looks up the value associated with a key by walking the chain in
    /// the key's bucket.
    /// @param key the key to search for
    /// @param profile a single-element array; profile[0] is incremented by
    ///        the number of key comparisons performed
    /// @return the value associated with the key if found; otherwise, null
    public String get(String key, int[] profile) {
        int index = hash(key);
        ListNode current = buckets[index];
        while (current != null) {
            profile[0]++;
            if (current.getKey().equals(key)) {
                return current.getValue();
            }
            current = current.next;
        }
        return null;
    }

    /// Removes a key-value pair from the table by unlinking its node from
    /// the bucket's chain.
    /// @param key the key to remove
    /// @param profile a single-element array; profile[0] is incremented by
    ///        the number of key comparisons performed
    /// @return true if the key was found and removed; otherwise, false
    public boolean delete(String key, int[] profile) {
        int index = hash(key);
        ListNode current = buckets[index];
        ListNode previous = null;
        while (current != null) {
            profile[0]++;
            if (current.getKey().equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    /// Empties every bucket, discarding all key-value pairs.
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
    }

    //-------------------------------------------------------------------
    // Utilities
    //-------------------------------------------------------------------
    /// Hash function.  DO NOT MODIFY
    /// @param key string input to be hashed
    /// @return index location of where an object should be put in the table
    private int hash(String key) {
        return Math.abs(key.hashCode() % buckets.length);
    }
}
