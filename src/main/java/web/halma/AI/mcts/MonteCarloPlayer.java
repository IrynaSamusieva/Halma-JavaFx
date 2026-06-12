package web.halma.AI.mcts;

import javafx.scene.paint.Color;
import web.halma.AI.GameState;
import web.halma.AI.Move;
import web.halma.config.Hole;
import web.halma.controllers.BoardController;

import java.util.List;
import java.util.Random;

public class MonteCarloPlayer {
    private static final long TIME_LIMIT = 2000;
    private static final int MAX_DEPTH = 30;
    private static final double UCT_C = 1.41;
    private static final Random rnd = new Random();

    public static Move findBestMove(GameState state) {
        Node root = new Node(state, null, null);
        long start = System.currentTimeMillis();
        int iterations = 0;
        Color aiPlayer = state.currentPlayer;

        while (System.currentTimeMillis() - start < TIME_LIMIT) {
            Node node = select(root);

            if (!node.state.isGameOverFast()) {
                node = node.expand();
            }
            double score = simulate(node.state, aiPlayer);

            backpropagate(node, score, aiPlayer);
            iterations++;
        }

        Node best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Node child : root.children) {
            if (child.visits == 0) continue;
            double score = child.wins / child.visits;
            if (score > bestScore) {
                bestScore = score;
                best = child;
            }
        }
        return best == null ? null : best.move;
    }

    private static Node select(Node node) {
        while (node != null) {
            if (node.state.isGameOverFast())
                return node;
            if (!node.isFullyExpanded())
                return node;
            Node next = node.bestUCT();
            if (next == null)
                return node;
            node = next;
        }
        return null;
    }

    private static double simulate(GameState state, Color aiPlayer) {
        GameState s = state;
        int depth = MAX_DEPTH;

        while (!s.isGameOverFast() && depth-- > 0) {
            List<Move> moves = s.getLegalMoves();
            if (moves.isEmpty()) break;
            Move move;
            if (rnd.nextDouble() < 0.3) {
                move = bestHeuristicMove(s, moves);
            } else {
                move = moves.get(rnd.nextInt(moves.size()));
            }
            s = s.applyMove(move);
        }

        return s.evaluate(aiPlayer);
    }

    private static Move bestHeuristicMove(GameState s, List<Move> moves) {
        List<Hole> goal = BoardController.baseCamps.get(s.currentPlayer);
        if (goal == null) return moves.get(rnd.nextInt(moves.size()));

        Move best = null;
        int bestDist = Integer.MAX_VALUE;

        int sampleSize = Math.min(5, moves.size());
        for (int i = 0; i < sampleSize; i++) {
            Move m = moves.get(rnd.nextInt(moves.size()));
            int[] tc = m.to.getCoordinates();
            int dist = Integer.MAX_VALUE;
            for (Hole g : goal) {
                int[] gc = g.getCoordinates();
                int d = Math.abs(tc[0] - gc[0])
                        + Math.abs(tc[1] - gc[1])
                        + Math.abs(tc[2] - gc[2]);
                if (d < dist) dist = d;
            }
            if (dist < bestDist) {
                bestDist = dist;
                best = m;
            }
        }
        return best != null ? best : moves.get(0);
    }

    private static void backpropagate(Node node, double score, Color aiPlayer) {
        while (node != null) {
            node.visits++;
            if (node.parent != null) {
                Color playerWhoMoved = node.parent.state.currentPlayer;
                node.wins += playerWhoMoved.equals(aiPlayer) ? score : -score;
            }
            node = node.parent;
        }
    }
}
