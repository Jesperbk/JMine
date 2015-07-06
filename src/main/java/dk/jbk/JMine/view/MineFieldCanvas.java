package dk.jbk.JMine.view;

import dk.jbk.JMine.controller.ImageManager;
import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.controller.InputHandler;
import dk.jbk.JMine.model.SweepState;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class MineFieldCanvas extends Canvas {
	public static final int CELL_SIZE = 30;
	public static final int CELL_OFFSET = 3;

	private MineField mineField;
	private ImageManager imageManager;

	public MineFieldCanvas(MineField mineField, ImageManager imageManager) {

		this.mineField = mineField;
		this.imageManager = imageManager;

		setupInputHandler();

		int mineFieldPixelWidth = calculateDimensionFromCellCount(mineField.getWidth());
		int mineFieldPixelHeight = calculateDimensionFromCellCount(mineField.getHeight());

		setWidth(mineFieldPixelWidth);
		setHeight(mineFieldPixelHeight);

		// TEST
		mineField.togglePressDown(12, 7);
		mineField.expose(12, 7);

		mineField.toggleFlagged(10, 10);
		/*
		for (int x = 1; x < mineField.getWidth(); x++) {
			for (int y = 1; y < mineField.getHeight(); y++) {
				if (mineField.isMined(x, y)) {
					mineField.togglePressDown(x, y);
					mineField.expose(x, y);
				}
			}
		}*/

		// END TEST

		this.draw();
	}

	public int translatePixelToCell(int pixels) {
		int locationInCell = (pixels - 1) % (CELL_SIZE + CELL_OFFSET);

		if (locationInCell < CELL_OFFSET) { // Click is on border
			return -1;
		}
		else {
			return (pixels + 1) / (CELL_SIZE + CELL_OFFSET);
		}
	}

	private void draw() {
		GraphicsContext gc = getGraphicsContext2D();

		for (int cellY = 0; cellY < mineField.getHeight(); cellY++) {
			for (int cellX = 0; cellX < mineField.getWidth(); cellX++) {

				SweepState cellState = mineField.getCellSweepState(cellX, cellY);
				boolean cellIsMined = mineField.isMined(cellX, cellY);
				Image cellTile = imageManager.getRelevantTile(cellState, cellIsMined);

				int pixelX = cellX * (CELL_SIZE+CELL_OFFSET) + CELL_OFFSET;
				int pixelY = cellY * (CELL_SIZE+CELL_OFFSET) + CELL_OFFSET;

				gc.drawImage(cellTile, pixelX, pixelY, CELL_SIZE, CELL_SIZE);

				if (cellState == SweepState.EXPOSED && !cellIsMined) {
					int neighbourMineCount = mineField.getNeighbouringMinesCount(cellX, cellY);

					if (neighbourMineCount > 0) {
						drawNeighbourMineCount(gc, neighbourMineCount, pixelX, pixelY);
					}
				}
			}
		}

		drawBorders(gc);
	}

	private void drawNeighbourMineCount(GraphicsContext gc, int neighbourMineCount,
																			int cellOriginX, int cellOriginY) {
		setStrokeForNeighbourMineCount(gc, neighbourMineCount);

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);

		gc.setFont(Font.font("Arial", FontWeight.BLACK, CELL_SIZE * 0.85));

		gc.fillText(Integer.toString(neighbourMineCount),
				cellOriginX + CELL_SIZE / 2, cellOriginY + CELL_SIZE / 2);
	}

	private void setupInputHandler() {
		InputHandler inputHandler = new InputHandler(mineField, this);

		setOnMouseMoved(inputHandler);
		setOnMouseDragged(inputHandler);
		setOnMousePressed(inputHandler);
		setOnMouseReleased(inputHandler);
	}

	private void setStrokeForNeighbourMineCount(GraphicsContext gc, int neighbourMineCount) {
		switch (neighbourMineCount) {
			case 1:
				gc.setFill(new Color(0.255, 0.275, 0.682, 1));
				break;
			case 2:
				gc.setFill(new Color(0.125, 0.455, 0.055, 1));
				break;
			case 3:
				gc.setFill(new Color(0.55, 0, 0, 1));
				break;
			case 4:
				gc.setFill(new Color(0.043, 0.035, 0.498, 1));
				break;
			case 5:
				gc.setFill(new Color(0.31, 0, 0, 1));
				break;
			case 6:
				gc.setFill(new Color(0.008, 0.475, 0.486, 1));
				break;
			case 7:
				gc.setFill(new Color(0, 0, 0, 1));
				break;
			case 8:
				gc.setFill(new Color(0.502, 0.502, 0.502, 1));
				break;
		}
	}

	private int calculateDimensionFromCellCount(int cellCount) {
		int pixelsForCells = cellCount * CELL_SIZE;
		int pixelsForBorders = (cellCount + 1) * 3;

		return pixelsForCells + pixelsForBorders;
	}

	private void drawBorders(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);

		gc.strokeRect(0, 1, getWidth()-1, getHeight()-1);
		gc.strokeRect(1, 2, getWidth()-2, getHeight()-2);

		for (int i = CELL_SIZE+4; i < getWidth(); i += CELL_SIZE+3) {
			gc.strokeRect(i, 0, 1, getHeight());
		}

		for (int i = CELL_SIZE+4; i < getHeight(); i += CELL_SIZE+3) {
			gc.strokeRect(0, i, getWidth(), 1);
		}
	}
}
