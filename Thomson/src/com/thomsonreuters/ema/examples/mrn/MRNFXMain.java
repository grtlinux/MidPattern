/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomsonreuters.ema.examples.mrn;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author zoya.farberov
 */
public class MRNFXMain extends Application {

    private static int MAX_TEXTAREA_LEN = 125000;
  //  private Object gridpane;
    private TextArea txtAreas[];
    private Button stopButtons[];
    private boolean frozen[];

    private MRNConsumer _consumer = null;
    private static String[] _argsPassed;
    private Thread _consumerThread = null;

    @Override
    public void start(Stage primaryStage) {
        _consumer = new MRNConsumer(_argsPassed);
        primaryStage.setTitle("MRN News Realtime");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("grid-pane");


        ColumnConstraints columns[] = new ColumnConstraints[4];
        for(int i = 0; i <4; i ++) {
            columns[i] = new ColumnConstraints();
            columns[i].setPercentWidth(25);
        }
        grid.getColumnConstraints().addAll(columns[0], columns[1], columns[2], columns[3]);

        txtAreas = new TextArea[4];
        stopButtons = new Button[4];
        frozen = new boolean[4];
        for(int i = 0;i <4; i++) {
            Label feedName = new Label(MRNConsumer._ricsMRN[i]);
            txtAreas[i] = new TextArea();
            txtAreas[i].setPrefRowCount(100);
            txtAreas[i].setPrefColumnCount(100);
            txtAreas[i].setWrapText(true);
            txtAreas[i].setPrefWidth(150);
            stopButtons[i] = new Button("Stop");
            frozen[i] = false;
            stopButtons[i].setOnAction(this::handleStopButtonAction);

            GridPane.setHalignment(txtAreas[i], HPos.LEFT);
            grid.add(feedName, i, 1);
            grid.add(txtAreas[i], i, 3);
            grid.add(stopButtons[i], i, 5);
            String noDataYetTxt = "No data yet for feed "+feedName.getText()+"\n";
            txtAreas[i].appendText(noDataYetTxt);
 //           txtAreas[i].getStyleClass().add("text-area");
        }

        Scene scene = new Scene(grid, 1200, 900);
        System.out.println("scene stylesheets: " + scene.getStylesheets());

        try {
            File file = new File(".\\css\\dark.css");
            URL url = file.toURI().toURL();
            scene.getStylesheets().add(url.toExternalForm());
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Exception reading css: "+ e);
            e.printStackTrace(System.err);
        }


        primaryStage.show();
        _consumerThread = new Thread(_consumer);
        _consumer.setFXMain(this);
        _consumerThread.start();  //      initCompleted = true;
    }

    public void updateTxtArea(int i, String str) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (!frozen[i]) {
                    String currentText = txtAreas[i].getText();
                    if ((currentText.length() + str.length()) > MAX_TEXTAREA_LEN) {
                        currentText = currentText.substring((new Double(MAX_TEXTAREA_LEN * 0.5)).intValue());
                        txtAreas[i].clear();
                        txtAreas[i].setText(currentText);
                    }
                    txtAreas[i].appendText(str);
                }
            }
        });
    }

    private void handleStopButtonAction(ActionEvent event) {
        for (int i = 0; i < 4; i++) {
            if ((Button) event.getSource() == stopButtons[i]) {
                if (frozen[i] == false) {
                    frozen[i] = true;
                    stopButtons[i].setText("Resume");
                } else {
                    frozen[i] = false;
                    stopButtons[i].setText("Stop");
                }
            }
        }
    }

    public void setConsumer(MRNConsumer consumer) {
        _consumer = consumer;
    }

    public MRNConsumer getConsumer() {
        return _consumer;
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("static-access")
	public static void main(String[] args) {
    	// KANG-20180917
    	args = new String[] { "10.117.216.106", "14002", "ELEKTRON_DD", "testuser" };
    	args = new String[] { "239.234.234.200", "51031", "ELEKTRON_DD", "testuser" };
    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "TY6_03_RHB_KR02895" };
    	args = new String[] { "159.220.246.3",  "14002", "hEDD", "TY6_03_RHB_KR02895" };
    	//args = new String[] { "159.220.246.19",  "8101", "hEDD", "TY6_03_RHB_KR02895" };
    	//args = new String[] { "159.220.246.19", "14002", "hEDD", "TY6_03_RHB_KR02895" };
    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "TY6_03_RHB_KR02895" };
    	//args = new String[] { "159.220.246.3",   "8101", "hEDD", "testuser" };

    //    MRNConsumer cons = new MRNConsumer();
    //     cons.mrnInit(mn, args);
        MRNFXMain myMain = new MRNFXMain();
        _argsPassed = new String[args.length];
        for(int i = 0; i < args.length; i++)
            _argsPassed[i] = args[i];
        myMain.launch(args);
    }
}
