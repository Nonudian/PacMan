package ViewController;

import Model.Game;
import Model.GumType;
import static Model.GumType.*;
import Model.Tile;
import Model.Lane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameView extends Application {

    private Game game;
    private TilePane gridTiles;

    private void drawGame() {
        Tile[][] grid = this.game.getTiles();
        this.gridTiles = new TilePane();

        for (int y = 0; y < this.game.getDimension(); y++) {
            for (int x = 0; x < this.game.getDimension(); x++) {
                Tile tile = grid[x][y];
                StackPane pane = new StackPane();
                Rectangle rect = new Rectangle(30, 30, Color.BLUE);
                if (tile instanceof Lane) {
                    rect.setFill(Color.BLACK);
                    GumType type = ((Lane) tile).getType();
                    if (type != EMPTY) {
                        Circle gum = new Circle(15, 15, 5, Color.WHITE);
                        if(type == SUPER) {
                            gum.setRadius(10);
                        }
                        pane.getChildren().addAll(rect, gum);
                    } else {
                       pane.getChildren().add(rect); 
                    }
                } else {
                    pane.getChildren().add(rect);
                }
                this.gridTiles.getChildren().add(pane);
            }
            
        }
    }
    
    private void display(Stage primaryStage) {
        primaryStage.setScene(new Scene(this.gridTiles, this.game.getDimension()*30, this.game.getDimension()*30));
        
        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            primaryStage.close();
        });
        
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.game = new Game();
        this.drawGame();
        this.display(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
