// package edu.vt.ece.hw5;

// import java.util.Arrays;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
// import java.util.concurrent.Callable;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
// import java.util.concurrent.ThreadLocalRandom;

// import edu.vt.ece.hw5.sets.CoarseSet;
// import edu.vt.ece.hw5.sets.FineSet;
// import edu.vt.ece.hw5.sets.LazySet;
// import edu.vt.ece.hw5.sets.LockFreeSet;
// import edu.vt.ece.hw5.sets.OptimisticSet;
// import edu.vt.ece.hw5.sets.Set;

// public class Benchmark {
//     private static final int UPPER_BOUND = 100;
//     private static final int ITERATIONS = 10000;
//     private static final int BYTE_PADDING = 64;      // To avoid false sharing.
//     private static final float ADD_LIMIT = 0.1f;     // Modify this per your requirement.
//     private static final float REMOVE_LIMIT = 0.2f;  // Modify this per your requirement.

//     private static Set<Integer> mySet;
//     private static boolean[] containsResults;

//     public static void main(String[] args) throws Throwable {
//         mySet = getSet(args[0]); // SetType
//         int threadCount = Integer.parseInt(args[1]); // ThreadCount

//         containsResults = new boolean[threadCount * BYTE_PADDING];
//         List<Callable<Long>> calls = getCallables(threadCount);
//         ExecutorService excs = Executors.newFixedThreadPool(threadCount);
        
//         long nanos = 0;
//         for (Future<Long> f : excs.invokeAll(calls)) {
//             try {
//                 nanos += f.get();
//             } catch (ExecutionException e) {
//                 throw e.getCause(); 
//             }
//         }

//         System.out.println(Arrays.toString(containsResults));
//         System.out.println(nanos);
//     }

//     private static Set<Integer> getSet(String setType) {
//         switch(SetType.valueOf(setType)) {
//             case CoarseSet:
//                 return new CoarseSet<>();
//             case FineSet:
//                 return new FineSet<>();
//             case LazySet:
//                 return new LazySet<>();
//             case LockFreeSet:
//                 return new LockFreeSet<>();
//             case OptimisticSet:
//                 return new OptimisticSet<>();
//         }

//         return null;
//     }

//     private static List<Callable<Long>> getCallables(int threadCount) {
//         List<Callable<Long>> calls = new ArrayList<>(threadCount);

//         for (int i = 0; i < threadCount; i++) {
//             final int x = i;
//             calls.add(() -> doStuff(x));
//         }

//         return calls;
//     }

//     /* Invoke operations of set: add, remove, contains based on configurable
//     ADD_LIMIT and REMOVE_LIMIT.

//     Hint: Use System.nanoTime and ThreadLocalRandom.current for execution time
//     and Random number generation (for randomizing set operations). */
//     private static long doStuff(int index) {
        
//         /* YOUR IMPLEMENTATION HERE */
//         //Execution time;
//     }
// }
/*package edu.vt.ece.hw5;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import edu.vt.ece.hw5.sets.CoarseSet;
import edu.vt.ece.hw5.sets.FineSet;
import edu.vt.ece.hw5.sets.LazySet;
import edu.vt.ece.hw5.sets.LockFreeSet;
import edu.vt.ece.hw5.sets.OptimisticSet;
import edu.vt.ece.hw5.sets.Set;

public class Benchmark {
    private static final int UPPER_BOUND = 100;
    private static final int ITERATIONS = 10000;
    private static final int BYTE_PADDING = 64;      // To avoid false sharing.
    private static final float ADD_LIMIT = 0.1f;     // Probability of add operation.
    private static final float REMOVE_LIMIT = 0.2f;  // Probability of remove operation.

    private static Set<Integer> mySet;
    private static boolean[] containsResults;

    public static void main(String[] args) throws Throwable {
        mySet = getSet(args[0]); // SetType
        int threadCount = Integer.parseInt(args[1]); // ThreadCount

        containsResults = new boolean[threadCount * BYTE_PADDING];
        List<Callable<Long>> calls = getCallables(threadCount);
        ExecutorService excs = Executors.newFixedThreadPool(threadCount);
        
        long nanos = 0;
        for (Future<Long> f : excs.invokeAll(calls)) {
            try {
                nanos += f.get();
            } catch (ExecutionException e) {
                throw e.getCause(); 
            }
        }

        excs.shutdown();
        // System.out.println(Arrays.toString(containsResults));
        System.out.println("Total execution time (ns): " + nanos);
    }

    private static Set<Integer> getSet(String setType) {
        switch(SetType.valueOf(setType)) {
            case CoarseSet:
                return new CoarseSet<>();
            case FineSet:
                return new FineSet<>();
            case LazySet:
                return new LazySet<>();
            case LockFreeSet:
                return new LockFreeSet<>();
            case OptimisticSet:
                return new OptimisticSet<>();
        }

        return null;
    }

    private static List<Callable<Long>> getCallables(int threadCount) {
        List<Callable<Long>> calls = new ArrayList<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int x = i;
            calls.add(() -> doStuff(x));
        }

        return calls;
    }

    private static long doStuff(int index) {
        long startTime = System.nanoTime();
        Random random = ThreadLocalRandom.current();

        for (int i = 0; i < ITERATIONS; i++) {
            int value = random.nextInt(UPPER_BOUND);
            float operation = random.nextFloat();

            if (operation < ADD_LIMIT) {
                mySet.add(value);
            } else if (operation < REMOVE_LIMIT) {
                mySet.remove(value);
            } else {
                containsResults[index * BYTE_PADDING] = mySet.contains(value);
            }
        }

        return System.nanoTime() - startTime;
    }
} */

package edu.vt.ece.hw5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import edu.vt.ece.hw5.sets.CoarseSet;
import edu.vt.ece.hw5.sets.FineSet;
import edu.vt.ece.hw5.sets.LazySet;
import edu.vt.ece.hw5.sets.LockFreeSet;
import edu.vt.ece.hw5.sets.OptimisticSet;
import edu.vt.ece.hw5.sets.Set;

public class Benchmark {
    private static final int UPPER_BOUND = 100;
    private static final int BYTE_PADDING = 64;      // To avoid false sharing.

    private static Set<Integer> mySet;
    private static boolean[] containsResults;

    // public static void main(String[] args) throws Throwable {
    //     mySet = getSet(args[0]);                // SetType (CoarseSet, FineSet, etc.)
    //     int threadCount = Integer.parseInt(args[1]); // ThreadCount
    //     int iterations = Integer.parseInt(args[2]);  // Number of iterations per thread
    //     float containsRatio = Float.parseFloat(args[3]) / 100;  // Percentage of contains operations (e.g., 0.6 for 60%)

    //     containsResults = new boolean[threadCount * BYTE_PADDING];
    //     List<Callable<Long>> calls = getCallables(threadCount, containsRatio, iterations);
    //     ExecutorService excs = Executors.newFixedThreadPool(threadCount);

    //     long totalNanos = 0;
    //     for (Future<Long> f : excs.invokeAll(calls)) {
    //         try {
    //             totalNanos += f.get();
    //         } catch (ExecutionException e) {
    //             throw e.getCause(); 
    //         }
    //     }

    //     excs.shutdown();
    //     // System.out.println("Contains results: " + Arrays.toString(containsResults));
    //     System.out.println("Total execution time (ns): " + totalNanos);
    // }
    public static void main(String[] args) throws Throwable {
        mySet = getSet(args[0]);                 // SetType (e.g., "CoarseSet", "FineSet", etc.)
        int threadCount = Integer.parseInt(args[1]); // ThreadCount
        int iterations = Integer.parseInt(args[2]);  // Number of iterations per thread
        float containsRatio = Float.parseFloat(args[3]) / 100;  // Percentage of contains operations (e.g., 0.6 for 60%)
    
        int numRuns = 5;  // Number of runs to calculate average
        long totalNanos = 0;
    
        for (int run = 1; run <= numRuns; run++) {
            containsResults = new boolean[threadCount * BYTE_PADDING];
            List<Callable<Long>> calls = getCallables(threadCount, containsRatio, iterations);
            ExecutorService excs = Executors.newFixedThreadPool(threadCount);
    
            long nanos = 0;
            for (Future<Long> f : excs.invokeAll(calls)) {
                try {
                    nanos += f.get();
                } catch (ExecutionException e) {
                    throw e.getCause(); 
                }
            }
            excs.shutdown();
    
            System.out.println("Run " + run + ": Total execution time (ns): " + nanos);
            totalNanos += nanos;
        }
    
        long averageNanos = totalNanos / numRuns;
        System.out.println("Average execution time over " + numRuns + " runs (ns): " + averageNanos);
    }
    

    private static Set<Integer> getSet(String setType) {
        switch (SetType.valueOf(setType)) {
            case CoarseSet:
                return new CoarseSet<>();
            case FineSet:
                return new FineSet<>();
            case LazySet:
                return new LazySet<>();
            case LockFreeSet:
                return new LockFreeSet<>();
            case OptimisticSet:
                return new OptimisticSet<>();
            default:
                throw new IllegalArgumentException("Unknown set type: " + setType);
        }
    }

    private static List<Callable<Long>> getCallables(int threadCount, float containsRatio, int iterations) {
        List<Callable<Long>> calls = new ArrayList<>(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            calls.add(() -> performOperations(index, containsRatio, iterations));
        }

        return calls;
    }

    private static long performOperations(int index, float containsRatio, int iterations) {
        long startTime = System.nanoTime();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        float addRemoveRatio = (1 - containsRatio) / 2;
        for (int i = 0; i < iterations; i++) {
            int value = random.nextInt(UPPER_BOUND);
            float operation = random.nextFloat();

            if (operation < containsRatio) {
                containsResults[index * BYTE_PADDING] = mySet.contains(value);
            } else if (operation < containsRatio + addRemoveRatio) {
                mySet.add(value);
            } else {
                mySet.remove(value);
            }
        }

        return System.nanoTime() - startTime;
    }
}

