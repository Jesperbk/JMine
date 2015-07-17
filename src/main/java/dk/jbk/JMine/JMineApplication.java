package dk.jbk.JMine;

import dk.jbk.JMine.controller.LibraryIntegerGenerator;
import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.view.MineFieldPane;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class JMineApplication extends Application {
	private static final int GAME_WIDTH = 30;
	private static final int GAME_HEIGHT = 16;
	private static final int MINE_COUNT = 99;

	private static JMineApplication instance;
	private Stage stage;

	private MineField mineField;

	@Override
	public void start(Stage stage) throws Exception {
		instance = this;
		this.stage = stage;

		stage.setTitle("JMine");

		newGame();
	}

	public void newGame() {
		if (stage.isShowing()) {
			stage.hide();
		}

		mineField = new MineField(GAME_WIDTH, GAME_HEIGHT, MINE_COUNT, new LibraryIntegerGenerator());

		StackPane root = new StackPane();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		scene.setFill(Color.LIGHTGRAY);

		MineFieldPane mineFieldPane = new MineFieldPane(mineField);
		root.getChildren().add(mineFieldPane);
		root.setAlignment(Pos.CENTER);

		stage.setResizable(false);
		stage.sizeToScene();
		stage.show();
	}

	public void exit() {
		stage.close();
	}

	public static JMineApplication getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
