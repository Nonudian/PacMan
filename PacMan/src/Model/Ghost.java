package Model;

import Controller.Game;
import Util.Direction;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class Ghost extends Entity implements Runnable {
    
    private final AtomicBoolean movable;
    private final Thread worker;
    private final int interval;
    private final Game game;
    
    public Ghost(Point2D coords, Direction direction, Color color, Game game, int interval) {
        super(coords, direction, color);
        this.movable = new AtomicBoolean(false);
        this.worker = new Thread(this);
        this.interval = interval;
        this.game = game;
    }
    
    public void start() {
        this.movable.set(true);
        this.worker.start();
    }
    
    public void stop() {
        this.movable.set(false);
    }
    
    public boolean canMove() {
        return this.movable.get();
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof PacMan && !((PacMan)enemy).isPowered());
    }

    @Override
    public void run() {
        while(this.movable.get()) {
            try {
                this.worker.sleep(this.interval);
                this.game.move(this, Direction.values()[new Random().nextInt(4)]);
            } catch (InterruptedException ex) {
                System.out.println("Interrupted thread");
            }
        }
    }
}
