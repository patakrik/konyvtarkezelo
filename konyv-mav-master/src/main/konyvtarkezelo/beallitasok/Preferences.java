/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konyvtarkezelo.beallitasok;

import com.google.gson.Gson;
import javafx.scene.control.Alert;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Csicsek Máté
 * 
 * Ebben a preferencben adjuk meg a változókat, és hozzuk létre a json txt-t.
 */
public class Preferences {
    public static final String CONFIG_FILE = "config.txt";
    
    int nDaysWithoutFine;
    float finePerDay;
    String username;
    String password;
    
    //Miket írjon ki az fxml-re.
    public Preferences() {
        nDaysWithoutFine = 21;
        finePerDay = (float) 2.5;
        username = "admin";
        password = "admin";
    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE); //Itt hozza létre a config.txt-t
            gson.toJson(preference,writer);
                    } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex){
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
    
    public static Preferences getPreferences(){
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return preferences;
    }

    public static void writePreferenceToFile(Preferences preference) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference,writer);
            
            /**
             * Értesítéseket ad mikor sikeres, és mikor nem.
             */
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sikeres");
                alert.setHeaderText(null);
                alert.setContentText("Beállítások frissítve");
                alert.showAndWait();
            } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed");
                alert.setContentText("Nincsenek mentve a beállítások");
                alert.showAndWait();
        
        } finally {
            try {
                writer.close();
            } catch (IOException ex){
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
