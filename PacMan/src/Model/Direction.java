package Model;

import javafx.scene.input.KeyCode;

public enum Direction {
    LEFT, UP, RIGHT, DOWN;

    public static Direction get(KeyCode code) {
        switch (code) {
            case LEFT:
                return LEFT;
            case UP:
                return UP;
            case RIGHT:
                return RIGHT;
            case DOWN:
                return DOWN;
        }
        return null;
    }
}
