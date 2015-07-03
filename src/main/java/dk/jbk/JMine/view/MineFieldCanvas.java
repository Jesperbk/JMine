package dk.jbk.JMine.view;

import dk.jbk.JMine.controller.MineField;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MineFieldCanvas extends Canvas {
	public static final int CELL_SIZE = 30;

	private MineField mineField;

	public MineFieldCanvas(MineField mineField) {

		this.mineField = mineField;

		int mineFieldPixelWidth = calculateDimensionFromCellCount(mineField.getWidth());
		int mineFieldPixelHeight = calculateDimensionFromCellCount(mineField.getHeight());

		setWidth(mineFieldPixelWidth);
		setHeight(mineFieldPixelHeight);

		this.draw();
	}

	public void draw() {
		GraphicsContext gc = getGraphicsContext2D();

		Image blank = new Image("/blank-tile.png");

		for (int y = 0; y < mineField.getHeight(); y++) {
			for (int x = 0; x < mineField.getWidth(); x++) {
				gc.drawImage(blank, x * (CELL_SIZE+3) + 3, y * (CELL_SIZE+3) + 3, CELL_SIZE, CELL_SIZE);
			}
		}

		drawBorders(gc);
	}

	private int calculateDimensionFromCellCount(int cellCount) {
		int pixelsForCells = cellCount * CELL_SIZE;
		int pixelsForBorders = (cellCount + 1) * 3;

		return pixelsForCells + pixelsForBorders;
	}

	private void drawBorders(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);

		gc.strokeRect(1, 1, getWidth()-1, getHeight()-1);
		gc.strokeRect(2, 2, getWidth()-2, getHeight()-2);

		for (int i = CELL_SIZE+4; i < getWidth(); i += CELL_SIZE+3) {
			gc.strokeRect(i, 0, 1, getHeight());
		}

		for (int i = CELL_SIZE+4; i < getHeight(); i += CELL_SIZE+3) {
			gc.strokeRect(0, i, getWidth(), 1);
		}
	}
}
