package Model;

import Controller.Game;
import Util.Direction;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public abstract class Entity implements Runnable {

    protected Direction currentDirection;
    protected Direction defaultDirection;
    
    protected Point2D coords;
    protected final Point2D startingCoords;
    
    protected Color color;
    protected Color defaultColor;

    protected final AtomicBoolean runnable;
    protected final Thread worker;
    
    protected int interval;
    protected int oldInterval;
    
    protected final Game game;
    protected boolean turnBack;

    public Entity(Point2D coords, Direction direction, Color color, Game game, int interval) {
        this.currentDirection = direction;
        this.defaultDirection = direction;
        this.startingCoords = coords;
        this.coords = coords;
        this.color = color;
        this.defaultColor = color;
        this.game = game;
        this.interval = interval;
        this.oldInterval = interval;
        this.turnBack = false;

        this.runnable = new AtomicBoolean(false);
        this.worker = new Thread(this);
    }
    
    public void reset() {
        this.resetTurnBack();
        this.resetDirection();
        this.moveToStartingCoords();
        this.resetColor();
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
    
    public Color getDefaultColor() {
        return this.defaultColor;
    }
    
    public void setColor(Color color) {
        this.color = color;
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

    public void setTurnBack(boolean turnBack) {
        this.turnBack = turnBack;
    }
    
    public boolean isTurnBack() {
        return this.turnBack;
    }
    
    public void resetInterval() {
        this.setInterval(this.oldInterval);
    }
    
    public int getDefaultInterval() {
        return this.oldInterval;
    }
    
    public void setInterval(int interval) {
        this.interval = interval;
    }
    
    public int getInterval() {
        return this.interval;
    }
    
    public Direction getDefaultDirection() {
        return this.defaultDirection;
    }
    
    public void resetDirection() {
        this.setDirection(this.defaultDirection);
    }
    
    public void resetTurnBack() {
        this.setTurnBack(false);
    }
    
    public void resetColor() {
        this.setColor(this.defaultColor);
    }

    public abstract boolean canKill(Entity enemy);

    public abstract Direction getNextDirection();

    @Override
    public void run() {
        while (this.runnable.get()) {
            try {
                this.worker.sleep(this.interval);
                this.game.move(this, this.getNextDirection());
            } catch (InterruptedException ex) {
                System.out.println("Interrupted thread");
            }
        }
    }
}
