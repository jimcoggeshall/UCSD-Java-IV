package edu.ucsd.javaiv.assignment2;

import edu.ucsd.javaiv.assignment2.BinarySearch;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class containing JUnit tests for {@link BinarySearch}, for illustrating the
 * conditions under which the bug in
 * {@link BinarySearch#origBinarySearch(int[], int)} is exposed, and for
 * demonstrating that this bug is fixed in
 * {@link BinarySearch#modifiedBinarySearch(int[], int)}.
 * <p>
 * The strategy is to allocate a sorted array of integers such that, for at
 * least one pair of zero-counted indices <code>i</code> and <code>j</code> of
 * elements in the array, the sum <code>i + j</code> is greater than the maximum
 * <code>int</code> value <code>2^31 - 1</code>. As the binary search method
 * involves computing sums of indices in this manner based on the key value, one
 * of the methods here uses a key that creates a set of conditions under which
 * the binary search method works as expected. Another repeats the same
 * experiment but with a slightly different key, causing the buggy behavior to
 * take place.
 *
 * @author jcc
 */
public class TestBinarySearch
{

    // Array for which the sum of highest and second-highest indices is Integer.MAX_VALUE + 1
    private static final int arrLength = Integer.MAX_VALUE / 2 + 2;
    private static int[] arr = new int[arrLength];

    static
    {
        for (int i = 1; i < arrLength; i++)
        {
            arr[i] = indexToValue(i);
        }
    }

    private static int indexToValue(int index)
    {
        int value = index + 100;  // Doesn't really matter; want monotonic and no overflow
        return value;
    }

    /**
     * Choose a key that will <i>just barely</i> avoid computing a sum in {@link
     * BinarySearch#origBinarySearch(int[], int)} large enough to trigger the
     * bug, and assert that the method works as expected in this case
     */
    @Test
    public void testOrigBinarySearchLargestWorkingCase()
    {

        int indexToFind = arrLength - 2;  // Will require adding third- and second-highest indices
        int key = indexToValue(indexToFind);

        int index = -1;
        boolean caughtException = false;
        try
        {
            index = BinarySearch.origBinarySearch(arr, key);
        } catch (ArrayIndexOutOfBoundsException e)
        {
            caughtException = true;
        }

        assertFalse(caughtException);
        assertEquals(index, indexToFind);
    }

    /**
     * Choose a key that will necessitate computing a sum in {@link
     * BinarySearch#origBinarySearch(int[], int)} large enough to trigger the
     * bug, and assert that the method throws the relevant exception
     */
    @Test
    public void testOrigBinarySearchMinimalBrokenCase()
    {

        int indexToFind = arrLength - 1;  // Will require adding highest and second-highest indices
        int key = indexToValue(indexToFind);

        int index = -1;
        boolean caughtException = false;
        try
        {
            index = BinarySearch.origBinarySearch(arr, key);
        } catch (ArrayIndexOutOfBoundsException e)
        {
            caughtException = true;
        }

        assertTrue(caughtException);
        assertEquals(index, -1);

    }

    /**
     * Exactly the same test as {@link #testOrigBinarySearchMinimalBrokenCase()}
     * but using the modified method
     * {@link BinarySearch#modifiedBinarySearch(int[], int)}
     */
    @Test
    public void testModifiedBinarySearchMinimalBrokenCaseFixed()
    {

        int indexToFind = arrLength - 1;  // Will require adding highest and second-highest indices
        int key = indexToValue(indexToFind);

        int index = -1;
        boolean caughtException = false;
        try
        {
            index = BinarySearch.modifiedBinarySearch(arr, key);
        } catch (ArrayIndexOutOfBoundsException e)
        {
            caughtException = true;
        }

        assertFalse(caughtException);
        assertEquals(index, indexToFind);

    }

}
