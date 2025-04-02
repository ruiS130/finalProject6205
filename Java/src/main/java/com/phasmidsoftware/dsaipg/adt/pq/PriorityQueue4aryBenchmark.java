package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.benchmark.Benchmark_Timer;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Supplier;

public class PriorityQueue4aryBenchmark {

    // Capacity of the priority queue (the maximum number of elements held simultaneously)
    private final int M;
    // Number of insert operations, e.g., 16,000 times
    private final int numInsert;
    // Number of delete operations, e.g., 4,000 times
    private final int numRemove;
    // Number of test runs for averaging
    private final int runs;

    /**
     * Constructor.
     *
     * @param M         the capacity of the priority queue (maximum number of elements held simultaneously)
     * @param numInsert the number of insert operations
     * @param numRemove the number of delete operations
     * @param runs      the number of test runs
     */
    public PriorityQueue4aryBenchmark(int M, int numInsert, int numRemove, int runs) {
        this.M = M;
        this.numInsert = numInsert;
        this.numRemove = numRemove;
        this.runs = runs;
    }

    /**
     * Execute a series of operations on the given PriorityQueue4ary instance:
     * 1. Insert numInsert random integers;
     * 2. Delete numRemove elements.
     *
     * @param pq the PriorityQueue4ary instance
     */
    private void runPQTest(PriorityQueue4ary<Integer> pq) {
        Random rand = new Random();
        for (int i = 0; i < numInsert; i++) {
            pq.give(rand.nextInt());
        }
        for (int i = 0; i < numRemove; i++) {
            try {
                pq.take();
            } catch (PQException e) {
                // Exit the deletion loop if the queue is empty
                break;
            }
        }
    }

    /**
     * Test the basic implementation (without using Floyd's trick, i.e., floyd = false).
     *
     * @return the average runtime in milliseconds
     */
    public double benchmarkBasic() {
        // Create a PriorityQueue4ary instance with the floyd parameter set to false
        Supplier<PriorityQueue4ary<Integer>> supplier =
                () -> new PriorityQueue4ary<Integer>(M, true, Comparator.naturalOrder(), false);
        Benchmark_Timer<PriorityQueue4ary<Integer>> benchmark = new Benchmark_Timer<>(
                "PriorityQueue4ary Basic (floyd = false)",
                pq -> pq,           // Preprocessing function (does nothing extra)
                pq -> runPQTest(pq),// Run the test operations
                null);
        double time = benchmark.runFromSupplier(supplier, runs);
        System.out.printf("PriorityQueue4ary Basic (floyd = false) average time: %.3f ms%n", time);
        return time;
    }

    /**
     * Test the implementation using Floyd's trick (floyd = true, i.e., using the snake method).
     *
     * @return the average runtime in milliseconds
     */
    public double benchmarkFloyd() {
        // Create a PriorityQueue4ary instance with the floyd parameter set to true
        Supplier<PriorityQueue4ary<Integer>> supplier =
                () -> new PriorityQueue4ary<Integer>(M, true, Comparator.naturalOrder(), true);
        Benchmark_Timer<PriorityQueue4ary<Integer>> benchmark = new Benchmark_Timer<>(
                "PriorityQueue4ary with Floyd's trick (floyd = true)",
                pq -> pq,
                pq -> runPQTest(pq),
                null);
        double time = benchmark.runFromSupplier(supplier, runs);
        System.out.printf("PriorityQueue4ary with Floyd's trick (floyd = true) average time: %.3f ms%n", time);
        return time;
    }

    public static void main(String[] args) {
        // Example: Using capacity 4095, inserting 16,000 elements, deleting 4,000 elements, and running tests 200 times.
        PriorityQueue4aryBenchmark benchmark = new PriorityQueue4aryBenchmark(4095, 16000, 4000, 200);
        benchmark.benchmarkBasic();
        benchmark.benchmarkFloyd();
        PriorityQueue4aryBenchmark benchmark2 = new PriorityQueue4aryBenchmark(4095, 16000, 4000, 400);
        benchmark2.benchmarkBasic();
        benchmark2.benchmarkFloyd();
        PriorityQueue4aryBenchmark benchmark3 = new PriorityQueue4aryBenchmark(4095, 16000, 4000, 800);
        benchmark3.benchmarkBasic();
        benchmark3.benchmarkFloyd();
        PriorityQueue4aryBenchmark benchmark4 = new PriorityQueue4aryBenchmark(4095, 16000, 4000, 1600);
        benchmark4.benchmarkBasic();
        benchmark4.benchmarkFloyd();

        PriorityQueue4aryBenchmark benchmark5 = new PriorityQueue4aryBenchmark(4095, 16000, 4000, 3200);
        benchmark5.benchmarkBasic();
        benchmark5.benchmarkFloyd();
    }
}
