package web.halma.AI.mcts;

import web.halma.AI.*;

import java.util.*;

public class Node {
    public GameState state;
    public Node parent;
    public Move move;
    public List<Node> children = new ArrayList<>();
    public int visits=0;
    public double wins=0;
    private List<Move> untriedMoves;

    public Node(GameState state, Node parent, Move move){
        this.state=state;
        this.parent=parent;
        this.move=move;
        untriedMoves = new ArrayList<>(state.getLegalMoves());
    }

    public boolean isFullyExpanded(){
        return untriedMoves.isEmpty();
    }

    public Node expand(){
        if (untriedMoves.isEmpty()) {
            return this;
        }
        Move m = untriedMoves.remove(untriedMoves.size()-1);
        GameState next = state.applyMove(m);
        Node child = new Node(next, this, m);
        children.add(child);
        return child;
    }

    public Node bestUCT(){
        Node best = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for(Node c : children){
            double uct;
            if(c.visits == 0){
                uct = Double.MAX_VALUE;
            }
            else{
                uct = c.wins / c.visits + 1.41 * Math.sqrt(
                                        Math.log(visits + 1) / c.visits);
            }
            if(uct > bestScore){
                bestScore = uct;
                best = c;
            }
        }
        return best;
    }
}
