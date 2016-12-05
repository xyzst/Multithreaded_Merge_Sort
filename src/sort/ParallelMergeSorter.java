package sort;

import java.util.Comparator;

/**
 * ParallelMergeSorter class performs the Merge Sort algorithm in a "divide and conquer" manner using multithreading.
 * The maximum number of threads used is dependent on the system's available processors (physical + logical)
 *
 * @author Darren Rambaud
 * @version December 7, 2016
 */

public class ParallelMergeSorter extends MergeSorter {

    private static int determineDepth(int x) {
        if ((x & (x - 1)) == 0) { // check if the number of threads is a power of 2
            for (int j = 0; j < 100; ++j) { // finding the power of 2
                if (Math.pow(2, j) == x) {
                    return j;
                }
            }
        }
        return 1; // worst case scenario, the program will run serially
    }

    /**
     * sort method is similar to the super classes' implementation however has an extra parameter to include the number
     * of available processors.
     *
     * @param a refers to the array to be sorted
     * @param comp refers to the Comparator object that is defined in the callee
     * @param threads refers to the number of threads that is available
     */
    public static <E> void sort(E[] a, Comparator<? super E> comp, int threads){
        int depth = determineDepth(threads);

        parallelMergeSort(a, 0, a.length - 1, comp, depth);
    }

    /**
     * parallelMergeSort is similar to the super classes mergeSort method however utilizes multithreading to divide the
     * work evenly amongst the available threads. The method works with a variety of data types (ints, doubles ...)
     * as long as there is a properly defined Comparator object.
     *
     * @param a refers to the array to be sorted recursively
     * @param from indicates the index of the beginning of the array to sort
     * @param to indicates the index of the last element of the array to sort
     * @param comp is the Comparator object which is definined in the ParallelSort class
     * @param depth refers to the number of levels to allocate threads
     */
    private static <E> void parallelMergeSort(E[] a, int from, int to, Comparator<? super E> comp, int depth) {
        if (from == to) {
            return;
        }
        if (depth == 0) {
            mergeSort(a, from, to, comp); // end of recursion, sort the array in a serial fashion
            return;
        }

        int mid = (from + to) / 2;
        // Sort the first and the second half
        Thread former = new Thread() {
            public void run() {
                parallelMergeSort(a, from, mid, comp, depth - 1);
            }
        };
        Thread latter = new Thread() {
            public void run() {
                parallelMergeSort(a, mid + 1, to, comp, depth - 1);
            }
        };

        former.start();
        latter.start();

        try {
            former.join();
            latter.join();
        }
        catch (InterruptedException i) {
            i.printStackTrace();
        }

        merge(a, from, mid, to, comp);
    }
}