package Model;

import com.sun.javafx.scene.traversal.Direction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;

public class Game extends Observable {

    private final int dimension = 21;
    private final int defaultPacManCount = 1;
    private final int defaultGhostCount = 4;
    private Tile[][] grid;
    private Entity[] entities;

    public Game() {
        initGrid();
        this.entities = new Entity[this.defaultPacManCount + this.defaultGhostCount];
    }

    private void initGrid() {
        this.grid = new Tile[this.dimension][this.dimension];
        
        File file = new File("./src/Assets/gridFile.txt");
        Scanner reader;
        try {
            reader = new Scanner(file);
            int y = 0;
            while (reader.hasNextLine()) {
                int x = 0;
                for (char c : reader.nextLine().toCharArray()) {
                    if (c == 'X') {
                        this.grid[x][y] = new Wall(new Point2D(x, y));
                    }
                    else {
                        GumType type = GumType.values()[Character.getNumericValue(c)];
                        this.grid[x][y] = new Lane(new Point2D(x, y), this, type);
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

    public boolean isFinished() {
        return true;
    }

    public void move(Entity entity, Direction direction) {

    }
}
