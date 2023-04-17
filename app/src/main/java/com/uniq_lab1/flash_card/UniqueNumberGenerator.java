package com.uniq_lab1.flash_card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueNumberGenerator {
    private List<Integer> numbers; // A list to store the numbers to be returned

    public UniqueNumberGenerator(int minNumber, int maxNumber) {
        numbers = new ArrayList<>(); // Initialize the list
        // Populate the list with all numbers between minNumber and maxNumber (inclusive)
        for (int i = minNumber; i <= maxNumber; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers); // Randomly shuffle the list
    }

    public int getNextNumber() {
        if (numbers.isEmpty()) { // If there are no more numbers to return
            throw new IllegalStateException("All numbers have been returned");
        }
        return numbers.remove(0); // Remove and return the first number in the shuffled list
    }
}