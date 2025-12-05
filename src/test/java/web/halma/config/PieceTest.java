package web.halma.config;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.halma.controllers.BoardController;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    @BeforeAll
    static void initJfx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {} // Игнорируем, если уже запущено
    }

    // 2. Подготовка доски перед КАЖДЫМ тестом
    @BeforeEach
    void setUp() {
        // Инициализируем цвета фишек
        Piece.init();

        // Очищаем и готовим список лунок в контроллере
        BoardController.holes = new ArrayList<>();

        // Создаем лунку-центр (0,0,0) и соседа (1,0,-1)
        // Нам нужен хотя бы минимальный набор лунок для теста
        createAndAddHole(new int[]{0, 0, 0});
        createAndAddHole(new int[]{1, 0, -1});
        createAndAddHole(new int[]{2, 0, -2}); // Для теста illegal move

        // ВАЖНО: Заставляем лунки найти друг друга!
        for (Hole h : BoardController.holes) {
            h.findNeighbours();
        }
    }

    // Вспомогательный метод для создания лунки
    private void createAndAddHole(int[] coords) {
        // null вместо Circle, так как в логике теста графика не важна,
        // но если класс Hole требует Circle, создадим заглушку
        Hole h = new Hole(new javafx.scene.shape.Circle(), coords);
        BoardController.holes.add(h);
    }

    @Test
    public void testMove() {
        // Теперь getHole вернет реальный объект
        Hole hole1 = BoardController.getHole(new int[] {0, 0, 0});
        Hole hole2 = BoardController.getHole(new int[] {1, 0, -1});

        Piece marble = new Piece(hole1, "red");

        // Проверяем начальное положение
        assertEquals(hole1, marble.getHole());

        // Двигаем
        marble.move(hole2);

        // ТЕПЕРЬ СРАБОТАЕТ, так как hole1 знает, что hole2 — его сосед
        assertEquals(hole2, marble.getHole());
        assertTrue(hole2.HoleIsOccupied());
        assertFalse(hole1.HoleIsOccupied());
    }

    @Test
    public void testLegalMove() {
        Hole startHole = BoardController.getHole(new int[] {0, 0, 0});
        Hole legalDest = BoardController.getHole(new int[] {1, 0, -1});
        Hole illegalDest = BoardController.getHole(new int[] {2, 0, -2}); // Далеко

        Piece marble = new Piece(startHole, "red");
        startHole.setOccupied(true); // Piece конструктор не всегда ставит setOccupied, лучше явно

        // 1. Попытка нелегального хода (слишком далеко, не сосед)
        marble.move(illegalDest);

        // Проверяем, что шарик НЕ сдвинулся
        assertEquals(startHole, marble.getHole());
        assertTrue(startHole.HoleIsOccupied());

        // 2. Легальный ход
        marble.move(legalDest);

        // Проверяем, что сдвинулся
        assertEquals(legalDest, marble.getHole());
        assertTrue(legalDest.HoleIsOccupied());
    }
}
