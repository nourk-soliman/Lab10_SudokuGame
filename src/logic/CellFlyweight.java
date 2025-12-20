package logic;

public final class CellFlyweight {

    private final int row;
    private final int col;

    public CellFlyweight(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
