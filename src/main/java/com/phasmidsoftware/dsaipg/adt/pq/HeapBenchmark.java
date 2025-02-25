package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;
import com.phasmidsoftware.dsaipg.adt.pq.HeapQueue;

import java.util.*;

public class HeapBenchmark {
    private static final int M = 4095; // Max elements in heap
    private static final int INSERTIONS = 16000;
    private static final int REMOVALS = 4000;

    public static void main(String[] args) {
        // Benchmark the different heap implementations
        benchmarkHeap("Binary Heap", 2, false);
        benchmarkHeap("Binary Heap (Floyd's Trick)", 2, true);
        benchmarkHeap("4-ary Heap", 4, false);
        benchmarkHeap("4-ary Heap (Floyd's Trick)", 4, true);
    }

    private static void benchmarkHeap(String description, int d, boolean useFloyd) {
        Comparator<Integer> comparator = Integer::compareTo;
        Random random = new Random(42); // Fixed seed for consistency

        // Create a Benchmark_Timer instance for insertions
        Benchmark_Timer<HeapQueue<Integer>> insertionBenchmark = new Benchmark_Timer<>(
                description + " (Insertion)",
                heapQueue -> {
                    for (int i = 0; i < INSERTIONS; i++) {
                        int num = random.nextInt(1000000);
                        heapQueue.give(num);
                    }
                    if (useFloyd) heapQueue.heapify(); // Apply Floyd’s Trick once after insertions
                });

        // Run insertion benchmark
        double insertionTime = insertionBenchmark.runFromSupplier(
                () -> new HeapQueue<>(M, 1, d, true, comparator, useFloyd), 5);
        System.out.println(description + " - Avg insertion time: " + insertionTime + " ms");

        // Create a Benchmark_Timer instance for removals
        Benchmark_Timer<HeapQueue<Integer>> removalBenchmark = new Benchmark_Timer<>(
                description + " (Removal)",
                heapQueue -> {
                    int highestPrioritySpilled = Integer.MIN_VALUE;
                    for (int i = 0; i < REMOVALS; i++) {
                        if (!heapQueue.isEmpty()) {
                            int removed = 0;
                            try {
                                removed = heapQueue.take();
                            } catch (PQException e) {
                                e.printStackTrace();
                            }
                            highestPrioritySpilled = Math.max(highestPrioritySpilled, removed);
                        }
                    }
                    System.out.println(description + " - Highest priority spilled: " + highestPrioritySpilled);
                });

        // Run removal benchmark
        double removalTime = removalBenchmark.runFromSupplier(
                () -> new HeapQueue<>(M, 1, d, true, comparator, useFloyd), 5);
        System.out.println(description + " - Avg removal time: " + removalTime + " ms");
    }
}
