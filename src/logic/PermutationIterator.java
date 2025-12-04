/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author AltAWKEl
 */
public class PermutationIterator implements Iterator<int[]> {

    private final int positions = 5;
    private final int total = 60000;
    private int index = 0;
    private final int[] buffer = new int[positions];

    public PermutationIterator() {
    }

    @Override
    public boolean hasNext() {
        return index < total;
    }

    @Override
    public int[] next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        int v = index;
        for (int i = 0; i < positions; i++) {
            buffer[i] = (v % 9) + 1;
            v /= 9;
        }

        index++;
        return buffer;
    }

}
