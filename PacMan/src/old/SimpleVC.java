/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package old;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.image.Image;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author frederic.armetta
 */
public class SimpleVC extends Application {
    
    public final int SIZE_X =10;
    public final int SIZE_Y = 10;
    
    @Override
    public void start(Stage primaryStage) {
        
        
        SimplePacMan spm = new SimplePacMan(SIZE_X, SIZE_Y); // initialisation du modèle
        
        GridPane grid = new GridPane(); // création de la grille 
        
        // Pacman.svg.png
        Image imPM = new Image("Pacman.png"); // préparation des images
        Image imVide = new Image("Vide.png");
        
        
        //img.setScaleY(0.01);
        //img.setScaleX(0.01);
        
        ImageView[][] tab = new ImageView[SIZE_X][SIZE_Y]; // tableau permettant de récupérer les cases graphiques lors du rafraichissement

        for (int i = 0; i < SIZE_X; i++) { // initialisation de la grille (sans image)
            for (int j = 0; j < SIZE_Y; j++) {
                ImageView img = new ImageView();
                
                tab[i][j] = img;
                
                grid.add(img, i, j);
            }
            
        }
        
        Observer o =  new Observer() { // l'observer observe l'obervable (update est exécuté dès notifyObservers() est appelé côté modèle )
            @Override
            public void update(Observable o, Object arg) {
                for (int i = 0; i < SIZE_X; i++) { // rafraichissement graphique
                    for (int j = 0; j < SIZE_Y; j++) {
                        
                        if (spm.getX() == i && spm.getY() == j) { // spm est à la position i, j => le dessiner
                            tab[i][j].setImage(imPM);
                            
                        } else {
                            
                            tab[i][j].setImage(imVide);
                        }
                        
                    }
                }    
            }
        };
        
        
        
        
        
        spm.addObserver(o);
        spm.start(); // on démarre spm
        
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        root.setOnKeyPressed((javafx.scene.input.KeyEvent event) -> {
            if (event.isShiftDown()) {
                spm.initXY(); // si on clique sur shift, on remet spm en haut à gauche
            }
        });
        
        grid.requestFocus();
         
        
        
    }
    
}
