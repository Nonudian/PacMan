
package Model;

import Model.Tile.Tile;
import javafx.geometry.Point2D;

public class Board {
    private final int dimension;
    private final Tile[][] grid;

    public Board(int dimension) {
        this.dimension = dimension;
        this.grid = new Tile[this.dimension][this.dimension];
    }
    
    public int getDimension() {
        return this.dimension;
    }
    
    public Tile[][] getTiles() {
        return this.grid;
    }
    
    public Tile getTile(int x, int y) {
        return this.grid[x][y];
    }
    
    public void setTile(int x, int y, Tile tile) {
        this.grid[x][y] = tile;
    }
    
    public Tile getTileByCoords(Point2D coords) {
        return this.getTile((int) coords.getX(), (int) coords.getY());
    }
}
