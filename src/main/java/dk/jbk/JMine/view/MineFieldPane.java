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
	private ImageManager imageManager;

	public MineFieldPane(MineField mineField, ImageManager imageManager) {

		this.mineField = mineField;
		this.imageManager = imageManager;

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

				newTileCanvas.setOnMousePressed(inputHandler);
				newTileCanvas.setOnMouseReleased(inputHandler);
				newTileCanvas.setOnDragDetected(inputHandler);
				newTileCanvas.setOnMouseDragEntered(inputHandler);
				newTileCanvas.setOnMouseDragExited(inputHandler);
				newTileCanvas.setOnMouseDragReleased(inputHandler);
				newTileCanvas.setOnMouseExited(inputHandler);
				newTileCanvas.setOnMouseEntered(inputHandler);

				setConstraints(newTileCanvas, x, y);
				getChildren().add(newTileCanvas);

				newTileCanvas.draw();
			}
		}
	}

	public int translatePixelToCell(int pixels) {
		int locationInCell = (pixels - 1) % (TileCanvas.CELL_SIZE + CELL_OFFSET);

		if (locationInCell < CELL_OFFSET) { // Click is on border
			return -1;
		}
		else {
			return (pixels + 1) / (TileCanvas.CELL_SIZE + CELL_OFFSET);
		}
	}

	public void draw() {
		for (Node node : getChildren()) {
			TileCanvas tile = (TileCanvas) node;

			tile.draw();
		}
	}
}
