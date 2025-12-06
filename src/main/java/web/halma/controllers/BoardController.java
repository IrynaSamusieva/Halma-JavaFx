package web.halma.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import web.halma.config.Hole;
import web.halma.config.Piece;
import web.halma.models.board.field.FieldState;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class BoardController {
    @FXML
    private AnchorPane root;
    @FXML
    private Group marbleGroup;

    public static Group checkerGroup;
    public static Piece selected = null;
    public static List<Hole> holes = new ArrayList<>();
    private static final List<Color> turnOrder = new ArrayList<>();
    public static Map<Color, List<Hole>> baseCamps = new HashMap<>();
    private static int currentTurnIndex = 0;

    @FXML
    public void initialize() {
        turnOrder.clear();
        currentTurnIndex = 0;
        try {
            Piece.init();

            checkerGroup = marbleGroup;
            if (checkerGroup == null) {
                checkerGroup = new Group();
                root.getChildren().add(checkerGroup);
            }
            checkerGroup.toFront();

            createHole();

            Platform.runLater(() -> {
                startBoard();
                System.out.println("Game started first color is: " + getCurrentTurnColor());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startBoard(){
        InputStream in = getClass().getResourceAsStream("/web/halma/Coord.chc");
        if (in == null) return;

        try {
            String startBoardCoord = new String(in.readAllBytes());
            String[] parts = startBoardCoord.split("\\r?\\n");

            for(String part : parts){
                if (part.isBlank()) continue;
                String[] data = part.split(":");
                if (data.length >= 2) {
                    String val1 = data[0].trim();
                    String val2 = data[1].trim();

                    String coord, colorName;
                    if (val1.matches(".*\\d.*")) {
                        coord = val1;
                        colorName = val2;
                    } else {
                        coord = val2;
                        colorName = val1;
                    }
                    createPiece(coord, colorName);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int Decoder(String coord){
        if (coord.length() < 2) return 0;
        try {
            int val = Integer.parseInt(coord.substring(1));
            return (coord.charAt(0) == 'N') ? -val : val;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void createHole(){
        List<Node> circleNodes = new ArrayList<>();
        if (root == null) return;
        for(Node node : root.getChildren()){
            if(node instanceof Circle && node.getId() != null && node.getId().startsWith("hole")){
                circleNodes.add(node);
            }
        }
        for(Node node : circleNodes){
            Circle circle = (Circle) node;
            String id = circle.getId().substring(4);
            if (id.length() >= 6) {
                int posX = Decoder(id.substring(0,2));
                int posY = Decoder(id.substring(2,4));
                int posZ = Decoder(id.substring(4,6));
                holes.add(new Hole(circle, new int[] {posX, posY, posZ}));
            }
        }
        for(Hole hole: holes) hole.findNeighbours();
    }

    public static void createPiece(String coord, String colorName){
        Hole current = null;
        for (Hole hole : holes) {
            if (hole.getCircle().getId().substring(4).equals(coord)){
                current = hole;
                break;
            }
        }

        if (current != null) {
            current.getField().setState(FieldState.OCCUPIED);
            Piece piece = new Piece(current, colorName);
            checkerGroup.getChildren().add(piece);
            Color c = piece.getColor();
            baseCamps.putIfAbsent(c, new ArrayList<>());
            baseCamps.get(c).add(current);
            if (!turnOrder.contains(c)) {
                turnOrder.add(c);
            }
        }
    }

    public static Color getOppositeColor(Color c) {
        if (c.equals(Color.GREEN)) return Color.ORANGE;
        if (c.equals(Color.BLUE)) return Color.RED;

        if (c.equals(Color.RED)) return Color.BLUE;
        if (c.equals(Color.ORANGE)) return Color.GREEN;

        if (c.equals(Color.MAGENTA)) return Color.YELLOW;
        if (c.equals(Color.YELLOW)) return Color.MAGENTA;
        return null;
    }

    public static boolean checkWinner(Color playerColor) {
        Color targetColor = getOppositeColor(playerColor);
        if (targetColor == null) return false;

        List<Hole> targetHoles = baseCamps.get(targetColor);
        if (targetHoles == null || targetHoles.isEmpty()) return false;

        List<Piece> playerPieces = new ArrayList<>();
        for (Node node : checkerGroup.getChildren()) {
            if (node instanceof Piece) {
                Piece p = (Piece) node;
                if (p.getColor().equals(playerColor)) {
                    playerPieces.add(p);
                }
            }
        }

        for (Piece p : playerPieces) {
            if (!targetHoles.contains(p.getHole())) {
                return false;
            }
        }

        return true;
    }


    public static boolean isMyTurn(Color pieceColor) {
        if (turnOrder.isEmpty()) return true; // Если список пуст, разрешаем всё (защита)
        Color activeColor = turnOrder.get(currentTurnIndex);
        return activeColor.equals(pieceColor);
    }


    public static void switchTurn() {
        if (turnOrder.isEmpty()) return;
        currentTurnIndex = (currentTurnIndex + 1) % turnOrder.size();
    }

    private static Color getCurrentTurnColor() {
        if (turnOrder.isEmpty()) return Color.BLACK;
        return turnOrder.get(currentTurnIndex);
    }

    public static Hole getHole(int[] nc){
        for(Hole h: holes) if(Arrays.equals(h.getCoordinates(), nc)) return h;
        return null;
    }
    public static void resetColorAllGroup(){
        for(Node n : checkerGroup.getChildren()) if(n instanceof Piece) ((Piece)n).resetColor();
    }
    public static void resetColorHoles(){
        for(Hole h: holes) h.getCircle().setFill(Color.web("#222426"));
    }

}
