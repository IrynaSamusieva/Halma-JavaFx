package web.halma.config;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import web.halma.controllers.BoardController;

import java.util.*;

public class Piece extends Circle {
    @Getter
    private Color color;
    @Getter
    private String group;
    private static Map<String, Color> colors;
    @Getter
    private Hole hole;
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

    public Piece( Hole hole,String color) {
        super(); // Создаем пока без координат

        this.hole = hole;
        this.color = colors.get(color);

        // --- ВАЖНОЕ ИЗМЕНЕНИЕ 1: РАДИУС ---
        // Лунка в FXML имеет радиус 7. Шашка должна быть больше, чтобы перекрывать её.
        // Ставим 12 или 15. Это решит проблему "промаха" мышкой.
        this.setRadius(12);

        this.setFill(colors.get(color));
        this.setStroke(Color.BLACK);

        // --- ВАЖНОЕ ИЗМЕНЕНИЕ 2: ПРИВЯЗКА ---
        // Жестко привязываем координаты шашки к центру лунки.
        // Даже если координаты лунки изменятся, шашка поедет за ней.
        this.centerXProperty().bind(hole.getCircle().layoutXProperty());
        this.centerYProperty().bind(hole.getCircle().layoutYProperty());

        clickHandler();
    }


    public void move(Hole hole){
        boolean isLegalMove = false;
        for(Hole legalHole : findMove(this.hole)){
            if(hole == legalHole){
                isLegalMove = true;
            }
        }
        if (isLegalMove) {
            this.setCenterX(hole.getCircle().getLayoutX());
            this.setCenterY(hole.getCircle().getLayoutY());
            this.hole.setOccupied(false);
            this.hole = hole;
            this.hole.setOccupied(true);
        }
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
                for(Hole landingSpot : neighbor.getNeighbours()){
                    if(!landingSpot.HoleIsOccupied() &&
                            occupiedOnSameAxis(current, landingSpot) &&
                            !jump.contains(landingSpot)){
                        findJump(landingSpot, jump);
                    }
                }
            }
        }
        return jump;
    }
    private boolean occupiedOnSameAxis(Hole current, Hole nieghbour) {
        return current.getCoordinates()[0] == nieghbour.getCoordinates()[0] ||
                current.getCoordinates()[1] == nieghbour.getCoordinates()[1] ||
                current.getCoordinates()[2] == nieghbour.getCoordinates()[2];
    }
    private void clickHandler(){
        this.setOnMouseClicked(event -> {
            if(this != BoardController.selected){
                System.out.println("Клик по ШАШКЕ!");
                if(BoardController.selected != null){
                    System.out.println("Выбираю новую шашку...");
                    BoardController.selected.resetColor();
                    BoardController.resetColorHoles();
                }
                BoardController.selected = this;
                this.setFill(Color.WHITE);
                event.consume();
            }
        });
    }
}
