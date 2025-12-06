package web.halma.config;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.halma.controllers.BoardController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {}
    }

    @BeforeEach
    void setUp() {
        Piece.init();
        BoardController.holes = new ArrayList<>();
        createAndAddHole(new int[]{0, 0, 0});
        createAndAddHole(new int[]{1, 0, -1});
        createAndAddHole(new int[]{2, 0, -2});
        for (Hole h : BoardController.holes) {
            h.findNeighbours();
        }
    }

    private void createAndAddHole(int[] coords) {
        Hole h = new Hole(new javafx.scene.shape.Circle(), coords);
        BoardController.holes.add(h);
    }

    @Test
    public void testMove() {
        Hole hole1 = BoardController.getHole(new int[] {0, 0, 0});
        Hole hole2 = BoardController.getHole(new int[] {1, 0, -1});

        Piece marble = new Piece(hole1, "red");

        assertEquals(hole1, marble.getHole());

        marble.move(hole2);

        assertEquals(hole2, marble.getHole());
        assertTrue(hole2.HoleIsOccupied());
        assertFalse(hole1.HoleIsOccupied());
    }

    @Test
    public void testLegalMove() {
        Hole startHole = BoardController.getHole(new int[] {0, 0, 0});
        Hole legalDest = BoardController.getHole(new int[] {1, 0, -1});
        Hole illegalDest = BoardController.getHole(new int[] {2, 0, -2});

        Piece marble = new Piece(startHole, "red");
        startHole.setOccupied(true);

        marble.move(illegalDest);

        assertEquals(startHole, marble.getHole());
        assertTrue(startHole.HoleIsOccupied());
        marble.move(legalDest);
        assertEquals(legalDest, marble.getHole());
        assertTrue(legalDest.HoleIsOccupied());
    }
    @Test
    public void testWinnerMessages() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Object[][] testCases = {
                {Color.web("0x008000ff"), "Green"},
                {Color.web("0x0000ffff"), "Blue"},
                {Color.web("0xffff00ff"), "Yellow"},
                {Color.web("0xff0000ff"), "Red"},
                {Color.web("0xff00ffff"), "Magenta"},
                {Color.web("0xffa500ff"), "Orange"}
        };
        for (Object[] tc : testCases) {
            Color color = (Color) tc[0];
            String expectedName = (String) tc[1];
            String winner = null;
            switch(color.toString()) {
                case "0x008000ff": winner = "Green"; break;
                case "0x0000ffff": winner = "Blue"; break;
                case "0xffff00ff": winner = "Yellow"; break;
                case "0xff0000ff": winner = "Red"; break;
                case "0xff00ffff": winner = "Magenta"; break;
                case "0xffa500ff": winner = "Orange"; break;
                default: throw new IllegalStateException("Unexpected value: " + color);
            }
            System.out.println("WIN! PLayer " + winner + " won the game!");
            assertTrue(outContent.toString().contains("WIN! PLayer " + expectedName + " won the game!"),
                    "This color " + color + " must contain " + expectedName);
        }
    }
}

