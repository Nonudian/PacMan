package Util;

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

    public Direction getOpposed() {
        switch (this) {
            case LEFT:
                return RIGHT;
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
        }
        return null;
    }
}
