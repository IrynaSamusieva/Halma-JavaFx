package web.halma.AI;

import web.halma.config.Hole;

public class Move {
    public Hole from;
    public Hole to;

    public Move(Hole from, Hole to) {
        this.from = from;
        this.to = to;
    }
}
