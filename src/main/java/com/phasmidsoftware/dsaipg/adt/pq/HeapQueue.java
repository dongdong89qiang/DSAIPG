package com.phasmidsoftware.dsaipg.adt.pq;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class HeapQueue<K> implements Iterable<K> {
    /**
     * Primary constructor that takes the max value, an actual array of elements, and a comparator.
     */
    public HeapQueue(boolean max, Object[] binHeap, int first, int last, int d, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = first;
        this.d = d; // Branching factor (2 for binary heap, 4 for 4-ary heap, etc.)
        this.comparator = comparator;
        this.last = last;
        this.binHeap = (K[]) binHeap;
        this.floyd = floyd;
    }

    /**
     * Secondary constructor specifying max capacity, branching factor, and comparator.
     */
    public HeapQueue(int n, int first, int d, boolean max, Comparator<K> comparator, boolean floyd) {
        this(max, new Object[n + first], first, 0, d, comparator, floyd);
    }

    /**
     * Insert an element into the priority queue.
     */
    public void give(K key) {
        if (last == binHeap.length - first)
            last--; // Evict if at capacity
        binHeap[++last + first - 1] = key;
        if (!floyd) {
            swimUp(last + first - 1); // Regular insertion
        }
    }

    /**
     * Heapify: Bulk heap construction using Floyd's Trick.
     * This method ensures that the heap property is maintained in O(n) time.
     */
    public void heapify() {
        for (int i = (last + first - 1) / d; i >= first; i--) {
            doHeapify(i, (a, b) -> !unordered(a, b));
        }
    }

    /**
     * Remove the root element from this priority queue and adjust heap.
     */
    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        return floyd ? doTake(this::heapify) : doTake(this::sink);
    }

    public void heapify(int ignored) { // Parameter is unused, but needed for compatibility
        for (int i = (last + first - 1) / d; i >= first; i--) { // Process non-leaf nodes
            doHeapify(i, (a, b) -> !unordered(a, b));
        }
    }

    /**
     * Performs heapify operation, adjusting the heap from the given node downwards.
     */
    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int bestChild = firstChild(i);
            for (int j = 1; j < d; j++) { // Check all children for the best one
                int sibling = bestChild + j;
                if (sibling <= last + first - 1 && unordered(bestChild, sibling)) {
                    bestChild = sibling;
                }
            }
            if (p.test(i, bestChild)) break;
            swap(i, bestChild);
            i = bestChild;
        }
        return i;
    }

    /**
     * Swap two elements in the heap.
     */
    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    /**
     * Computes the index of the parent of node k.
     */
    private int parent(int k) {
        return (k - first + d - 1) / d + first - 1;
    }

    /**
     * Computes the index of the first child of node k.
     */
    private int firstChild(int k) {
        return d * (k - first) + first + 1;
    }

    /**
     * Swim an element up to maintain heap order.
     */
    void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Remove an element from the queue using heap property adjustment.
     */
    K doTake(Consumer<Integer> f) {
        K result = binHeap[first]; // Get the root element
        swap(first, last-- + first - 1); // Swap root with the last element
        f.accept(first); // Reheapify
        binHeap[last + first] = null; // Prevent loitering
        return result;
    }

    /**
     * Sink an element down to maintain heap order.
     */
    void sink(int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    /**
     * Special sink method that sinks the element and then swims it back up.
     */
    void snake(int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    /**
     * Compare two heap elements to determine if they are unordered.
     */
    boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    /**
     * @return true if the queue is empty.
     */
    public boolean isEmpty() {
        return last == 0;
    }

    /**
     * @return the size of the priority queue.
     */
    public int size() {
        return last;
    }

    /**
     * Iterable method.
     */
    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(binHeap, last + first)));
        return copy.iterator();
    }

    private final boolean max;
    private final int first;
    private final int d; // New field: branching factor
    private final Comparator<K> comparator;
    private final K[] binHeap;
    private int last;
    private final boolean floyd;
}
