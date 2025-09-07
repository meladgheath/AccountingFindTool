package org.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Duration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet ;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class HelloApplication extends Application {

    private TextField ExeclFile ;
    private TextField outputCellTextField ;

    private TextField start ;
    private TextField accountTextField ;

    private Text waiting ;
    private Scene scene ;
    private Timeline time ;

    private static String change = "wait" ;

    private Label TotalResult   ;
    private Label successResult ;
    private Label errorsResult  ;
    private int count ;

    private float percent ;

    @Override
    public void start(Stage stage) throws IOException {


        Label filePath = new Label("File Path : ");
        ExeclFile = new TextField();
        Button    ExeclFindBtn = new Button("Browser ");

        ExeclFile.setDisable(true);
        ExeclFindBtn.setOnAction(browserBtn());


        Label  startLable = new Label("Start Row : ");
        start = new TextField();
        start.setPrefWidth(35);
        start.setMaxWidth(35);
        start.setMinWidth(35);

        Label Cell = new Label("OutPut Cell:");
        outputCellTextField = new TextField();
        outputCellTextField.setMinWidth(35);
        outputCellTextField.setMaxWidth(35);
        outputCellTextField.setPrefWidth(35);

        Label accountCellLable  = new Label("Account Cell : ");
        accountTextField = new TextField();
        accountTextField.setPrefWidth(35);
        accountTextField.setMinWidth(35);
        accountTextField.setMaxWidth(35);


        Button convertBtn = new Button("Convert");
        Platform.runLater(()-> convertBtn.setOnAction(convertBtnAction()));

        waiting = new Text(". . .");

        GridPane controlPane = new GridPane();
        controlPane.add(filePath, 0 , 0);
        controlPane.add(ExeclFile,1,0);
        controlPane.add(ExeclFindBtn, 2 , 0);

        controlPane.add(startLable,0,1);
        controlPane.add(start,1,1);

        controlPane.add(Cell,0,2);
        controlPane.add(outputCellTextField,1,2);

        controlPane.add(accountCellLable,0,3);
        controlPane.add(accountTextField,1,3);

        controlPane.add(convertBtn,1,4);

        controlPane.setHgap(5);
        controlPane.setVgap(6);
        controlPane.setAlignment(Pos.CENTER);

        AnchorPane waitPane = new AnchorPane();
        waitPane.getChildren().add(waiting);
        AnchorPane.setLeftAnchor(waitPane,5d);
        AnchorPane.setBottomAnchor(waitPane,5d);

        VBox leftSide = new VBox();
        leftSide.getChildren().addAll(controlPane,waitPane);
        leftSide.setAlignment(Pos.CENTER);

        Separator centerLine = new Separator();
        centerLine.setOrientation(Orientation.VERTICAL);
        //////////////////////////////////////// right border
        Label TotalLabel = new Label("Totals : ");
        Label successLabel = new Label("Successfully Process : ") ;
        Label errorsLabel = new Label("Error Process : ");

        TotalResult = new Label();
        successResult = new Label();
        errorsResult = new Label();

        GridPane right = new GridPane();
        right.add(TotalLabel,0,0);
        right.add(TotalResult,1,0);
        right.add(successLabel,0,1);
        right.add(successResult,1,1);
        right.add(errorsLabel,0,2);
        right.add(errorsResult,1,2);
        right.setVgap(8);
        right.setHgap(5);
        right.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setLeft(leftSide);
        root.setCenter(centerLine);
        root.setRight(right);
        root.setPadding(new Insets(0,150,0,25));
        /// ///////////////////////////////////
        scene = new Scene(root,650,200);
        stage.setTitle("!?");
        stage.setScene(scene);
        stage.show();

        time = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (change == null)
                System.out.println("isEmpty");
            else {
                if (change.length() < 13)
                    change = change + ". ";
                else
                    change = "wait";

                float d = (percent - (int) percent   )*100; // to take the first two number after the point

                waiting.setText(change+" "+((int)percent)+"."+((int) d)+"%");
            }
        }));
        time.setCycleCount(Timeline.INDEFINITE);

        task.setOnRunning(e-> time.play());
        task.setOnSucceeded(e->{
            time.stop();
            waiting.setText("the search will Done . . .");
        });
    }

    public EventHandler<ActionEvent> browserBtn () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FileChooser execlFile = new FileChooser();
                ExeclFile.setText(execlFile.showOpenDialog(null).getAbsolutePath());
//                System.out.println(scene.getWidth());
//                System.out.println(scene.getHeight());
                FileInputStream in = null ;
                try {
                     in = new FileInputStream(ExeclFile.getText());
                    Sheet s = Execl.getFileExeclSheet(in) ;
                    count = s.getLastRowNum();
                    TotalResult.setText(count+"");

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    try {
                        Execl.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        };
    }

    public EventHandler<ActionEvent> convertBtnAction () {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Thread tr = new Thread(task);
                tr.start();
            }
        };
    }

    Task<Void>  task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            FileOutputStream out = null;

            int postion = -1 ;
            int account = -1 ;
            here: for (char i = 'A' ; i <= 'z' ; i++) {
                postion++ ;
                if (i == outputCellTextField.getText().charAt(0))
                    break here;
            }
            here: for (char i = 'A' ; i <= 'z' ; i++) {
                account++ ;
                if (i == accountTextField.getText().charAt(0))
                    break here;
            }
            System.out.println("the poistion = "+postion);
            System.out.println("the account  = "+account);


            int successfully = 0 ;
            int error = 0 ;
            int stopon = 0 ;

            try {
                Sheet s = Execl.getFileExeclSheet(new FileInputStream(ExeclFile.getText()));
//                int count = s.getLastRowNum()+1 ;
                int startPoint = Integer.parseInt(start.getText());


                AccountRepository repository = new AccountRepository();

                int j = 1 ;
            Connection con = null ;
            try {
                con = OracleDatabase.getConnection().createStatement().getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

                for (int i = startPoint ; i < count ; i++) {

                    Row row = s.getRow(i);
                    Cell cell = row.getCell(account);

                    cell.setCellType(CellType.STRING);
                    String acc = cell.toString() ;
                    String check = "" ;
                    Account person = null ;
                    if (acc.length() == 14)
                        acc = "0"+acc ;
                    if (acc.length() == 15)
                    person = repository.getInfo(acc,con);

                    Cell c = row.createCell(postion);
                    if (person != null) {
                    c.setCellValue(person.getIban()+";"+person.getBrn()+";"+person.getAccount()+";"+" لا توجد "+";"+person.getName());
//                        c.setCellValue(check);
                        System.out.println("one Account has been converted . . ."+i+" ");
                        Thread.sleep(100);
                        successfully++;
                        int finalSuccessfully = successfully;
                        Platform.runLater(()-> successResult.setText(finalSuccessfully +""));

                    }else {
                        c.setCellValue("N/A;N/A;N/A;N/A;N/A");
                        Thread.sleep(100);
                        error++;
                        int Erros = error;
                        Platform.runLater(()-> errorsResult.setText(Erros+""));
                    }
                    j++ ;
                    if (j == 800){
                        FileOutputStream outer = new FileOutputStream(ExeclFile.getText());
                        Execl.save(outer);
                        j=0 ;
                        OracleDatabase.closeConnection();
                        outer.close();
                    con = OracleDatabase.getConnection();
                    }
                    stopon++;
                    percent = (i / (float) count ) * 100 ;

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                FileOutputStream outt = null;
                try {
                    outt = new FileOutputStream(ExeclFile.getText());
                    Execl.save(outt);
                    outt.close();
                    OracleDatabase.closeConnection();
                    waiting.setText("Successfully : "+ successfully+" ; Error : "+error +"\n Stop On : "+stopon );
                    Alert message = new Alert(Alert.AlertType.INFORMATION);
                    message.setContentText("Successfully : "+ successfully+" ; Error : "+error +"\n Stop On : "+stopon );
                    message.show();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            return null;
        }
    }};
}