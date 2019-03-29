package Controller;

import Model.Entity;
import Model.Ghost;
import Model.GhostDoor;
import Model.Lane;
import Model.PacMan;
import Model.Portal;
import Model.Tile;
import Model.Wall;
import Util.GumType;
import Util.Direction;
import static Util.Direction.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

public class Game extends Observable implements Runnable {

    private final int dimension;
    private Tile[][] grid;
    private ArrayList<Ghost> ghosts;
    private final AtomicBoolean running;
    private int score;
    private Thread worker;
    private final int interval;
    private ArrayList<Portal> portals;
    private PacMan pacman;

    public Game() {
        this.dimension = 21;
        this.score = 0;
        this.interval = 500;
        this.running = new AtomicBoolean(false);
        this.initGrid();
        this.linkPortals();
    }

    public void start() {
        this.worker = new Thread(this);
        this.worker.start();
    }

    public void stop() {
        this.running.set(false);
    }

    public void interrupt() {
        this.stop();
        this.worker.interrupt();
    }

    @Override
    public void run() {
        this.running.set(true);
        while (this.running.get()) {
            try {
                this.worker.sleep(this.interval);
                this.ghosts.forEach((ghost) -> {
                    this.move(ghost, Direction.values()[new Random().nextInt(3)]);
                });
                setChanged();
                notifyObservers();
            } catch (InterruptedException e) {
                this.interrupt();
                System.out.println("Thread was interrupted, Failed to complete operation");
            }
        }
    }

    private void initGrid() {
        this.portals = new ArrayList();
        this.ghosts = new ArrayList();
        this.grid = new Tile[this.dimension][this.dimension];

        Color[] ghostColors = new Color[]{RED, PINK, CYAN, ORANGE};
        int ghostAdded = 0;

        File file = new File("./src/Assets/gridFile.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
            int y = 0;
            while (reader.hasNextLine()) {
                int x = 0;
                for (char c : reader.nextLine().toCharArray()) {
                    Point2D coords = new Point2D(x, y);
                    switch (c) {
                        case 'X':
                            this.grid[x][y] = new Wall(coords);
                            break;
                        case 'P':
                            this.pacman = new PacMan(coords, RIGHT, Color.YELLOW);
                            this.grid[x][y] = new Lane(coords, this, this.pacman);
                            break;
                        case 'D':
                            this.grid[x][y] = new GhostDoor(coords, this);
                            break;
                        case 'G':
                            Color color = ghostColors[ghostAdded];
                            Ghost ghost = new Ghost(coords, UP, color);
                            this.ghosts.add(ghost);
                            this.grid[x][y] = new Lane(coords, this, ghost);
                            ghostAdded++;
                            break;
                        case 'T':
                            Portal portal = new Portal(coords, this);
                            this.portals.add(portal);
                            this.grid[x][y] = portal;
                            break;
                        default:
                            GumType type = GumType.values()[Character.getNumericValue(c)];
                            this.grid[x][y] = new Lane(coords, this, type);
                            break;
                    }
                    x++;
                }
                y++;
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void linkPortals() {
        for (int i = 0; i < this.portals.size(); i += 2) {
            this.portals.get(i).setTarget(this.portals.get(i + 1));
            this.portals.get(i + 1).setTarget(this.portals.get(i));
        }
    }

    public int getDimension() {
        return this.dimension;
    }

    public Tile[][] getTiles() {
        return this.grid;
    }

    public ArrayList<Ghost> getGhosts() {
        return this.ghosts;
    }

    public PacMan getPacMan() {
        return this.pacman;
    }

    public void kill(Entity entity) {
        if (entity instanceof PacMan) {
            this.stop();
        } else {
            entity.moveToStart();
        }
    }

    public boolean isFinished() {
        return this.running.get();
    }

    public Point2D getNextCoords(Point2D coords, Direction direction) {
        switch (direction) {
            case UP:
                return coords.add(0, -1);
            case DOWN:
                return coords.add(0, 1);
            case LEFT:
                return coords.add(-1, 0);
            case RIGHT:
                return coords.add(1, 0);
            default:
                return coords;
        }
    }

    public Tile getTileByCoords(Point2D coords) {
        return this.grid[(int) coords.getX()][(int) coords.getY()];
    }

    public boolean isReachable(Point2D coords) {
        return (coords.getX() > -1)
                && (coords.getX() < this.dimension)
                && (coords.getY() > -1)
                && (coords.getY() < this.dimension);
    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return this.score;
    }

    public boolean move(Entity entity, Direction direction) {
        Point2D entityCoords = entity.getCoords();
        Lane oldLane = ((Lane) this.getTileByCoords(entityCoords));
        Point2D nextCoords = this.getNextCoords(entityCoords, direction);

        if (this.isReachable(nextCoords)) {
            Tile newTile = this.getTileByCoords(nextCoords);

            if (entity instanceof PacMan && newTile instanceof GhostDoor) {
                return false;
            }

            if (newTile instanceof Lane) {
                Lane newLane = ((Lane) newTile);
                Entity enemy = newLane.getEntity();

                if (enemy != null) {
                    if (entity.canKill(enemy)) {
                        this.kill(enemy);
                    } else if (enemy.canKill(entity)) {
                        oldLane.removeEntity();
                        this.kill(entity);
                        return false;
                    } else {
                        return false;
                    }
                }

                if (newLane instanceof Portal) {
                    newLane = ((Portal) newLane).getTarget();
                }

                oldLane.removeEntity();
                entity.moveTo(newLane.getCoords());
                newLane.setEntity(entity);

                return true;
            }
        }

        return false;
    }
}
