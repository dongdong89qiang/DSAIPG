package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.Helper;
import com.phasmidsoftware.dsaipg.sort.HelperFactory;
import com.phasmidsoftware.dsaipg.util.Config;
import com.phasmidsoftware.dsaipg.util.Config_Benchmark;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class InsertionSortBenchmark {
    public static void main(String[] args) {
        int[] ns = {1024, 2048, 4096, 8192, 16384};  // Doubling method for array sizes
        int warmupRuns = 10;
        int testRuns = 10;  // Number of timing runs per array size
        Random random = new Random();

        System.out.printf("%-15s %-10s %-10s %-10s %-10s\n", "Array Size", "Random", "Ordered", "Partial", "Reverse");

        for (int n : ns) {
            long randomTime = runBenchmark(n, warmupRuns, testRuns, arr -> fillRandom(arr, random));
            long orderedTime = runBenchmark(n, warmupRuns, testRuns, InsertionSortBenchmark::fillOrdered);
            long partialTime = runBenchmark(n, warmupRuns, testRuns, InsertionSortBenchmark::fillPartiallyOrdered);
            long reverseTime = runBenchmark(n, warmupRuns, testRuns, InsertionSortBenchmark::fillReverseOrdered);

            System.out.printf("%-15d %-10.2f %-10.2f %-10.2f %-10.2f\n", n,
                    randomTime / 1e6, orderedTime / 1e6, partialTime / 1e6, reverseTime / 1e6);
        }
    }

    private static long runBenchmark(int n, int warmupRuns, int testRuns, Function<Integer[], Void> arrayInitializer) {
        Integer[] array = new Integer[n];
        Config config = Config_Benchmark.setupConfigFixes();  // Obtain configuration
        // Warm-up phase
        for (int i = 0; i < warmupRuns; i++) {
            arrayInitializer.apply(array);
            sortArray(array,config);  // Perform the sort during warm-up
        }

        // Actual benchmarking
        long totalTime = 0;
        for (int i = 0; i < testRuns; i++) {
            arrayInitializer.apply(array);
            long startTime = System.nanoTime();
            sortArray(array,config);
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }

        // Return the average time
        return totalTime / testRuns;
    }

    private static void sortArray(Integer[] array, Config config) {

        Helper<Integer> helper = HelperFactory.create("BenchmarkHelper", array.length, config);
        InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(helper);
        sorter.sort(Arrays.copyOf(array, array.length), 0, array.length);
    }

    // Array Initialization Methods
    private static Void fillRandom(Integer[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(array.length);
        }
        return null;
    }

    private static Void fillOrdered(Integer[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        return null;
    }

    private static Void fillPartiallyOrdered(Integer[] array) {
        int mid = array.length / 2;
        for (int i = 0; i < mid; i++) {
            array[i] = i;
        }
        for (int i = mid; i < array.length; i++) {
            array[i] = array.length - i;
        }
        return null;
    }

    private static Void fillReverseOrdered(Integer[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array.length - i;
        }
        return null;
    }

}
