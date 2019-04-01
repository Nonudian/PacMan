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
    private final Timeline powerTimeline;

    public PacMan(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color, game, interval);
        this.powered = false;
        this.powerTimeline = new Timeline(new KeyFrame(Duration.seconds(10), (e) -> {
            this.endPower();
        }));
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void startPower() {
        if(!this.isPowered()) {
            this.setColor(Color.ORANGE);
            this.powered = true;
            this.setInterval(this.interval / 2);
        }
        if (this.powerTimeline.getStatus() == RUNNING) {
            this.powerTimeline.jumpTo(Duration.ZERO);
        } else {
            this.powerTimeline.play();
        }
    }

    public void endPower() {
        System.out.println("endpower");
        this.setInterval(this.interval * 2);
        this.powered = false;
        this.setColor(this.defaultColor);
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof Ghost && this.powered);
    }

    @Override
    protected Direction getNextDirection() {
        return this.currentDirection;
    }
}
