package sort;

import java.util.Arrays;
import java.util.Comparator;

/**
 * ParallelSortTester class will run sorting simulations using the parallelized ParallelMergeSorter class and
 * assesses the speedup from using multiple threads (serial run time is included for comparison). Each trial
 * times the sorting speed with different sizes of random number arrays (using the super class's createRandomArray
 * method)
 *
 * @author Darren Rambaud
 * @version December 7, 2016
 */
public class ParallelSortTester extends SortTester {

    /**
     * main method simply calls the runParallelSortTester() method
     */
    public static void main(String[] args) {
        runParallelSortTester();
    }

    /**
     * runParallelSortTester method defines the parameters of each experiment (number of threads) and prints out the
     * run time of each experiment. In each experiment, the array is checked if it has been sorted correctly.
     */
    public static void runParallelSortTester() {
        final int INITIAL_LEN = 1000,   // initial length of array to sort
                  ROUNDS = 15,
                  AVAIL_PROCESSORS = Runtime.getRuntime().availableProcessors();

        Integer[] a;

        Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer d1, Integer d2) {
                return d1.compareTo(d2);
            }
        };

        System.out.printf("SYSTEM INFORMATION: \n\tAvailable Processors == %d\n\n", AVAIL_PROCESSORS);
        for (int i = 1; i <= AVAIL_PROCESSORS; i*=2) {
            System.out.printf("Running trial with %d thread(s) ...\n", i);
            for (int q = 0, j = INITIAL_LEN; q < ROUNDS; ++q, j*=2) {
                a = createRandomArray(j);
                // run the algorithm and time how long it takes to sort the elements
                long startTime = System.currentTimeMillis();
                ParallelMergeSorter.sort(a, comp, i);
                long endTime = System.currentTimeMillis();

                if (!isSorted(a, comp)) {
                    throw new RuntimeException("not sorted afterward: " + Arrays.toString(a));
                }

                System.out.printf("%10d elements  =>  %6d ms \n", j, endTime - startTime);
            }
            System.out.print("\n");
        }
    }
}