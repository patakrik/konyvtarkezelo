/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import database.AdatbazisKezelo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Kalmár Patrik
 */
public class MainKezelo implements Initializable {

    @FXML
    private HBox konyv_info;
    @FXML
    private HBox tag_info;
    @FXML
    private TextField konyvIDinp;
    @FXML
    private Text konyvNev;
    @FXML
    private Text konyvSzerzo;
    @FXML
    private Text konyvStatusz;
    
    AdatbazisKezelo adatbazisKezelo;
    @FXML
    private TextField tagIDinp;
    @FXML
    private Text tagNev;
    @FXML
    private Text tagEmail;
    @FXML
    private Text tagTel;
    @FXML
    private JFXTextField konyvID;
    @FXML
    private ListView<String> kiadDataList;
    
    Boolean keszTorlesre = false;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(konyv_info, 1);
        JFXDepthManager.setDepth(tag_info, 1);
        
        adatbazisKezelo = AdatbazisKezelo.getInstance(); 
    }    
    
    
    /**
     * Az alábbi 5 javafx function adja mega main.fxml-en lévő gomboknak, 
     * hogy miket nyisson meg.
     * 
     * @param event 
     */
    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/add_member.fxml", "Tag hozzáadás");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/view_2.fxml", "Könyv hozzáadás");
    }

    @FXML
    private void loadMemberList(ActionEvent event) {
        loadWindow("/member_lista.fxml", "Tag hozzáadás");
    }

    @FXML
    private void loadBookList(ActionEvent event) {
        loadWindow("/konyv_lista.fxml", "Tag hozzáadás");
    }
    
    @FXML
    private void loadSettings(ActionEvent event) {
        loadWindow("/beallitasok.fxml", "Beallitasok");
    }

    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    //A main oldalon írja ki a konyv információkat.
    @FXML
    private void loadKonyvInfo(ActionEvent event) {
        String id = konyvIDinp.getText();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet rs = adatbazisKezelo.execQuery(qu);
        
        Boolean flag = false;
        
        try {
            while(rs.next()){
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");
                
                konyvNev.setText(bName);
                konyvSzerzo.setText(bAuthor);
                String status = (bStatus)?"Elérhető" : "Nem elérhető";
                konyvStatusz.setText(status);
                
                flag = true;
            }
            if(!flag){
                konyvNev.setText("Nem elérhető a könyv.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MainKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //A main oldalon írja ki a tag információkat.
    @FXML
    private void loadTagInfo(ActionEvent event) {
        String id = tagIDinp.getText();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet rs = adatbazisKezelo.execQuery(qu);
        
        try {
            while(rs.next()){
                String tName = rs.getString("name");
                String tEmail = rs.getString("email");
                String tMobile = rs.getString("mobile");
                
                tagNev.setText(tName);
                tagEmail.setText(tEmail);
                tagTel.setText(tMobile);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**A main oldalon a kiadás fülön tudjuk kiadni a könyveket személyeknek,
    *  és visszavenni.
    */
    @FXML
    private void loadKiadOp(ActionEvent event) {
        String membeID = tagIDinp.getText();
        String bookID = konyvIDinp.getText();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Megerősítve");
        alert.setHeaderText(null);
        alert.setContentText("Biztos ki akarod adni ezt a könyvet? -> " + konyvNev.getText() + "\n neki? -> " + tagNev.getText());
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
            String str = "INSERT INTO KIAD(memberID,bookID) VALUES ( "
                    + "'" + membeID + "',"
                    + "'" + bookID + "')";
            String str2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "'";
            System.out.println(str + " and " + str2);
            
            if (adatbazisKezelo.execAction(str)&&adatbazisKezelo.execAction(str2)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Sikeres");
                alert1.setHeaderText(null);
                alert1.setContentText("A könyv kiadása megtörtént");
                alert1.showAndWait();
                Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "Sikerült lekérdezni a tag infót");
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Hiba");
                alert1.setHeaderText(null);
                alert1.setContentText("Sikertelen a könyv kiadása");
                alert1.showAndWait();
                Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "Nem sikerült lekérdezni a tag infót");
            }
        } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Megszakítva");
                alert1.setHeaderText(null);
                alert1.setContentText("A könyv kiadása megszakítva");
                alert1.showAndWait();
            Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "Megszakítva");
        }
    }
    
    /**
     * Itt tudjuk lekérni az információt, hogy kinél van az aktuális könyv.
     * 
     * @param event 
     */
    @FXML
    private void loadBookInfo2(ActionEvent event) {
        ObservableList<String> kiadData = FXCollections .observableArrayList();
        keszTorlesre = false;
        
        String id = konyvID.getText();
        String qu = "SELECT * FROM KIAD WHERE bookID = '" + id +"'";
        ResultSet rs = adatbazisKezelo.execQuery(qu);
        try {
            while(rs.next()){
                String mKonyvID = id;
                String mTagID = rs.getString("memberID");
                Timestamp mKiadTime = rs.getTimestamp("kiadTime");
                int mMegujitCount = rs.getInt("megujit_count");
                
                kiadData.add("Kiadás dátuma és ideje: " + mKiadTime.toGMTString());
                kiadData.add("Megújítások száma: " + mMegujitCount);
                
                
                kiadData.add("---------Könyv információja---------" );
                qu = "SELECT * FROM BOOK WHERE ID = '" + mKonyvID + "'";
                ResultSet rl = adatbazisKezelo.execQuery(qu);
                while(rl.next()) {
                    kiadData.add("      Könyv neve: " +rl.getString("title"));
                    kiadData.add("      Könyv ID: " +rl.getString("id"));
                    kiadData.add("      Könyv szerzője: " +rl.getString("author"));
                    kiadData.add("      Könyv kiadója: " +rl.getString("publisher"));
                }
                    qu = "SELECT * FROM MEMBER WHERE ID = '" + mTagID + "'";
                    rl = adatbazisKezelo.execQuery(qu);
                    kiadData.add("---------Tag információja---------" );
                    while(rl.next()){
                        kiadData.add("      Név: " + rl.getString("name"));
                        kiadData.add("      Mobil: " + rl.getString("mobile"));
                        kiadData.add("      Email: " + rl.getString("email"));
                }
                    
                    keszTorlesre = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainKezelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       kiadDataList.getItems().setAll(kiadData);
    }

    /**
     * Itt tudjuk törölni a kiad táblából a könyvet.
     * 
     * @param event 
     */
    
    @FXML
    private void loadTorolOp(ActionEvent event) {
        if(!keszTorlesre){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SIKERTELEN");
            alert.setHeaderText(null);
            alert.setContentText("Kérlek válassz egy könyvet");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Megerősítve");
        alert.setHeaderText(null);
        alert.setContentText("Biztos vissza akarod venni a könyvet?");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
              
        String id = konyvID.getText();
        String ac1 = "DELETE FROM KIAD WHERE bookID = '" + id + "'";
        String ac2 = "UPDATE BOOK SET ISAVAIL = TRUE WHERE ID = '" + id + "'";
        
        if(adatbazisKezelo.execAction(ac1) && adatbazisKezelo.execAction(ac2)){
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("SIKERES");
            alert1.setHeaderText(null);
            alert1.setContentText("Kiadás törölve");
            alert1.showAndWait();
            Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "Kiadás törölve");
        }else{
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("SIKERTELEN");
            alert1.setHeaderText(null);
            alert1.setContentText("A könyv kiadásának törlése sikertelen");
            alert1.showAndWait();
            Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "Kiadás törlése sikertelen.");
            }
        }else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Megszakítva");
            alert1.setHeaderText(null);
            alert1.setContentText("Megszakítva a visszavétel");
            alert1.showAndWait();
        }
    }

    /**
     * Itt tudjuk meghosszabbítani a kiadást a könyvnek.
     * 
     * @param event 
     */
    @FXML
    private void loadMegujitOP(ActionEvent event) {
        if(!keszTorlesre){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SIKERTELEN");
            alert.setHeaderText(null);
            alert.setContentText("Kérlek válassz egy könyvet");
            alert.showAndWait();
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Megerősítve");
        alert.setHeaderText(null);
        alert.setContentText("Biztos meg akarod újítani?");
        
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get()==ButtonType.OK){
            String ac = "UPDATE KIAD SET kiadTime = CURRENT_TIMESTAMP, megujit_count = megujit_count+1 WHERE BOOKID = '" + konyvID.getText() + "'";
            System.out.println(ac);
            if(adatbazisKezelo.execAction(ac)){
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("SIKERES");
                alert1.setHeaderText(null);
                alert1.setContentText("A könyv megújítva");
                alert1.showAndWait();
                Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "A könyv megújítása sikeres");
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("SIKERTELEN");
            alert1.setHeaderText(null);
            alert1.setContentText("A könyv megújítása sikertelen");
            alert1.showAndWait();
            Logger.getLogger(MainKezelo.class.getName()).log(Level.INFO, "A könyv megújítása sikertelen");
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Megszakítva");
            alert1.setHeaderText(null);
            alert1.setContentText("A könyv megújítása megszakítva");
            alert1.showAndWait();
        }
    }

}
