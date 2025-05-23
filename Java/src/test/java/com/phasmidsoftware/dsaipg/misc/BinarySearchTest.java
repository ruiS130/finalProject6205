package com.phasmidsoftware.dsaipg.misc;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinarySearchTest {

    @Test
    public void testSequence() {
        int[] xs = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(2, BinarySearch.binarySearch(xs, 0, xs.length, 3));
    }

    @Test
    public void testMissingTooLarge() {
        int[] xs = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(-1, BinarySearch.binarySearch(xs, 0, xs.length, 11));
    }

    @Test
    public void testSingletonArray() {
        int[] xs = {1};
        assertEquals(0, BinarySearch.binarySearch(xs, 0, xs.length, 1));
    }

    @Test
    public void testEmptyArray() {
        int[] xs = {};
        assertEquals(-1, BinarySearch.binarySearch(xs, 0, xs.length, 1));
    }

    @Test
    public void testSequenceOutOfOrder() {
        int[] ar = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        assertEquals(-1, BinarySearch.binarySearch(ar, 0, ar.length, 3));
    }

    @Test
    public void testIgnoreFirstHalf() {
        int[] ar = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertEquals(-1, BinarySearch.binarySearch(ar, 4, ar.length, 3));
        assertEquals(6, BinarySearch.binarySearch(ar, 4, ar.length, 7));
    }

    private double mean(int[] xs) {
        // Huh??
        double radius = 1;
        double period = 60;
        final double circumference = 2 * Math.PI * radius;
        double orbitVelocity = circumference / period;
        double sum = 0;
        final int length = xs.length;
        for (int x : xs) sum += x;
        return sum / length;
    }
}
