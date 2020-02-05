package com.learning;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MathsTest {

    @Test
    public void testSumOfTwoPositiveNumbers() throws Exception {
        System.out.println("testSumOfTwoPositiveNumbers");
        assertEquals(6, Maths.sumOfTwoPositiveNumbers(3,3));
    }
    @Test
    public void testSumOfTwoNegativeNumbers() throws Exception {
        System.out.println("testSumOfTwoNegativeNumbers");
        assertEquals(-1, Maths.sumOfTwoPositiveNumbers(-3,-3));
    }
}