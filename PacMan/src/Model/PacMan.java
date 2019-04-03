package Model;

import Controller.Game;
import Util.Direction;
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

    public PacMan(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color, game, interval);
        this.powered = false;
        this.powerTimeline = new Timeline(new KeyFrame(Duration.seconds(10), (e) -> {
            this.endPower();
        }));
    }

    @Override
    public void reset() {
        this.game.resetGum();
        this.game.resetGhosts();
        this.resetSick();
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
        if (!this.isPowered()) {
            this.resetTurnBack();
            this.setColor(Color.ORANGE);
            this.resetSick();
            this.powered = true;
            this.setInterval(this.interval / 2);
            this.game.notifyPowerToGhosts();
        }
        if (this.powerTimeline.getStatus() == RUNNING) {
            this.powerTimeline.jumpTo(Duration.ZERO);
        } else {
            this.powerTimeline.play();
        }
    }

    public void endPower() {
        this.setInterval(this.interval * 2);
        this.powered = false;
        this.setColor(this.defaultColor);
        this.game.notifyEndPowerToGhosts();
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof Ghost && this.powered && ((Ghost)enemy).isScared());
    }

    @Override
    public Direction getNextDirection() {
        return this.currentDirection;
    }
}
