package dk.jbk.JMine;

import dk.jbk.JMine.controller.LibraryIntegerGenerator;
import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.view.MineFieldCanvas;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JMineApplication extends Application {
	MineField mineField;

	@Override
	public void start(Stage stage) throws Exception {
		mineField = new MineField(30, 16, 99, new LibraryIntegerGenerator());

		stage.setTitle("JMine");

		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		scene.setFill(Color.LIGHTGRAY);

		MineFieldCanvas canvas = new MineFieldCanvas(mineField);
		root.getChildren().add(canvas);

		stage.sizeToScene();
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
