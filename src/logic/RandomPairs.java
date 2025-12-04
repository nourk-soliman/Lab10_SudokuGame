/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;
import java.util.ArrayList;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Random;
 import java.util.Set;
/**
 *
 * @author Ali
 */
public class RandomPairs {
    // Range 0..80 for both x and y
    private static final int MAX_COORD = 80;
    private static final int MAX_UNIQUE_PAIRS = (MAX_COORD + 1) * (MAX_COORD + 1);
    private final Random random;
    public RandomPairs() {
        this.random = new Random();
           }
    /**
     * Generate n distinct random pairs (x, y) where 0 <= x <= 80 and 0 <= y <= 80.
     * @param n
     * @return 
     */
    public List<int[]> generateDistinctPairs(int n) {
        if (n < 0 || n > MAX_UNIQUE_PAIRS) {
            throw new IllegalArgumentException(
                "n must be between 0 and " + MAX_UNIQUE_PAIRS + " (inclusive)");
        }
        Set<Integer> used = new HashSet<>();
        List<int[]> result = new ArrayList<>(n);
        while (result.size() < n) {
            int x = random.nextInt(MAX_COORD + 1); // 0..80
            int y = random.nextInt(MAX_COORD + 1); // 0..80
            // Encode pair (x, y) as a single int to track uniqueness
            int key = x * (MAX_COORD + 1) + y;
            if (used.add(key)) {
                result.add(new int[] { x, y });
            }
        }
        return result;
    }
}
