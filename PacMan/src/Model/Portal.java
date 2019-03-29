/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.Game;
import static Util.GumType.EMPTY;
import javafx.geometry.Point2D;

/**
 *
 * @author thomas
 */
public class Portal extends Lane {
    
    private Portal target;
    
    public Portal(Point2D coords, Game game) {
        super(coords, game, EMPTY);
    }
    
    public void setTarget(Portal target) {
        this.target = target;
    }
    
    public Portal getTarget() {
        return this.target;
    }
    
}
