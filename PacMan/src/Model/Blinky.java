/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Game;
import Util.Direction;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**
 *
 * @author thomas
 */
public class Blinky extends Ghost {
    
    public Blinky(Point2D coords, Direction direction, Game game, int interval) {
        super(coords, direction, Color.RED, game, interval);
    }
    
}
