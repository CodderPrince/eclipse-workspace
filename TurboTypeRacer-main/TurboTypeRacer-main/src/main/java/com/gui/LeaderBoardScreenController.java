package com.gui;

import com.game.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LeaderBoardScreenController {
    @FXML
    private ImageView goBackButton;
    @FXML
    private TabPane tabPane;
    private Stage stage;
    private Parent root;

    @FXML
    void animationPop(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#E34255" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle(null);
    }

    @FXML
    void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        if(LoginManager.getCurrentPlayer()!=null){
            StartScreenController.sceneGenerator(stage,root,"MainScreen-view.fxml", event, "Main Screen");
        } else {
            StartScreenController.sceneGenerator(stage,root,"StartScreen-view.fxml", event, "Start Screen");
        }
    }

    @FXML
    void initialize() {
        assert tabPane != null : "fx:id=\"TabPane\" was not injected: check your FXML file 'LeaderBoardScreen-view.fxml'.";
        assert goBackButton != null : "fx:id=\"goBackButton\" was not injected: check your FXML file 'EditProfileScreen-view.fxml'.";
        initializeTabPane();
    }

    //Creates tabs for the tabPane and fills them.
    public void initializeTabPane(){
        for(Level level : LevelManager.getLevelDatabase()){
            Tab tab = new Tab("Level " + level.getLevelNum());
            tab.setClosable(false);

            TableView<PlayerData> tableView = createTable();
            fillTable(tableView, level);

            //Sorts the table if it's not empty.
            //adds it to the tab and adds the tab to the tabPane.
            if(!tableView.getItems().isEmpty()){
                tableView.getColumns().get(2).setSortType(TableColumn.SortType.DESCENDING);
                tableView.getSortOrder().add(tableView.getColumns().get(2));
                tableView.sort();

                tab.setContent(tableView);
                tabPane.getTabs().add(tab);
            }
        }
        tabPane.getTabs().remove(0); //Used to remove a placeholder tab.
    }

    //Returns TableView containing three columns "Icon | Player | Wins".
    public TableView<PlayerData> createTable(){
        TableView<PlayerData> tableView = new TableView<>();
        tableView.setEditable(false);

        TableColumn<PlayerData, ImageView> iconColumn = new TableColumn<>("Icon");
        TableColumn<PlayerData, String> playerNameColumn = new TableColumn<>("Player");
        TableColumn<PlayerData, Integer> winsColumn = new TableColumn<>("Wins");

        iconColumn.setCellValueFactory(new PropertyValueFactory<>("Icon"));
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("Player"));
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("Wins"));

        //Stops users from being able to reorder the table's columns
        iconColumn.setReorderable(false);
        playerNameColumn.setReorderable(false);
        winsColumn.setReorderable(false);

        //Sets the same size for every column
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        tableView.getColumns().add(iconColumn);
        tableView.getColumns().add(playerNameColumn);
        tableView.getColumns().add(winsColumn);

        return tableView;
    }

    //Fills a table's columns using every player's data.
    //The PlayerData class contains the player's profile icon, the player's name and their wins.
    public void fillTable(TableView<PlayerData> tableView, Level level){
        ObservableList<PlayerData> data = FXCollections.observableArrayList();
        for(Player p : LoginManager.getPlayerDatabase()){
            if(p.getScoreMap().containsKey(level)){
                ImageView icon = new ImageView(new Image(p.getProfileImage()));

                double ratio = Math.min(Screen.getPrimary().getBounds().getHeight() / icon.getImage().getHeight(), Screen.getPrimary().getBounds().getWidth() / icon.getImage().getWidth());
                icon.setFitHeight(icon.getImage().getHeight()*ratio*0.05);
                icon.setFitWidth(icon.getImage().getWidth()*ratio*0.05);

                data.add(new PlayerData(icon, p.getName(), p.getScore(level)));
            }
        }

        tableView.setItems(data);
    }

    //Used to export all the player scores in the playerScores.txt file
    public void exportScores(){
        Music.playButtonSound();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a Folder");

        // Show the file chooser dialog
        java.io.File selectedDirectory = directoryChooser.showDialog(stage);

        // Handle the selected directory
        if (selectedDirectory != null) {
            LoginManager.exportPlayerScores(selectedDirectory.getAbsolutePath().replace("\\", "/"), "playerScores.txt");
        }
    }

}
