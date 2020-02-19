package edu.ucsd.javaiv.assignment2;

/**
 * Class containing two binary search methods, identical other than that one
 * contains one more bug than the other.
 */
public class BinarySearch
{

    /**
     * Buggy binary search method from an old version of java.util.Collections,
     * copied and pasted verbatim from online assignment
     *
     * @param a a sorted array of integers
     * @param key the value to search for in the array a
     * @return the index in the array a corresponding to the key if found
     * @deprecated Use {@link #modifiedBinarySearch(int[], int)} instead
     */
    @Deprecated
    public static int origBinarySearch(int[] a, int key)
    {
        int low = 0;
        int high = a.length - 1;

        while (low <= high)
        {
            int mid = (low + high) / 2;
            int midVal = a[mid];

            if (midVal < key)
            {
                low = mid + 1;
            } else
            {
                if (midVal > key)
                {
                    high = mid - 1;
                } else
                {
                    return mid; // key found 
                }
            }
        }
        return -(low + 1);  // key not found. 
    }

    /**
     * Copy of {@link #origBinarySearch(int[], int)} with one bug fixed
     *
     * @param a a sorted array of integers
     * @param key the value to search for in the array a
     * @return the index in the array a corresponding to the key if found
     */
    public static int modifiedBinarySearch(int[] a, int key)
    {
        int low = 0;
        int high = a.length - 1;

        while (low <= high)
        {
            int mid = (high - low) / 2 + low;
            int midVal = a[mid];

            if (midVal < key)
            {
                low = mid + 1;
            } else
            {
                if (midVal > key)
                {
                    high = mid - 1;
                } else
                {
                    return mid; // key found 
                }
            }
        }
        return -(low + 1);  // key not found. 
    }

    /**
     * Driver method.
     * <p>
     * For fun, uses the binary search method to test whether the specified
     * integer is among the first several Fibonacci numbers.
     *
     * @param args Command line arguments, consisting of a single field
     * containing the integer to be tested.
     */
    public static void main(String[] args)
    {

        int arrayLength = 47;
        if (args.length != 1 || args[0] == "-h" || args[0] == "--help")
        {
            System.err.println("Usage: Determine whether the specified integer is among the first "
                    + arrayLength
                    + " Fibonacci numbers, and specify its rank if so.");
            return;
        }

        int key = Integer.valueOf(args[0]);

        int[] arr = new int[arrayLength];
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i < arrayLength; i++)
        {
            arr[i] = arr[i - 1] + arr[i - 2];  // Recursive definition of Fibonacci Sequence
        }
        int index = BinarySearch.modifiedBinarySearch(arr, key);
        int outputIndex = index + 1;
        if (index >= 0)
        {
            System.out.println(key + " is the " + outputIndex + "th Fibonacci number");
        } else
        {
            System.out.println(key + " is not among the lowest "
                    + arrayLength
                    + " Fibonacci numbers");
        }

        return;

    }

}
