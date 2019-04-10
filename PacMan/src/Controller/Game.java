package Controller;

import Model.Entity.Ghost.*;
import Model.Board;
import Model.Entity.*;
import Model.Tile.*;
import Util.*;
import static Util.Direction.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;

public class Game extends Observable {

    private ArrayList<Ghost> ghosts;
    private boolean running;
    private int score;
    private int oldScore;
    private int bestScore;
    private int ghostScore;
    private int level;
    private ArrayList<Gate> portals;
    private GhostDoor ghostdoor;
    private ArrayList<Lane> gumLanes;
    private Board board;
    private PacMan pacman;
    private Blinky blinky;
    private Pinky pinky;
    private Inky inky;
    private Clyde clyde;

    public Game() {
        this.score = 0;
        this.oldScore = 0;
        this.bestScore = this.score;
        this.ghostScore = 200;
        this.level = 1;
        this.running = false;
        this.initGrid();
        this.linkPortals();
    }

    public void resetGum() {
        this.gumLanes.clear();
        for (int j = 0; j < this.getDimension(); j++) {
            for (int i = 0; i < this.getDimension(); i++) {
                Tile tile = this.board.getTile(i, j);
                if (tile instanceof Lane) {
                    ((Lane) tile).resetGum();
                }
            }
        }
    }

    public void resetGhosts() {
        this.ghosts.forEach((ghost) -> {
            ghost.setMoving(false);
            this.kill(ghost, true);
        });
        this.resetGhostScore();
    }

    public void resetScore() {
        this.score = 0;
        this.oldScore = 0;
    }

    public void resetGhostScore() {
        this.ghostScore = 200;
    }

    public void resetLevel() {
        this.level = 1;
    }

    public void start() {
        this.running = true;
        this.ghosts.forEach((ghost) -> {
            ghost.start();
        });
        this.blinky.setMoving(true);
        this.pacman.start();
    }

    public void stop() {
        this.running = false;
        this.ghosts.forEach((ghost) -> {
            ghost.stop();
        });
        this.pacman.stop();
    }

    public void update() {
        this.setChanged();
        this.notifyObservers();
    }

    private void initGrid() {
        this.portals = new ArrayList();
        this.ghosts = new ArrayList();
        this.gumLanes = new ArrayList();
        this.board = new Board(21);

        File file = new File("./src/Assets/gridFile.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
            int y = 0;
            while (reader.hasNextLine()) {
                int x = 0;
                for (char c : reader.nextLine().toCharArray()) {
                    Point2D coords = new Point2D(x, y);
                    Tile tile;
                    switch (c) {
                        case 'B':
                            this.blinky = new Blinky(coords, UP, this, 450);
                            this.ghosts.add(this.blinky);
                            tile = new Lane(coords, this, this.blinky);
                            break;
                        case 'P':
                            this.pinky = new Pinky(coords, UP, this, 300);
                            this.ghosts.add(this.pinky);
                            tile = new Lane(coords, this, this.pinky);
                            break;
                        case 'I':
                            this.inky = new Inky(coords, UP, this, 500);
                            this.ghosts.add(this.inky);
                            tile = new Lane(coords, this, this.inky);
                            break;
                        case 'C':
                            this.clyde = new Clyde(coords, UP, this, 550);
                            this.ghosts.add(this.clyde);
                            tile = new Lane(coords, this, this.clyde);
                            break;
                        case 'M':
                            this.pacman = new PacMan(coords, RIGHT, this, 300);
                            tile = new Lane(coords, this, this.pacman);
                            break;
                        case 'X':
                            tile = new Wall(coords);
                            break;
                        case 'D':
                            this.ghostdoor = new GhostDoor(coords, this, UP);
                            tile = this.ghostdoor;
                            break;
                        case 'S':
                            tile = new GhostLane(coords, this);
                            break;
                        case 'G':
                            Gate gate = new Gate(coords, this);
                            this.portals.add(gate);
                            tile = gate;
                            break;
                        default:
                            int gumNumber = Character.getNumericValue(c);
                            GumType type = GumType.values()[gumNumber];
                            Lane lane = new Lane(coords, this, type);
                            if (gumNumber != 0) {
                                this.gumLanes.add(lane);
                            }
                            tile = lane;
                            break;
                    }
                    this.board.setTile(x, y, tile);
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
        return this.board.getDimension();
    }

    public Tile[][] getTiles() {
        return this.board.getTiles();
    }

    public PacMan getPacMan() {
        return this.pacman;
    }

    public GhostDoor getGhostDoor() {
        return this.ghostdoor;
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
        return this.board.getTileByCoords(coords);
    }

    public boolean isReachable(Point2D coords) {
        return (coords.getX() > -1)
                && (coords.getX() < this.getDimension())
                && (coords.getY() > -1)
                && (coords.getY() < this.getDimension());
    }

    public void addScore(int score) {
        this.score += score;
        int pointsGap = 1000;
        if (this.score > (this.oldScore + pointsGap) && !this.pinky.canMove()) {
            this.pinky.setMoving(true);
        } else if (this.score > (this.oldScore + pointsGap * 2) && !this.inky.canMove()) {
            this.inky.setMoving(true);
        } else if (this.score > (this.oldScore + pointsGap * 3) && !this.clyde.canMove()) {
            this.clyde.setMoving(true);
        }
    }

    public int getScore() {
        return this.score;
    }

    public int getBestScore() {
        return this.bestScore;
    }

    public void updateBestScore() {
        this.bestScore = this.score;
    }

    public void updateOldScore() {
        this.oldScore = this.score;
    }

    public void updateGhostScore() {
        this.ghostScore *= 2;
    }

    public void increaseLevel() {
        this.level++;
    }

    public int getLevel() {
        return this.level;
    }

    public ArrayList<Lane> getGumLanes() {
        return this.gumLanes;
    }

    public void addGumLane(Lane lane) {
        this.gumLanes.add(lane);
    }

    public void removeGumLane(Lane lane) {
        this.gumLanes.remove(lane);
    }

    public void notifyPowerToGhosts() {
        this.ghosts.forEach((ghost) -> {
            if (!ghost.isScared()) {
                ghost.scare();
            }
        });
    }

    public void notifyEndPowerToGhosts() {
        this.ghosts.forEach((ghost) -> {
            if (ghost.isScared()) {
                ghost.resetScared();
            }
        });
    }

    private void applyMove(Entity entity, Point2D newCoords) {
        ((Lane) this.getTileByCoords(entity.getCoords())).removeEntity();
        entity.moveTo(newCoords);
    }

    private void kill(Entity entity, boolean gameReset) {
        if (entity instanceof PacMan && ((PacMan) entity).isAlive()) {
            this.getPacMan().loseLife();
            ((PacMan) entity).setAlive(false);
        }

        if (!gameReset && entity instanceof Ghost) {
            this.addScore(this.ghostScore);
            this.updateGhostScore();
        }

        ((Lane) this.getTileByCoords(entity.getCoords())).removeEntity();
        Entity conflict = ((Lane) this.getTileByCoords(entity.getStartingCoords())).getEntity();
        // for ghosts, wait thread until they can respawn
        if (conflict != null && entity instanceof Ghost && conflict instanceof Ghost) {
            synchronized (entity) {
                while (conflict.getCoords() == entity.getStartingCoords()) {
                    try {
                        entity.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                this.respawn(entity);
                entity.notify();
            }
        } else {
            this.respawn(entity);
        }
    }

    private void respawn(Entity entity) {
        if (entity instanceof PacMan && !((PacMan) entity).isAlive()) {
            ((PacMan) entity).setAlive(true);
        }
        entity.reset();
        if (entity instanceof Blinky) {
            this.blinky.setMoving(true);
        }
        ((Lane) this.getTileByCoords(entity.getCoords())).setEntity(entity);
    }

    public void updatePacManDirection(Direction direction) {
        if (this.pacman.isTurnBack()) {
            direction = direction.getOpposed();
        }

        Point2D adjacentCoords = this.getNextCoords(this.pacman.getCoords(), direction);
        if (this.isReachable(adjacentCoords)) {
            Tile tile = this.getTileByCoords(adjacentCoords);
            if (tile instanceof Lane && !(tile instanceof GhostLane)) {
                this.pacman.setDirection(direction);
            }
        }
    }

    public boolean move(Entity entity, Direction direction) {
        Point2D entityCoords = entity.getCoords();
        Point2D nextCoords = this.getNextCoords(entityCoords, direction);

        if (this.isReachable(nextCoords)) {
            Tile newTile = this.getTileByCoords(nextCoords);

            if (entity instanceof PacMan && newTile instanceof GhostLane) {
                return false;
            }

            if (newTile instanceof Lane) {
                Lane newLane = ((Lane) newTile);
                Entity enemy = newLane.getEntity();

                if (newLane instanceof Gate) {
                    newLane = ((Gate) newLane).getTarget();
                    nextCoords = newLane.getCoords();
                }

                if (enemy != null) {
                    if (entity.canKill(enemy)) {
                        this.kill(enemy, false);
                        if (entity instanceof Ghost) {
                            this.update();
                            return false;
                        }
                    } else if (enemy.canKill(entity)) {
                        this.kill(entity, false);
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

                if (this.gumLanes.isEmpty()) {
                    this.kill(this.pacman, false);
                    this.update();
                    return false;
                }

                this.update();
                return true;
            }
        }
        return false;
    }
}
