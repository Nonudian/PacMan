package View;

import Util.Direction;
import Model.Entity;
import Controller.Game;
import Model.Ghost;
import Model.GhostDoor;
import Model.GhostLane;
import Util.GumType;
import static Util.GumType.*;
import Model.Tile;
import Model.Lane;
import Model.PacMan;
import Model.Portal;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameView extends Application {

    private Game game;
    private Observer observer;
    private TilePane board;
    private TilePane gridTiles;
    private BorderPane rootPane;
    private Stage stage;

    private void initObservable() {
        this.observer = (Observable o, Object arg) -> {
            Platform.runLater(() -> {
                if (!this.game.isFinished()) {
                    this.drawGame();
                    this.display();
                }
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
                    } else if (tile instanceof GhostLane) {
                        if (tile instanceof GhostDoor) {
                            rect.setFill(Color.GREY);
                        } else {
                            rect.setFill(Color.BLUE);
                        }
                    } else {
                        rect.setFill(Color.BLACK);
                    }
                    Lane lane = ((Lane) tile);
                    GumType type = lane.getState();
                    if (type != EMPTY) {
                        Circle gum = new Circle(15, 15, 5, Color.WHITE);
                        switch (type) {
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
                    rect.setFill(Color.DARKBLUE);
                }
                this.gridTiles.getChildren().add(pane);
            }
        }
        this.drawBoard();

        this.rootPane = new BorderPane();
        this.rootPane.setTop(this.board);
        this.rootPane.setBottom(this.gridTiles);
    }

    private void drawBoard() {
        this.board = new TilePane();

        int rectHeight = this.game.getDimension() * 30 / 3;

        // [SCORES]
        VBox scoresBox = new VBox();
        
        Rectangle bestScoreRect = new Rectangle(rectHeight, 20);
        Rectangle scoreRect = new Rectangle(rectHeight, 40);
        Text bestScore = new Text(27, 27, "BEST: " + String.valueOf(this.game.getBestScore()));
        Text score = new Text(27, 27, String.valueOf(this.game.getScore()));
        bestScore.setFill(Color.WHITE);
        score.setFill(Color.WHITE);
        try {
            String fontFile = "./src/Assets/Fonts/emulogic.ttf";
            InputStream fontBestScore = new FileInputStream(fontFile);
            InputStream fontScore = new FileInputStream(fontFile);
            
            bestScore.setFont(Font.loadFont(fontBestScore, 9));
            score.setFont(Font.loadFont(fontScore, 18));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
        }
        StackPane bestScorePane = new StackPane(bestScoreRect, bestScore);
        bestScorePane.setAlignment(Pos.BOTTOM_CENTER);
        StackPane scorePane = new StackPane(scoreRect, score);
        scoresBox.getChildren().addAll(bestScorePane, scorePane);

        // [BANNER]
        StackPane bannerPane = new StackPane();
        ImageView imageView = new ImageView("/Assets/Images/Pac_Man_Logo.png");
        Rectangle bannerRect = new Rectangle(rectHeight, 60);
        bannerPane.getChildren().addAll(bannerRect, imageView);

        // [LIVES]
        VBox gameBox = new VBox();
        
        Rectangle levelRect = new Rectangle(rectHeight, 20);
        Rectangle livesRect = new Rectangle(rectHeight, 40);
        Text level = new Text(27, 27, "LVL: " + this.game.getLevel());
        level.setFill(Color.WHITE);
        try {
            String fontFile = "./src/Assets/Fonts/emulogic.ttf";
            InputStream fontLevel = new FileInputStream(fontFile);
            level.setFont(Font.loadFont(fontLevel, 9));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GameView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TilePane lives = new TilePane();
        for (int i = 0; i < this.game.getPacMan().getRemainingLives(); i++) {
            Arc pacmanLife = new Arc(0, 0, 10, 10, 45, 270);
            pacmanLife.setType(ArcType.ROUND);
            pacmanLife.setFill(Color.YELLOW);
            lives.getChildren().add(pacmanLife);
        }
        lives.setAlignment(Pos.CENTER);
        
        StackPane levelPane = new StackPane(levelRect, level);
        levelPane.setAlignment(Pos.BOTTOM_CENTER);
        StackPane livesPane = new StackPane(livesRect, lives);
        gameBox.getChildren().addAll(levelPane, livesPane);
        

        this.board.setMaxSize(this.game.getDimension() * 30, 60);
        this.board.getChildren().addAll(scoresBox, bannerPane, gameBox);
    }

    private void display() {
        int sceneHeight = this.game.getDimension() * 30;
        int sceneWidth = this.game.getDimension() * 30 + 60;
        Scene scene = new Scene(this.rootPane, sceneHeight, sceneWidth);

        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    this.game.updatePacManDirection(Direction.get(event.getCode()));
            }
        });

        Image icon = new Image(getClass().getResourceAsStream("/Assets/Images/Pac_Man_Icon.png"));
        this.stage.getIcons().add(icon);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.centerOnScreen();
        this.stage.setTitle("Pac-Man Game");
        this.stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.game = new Game();
        this.stage = primaryStage;
        this.stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            this.game.stop();
            this.stage.close();
        });
        this.drawGame();
        this.display();
        this.initObservable();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
