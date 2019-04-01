package View;

import Util.Direction;
import Model.Entity;
import Controller.Game;
import Model.Ghost;
import Model.GhostDoor;
import Util.GumType;
import static Util.GumType.*;
import Model.Tile;
import Model.Lane;
import Model.PacMan;
import Model.Portal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Platform;

public class GameView extends Application {

    private Game game;
    private Observer observer;
    private TilePane gridTiles;
    private Stage stage;

    private void initObservable() {
        this.observer = (Observable o, Object arg) -> {
            // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle)
            Platform.runLater(() -> {
                this.drawGame();
                this.display();
            });
        };
        this.game.addObserver(this.observer);
        this.game.start();
    }

    private void drawGame() {
        Tile[][] grid = this.game.getTiles();
        this.gridTiles = new TilePane();

        for (int y = 0; y < this.game.getDimension(); y++) {
            for (int x = 0; x < this.game.getDimension(); x++) {
                Tile tile = grid[x][y];
                StackPane pane = new StackPane();
                Rectangle rect = new Rectangle(30, 30);
                pane.getChildren().add(rect);
                if (tile instanceof Lane) {
                    if (tile instanceof Portal) {
                        rect.setFill(Color.WHITE);
                    } else if (tile instanceof GhostDoor) {
                        rect.setFill(Color.GREY);
                    } else {
                        rect.setFill(Color.BLACK);
                    }
                    Lane lane = ((Lane) tile);
                    GumType type = lane.getState();
                    if (type != EMPTY) {
                        Circle gum = new Circle(15, 15, 5, Color.WHITE);
                        switch(type) {
                            case INVERTED:
                                gum.setFill(Color.RED);
                            case SUPER:
                                gum.setRadius(10);
                                break;
                        }
                        pane.getChildren().add(gum);
                        gum.requestFocus();
                    }

                    // at start, entities spawn on EMPTY Lanes
                    Entity entity = lane.getEntity();
                    if (entity != null) {
                        if (entity instanceof PacMan) {
                            int startingAngle;
                            switch (entity.getDirection()) {
                                case LEFT:
                                    startingAngle = 225;
                                    break;
                                case UP:
                                    startingAngle = 135;
                                    break;
                                case RIGHT:
                                    startingAngle = 45;
                                    break;
                                case DOWN:
                                    startingAngle = 315;
                                    break;
                                default:
                                    startingAngle = 45;
                            }
                            Arc pacman = new Arc(0, 0, 10, 10, startingAngle, 270);
                            pacman.setType(ArcType.ROUND);
                            pacman.setFill(entity.getColor());
                            pane.getChildren().add(pacman);
                            pacman.requestFocus();
                        } else if (entity instanceof Ghost) {
                            Rectangle ghost = new Rectangle(15, 15);
                            ghost.setFill(entity.getColor());
                            pane.getChildren().add(ghost);
                            ghost.requestFocus();
                        }
                    }
                } else {
                    rect.setFill(Color.BLUE);
                }
                this.gridTiles.getChildren().add(pane);
            }
        }
    }

    private void display() {
        Scene scene = new Scene(this.gridTiles, this.game.getDimension() * 30, this.game.getDimension() * 30);

        this.stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            this.game.stop();
            this.stage.close();
        });

        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    this.game.updatePacManDirection(Direction.get(event.getCode()));
            }
        });

        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.centerOnScreen();
        this.stage.setTitle("Pac Man");
        this.stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.game = new Game();
        this.stage = primaryStage;
        this.drawGame();
        this.display();
        this.initObservable();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
