package web.halma.config;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;

public class Piece extends Circle {
    private Color color;
    private String group;
    private static Map<String, Color> colors;
    private Hole hole;

    public Hole getHole() {
        return hole;
    }

    public Color getColor() {
        return color;
    }

    public String getGroup() {
        return group;
    }

    public static void init() {
        Map<String, Color> map = new HashMap<>();
        map.put("green", Color.GREEN);
        map.put("orange", Color.ORANGE);
        map.put("yellow", Color.YELLOW);
        map.put("magenta", Color.MAGENTA);
        map.put("blue", Color.BLUE);
        map.put("red", Color.RED);
        colors = Collections.unmodifiableMap(map);
    }

    public Piece(String color, Hole hole) {
        super(hole.getCircle().getLayoutX(), hole.getCircle().getLayoutY(), 7, colors.get(color));
        this.hole = hole;
        this.color = colors.get(color);
    }


    public List<Hole> findMove(Hole current) {
        List<Hole> possibleMove = new ArrayList<>();
        for(Hole hole: current.getNeighbours()){
            if(!hole.HoleIsOccupied()){
                possibleMove.add(hole);
            }
        }
        possibleMove.addAll(findJump(current, new ArrayList<>()));
        //да тут нужно не забыть удалить саму ячейку
        possibleMove.remove(current);
        return possibleMove;
    }
    public void resetColor(){
        this.setFill(color);
    }
    public List<Hole> findJump(Hole current, List<Hole> jump){
        jump.add(current);
        for(Hole neighbor : current.getNeighbours()){
            if(neighbor.HoleIsOccupied()){
                for(Hole neighbor2 : neighbor.getNeighbours()){
                    if(occupiedOnSame(current,neighbor2) &&
                      !neighbor2.HoleIsOccupied() &&
                      current.getNeighbours().contains(neighbor2) &&
                     !jump.contains(neighbor2)){
                        findJump(neighbor2,jump);
                    }
                }
            }
        }
        return jump;
    }
    private boolean occupiedOnSame(Hole current, Hole nieghbour) {
        return current.getCoordinates()[0] == nieghbour.getCoordinates()[0] ||
                current.getCoordinates()[1] == nieghbour.getCoordinates()[1] ||
                current.getCoordinates()[2] == nieghbour.getCoordinates()[2];
    }
//    private void clickHandler(){
//        this.setOnMouseClicked(event -> {
//
//        });
//    }
}
