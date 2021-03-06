package konyvtarkezelo.ui.konyvlista;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import konyvtar.database.AdatbazisKezelo;
import konyvtar.ui.addbook.View_2Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KonyvListaKezelo implements Initializable {

    ObservableList<Book> list = FXCollections.observableArrayList();

    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol;
    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private Stage getStage() {
        return (Stage) tableView.getScene().getWindow();
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availabilty"));
    }

    private void loadData() {
        list.clear();

        AdatbazisKezelo kezelo = AdatbazisKezelo.getInstance();
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = kezelo.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                String author = rs.getString("author");
                String id = rs.getString("id");
                String publisher = rs.getString("publisher");
                Boolean avail = rs.getBoolean("isAvail");

                list.add(new Book(titlex, id, author, publisher, avail));

            }
        } catch (SQLException ex) {
            Logger.getLogger(View_2Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableView.setItems(list);
    }

//    @FXML
//    private void handleBookDeleteOption(ActionEvent event) {
//        //Fetch the selected row
//        Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
//        if (selectedForDeletion == null) {
//            AlertMaker.showErrorMessage("No book selected", "Please select a book for deletion.");
//            return;
//        }
//        if (DatabaseHandler.getInstance().isBookAlreadyIssued(selectedForDeletion)) {
//            AlertMaker.showErrorMessage("Cant be deleted", "This book is already issued and cant be deleted.");
//            return;
//        }
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Deleting book");
//        alert.setContentText("Are you sure want to delete the book " + selectedForDeletion.getTitle() + " ?");
//        Optional<ButtonType> answer = alert.showAndWait();
//        if (answer.get() == ButtonType.OK) {
//            Boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
//            if (result) {
//                AlertMaker.showSimpleAlert("Book deleted", selectedForDeletion.getTitle() + " was deleted successfully.");
//                list.remove(selectedForDeletion);
//            } else {
//                AlertMaker.showSimpleAlert("Failed", selectedForDeletion.getTitle() + " could not be deleted");
//            }
//        } else {
//            AlertMaker.showSimpleAlert("Deletion cancelled", "Deletion process cancelled");
//        }
//    }

//    @FXML
//    private void handleBookEditOption(ActionEvent event) {
//        //Fetch the selected row
//        Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
//        if (selectedForEdit == null) {
//            AlertMaker.showErrorMessage("No book selected", "Please select a book for edit.");
//            return;
//        }
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"));
//            Parent parent = loader.load();
//
//            BookAddController controller = (BookAddController) loader.getController();
//            controller.inflateUI(selectedForEdit);
//
//            Stage stage = new Stage(StageStyle.DECORATED);
//            stage.setTitle("Edit Book");
//            stage.setScene(new Scene(parent));
//            stage.show();
//            LibraryAssistantUtil.setStageIcon(stage);
//
//            stage.setOnHiding((e) -> {
//                handleRefresh(new ActionEvent());
//            });
//
//        } catch (IOException ex) {
//            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadData();
    }

    @FXML
    private void closeStage(ActionEvent event) {
        getStage().close();
    }

    public static class Book {

        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleStringProperty availabilty;

        public Book(String title, String id, String author, String pub, Boolean avail) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(pub);
            if (avail) {
                this.availabilty = new SimpleStringProperty("Elérhető");
            } else {
                this.availabilty = new SimpleStringProperty("Hiány");
            }
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public String getAvailabilty() {
            return availabilty.get();
        }

    }

}
