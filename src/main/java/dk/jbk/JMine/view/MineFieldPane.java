package dk.jbk.JMine.view;

import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.controller.InputHandler;
import dk.jbk.JMine.model.MineFieldCell;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MineFieldPane extends GridPane {
	public static final int CELL_OFFSET = 2;

	private MineField mineField;

	public MineFieldPane(MineField mineField) {

		this.mineField = mineField;

		double aspectRatio = (double) mineField.getWidth() / mineField.getHeight();
		minWidthProperty().bind(heightProperty().multiply(aspectRatio));
		minHeightProperty().bind(widthProperty().divide(aspectRatio));

		BorderStroke borderStroke = new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, null, new BorderWidths(CELL_OFFSET));
		setBorder(new Border(borderStroke));
		setHgap(CELL_OFFSET);
		setVgap(CELL_OFFSET);
		setStyle("-fx-background-color: #000000;");

		setupTileCanvases();

		draw();
	}

	private void setupTileCanvases() {
		InputHandler inputHandler = new InputHandler(mineField, this);

		for (int y = 0; y < mineField.getHeight(); y++) {
			for (int x = 0; x < mineField.getWidth(); x++) {
				if (y == 0) {
					getColumnConstraints().add(new ColumnConstraints(TileCanvas.CELL_SIZE, Control.USE_COMPUTED_SIZE,
							Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
				}

				StackPane stackPane = new StackPane();

				TileCanvas newTileCanvas = new TileCanvas(mineField, x, y);

				newTileCanvas.setListener(inputHandler);

				setConstraints(stackPane, x, y);
				stackPane.getChildren().add(newTileCanvas);
				getChildren().add(stackPane);

				/*
				stackPane.minWidthProperty().bind(widthProperty().divide(mineField.getWidth()).subtract(CELL_OFFSET));
				stackPane.prefWidthProperty().bind(widthProperty().divide(mineField.getWidth()).subtract(CELL_OFFSET));
				stackPane.maxWidthProperty().bind(widthProperty().divide(mineField.getWidth()).subtract(CELL_OFFSET));

				stackPane.minHeightProperty().bind(heightProperty().divide(mineField.getHeight()).subtract(CELL_OFFSET));
				stackPane.prefHeightProperty().bind(heightProperty().divide(mineField.getHeight()).subtract(CELL_OFFSET));
				stackPane.maxHeightProperty().bind(heightProperty().divide(mineField.getHeight()).subtract(CELL_OFFSET));
				*/

				newTileCanvas.heightProperty().bind(stackPane.heightProperty());
				newTileCanvas.widthProperty().bind(stackPane.widthProperty());

				newTileCanvas.heightProperty().addListener((observable, oldValue, newValue) -> draw());
				newTileCanvas.widthProperty().addListener((observable, oldValue, newValue) -> draw());

				newTileCanvas.draw();
			}

			getRowConstraints().add(new RowConstraints(TileCanvas.CELL_SIZE, Control.USE_COMPUTED_SIZE,
					Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
		}
	}

	public void draw() {
		for (Node node : getChildren()) {
			StackPane pane = (StackPane) node;

			for (Node tileNode : pane.getChildren()) {
				TileCanvas tile = (TileCanvas) tileNode;
				tile.draw();
			}

		}
	}
}
