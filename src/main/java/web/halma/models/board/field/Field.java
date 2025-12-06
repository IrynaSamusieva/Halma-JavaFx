package web.halma.models.board.field;
public class Field {
    protected int row;
    protected int diagonal;
    protected FieldColor color;
    protected FieldState state;

    public Field(FieldState state) {
        this.state = state;
    }

    public boolean isOccupied(){
        return this.state == FieldState.OCCUPIED;
    }

    public void setState(FieldState state) {
        this.state = state;
    }
}
