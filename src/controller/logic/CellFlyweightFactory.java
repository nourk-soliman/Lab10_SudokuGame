package controller.logic;

public class CellFlyweightFactory {

    private static final CellFlyweight[] cache = new CellFlyweight[5];
    private static int index = 0;

    public static CellFlyweight getCell(int row, int col) {
        if (index >= 5) {
            throw new IllegalStateException("Only 5 flyweights allowed");
        }
        cache[index] = new CellFlyweight(row, col);
        return cache[index++];
    }
    
    public static void reset() {
        index = 0;
    }
}