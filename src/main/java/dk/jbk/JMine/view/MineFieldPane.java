package dk.jbk.JMine.view;

import dk.jbk.JMine.controller.ImageManager;
import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.controller.InputHandler;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MineFieldPane extends GridPane {
	public static final int CELL_OFFSET = 3;

	private MineField mineField;

	public MineFieldPane(MineField mineField) {

		this.mineField = mineField;

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
				TileCanvas newTileCanvas = new TileCanvas(mineField, x, y);

				newTileCanvas.setListener(inputHandler);

				setConstraints(newTileCanvas, x, y);
				getChildren().add(newTileCanvas);

				newTileCanvas.draw();
			}
		}
	}

	public void draw() {
		for (Node node : getChildren()) {
			TileCanvas tile = (TileCanvas) node;

			tile.draw();
		}
	}
}
