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

            // Проверки
            assertEquals(testCircle, hole.getCircle());

            // Для массивов лучше использовать assertArrayEquals,
            // хотя assertEquals сработает, если ссылка та же самая
            assertEquals(coordinates, hole.getCoordinates());

            // Используем assertFalse
            // Убедись, что в конструкторе Hole инициализируется field!
            assertFalse(hole.HoleIsOccupied(), "Новая лунка должна быть свободна");
        }
    }

    @Test
    public void testFindNeighbours() {
        // 1. Очищаем или готовим статический список дырок в контроллере
        // Предположим, что в BoardController есть доступ к списку holes
        // Если поле private, добавь сеттер или сделай public для тестов (или используй Reflection)
        BoardController.holes = new ArrayList<>();

        // 2. Создаем "Центральную" лунку (0, 0, 0)
        // Нам все равно на Circle, но конструктор требует его
        Hole centerHole = new Hole(new Circle(), new int[]{0, 0, 0});
        BoardController.holes.add(centerHole);

        // 3. Вручную создаем 6 соседей и добавляем их в "общий список" контроллера
        // Чтобы findNeighbours мог их найти
        int[][] neighbourCoords = {
                {1, 0, -1}, {1, -1, 0}, {0, -1, 1},
                {-1, 0, 1}, {-1, 1, 0}, {0, 1, -1}
        };

        for (int[] coords : neighbourCoords) {
            BoardController.holes.add(new Hole(new Circle(), coords));
        }

        // 4. Запускаем тестируемый метод
        centerHole.findNeighbours();

        // 5. Проверяем результат
        List<Hole> result = centerHole.getNeighbours();

        assertEquals(6, result.size(), "Должно быть найдено 6 соседей");

        // Проверяем, что конкретный сосед найден
        // Примечание: getHole внутри использует ту же статику, которую мы наполнили выше
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


