package dk.jbk.JMine.view;

import dk.jbk.JMine.controller.ImageManager;
import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.model.SweepState;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class TileCanvas extends Canvas {
	public static final int CELL_SIZE = 30;

	private MineField mineField;
	private int cellX, cellY;

	public TileCanvas(MineField mineField, int cellX, int cellY) {
		this.mineField = mineField;
		this.cellX = cellX;
		this.cellY = cellY;

		setWidth(CELL_SIZE);
		setHeight(CELL_SIZE);
	}

	public void draw() {
		GraphicsContext gc = getGraphicsContext2D();

		SweepState cellState = mineField.getCellSweepState(cellX, cellY);
		boolean cellIsMined = mineField.isMined(cellX, cellY);
		Image cellTile = ImageManager.getRelevantTile(cellState, cellIsMined);

		gc.drawImage(cellTile, 0, 0, CELL_SIZE, CELL_SIZE);

		if (cellState == SweepState.EXPOSED && !cellIsMined) {
			int neighbourMineCount = mineField.getNeighbouringMinesCount(cellX, cellY);

			if (neighbourMineCount > 0) {
				drawNeighbourMineCount(neighbourMineCount);
			}
		}
	}

	public int getCellX() {
		return cellX;
	}

	public int getCellY() {
		return cellY;
	}

	private void drawNeighbourMineCount(int neighbourMineCount) {
		GraphicsContext gc = getGraphicsContext2D();

		setStrokeForNeighbourMineCount(gc, neighbourMineCount);

		gc.setTextAlign(TextAlignment.CENTER);
		gc.setTextBaseline(VPos.CENTER);

		gc.setFont(Font.font("Arial", FontWeight.BLACK, CELL_SIZE * 0.85));

		gc.fillText(Integer.toString(neighbourMineCount), CELL_SIZE / 2, CELL_SIZE / 2);
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
}
