package web.halma.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import web.halma.config.Hole;
import web.halma.config.Piece;
import web.halma.models.board.field.FieldState;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BoardController {
    @FXML
    private AnchorPane root;
    @FXML
    Group marbleGroup;
    public static Group checkerGroup;
    public static Piece selected = null;
    public static List<Hole> holes = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            Piece.init();

            // 2. НЕ СОЗДАВАЙТЕ новую группу. Используйте ту, что пришла из FXML.
            // Присваиваем FXML-группу в нашу статическую переменную

            checkerGroup = marbleGroup;

            // Если вдруг в FXML нет fx:id="marbleGroup", то создаем запасную (защита от null)
            if (checkerGroup == null) {
                checkerGroup = new Group();
                root.getChildren().add(checkerGroup);
            }

            // Гарантируем, что шашки будут ПОВЕРХ лунок
            checkerGroup.toFront();

            createHole();

            // 3. Используем Platform.runLater, чтобы шашки создались
            // точно по координатам лунок ПОСЛЕ отрисовки окна
            javafx.application.Platform.runLater(() -> {
                startBoard();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void startBoard(){
        InputStream in = getClass().getResourceAsStream("/web/halma/Coord.chc");
        try{
            String startBoardCoord = new String(in.readAllBytes());
            String[] parts = startBoardCoord.split("\\r?\\n");
            for(String part : parts){
                String[] data = part.split(":");
                createPiece(data[0],data[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private int Decoder(String coord){
        char sign = coord.charAt(0);
        int val = Integer.parseInt(coord.substring(1));
        if(sign == 'N'){
            return -val;
        }
        return val;
    }
    private void createHole(){
        List<Node> circleNodes = new ArrayList<>();
        for(Node node : root.getChildren()){
            if(node instanceof Circle && node.getId().startsWith("hole")){
                circleNodes.add(node);
            }
        }
        for(Node node : circleNodes){
            Circle circle = (Circle) node;
            String id = circle.getId().substring(4);
            int posX = Decoder(id.substring(0,2));
            int posY = Decoder(id.substring(2,4));
            int posZ = Decoder(id.substring(4,6));
            Hole hole = new Hole(circle, new int[] {posX, posY, posZ});
            holes.add(hole);
        }
        for(Hole hole: holes){
            hole.findNeighbours();
        }

    }
    public static void createPiece(String coord, String color){
        Hole current = null;
        for (Hole hole : holes) {
            if (hole.getCircle().getId().substring(4).equals(coord)){
                current = hole;
            }

        }
        current.getField().setState(FieldState.OCCUPIED);
        Piece piece = new Piece( current, color);
        checkerGroup.getChildren().add(piece);

    }
    public static Hole getHole(int[]neighbourCoordinates){
        for(Hole hole: holes){
            if(Arrays.equals(hole.getCoordinates(), neighbourCoordinates)){
               return hole;
            }
        }
        return null;
    }

    private void clickHandler(Node node){
        node.setOnMouseClicked(event -> {
            resetColorAllGroup();
            resetColorHoles();
            selected = null;
        });
    }
    public static void resetColorAllGroup(){
        for(Node node : checkerGroup.getChildren()){
            ((Piece)node).resetColor();
        }
    }
    public static void resetCheckers(){
       checkerGroup.getChildren().clear();
       for(Hole hole : holes){
           hole.setOccupied(false);
       }
    }
    public static void resetColorHoles(){
        for(Hole hole: holes){
            hole.getCircle().setFill(Color.web("#222426"));
        }
    }
}
