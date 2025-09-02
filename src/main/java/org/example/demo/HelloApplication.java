package org.example.demo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;

public class HelloApplication extends Application {

    private TextField ExeclFile ;
    private TextField outputCellTextField ;
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);


        Label filePath = new Label("File Path: ");
        ExeclFile = new TextField();
        Button    ExeclFindBtn = new Button("Browser ");

        ExeclFile.setDisable(true);
        ExeclFindBtn.setOnAction(browserBtn());

//        HBox firstRow = new HBox();
//        firstRow.getChildren().add(filePath);
//        firstRow.getChildren().add(ExeclFile);
//        firstRow.getChildren().add(ExeclFindBtn);
//        firstRow.setSpacing(5);
////        firstRow.setPadding(new Insets(3));


        Label  startLable = new Label("Start Row : ");
        TextField start = new TextField();
        start.setPrefWidth(35);
        start.setMaxWidth(35);
        start.setMinWidth(35);

        Label Cell = new Label("OutPut Cell:");
        outputCellTextField = new TextField();
        outputCellTextField.setMinWidth(35);
        outputCellTextField.setMaxWidth(35);
        outputCellTextField.setPrefWidth(35);



        Button convertBtn = new Button("Convert");
        convertBtn.setOnAction(convertBtnAction());

        GridPane root = new GridPane();
        root.add(filePath, 0 , 0);
        root.add(ExeclFile,1,0);
        root.add(ExeclFindBtn, 2 , 0);

        root.add(startLable,0,1);
        root.add(start,1,1);

        root.add(Cell,0,2);
        root.add(outputCellTextField,1,2);

        root.add(convertBtn,1,3);
//        root.setAlignment();
        root.setHgap(5);
        root.setVgap(6);
        root.setAlignment(Pos.CENTER);
        /// ///////////////////////////////////
        Scene scene = new Scene(root,250,200);
        stage.setTitle("!?");
        stage.setScene(scene);
        stage.show();
    }

    public EventHandler<ActionEvent> browserBtn () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FileChooser execlFile = new FileChooser();
                ExeclFile.setText(execlFile.showOpenDialog(null).getAbsolutePath());


            }
        };
    }

    public EventHandler<ActionEvent> convertBtnAction () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // here the code of converting
                System.out.println(outputCellTextField.getText());
                int postion = -1 ;
                System.out.println();
                here: for (char i = 'A' ; i <= 'z' ; i++) {
                    postion++ ;
                    if (i == outputCellTextField.getText().charAt(0))
                    break here;
                }


            }
        };
    }
}