package com.learning;

public class Maths {

    public static int sumOfTwoPositiveNumbers(int firstNumber, int secondNumber){
        if (firstNumber < 0 || secondNumber < 0)
            return -1;
        return firstNumber+secondNumber;
    }
}
