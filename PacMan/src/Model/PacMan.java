package Model;

import Controller.Game;
import Util.Direction;
import java.util.concurrent.atomic.AtomicBoolean;
import static javafx.animation.Animation.Status.RUNNING;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PacMan extends Entity {

    private boolean powered;
    private boolean sick;
    private final Timeline powerTimeline;
    private final int defaultLives;
    private int currentLives;
    private AtomicBoolean alive;

    public PacMan(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color, game, interval);
        this.powered = false;
        this.defaultLives = 3;
        this.currentLives = this.defaultLives;
        this.alive = new AtomicBoolean(true);
        this.powerTimeline = new Timeline(new KeyFrame(Duration.seconds(10), (e) -> {
            this.endPower();
        }));
    }

    @Override
    public void reset() {
        this.game.resetGhosts();
        this.resetSick();
        if (this.game.getGumLanes().isEmpty()) {
            // [VICTORY]
            this.game.resetGum();
            this.game.increaseLevel();
            this.resetLives();
        } else if (this.currentLives == 0) {
            // [DEFEAT]
            this.game.resetGum();
            if (this.game.getScore() > this.game.getBestScore()) {
                this.game.updateBestScore();
            }
            this.game.resetScore();
            this.game.resetLevel();
            this.resetLives();
        }
        super.reset();
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void startSick() {
        this.sick = true;
        this.setTurnBack(true);
        this.setColor(Color.GREENYELLOW);
    }

    public boolean isSick() {
        return this.sick;
    }

    public void resetSick() {
        this.sick = false;
    }

    public void startPower() {
        this.game.notifyPowerToGhosts();
        if (!this.isPowered()) {
            this.resetTurnBack();
            this.setColor(Color.ORANGE);
            this.resetSick();
            this.powered = true;
        }
        if (this.powerTimeline.getStatus() == RUNNING) {
            this.powerTimeline.jumpTo(Duration.ZERO);
        } else {
            this.powerTimeline.play();
        }
    }

    public void endPower() {
        this.powered = false;
        this.setColor(this.defaultColor);
        this.game.notifyEndPowerToGhosts();
    }

    public int getRemainingLives() {
        return this.currentLives;
    }

    public void loseLife() {
        if (currentLives > 0) {
            this.currentLives--;
        }
    }

    public void resetLives() {
        this.currentLives = this.defaultLives;
    }

    public boolean isAlive() {
        return this.alive.get();
    }

    public void setAlive(boolean alive) {
        this.alive.set(alive);
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof Ghost && this.powered && ((Ghost) enemy).isScared());
    }

    @Override
    public Direction getNextDirection() {
        return this.currentDirection;
    }
}
