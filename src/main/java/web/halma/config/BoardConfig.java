package web.halma.config;

import java.util.List;
import java.util.Map;

public class BoardConfig {
    private int boardSize;
    private Map<Integer, List<Position>> playerStartPositions;
    private List<Position> validPositions;

    public BoardConfig(int boardSize,
                       Map<Integer, List<Position>> playerStartPositions,
                       List<Position> validPositions) {
        this.boardSize = boardSize;
        this.playerStartPositions = playerStartPositions;
        this.validPositions = validPositions;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Map<Integer, List<Position>> getPlayerStartPositions() {
        return playerStartPositions;
    }

    public List<Position> getValidPositions() {
        return validPositions;
    }

    public static class Position {
        public final int row;
        public final int col;

        public Position(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }
}
