package web.halma.models.board.field;



public class Field {
    protected int row;
    protected int diagonal;
    protected FieldColor color;
    protected FieldState state;

    public Field(FieldState state) {
        this.state = state;
    }

    public boolean isFree(){
        return this.state == FieldState.FREE;
    }

    public boolean isOccupied(){
        return this.state == FieldState.OCCUPIED;
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
