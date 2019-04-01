package Model;

import Controller.Game;
import Util.Direction;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Entity implements Runnable {
    
    protected Direction currentDirection;
    protected Point2D coords;
    protected final Point2D startingCoords;
    protected final Color color;
    
    protected final AtomicBoolean runnable;
    protected final Thread worker;
    protected final int interval;
    protected final Game game;
    protected boolean turnBack;
    
    public Entity(Point2D coords, Direction direction, Color color, Game game, int interval) {
        this.currentDirection = direction;
        this.startingCoords = coords;
        this.coords = coords;
        this.color = color;
        this.game = game;
        this.interval = interval;
        this.turnBack = false;
        
        this.runnable = new AtomicBoolean(false);
        this.worker = new Thread(this);
    }
    
    public void start() {
        this.runnable.set(true);
        this.worker.start();
    }

    public void stop() {
        this.runnable.set(false);
    }

    public boolean isRunning() {
        return this.runnable.get();
    }
    
    public Direction getDirection() {
        return this.currentDirection;
    }
    
    public void setDirection(Direction direction) {
        this.currentDirection = direction;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public Point2D getCoords() {
        return this.coords;
    }
    
    public Point2D getStartingCoords() {
        return this.startingCoords;
    }
    
    public void moveTo(Point2D coords) {
        this.coords = coords;
    }
    
    public void moveToStartingCoords() {
        this.coords = this.startingCoords;
    }
    
    public void setTurnBack() {
        this.turnBack = !this.turnBack;
    }
    
    public abstract boolean canKill(Entity enemy);
    
    protected abstract Direction getNextDirection();
    
    @Override
    public void run() {
        while (this.runnable.get()) {
            try {
                this.worker.sleep(this.interval);
                if (this.turnBack) {
                    this.game.move(this, this.currentDirection.getOpposed());
                    this.turnBack = false;
                } else {
                    this.game.move(this, this.getNextDirection());
                }
            } catch (InterruptedException ex) {
                System.out.println("Interrupted thread");
            }
        }
    }
}
