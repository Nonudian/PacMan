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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;

public class Game extends Observable {

    private final int dimension;
    private Tile[][] grid;
    private ArrayList<Ghost> ghosts;
    private boolean running;
    private int score;
    private ArrayList<Portal> portals;
    private PacMan pacman;

    public Game() {
        this.dimension = 21;
        this.score = 0;
        this.running = false;
        this.initGrid();
        this.linkPortals();
    }

    public void resetGum() {
        for (int j = 0; j < this.dimension; j++) {
            for (int i = 0; i < this.dimension; i++) {
                Tile tile = this.grid[i][j];
                if (tile instanceof Lane) {
                    ((Lane) tile).resetGum();
                }
            }
        }
    }

    public void resetGhosts() {
        this.ghosts.forEach((ghost) -> {
            this.kill(ghost);
        });
    }

    public void start() {
        this.running = true;
        this.ghosts.forEach((ghost) -> {
            ghost.start();
        });
        this.pacman.start();
    }

    public void stop() {
        this.ghosts.forEach((ghost) -> {
            ghost.stop();
        });
        this.pacman.stop();
        this.running = false;
    }

    public void update() {
        this.setChanged();
        this.notifyObservers();
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
                            this.pacman = new PacMan(coords, RIGHT, Color.YELLOW, this, 300);
                            this.grid[x][y] = new Lane(coords, this, this.pacman);
                            break;
                        case 'D':
                            this.grid[x][y] = new GhostDoor(coords, this);
                            break;
                        case 'G':
                            Color color = ghostColors[ghostAdded];
                            Ghost ghost = new Ghost(coords, UP, color, this, 300 + 50 * ghostAdded);
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

    public boolean isFinished() {
        return !this.running;
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

    public void notifyGhosts() {
        this.ghosts.forEach((ghost) -> {
            ghost.setTurnBack(true);
        });
    }

    private void applyMove(Entity entity, Point2D newCoords) {
        ((Lane) this.getTileByCoords(entity.getCoords())).removeEntity();
        entity.moveTo(newCoords);
    }

    private void kill(Entity entity) {
        ((Lane) this.getTileByCoords(entity.getCoords())).removeEntity();
        entity.moveToStartingCoords();
        ((Lane) this.getTileByCoords(entity.getCoords())).setEntity(entity);
        if (entity instanceof PacMan) {
            this.score = 0;
            entity.setTurnBack(false);
            this.resetGum();
            this.resetGhosts();
        }
    }

    public void updatePacManDirection(Direction direction) {
        if (this.pacman.isTurnBack()) {
            direction = direction.getOpposed();
        }
        this.pacman.setDirection(direction);
    }

    public boolean move(Entity entity, Direction direction) {
        Point2D entityCoords = entity.getCoords();
        Point2D nextCoords = this.getNextCoords(entityCoords, direction);

        if (this.isReachable(nextCoords)) {
            Tile newTile = this.getTileByCoords(nextCoords);

            if (entity instanceof PacMan && newTile instanceof GhostDoor) {
                return false;
            }

            if (newTile instanceof Lane) {
                Lane newLane = ((Lane) newTile);
                Entity enemy = newLane.getEntity();

                if (newLane instanceof Portal) {
                    newLane = ((Portal) newLane).getTarget();
                    nextCoords = newLane.getCoords();
                }

                if (enemy != null) {
                    if (entity.canKill(enemy)) {
                        this.kill(enemy);
                    } else if (enemy.canKill(entity)) {
                        this.kill(entity);
                        this.update();
                        return false;
                    } else {
                        return false;
                    }
                }

                if (!(entity instanceof PacMan)) {
                    entity.setDirection(direction);
                }
                this.applyMove(entity, nextCoords);
                newLane.setEntity(entity);
                this.update();

                return true;
            }
        }

        return false;
    }
}
