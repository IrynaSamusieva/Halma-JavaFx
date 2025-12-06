package web.halma.config;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;
import web.halma.controllers.BoardController;

import java.util.*;

public class Piece extends Circle {
    @Getter
    private Color color;
    @Getter
    private Hole hole;
    private static Map<String, Color> colors;
    String winner = "";

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

    public Piece(Hole hole, String colorName) {
        super();
        this.hole = hole;
        String safeColorName = colorName.trim();
        this.color = colors.get(safeColorName);
        if (this.color == null) this.color = Color.BLACK;

        this.setRadius(7);
        this.setFill(this.color);
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(2);

        double realX = hole.getCircle().getLayoutX() + hole.getCircle().getCenterX();
        double realY = hole.getCircle().getLayoutY() + hole.getCircle().getCenterY();
        this.setCenterX(realX);
        this.setCenterY(realY);

        clickHandler();
    }

    public void move(Hole targetHole){
        List<Hole> legalMoves = findMove(this.hole);
        boolean isLegalMove = legalMoves.contains(targetHole);

        if (isLegalMove) {
            double newX = targetHole.getCircle().getLayoutX() + targetHole.getCircle().getCenterX();
            double newY = targetHole.getCircle().getLayoutY() + targetHole.getCircle().getCenterY();
            this.setCenterX(newX);
            this.setCenterY(newY);

            this.hole.setOccupied(false);
            this.hole = targetHole;
            this.hole.setOccupied(true);

            if (BoardController.selected != null) {
                BoardController.selected.resetColor();
                BoardController.selected = null;
            }
            BoardController.resetColorHoles();
            if (BoardController.checkWinner(this.color)) {
                switch(this.color.toString()){
                    case "0x008000ff": winner = "Green"; break;
                    case "0x0000ffff": winner = "Blue"; break;
                    case "0xffff00ff":  winner = "Yellow"; break;
                    case "0xff0000ff":  winner = "Red"; break;
                    case "0xff00ffff":  winner = "Magenta"; break;
                    case "0xffa500ff": winner = "Orange"; break;
                    default: throw new IllegalStateException("Unexpected value: " + this.color);
                }
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("WIN! PLayer " + winner + " won the game!");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Конец игры");
                alert.setHeaderText(null);
                alert.setContentText("Player " + this.color + " WON!");
                alert.showAndWait();

                return;
            }
            BoardController.switchTurn();

        }
    }

    private void clickHandler(){
        this.setOnMouseClicked(event -> {

            if (!BoardController.isMyTurn(this.color)) {
                event.consume();
                return;
            }
            if(this != BoardController.selected){
                if(BoardController.selected != null){
                    BoardController.selected.resetColor();
                    BoardController.resetColorHoles();
                }
                BoardController.selected = this;
                this.setFill(Color.WHITE);
            } else {
                BoardController.selected = null;
                this.resetColor();
                BoardController.resetColorHoles();
            }
            event.consume();
        });
    }


    public List<Hole> findMove(Hole current) {
        List<Hole> possibleMove = new ArrayList<>();
        for(Hole neighbour : current.getNeighbours()){
            if(!neighbour.HoleIsOccupied()){
                possibleMove.add(neighbour);
            }
        }
        List<Hole> visited = new ArrayList<>();
        visited.add(current);
        possibleMove.addAll(findJump(current, visited));
        return possibleMove;
    }

    public List<Hole> findJump(Hole current, List<Hole> visited){
        List<Hole> jumps = new ArrayList<>();
        for(Hole neighbor : current.getNeighbours()){
            if(neighbor.HoleIsOccupied()){
                for(Hole landingSpot : neighbor.getNeighbours()){
                    if(!landingSpot.HoleIsOccupied() &&
                            occupiedOnSameAxis(current, landingSpot) &&
                            !visited.contains(landingSpot) &&
                            !current.getNeighbours().contains(landingSpot)) {
                        visited.add(landingSpot);
                        jumps.add(landingSpot);
                        jumps.addAll(findJump(landingSpot, visited));
                    }
                }
            }
        }
        return jumps;
    }

    private boolean occupiedOnSameAxis(Hole start, Hole end) {
        return start.getCoordinates()[0] == end.getCoordinates()[0] ||
                start.getCoordinates()[1] == end.getCoordinates()[1] ||
                start.getCoordinates()[2] == end.getCoordinates()[2];
    }

    public void resetColor(){
        this.setFill(color);
        this.setStroke(Color.BLACK);
    }
}
