package web.halma.config;

import javafx.scene.shape.Circle;
import lombok.Getter;
import web.halma.controllers.BoardController;
import web.halma.models.board.field.Field;
import web.halma.models.board.field.FieldColor;
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
                {1, 0, -1},   // right
                {1, -1, 0},   // up-right
                {0, -1, 1},   // up-left
                {-1, 0, 1},   // left
                {-1, 1, 0},   // down-left
                {0, 1, -1}    // down-right
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
            System.out.println("Клик по ЛУНКЕ: " + Arrays.toString(coordinates));
           if(!field.isOccupied()){
               System.out.println("Лунка свободна.");
               if(BoardController.selected != null){
                   System.out.println("Шашка выбрана, пытаюсь ходить...");
                   BoardController.selected.move(this);
               }
               else {
                   System.out.println("Шашка НЕ выбрана (selected == null)"); // <---
               }
           } else {
               System.out.println("Лунка занята!"); // <---
           }
            event.consume();
        });
    }

}
