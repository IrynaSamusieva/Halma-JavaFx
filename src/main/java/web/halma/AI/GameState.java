package web.halma.AI;

import javafx.scene.paint.Color;
import web.halma.config.Hole;
import web.halma.controllers.BoardController;

import java.util.*;

public class GameState {
    public Map<Hole, Color> board;
    public Color currentPlayer;

    private List<Move> cachedMoves = null;
    private final List<Color> activePlayers;

    public GameState(Map<Hole, Color> boardMapForAI, Color botColor) {
        this.board = new HashMap<>(boardMapForAI);
        this.activePlayers = new ArrayList<>();
        for (Color color : board.values()) {
            if (!activePlayers.contains(color)) {
                activePlayers.add(color);
            }
        }

        this.currentPlayer = botColor;
    }

    private GameState(GameState other) {
        this.board = new HashMap<>(other.board);
        this.currentPlayer = other.currentPlayer;
        this.activePlayers = other.activePlayers;
    }

    public List<Move> getLegalMoves() {
        if (cachedMoves != null) return cachedMoves;
        cachedMoves = computeLegalMoves();
        return cachedMoves;
    }

    private List<Move> computeLegalMoves() {
        List<Move> moves = new ArrayList<>();
        for (var entry : board.entrySet()) {
            if (!entry.getValue().equals(currentPlayer)) continue;
            Hole from = entry.getKey();

            for (Hole n : from.getNeighbours()) {
                if (!board.containsKey(n))
                    moves.add(new Move(from, n));
            }

            Set<Hole> visited = new HashSet<>();
            visited.add(from);
            jumpDFS(from, from, visited, moves);
        }
        return moves;
    }

    private void jumpDFS(Hole origin, Hole current, Set<Hole> visited, List<Move> result) {
        for (Hole middle : current.getNeighbours()) {
            if (!board.containsKey(middle))
                continue;
            for (Hole landing : middle.getNeighbours()) {
                if (board.containsKey(landing))
                    continue;
                if (visited.contains(landing))
                    continue;
                if (!occupiedOnSameAxis(current, landing))
                    continue;
                if (current.getNeighbours().contains(landing))
                    continue;

                visited.add(landing);
                result.add(new Move(origin, landing));
                jumpDFS(origin, landing, visited, result);
                visited.remove(landing);
            }
        }
    }

    private boolean occupiedOnSameAxis(Hole start, Hole end) {
        return start.getCoordinates()[0] == end.getCoordinates()[0] ||
                start.getCoordinates()[1] == end.getCoordinates()[1] ||
                start.getCoordinates()[2] == end.getCoordinates()[2];
    }

    public GameState applyMove(Move move) {
        GameState next = new GameState(this);
        Color moving = next.board.remove(move.from);
        next.board.put(move.to, moving);
        next.currentPlayer = BoardController.getOppositeColor(currentPlayer);
        next.cachedMoves = null;
        return next;
    }

    public Color getWinner() {
        for (Color c : activePlayers) {
            if (checkWinner(c) != null) return c;
        }
        return null;
    }

    private Color checkWinner(Color player){
        Color target = BoardController.getOppositeColor(player);
        List<Hole> targetHoles = BoardController.baseCamps.get(target);

        if(targetHoles==null)
            return null;

        boolean has=false;
        for(var e : board.entrySet()){
            if(e.getValue().equals(player)){
                has=true;
                if(!targetHoles.contains(e.getKey()))
                    return null;
            }
        }
        return has ? player : null;
    }
    private Color cachedWinner = null;
    private boolean winnerChecked = false;

    public Color getWinnerCached() {
        if (!winnerChecked) {
            cachedWinner = getWinner();
            winnerChecked = true;
        }
        return cachedWinner;
    }

    public boolean isGameOverFast() {
        return getWinnerCached() != null;
    }

    public double evaluate(Color player) {
        Color winner = getWinnerCached();
        if (winner != null)
            return winner.equals(player) ? 100000 : -100000;

        List<Hole> goal = BoardController.targetCamps.get(player);
        if (goal == null) {
            System.out.println("No target camp for: " + player);
            return 0;
        }

        double score = 0;
        for (var e : board.entrySet()) {
            if (!e.getValue().equals(player)) continue;
            Hole piece = e.getKey();
            int dist = distanceToGoalFast(piece, goal);
            score -= dist;
            if (goal.contains(piece)) score += 20;
        }
        return score;
    }

    private int distanceToGoalFast(Hole from, List<Hole> goal) {
        int[] fc = from.getCoordinates();
        int best = Integer.MAX_VALUE;
        for (Hole g : goal) {
            int[] gc = g.getCoordinates();
            int d = Math.abs(fc[0] - gc[0]) + Math.abs(fc[1] - gc[1]) + Math.abs(fc[2] - gc[2]);
            if (d < best) best = d;
        }
        return best;
    }
}
