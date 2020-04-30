/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konyvtarkezelo.beallitasok;

/**
 *
 * @author Csicsek Máté
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import konyvtar.database.AdatbazisKezelo;

import static javafx.application.Application.launch;

public class BeallitasokLoader extends Application {
    
    //Itt adjuk meg, melyik fxml fájlt nyissa meg.
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/beallitasok.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Settings");
        
        new Thread(() -> {
            AdatbazisKezelo.getInstance();
        }).start();       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
