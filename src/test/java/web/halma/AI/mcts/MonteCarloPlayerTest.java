package web.halma.AI.mcts;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.halma.AI.GameState;
import web.halma.AI.Move;
import web.halma.config.Hole;
import web.halma.controllers.BoardController;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MonteCarloPlayerTest {
    private Hole hole(int x, int y, int z) {
        return new Hole(new Circle(10), new int[]{x,y,z});
    }
    @BeforeEach
    void setupTargetCamps() {
        BoardController.targetCamps.clear();

        Hole redGoal1 = hole(5, -5, 0);
        Hole redGoal2 = hole(4, -4, 0);

        Hole blueGoal1 = hole(-5, 5, 0);
        Hole blueGoal2 = hole(-4, 4, 0);
        BoardController.targetCamps.put(Color.RED, List.of(redGoal1, redGoal2));

        BoardController.targetCamps.put(Color.BLUE, List.of(blueGoal1, blueGoal2));
        BoardController.baseCamps.clear();
        BoardController.baseCamps.put(Color.RED, List.of(hole(-5,5,0)));
        BoardController.baseCamps.put(Color.BLUE, List.of(hole(5,-5,0)));
    }
    @BeforeEach
    void setup() {
        BoardController.targetCamps.clear();
        Hole redGoal = hole(5,-5,0);
        Hole blueGoal = hole(-5,5,0);
        BoardController.targetCamps.put(Color.RED, List.of(redGoal));
        BoardController.targetCamps.put(Color.BLUE, List.of(blueGoal));
    }
    @Test
    void shouldReturnMoveWhenPossible() {
        Hole from = hole(0,0,0);
        Hole to = hole(1,-1,0);
        from.getNeighbours().add(to);
        to.getNeighbours().add(from);
        Map<Hole,Color> board = new HashMap<>();
        board.put(from, Color.RED);
        GameState state = new GameState(board, Color.RED);
        Move result = MonteCarloPlayer.findBestMove(state);
        assertNotNull(result);
        assertEquals(from, result.from);
    }

    @Test
    void applyMoveShouldMovePiece() {
        Hole from = hole(0,0,0);
        Hole to = hole(1,-1,0);
        Map<Hole,Color> board = new HashMap<>();
        board.put(from,Color.RED);
        GameState state = new GameState(board, Color.RED);
        Move move = new Move(from, to);
        GameState next = state.applyMove(move);
        assertFalse(next.board.containsKey(from));
        assertTrue(next.board.containsKey(to));
        assertEquals(Color.RED, next.board.get(to));
    }

    @Test
    void shouldReturnNullWhenNoMoves(){
        Hole h = hole(0,0,0);
        Map<Hole,Color> board = new HashMap<>();
        board.put(h, Color.RED);
        GameState state = new GameState(board, Color.BLUE);
        Move move = MonteCarloPlayer.findBestMove(state);
        assertNull(move);
    }

    @Test
    void returnedMoveMustBeLegal(){
        Hole a = hole(0,0,0);
        Hole b = hole(1,-1,0);
        a.getNeighbours().add(b);
        b.getNeighbours().add(a);
        Map<Hole,Color> board = new HashMap<>();
        board.put(a, Color.RED);
        GameState state = new GameState(board, Color.RED);
        List<Move> legal = state.getLegalMoves();
        Move ai = MonteCarloPlayer.findBestMove(state);
        assertNotNull(ai);
        boolean ok = legal.stream().anyMatch(m -> m.from.equals(ai.from) && m.to.equals(ai.to));
        assertTrue(ok);
    }

    @Test
    void aiShouldFinishQuickly(){
        Map<Hole,Color> board = new HashMap<>();
        Random r = new Random(1);
        for(int i=0;i<15;i++){
            Hole h = hole(i, -i, 0);
            board.put(h, r.nextBoolean() ? Color.RED : Color.BLUE);
        }
        GameState state = new GameState(board, Color.RED);
        long start = System.currentTimeMillis();
        MonteCarloPlayer.findBestMove(state);
        long time = System.currentTimeMillis() - start;
        assertTrue(time < 3000, "AI stucked: " + time);
    }
}
