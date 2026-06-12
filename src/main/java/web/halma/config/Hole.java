package web.halma.config;

import javafx.scene.shape.Circle;
import lombok.Getter;
import web.halma.controllers.BoardController;
import web.halma.models.board.field.Field;
import web.halma.models.board.field.FieldState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hole {
    @Getter
    private final Circle circle;
    @Getter
    private final int[] coordinates;
    private Field field;
    @Getter
    private final List<Hole> neighbours = new ArrayList<>();

    public Field getField() {
        return field;
    }

    public Hole(Circle circle, int[] coordinates) {
        this.circle = circle;
        this.coordinates = coordinates;
        this.field = new Field(FieldState.FREE);
        clickHandler();
    }

    public boolean HoleIsOccupied(){
        return field.isOccupied();
    }

    public Circle getCircle() {
        return circle;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setOccupied(boolean occupied) {
        if(occupied){
            FieldState state = FieldState.OCCUPIED;
            this.field.setState(state);
        }
        else{
            FieldState state = FieldState.FREE;
            this.field.setState(state);
        }

    }

    public void findNeighbours() {
        int[][] neighboursCoordinates = {
                {1, 0, -1},   // left
                {1, -1, 0},   // up-left
                {0, -1, 1},   // up-right
                {-1, 0, 1},   // right
                {-1, 1, 0},   // down-right
                {0, 1, -1}    // down-left
        };
      for(int i = 0; i<neighboursCoordinates.length; i++){
          int[] newCoordinate = new int[]{coordinates[0] + neighboursCoordinates[i][0],
                  coordinates[1] + neighboursCoordinates[i][1], coordinates[2] + neighboursCoordinates[i][2]};
          Hole neighbour = BoardController.getHole(newCoordinate);
          if(neighbour != null){
              neighbours.add(neighbour);
          }
      }
    }


    public void clickHandler(){
        this.circle.setOnMouseClicked(event -> {
           if(!field.isOccupied()){
               if(BoardController.selected != null){
                   BoardController.selected.move(this);
               }
           }
            event.consume();
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hole)) return false;
        Hole hole = (Hole) o;
        return Arrays.equals(this.getCoordinates(), hole.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getCoordinates());
    }

}
