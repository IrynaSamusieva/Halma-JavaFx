package web.halma.config;

import javafx.scene.shape.Circle;
import lombok.Getter;
import web.halma.models.board.field.Field;
import web.halma.models.board.field.FieldColor;
import web.halma.models.board.field.FieldState;

import java.util.ArrayList;
import java.util.List;

public class Hole {
    @Getter
    private final Circle circle;
    @Getter
    private final int[] coordinates;
    private Field field;
    @Getter
    private final List<Hole> neighbours = new ArrayList<>();

    public Hole(Circle circle, int[] coordinates, FieldState state) {
        this.circle = circle;
        this.coordinates = coordinates;
        this.field = field;
       // clickHandler();
    }

    public boolean HoleIsOccupied(Field field){
        return field.isOccupied(FieldState.OCCUPIED);
    }

    public void findNeighbours() {
        int[][] neighboursCoordinates = {
                {1, 0, -1},   // right
                {1, -1, 0},   // up-right
                {0, -1, 1},   // up-left
                {-1, 0, 1},   // left
                {-1, 1, 0},   // down-left
                {0, 1, -1}    // down-right
        };
    }



    /*
    public void clickHandler()
     */
}
