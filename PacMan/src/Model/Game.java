package Model;

import com.sun.javafx.scene.traversal.Direction;
import static com.sun.javafx.scene.traversal.Direction.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;

public class Game extends Observable {

    private final int dimension = 21;
    private Tile[][] grid;
    private ArrayList<Entity> entities;
    private boolean end;
    private int score;

    public Game() {
        this.end = false;
        this.score = 0;
        this.initGrid();
    }

    private void initGrid() {
        this.entities = new ArrayList();
        this.grid = new Tile[this.dimension][this.dimension];

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
                            PacMan pacman = new PacMan(coords, RIGHT);
                            this.entities.add(pacman);
                            this.grid[x][y] = new Lane(coords, this, pacman);
                            break;
                        case 'G':
                            Ghost ghost = new Ghost(coords, UP);
                            this.entities.add(ghost);
                            this.grid[x][y] = new Lane(coords, this, ghost);
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

    public int getDimension() {
        return this.dimension;
    }

    public Tile[][] getTiles() {
        return this.grid;
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

    public void kill(Entity entity) {
        if(entity instanceof PacMan) {
            this.end = true;
        }
        this.entities.remove(entity);
    }

    public boolean isFinished() {
        return this.end;
    }

    public Point2D getNextCoords(Point2D coords, Direction direction) {
        switch (direction) {
            case UP:
                coords.add(0, -1);
                break;
            case DOWN:
                coords.add(0, 1);
                break;
            case LEFT:
                coords.add(-1, 0);
                break;
            case RIGHT:
                coords.add(1, 0);
                break;
        }
        return coords;
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
        ((Lane) this.getTileByCoords(entityCoords)).removeEntity();
        Point2D nextCoords = this.getNextCoords(entityCoords, direction);

        if (this.isReachable(nextCoords)) {
            Tile newTile = this.getTileByCoords(this.getNextCoords(entityCoords, direction));
            if (newTile instanceof Lane) {
                Lane lane = ((Lane) newTile);
                Entity enemy = lane.getEntity();

                if (entity.canKill(enemy)) {
                    this.kill(enemy);
                } else if (entity.canDie(enemy)) {
                    this.kill(entity);
                    return false;
                }
                
                lane.setEntity(entity);
            }
        }

        return false;
    }
}
