package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.benchmark.Benchmark_Timer;
import com.phasmidsoftware.dsaipg.util.benchmark.Stopwatch;
import com.phasmidsoftware.dsaipg.util.benchmark.TimeLogger;
//import com.phasmidsoftware.dsaipg.util.Utilities;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class PriorityQueueBenchmark {

    // Heap capacity (maximum number of elements held simultaneously)
    private final int M;
    // Number of insert operations (e.g., 16,000 times)
    private final int numInsert;
    // Number of delete operations (e.g., 4,000 times)
    private final int numRemove;
    // Number of test runs
    private final int runs;

    /**
     * Constructor.
     *
     * @param M         the capacity of the priority queue (maximum number of elements held simultaneously)
     * @param numInsert the number of insert operations
     * @param numRemove the number of delete operations
     * @param runs      the number of test runs (used to obtain an average)
     */
    public PriorityQueueBenchmark(int M, int numInsert, int numRemove, int runs) {
        this.M = M;
        this.numInsert = numInsert;
        this.numRemove = numRemove;
        this.runs = runs;
    }

    /**
     * Executes a series of operations on the given priority queue:
     * 1. Inserts numInsert random integers;
     * 2. Performs numRemove delete operations.
     *
     * @param pq the priority queue instance
     */
    private void runPQTest(PriorityQueue<Integer> pq) {
        Random rand = new Random();
        for (int i = 0; i < numInsert; i++) {
            pq.give(rand.nextInt());
        }
        for (int j = 0; j < numRemove; j++) {
            try {
                pq.take();
            } catch (PQException e) {
                // If the queue is empty, exit the deletion loop
                break;
            }
        }
    }

    /**
     * Benchmarks the basic binary heap implementation (without using Floyd's trick).
     *
     * @return the average runtime in milliseconds
     */
    public double benchmarkBasic() {
        // Set floyd parameter to false
        Supplier<PriorityQueue<Integer>> supplier = () -> new PriorityQueue<Integer>() {
            /**
             * Checks if the priority queue is empty.
             *
             * @return true if the priority queue contains no elements, false otherwise.
             */
            @Override
            public boolean isEmpty() {
                return false;
            }

            /**
             * Retrieves the number of elements currently stored in the priority queue.
             *
             * @return the number of elements in the priority queue.
             */
            @Override
            public int size() {
                return 0;
            }

            /**
             * Attempts to add the specified element to the priority queue.
             * If the key has low priority, it will be spilled.
             *
             * @param key the element to be added to the priority queue
             */
            @Override
            public void give(Integer key) {

            }

            /**
             * Removes and returns the highest-priority element from the priority queue.
             * If max is false (it's a minimum PQ), then this will result in the smallest item.
             * If the queue is empty, throws a PQException.
             *
             * @return the highest-priority element in the priority queue (assuming getMax() is true).
             * @throws PQException if the priority queue is empty.
             */
            @Override
            public Integer take() throws PQException {
                return 0;
            }

            /**
             * Constructs or initializes the underlying data structure for the Priority Queue
             * in order to prepare it for efficient insertion and extraction operations.
             * Typically used to build the heap representation of the priority queue based
             * on its current elements.
             * Useful for the heap construction phase of HeapSort.
             */
            @Override
            public void heapConstructor() {

            }

            /**
             * Retrieves the element at the specified position in the priority queue without removing it.
             * WARNING: this is primarily for testing -- not recommended for general use.
             *
             * @param k the position index of the element to peek at, where 0 represents the highest priority element.
             * @return the element at the specified position in the priority queue.
             */
            @Override
            public Integer peek(int k) {
                return 0;
            }

            /**
             * Determines if this is a max-Priority Queue.
             * WARNING: this is primarily for testing -- not recommended for general use.
             *
             * @return true if the maximum value is present, false otherwise.
             */
            @Override
            public boolean getMax() {
                return false;
            }
        };
//        Supplier<PriorityQueue<Integer>> supplier = () -> new PriorityQueue<Integer>(M, true, Comparator.naturalOrder(), false);

        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                "PriorityQueue Basic (binary heap)",
                pq -> pq,   // Preprocessing function (does nothing)
                this::runPQTest,
                null);
        double time = benchmark.runFromSupplier(supplier, runs);
        System.out.printf("Basic binary heap (floyd = false) average time: %.3f ms%n", time);
        return time;
    }

    /**
     * Benchmarks the binary heap implementation using Floyd's trick (i.e., calling the snake method).
     *
     * @return the average runtime in milliseconds
     */
    public double benchmarkFloyd() {
        // Set floyd parameter to true
//        Supplier<PriorityQueue<Integer>> supplier = () -> new PriorityQueue<Integer>(M, true, Comparator.naturalOrder(), true);
        Supplier<PriorityQueue<Integer>> supplier = () -> new PriorityQueue<Integer>() {
            /**
             * Checks if the priority queue is empty.
             *
             * @return true if the priority queue contains no elements, false otherwise.
             */
            @Override
            public boolean isEmpty() {
                return false;
            }

            /**
             * Retrieves the number of elements currently stored in the priority queue.
             *
             * @return the number of elements in the priority queue.
             */
            @Override
            public int size() {
                return 0;
            }

            /**
             * Attempts to add the specified element to the priority queue.
             * If the key has low priority, it will be spilled.
             *
             * @param key the element to be added to the priority queue
             */
            @Override
            public void give(Integer key) {

            }

            /**
             * Removes and returns the highest-priority element from the priority queue.
             * If max is false (it's a minimum PQ), then this will result in the smallest item.
             * If the queue is empty, throws a PQException.
             *
             * @return the highest-priority element in the priority queue (assuming getMax() is true).
             * @throws PQException if the priority queue is empty.
             */
            @Override
            public Integer take() throws PQException {
                return 0;
            }

            /**
             * Constructs or initializes the underlying data structure for the Priority Queue
             * in order to prepare it for efficient insertion and extraction operations.
             * Typically used to build the heap representation of the priority queue based
             * on its current elements.
             * Useful for the heap construction phase of HeapSort.
             */
            @Override
            public void heapConstructor() {

            }

            /**
             * Retrieves the element at the specified position in the priority queue without removing it.
             * WARNING: this is primarily for testing -- not recommended for general use.
             *
             * @param k the position index of the element to peek at, where 0 represents the highest priority element.
             * @return the element at the specified position in the priority queue.
             */
            @Override
            public Integer peek(int k) {
                return 0;
            }

            /**
             * Determines if this is a max-Priority Queue.
             * WARNING: this is primarily for testing -- not recommended for general use.
             *
             * @return true if the maximum value is present, false otherwise.
             */
            @Override
            public boolean getMax() {
                return false;
            }
        };

        Benchmark_Timer<PriorityQueue<Integer>> benchmark = new Benchmark_Timer<>(
                "PriorityQueue with Floyd's trick (binary heap)",
                pq -> pq,
                this::runPQTest,
                null);
        double time = benchmark.runFromSupplier(supplier, runs);
        System.out.printf("Binary heap with Floyd's trick (floyd = true) average time: %.3f ms%n", time);
        return time;
    }

    public static void main(String[] args) {
        // Example: Use capacity 4095, insert 16,000 elements, delete 4,000 elements, and run the test 200 times
        PriorityQueueBenchmark benchmark = new PriorityQueueBenchmark(4095, 16000, 4000, 200);
        benchmark.benchmarkBasic();
        benchmark.benchmarkFloyd();

        PriorityQueueBenchmark benchmark2 = new PriorityQueueBenchmark(4095, 16000, 4000, 400);
        benchmark2.benchmarkBasic();
        benchmark2.benchmarkFloyd();

        PriorityQueueBenchmark benchmark3 = new PriorityQueueBenchmark(4095, 16000, 4000, 800);
        benchmark3.benchmarkBasic();
        benchmark3.benchmarkFloyd();

        PriorityQueueBenchmark benchmark4 = new PriorityQueueBenchmark(4095, 16000, 4000, 1600);
        benchmark4.benchmarkBasic();
        benchmark4.benchmarkFloyd();

        PriorityQueueBenchmark benchmark5 = new PriorityQueueBenchmark(4095, 16000, 4000, 3200);
        benchmark5.benchmarkBasic();
        benchmark5.benchmarkFloyd();
    }
}
