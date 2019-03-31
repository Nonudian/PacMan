package Model;

import Controller.Game;
import Util.Direction;
import java.util.ArrayList;
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

    private ArrayList<Direction> getPossibleDirections() {
        ArrayList<Direction> possibleDirections = new ArrayList();
        for (Direction direction : Direction.values()) {
            if (this.currentDirection.getOpposed() != direction) {
                Point2D adjacentCoords = this.game.getNextCoords(this.coords, direction);
                if (this.game.isReachable(adjacentCoords)) {
                    if (this.game.getTileByCoords(adjacentCoords) instanceof Lane) {
                        possibleDirections.add(direction);
                    }
                }
            }
        }
        return possibleDirections;
    }

    private void setDirectionAccordingToPacMan() {
        PacMan pacman = this.game.getPacMan();
        Point2D pacmanCoords = pacman.getCoords();
        
        double maxDistance = Double.MAX_VALUE;
        double minDistance = 0;
        
        for (Direction direction : getPossibleDirections()) {
            Point2D nextCoords = this.game.getNextCoords(this.coords, direction);
            double distance = pacmanCoords.distance(nextCoords);
            if (pacman.isPowered()) {
                // ghosts want to get away from pacman
                if (distance > minDistance) {
                    minDistance = distance;
                    this.currentDirection = direction;
                }
            } else {
                // ghosts want to reach pacman
                if (distance < maxDistance) {
                    maxDistance = distance;
                    this.currentDirection = direction;
                }
            }
        }
    }

    @Override
    public boolean canKill(Entity enemy) {
        return (enemy instanceof PacMan && !((PacMan) enemy).isPowered());
    }

    @Override
    public void run() {
        while (this.movable.get()) {
            try {
                this.worker.sleep(this.interval);
                this.setDirectionAccordingToPacMan();
                this.game.move(this, this.currentDirection);
            } catch (InterruptedException ex) {
                System.out.println("Interrupted thread");
            }
        }
    }
}
