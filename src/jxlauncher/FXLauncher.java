package jxlauncher;

import java.io.File;

import postalcode.PostalCode;
import postalcode.PostalCodeIndex;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.FindProperty;
/**
 * FXLauncher creates the primaryStage for PostalCode objects to be displayed on, particularly the ObservableList's after they have been sorted by our comparable methods.
 * Allows a visual representation of our Data
 * @author Brodie
 *
 */
public class FXLauncher extends Application {
	PostalCodeIndex postalCodeIndex;
	PostalCode mapPostalCode = null;

	public static void main(String[] args) { 
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		postalCodeIndex = new PostalCodeIndex();
		Group root = new Group();
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Postal Code Analyser");
		postalCodeIndex.load(new File("postal_codes.csv")); //Load in data file
		primaryStage.setHeight(828);primaryStage.setWidth(800);
		BorderPane border = new BorderPane();
		GridPane grid = new GridPane();
		
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(5);grid.setHgap(5);
		TextField textLat = new TextField();
		textLat.setPromptText("Enter Latitude");
		GridPane.setConstraints(textLat, 0, 0);
		
		textLat.setPrefColumnCount(10);
		grid.getChildren().add(textLat);
		TextField textLong = new TextField();
		textLong.setPromptText("Enter Longitude");
		GridPane.setConstraints(textLong, 0, 1);
		grid.getChildren().add(textLong);
		
		Button submit = new Button("Submit");
		GridPane.setConstraints(submit,1, 0);
		grid.getChildren().add(submit);
		TextField textLocation = new TextField();	textLocation.setPrefWidth(500);
		GridPane.setConstraints(textLocation, 0, 2);
		grid.getChildren().add(textLocation);
		border.setTop(grid);
		
		final WebView browser = new WebView();
		final WebEngine webEngine = browser.getEngine();
		Hyperlink hpl = new Hyperlink("Show your desired location"); //HyperLink to show the desired location
		hpl.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				webEngine.load("https://www.google.com/maps/place/"+textLat.getText()+","+textLong.getText()+"/@"+textLat.getText()+","+textLong.getText()+",5z");
			}
			
		});
		
		final Hyperlink hpl2 = new Hyperlink("Show the nearest location"); //Hyperlink to show the nearest Location
		hpl2.setVisible(false);
		hpl2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				webEngine.load("https://www.google.com/maps/place/"+Double.toString(mapPostalCode.getLatitude())+","+Double.toString(mapPostalCode.getLongitude())+"/@"+Double.toString(mapPostalCode.getLatitude())+","
						+Double.toString(mapPostalCode.getLongitude())+",5z");
			}
		});
		
		VBox browserVBox = new VBox();
		browserVBox.getChildren().addAll(hpl,hpl2,browser);
		border.setCenter(browserVBox);
		root.getChildren().add(border);
		
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(!textLat.getText().isEmpty() && !textLong.getText().isEmpty()) {
					postalCodeIndex.calculateBoundingBox(Double.parseDouble(textLat.getText()),Double.parseDouble(textLong.getText()));
					postalCodeIndex.getSubListWithinRange(postalCodeIndex.getmaxLat(), postalCodeIndex.getminLat(), postalCodeIndex.getmaxLong(), postalCodeIndex.getminLong());
					mapPostalCode = postalCodeIndex.findClosestCoordinate(Double.parseDouble(textLat.getText()),Double.parseDouble(textLong.getText()));
					//System.out.printf("Brute: %s\n",postalCodeIndex.findClosestCoordinateBruteForce(Double.parseDouble(textLat.getText()),Double.parseDouble(textLong.getText())));
					textLocation.setText(mapPostalCode.toString());
					hpl2.setVisible(true);
				}	
			}
		});
		primaryStage.show();
	}

}
