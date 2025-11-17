package web.halma.models.board.field;

import javafx.scene.paint.Color;

//If i create server-client(?) this can randomly set colors to players
public enum FieldColor {
    GREEN,
    MAGENTA,
    BLUE,
    ORANGE,
    YELLOW,
    RED,
    BLACK;

    public static Color getRandomColor(int num) {
        switch (num) {
            case 0: return Color.GREEN;
            case 1: return Color.MAGENTA;
            case 2: return Color.BLUE;
            case 3: return Color.ORANGE;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            default: return Color.BLACK;
        }
    }
    public static FieldColor getEnemy(FieldColor fieldColor){
        switch (fieldColor){
            case GREEN: return ORANGE;
            case MAGENTA: return YELLOW;
            case BLUE: return RED;
            case ORANGE: return GREEN;
            case YELLOW: return MAGENTA;
            case RED: return BLUE;
            default: return BLACK;
        }
    }
}
