/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.AdatbazisKezelo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author patakrik
 */
public class View_2Controller implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton save;
    @FXML
    private JFXButton cancel;
    
    private Boolean isInEditMode = Boolean.FALSE;
    
    AdatbazisKezelo adatbaziskezelo;
    
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        adatbaziskezelo = AdatbazisKezelo.getInstance();
        
        checkData();
    }
        
    public void inflateUI(KonyvListaKezelo.Book book) {
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }

    /**Ez a Könyv hozzáadás gombja
     * 
     * @param event 
     */
    @FXML
    private void addBook(javafx.event.ActionEvent event){
        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();
        
        //Errort dob ha nem töltjük ki.
        if(bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookAuthor.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Kérlek tölts ki minden mezőt.");
            alert.showAndWait();
            return;
        }
/*        stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "  id varchar(200) primary key,\n"
                        + "  title varchar(200),\n"
                        + "  author varchar(200),\n"
                        + "  publisher varchar(200),\n"
                        + "  isAvail boolean default true"
                        + " )");
        
*/      //Itt adjuk hozzá az adatbázishoz az adatokat.  
        String qu = "INSERT INTO BOOK VALUES (" +
                "'" + bookID + "',"+
                "'" + bookName + "',"+
                "'" + bookAuthor + "'," +
                "'" + bookPublisher + "'," +
                "" + "true" + "" +
                ")";
        System.out.println(qu);
        if (adatbaziskezelo.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Sikeres");
            alert.showAndWait();
            Logger.getLogger(View_2Controller.class.getName()).log(Level.INFO, "Sikerült a könyv felvétele");
        } else  { //error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Nem sikerült");
            alert.showAndWait();
            Logger.getLogger(View_2Controller.class.getName()).log(Level.INFO, "Nem sikerült");
        }
    }
    

    private void checkData() {
        String qu = "SELECT title FROM BOOK";
        ResultSet rs = adatbaziskezelo.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(View_2Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //A Bezárás gomb
    @FXML
    private void cancel(javafx.event.ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}

