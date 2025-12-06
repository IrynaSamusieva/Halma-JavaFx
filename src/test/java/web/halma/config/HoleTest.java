package web.halma.config;

import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import web.halma.controllers.BoardController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class HoleTest {
    @BeforeAll
    static void initJfxRuntime() {
        javafx.application.Platform.startup(() -> {});
    }
    @Test
    public void testConstructor() {
        Circle testCircle = new Circle(1, 1, 1);

        int[][] testCoordinates = {
                {1, 0, -1}, {1, -1, 0}, {0, -1, 1},
                {-1, 0, 1}, {-1, 1, 0}, {0, 1, -1}
        };

        for (int[] coordinates : testCoordinates) {
            Hole hole = new Hole(testCircle, coordinates);
            assertEquals(testCircle, hole.getCircle());

            assertEquals(coordinates, hole.getCoordinates());

            assertFalse(hole.HoleIsOccupied(), "Новая лунка должна быть свободна");
        }
    }

    @Test
    public void testFindNeighbours() {
        BoardController.holes = new ArrayList<>();

        Hole centerHole = new Hole(new Circle(), new int[]{0, 0, 0});
        BoardController.holes.add(centerHole);

        int[][] neighbourCoords = {
                {1, 0, -1}, {1, -1, 0}, {0, -1, 1},
                {-1, 0, 1}, {-1, 1, 0}, {0, 1, -1}
        };

        for (int[] coords : neighbourCoords) {
            BoardController.holes.add(new Hole(new Circle(), coords));
        }

        centerHole.findNeighbours();

        List<Hole> result = centerHole.getNeighbours();

        assertEquals(6, result.size(), "Должно быть найдено 6 соседей");

        assertTrue(result.contains(BoardController.getHole(new int[]{1, 0, -1})));
    }

    @Test
    public void testSetOccupied() {
        Circle circle = new Circle();
        int[] coordinates = new int[] {1, 2, 3};
        Hole hole = new Hole(circle, coordinates);
        hole.setOccupied(true);
        assertTrue(hole.HoleIsOccupied());
    }
}


