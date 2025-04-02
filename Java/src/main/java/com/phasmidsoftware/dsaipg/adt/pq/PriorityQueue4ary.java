package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class PriorityQueue4ary<K> implements Iterable<K> {

    /**
     * Primary constructor that receives whether this is a max-priority queue, a preconstructed array,
     * the index of the root element, the number of elements currently in the array,
     * a comparator for the elements, and whether to use Floyd's trick.
     *
     * @param max        true if it is a max-priority queue, false for a min-priority queue.
     * @param binHeap    a preconstructed array whose length is one more than the required capacity.
     * @param first      the index of the root in the array.
     * @param last       the number of elements currently in the array.
     * @param comparator the comparator for type K.
     * @param floyd      if true, Floyd's trick is used.
     */
    public PriorityQueue4ary(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        //noinspection unchecked
        this.binHeap = (K[]) binHeap;
        this.floyd = floyd;
    }

    /**
     * Secondary constructor that takes only the maximum capacity and a comparator,
     * while specifying the root index and whether to use Floyd's trick.
     *
     * @param n          the expected maximum capacity.
     * @param first      the index used for the root.
     * @param max        true if it is a max-priority queue.
     * @param comparator the comparator for type K.
     * @param floyd      if true, Floyd's trick is used.
     */
    public PriorityQueue4ary(int n, int first, boolean max, Comparator<K> comparator, boolean floyd) {
        // Note: Since we reserve the root position for the 4-ary heap, the array length must be n + first, not n.
        this(max, new Object[n + first], first, 0, comparator, floyd);
    }

    /**
     * Secondary constructor that takes only the maximum capacity and a comparator,
     * while specifying whether it is a max-priority queue and whether to use Floyd's trick.
     *
     * @param n          the expected maximum capacity.
     * @param max        true if it is a max-priority queue.
     * @param comparator the comparator for type K.
     * @param floyd      if true, Floyd's trick is used.
     */
    public PriorityQueue4ary(int n, boolean max, Comparator<K> comparator, boolean floyd) {
        // Note: Since we reserve the root position for the 4-ary heap, the array length must be n + first, not n.
        this(n, 1, max, comparator, floyd);
    }

    /**
     * Secondary constructor that takes only the maximum capacity and a comparator,
     * while specifying whether it is a max-priority queue.
     *
     * @param n          the expected maximum capacity.
     * @param max        true if it is a max-priority queue.
     * @param comparator the comparator for type K.
     */
    public PriorityQueue4ary(int n, boolean max, Comparator<K> comparator) {
        // Note: Since we reserve the root position for the 4-ary heap, the array length must be n + first, not n.
        this(n, 1, max, comparator, false);
    }

    /**
     * Secondary constructor that takes only the maximum capacity and a comparator,
     * defaulting to a max-priority queue and using Floyd's trick.
     *
     * @param n          the expected maximum capacity.
     * @param comparator the comparator for type K.
     */
    public PriorityQueue4ary(int n, Comparator<K> comparator) {
        this(n, 1, true, comparator, true);
    }

    /**
     * Checks if the priority queue is empty.
     *
     * @return true if the current size is 0.
     */
    public boolean isEmpty() {
        return last == 0;
    }

    /**
     * Returns the number of elements actually stored in this priority queue.
     *
     * @return the number of elements in the heap.
     */
    public int size() {
        return last;
    }

    /**
     * Inserts an element with the given key into this priority queue.
     *
     * @param key the key of the element to insert.
     */
    public void give(K key) {
        if (last == binHeap.length - first)
            last--; // If at capacity, arbitrarily discard one element that does not meet the criteria
        // (even if the new key has a higher priority).
        binHeap[++last + first - 1] = key; // Insert the key just after the last element of the 4-ary heap
        swimUp(last + first - 1); // Reorder the 4-ary heap
    }

    /**
     * Removes and returns the root element from this priority queue,
     * while adjusting the 4-ary heap to maintain heap order.
     * If max is true, returns the maximum element; otherwise, returns the minimum element.
     * Note: This method is called DelMax (or DelMin) in the book.
     *
     * @return the maximum element if max is true; otherwise, the minimum element.
     * @throws PQException if the priority queue is empty.
     */
    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        if (floyd) return doTake(this::snake);
        else return doTake(this::sink);
    }

    /**
     * Package-private method to remove the root element from the priority queue,
     * adjust the heap to maintain heap order, and invoke the given function on the root index.
     *
     * @param f a Consumer function used to maintain heap order.
     * @return the root element before removal.
     */
    K doTake(Consumer<Integer> f) {
        K result = binHeap[first]; // Get the root element (max or min depending on max)
        swap(first, last-- + first - 1); // Swap the root element with the last element and decrement count
        f.accept(first); // Invoke the given function to adjust the heap
        binHeap[last + first] = null; // Prevent loitering
        return result;
    }

    /**
     * Sinks the element at index k.
     *
     * @param k the starting index of the element to sink.
     */
    void sink(@SuppressWarnings("SameParameterValue") int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    /**
     * Special sink method: sinks the element at index k and then swims it up.
     *
     * @param k the starting index of the element to adjust.
     */
    void snake(@SuppressWarnings("SameParameterValue") int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    /**
     * Swims the element at index k up.
     *
     * @param k the starting index of the element to swim up.
     */
    void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Compares the elements at indices i and j.
     * It is assumed that the element at the smaller index should be greater than the one at the larger index when max is true.
     * If this condition holds, it returns false.
     *
     * @param i the smaller index.
     * @param j the larger index.
     * @return true if the order of the values is incorrect.
     */
    boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    /**
     * Returns a non-destructive iterator for all the values in this priority queue.
     * Note: Apart from the first element, the remaining elements do not have a fixed order.
     * This method is provided solely for convenience and is not an official part of the PriorityQueue API.
     *
     * @return an iterator based on a copy of the underlying array.
     */
    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(binHeap, last + first)));
        Iterator<K> result = copy.iterator();
        if (first > 0) result.next(); // Skip the initial null value
        return result;
    }

    /**
     * Adjusts the subtree rooted at index k to ensure it satisfies the heap order.
     * The method reorganizes the 4-ary heap by comparing the parent and child nodes,
     * swapping them if necessary, until the subtree is in proper heap order.
     *
     * @param k the root index of the subtree to adjust.
     * @param p a predicate that determines whether the heap order is satisfied.
     *          The predicate receives two indices (parent and child) and returns true if the parent satisfies the heap property.
     * @return the final position of the element originally at index k after adjustment.
     */
    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        // In a 4-ary heap, each node has at most 4 children
        while (firstChild(i) <= last + first - 1) {
            int fc = firstChild(i);
            int lastChild = Math.min(fc + 3, last + first - 1);
            int bestChild = fc;
            // Iterate over all children to choose the one with the highest (or lowest) priority
            for (int j = fc + 1; j <= lastChild; j++) {
                if (unordered(bestChild, j))
                    bestChild = j;
            }
            if (p.test(i, bestChild))
                break;
            swap(i, bestChild);
            i = bestChild;
        }
        return i;
    }

    /**
     * Exchanges the elements at indices i and j in the array.
     *
     * @param i the first index.
     * @param j the second index.
     */
    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    /**
     * Returns the index of the parent of the element at index k in the 4-ary heap.
     * For a 4-ary heap with root index 'first', the parent of node k is:
     *     parent(k) = ((k - (first + 1)) / 4) + first.
     *
     * When first is 1, this becomes ((k - 2) / 4) + 1.
     *
     * @param k the index of the node.
     * @return the index of the parent of node k.
     */
    private int parent(int k) {
        return ((k - (first + 1)) / 4) + first;
    }

    /**
     * Returns the index of the first child of the element at index k in the 4-ary heap.
     * For a 4-ary heap with root index 'first', the children of node k are located at:
     *     firstChild(k) = 4 * (k - first) + first + 1,
     * with the subsequent three children at firstChild(k) + 1, +2, and +3 respectively.
     *
     * When first is 1, this becomes 4 * (k - 1) + 2.
     *
     * @param k the index of the node.
     * @return the index of the first child of node k.
     */
    private int firstChild(int k) {
        return 4 * (k - first) + first + 1;
    }

    /**
     * The following methods are for unit testing ONLY!
     */
    @SuppressWarnings("unused")
    private K peek(int k) {
        return binHeap[k];
    }

    @SuppressWarnings("unused")
    private boolean getMax() {
        return max;
    }

    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] binHeap; // binHeap[i] represents the i-th element in the 4-ary heap (the first position is reserved)
    private int last; // Number of elements in the 4-ary heap
    private final boolean floyd; // Indicates whether Floyd's snake method is used in the take() method

    public static void main(String[] args) {
        doMain();
    }

    /**
     * Test method.
     */
    static void doMain() {
        String[] s1 = new String[5]; // Create a string array of size 5
        s1[0] = "A";
        s1[1] = "B";
        s1[2] = "C";
        s1[3] = "D";
        s1[4] = "E";
        boolean max = true;
        boolean floyd = true;
        Iterable<String> PQ_string_floyd = new PriorityQueue4ary<>(max, s1, 1, 5, Comparator.comparing(String::toString), floyd);
        Iterable<String> PQ_string_nofloyd = new PriorityQueue4ary<>(max, s1, 1, 5, Comparator.comparing(String::toString), false);
        Integer[] s2 = new Integer[5]; // Create an Integer array of size 5
        for (int i = 0; i < 5; i++) {
            s2[i] = i;
        }
        Iterable<Integer> PQ_int_floyd = new PriorityQueue4ary<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), floyd);
        Iterable<Integer> PQ_int_nofloyd = new PriorityQueue4ary<>(max, s2, 1, 5, Comparator.comparing(Integer::intValue), false);
    }
}
