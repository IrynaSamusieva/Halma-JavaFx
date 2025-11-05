package web.halma.models.board.field;

import java.util.Arrays;
import java.util.List;
//If i create server-client(?) this can randomly set colors to players
public enum FieldColor {
    GREEN,
    PURPLE,
    BLUE,
    ORANGE,
    YELLOW,
    RED,
    WHITE;

    public static FieldColor getRandomColor(int num) {
        switch (num) {
            case 0: return GREEN;
            case 1: return PURPLE;
            case 2: return BLUE;
            case 3: return ORANGE;
            case 4: return YELLOW;
            case 5: return RED;
            default: return WHITE;
        }
    }
    public static FieldColor getEnemy(FieldColor fieldColor){
        switch (fieldColor){
            case GREEN: return ORANGE;
            case PURPLE: return YELLOW;
            case BLUE: return RED;
            case ORANGE: return GREEN;
            case YELLOW: return PURPLE;
            case RED: return BLUE;
            default: return WHITE;
        }
    }
}
