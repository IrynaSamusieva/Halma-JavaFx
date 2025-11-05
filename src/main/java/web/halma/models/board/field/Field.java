package web.halma.models.board.field;

public class Field {
    protected int row;
    protected int diagonal;
    protected FieldColor color;
    protected FieldState state;

    public boolean isLegal(FieldState state){
        return state != state.ILLEGAL;
    }
    
    public boolean isFree(FieldState state){
        return state == FieldState.FREE;
    }

    public boolean isOccupied(FieldState state){
        return state == FieldState.OCCUPIED;
    }

    public FieldColor getColor() {
        return color;
    }

    public int getDiagonal() {
        return diagonal;
    }

    public int getRow() {
        return row;
    }

    public void setState(FieldState state) {
        this.state = state;
    }
}
