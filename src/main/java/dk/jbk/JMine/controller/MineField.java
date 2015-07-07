package dk.jbk.JMine.controller;


import dk.jbk.JMine.model.MineFieldCell;
import dk.jbk.JMine.model.SweepState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MineField {
	private MineFieldCell[][] field;
	private int mineCount;
	private IntegerGenerator integerGenerator;
	private GameState gameState;

	public MineField(int width, int height, int mineCount, IntegerGenerator integerGenerator) {
		validateFieldParameters(width, height, mineCount);

		this.mineCount = mineCount;

		this.integerGenerator = integerGenerator;

		field = new MineFieldCell[height][width];
		clearField();

		gameState = GameState.NEW;
	}

	public void toggleFlagged(int x, int y) {
		field[y][x].toggleFlagged();

		determineGameState();
	}

	public void togglePressDown(int x, int y) {
		field[y][x].togglePressDown();
	}

	public void expose(int x, int y) {
		if(gameState == GameState.NEW) {
			gameState = GameState.RUNNING;
			placeMinesWhileExemptingStartingArea(mineCount, x, y);
		}

		field[y][x].expose();

		exposeNeighboursIfBlank(x, y);

		determineGameState();
	}

	private void determineGameState() {
		int flaggedMines = 0;

		for (int y = 0; y < field.length; y++) {
			for (int x = 0; x < field[y].length; x++) {
				if (field[y][x].getSweepState() == SweepState.EXPLODED) {
					gameState = GameState.DEAD;
					revealGameOver();
					break;
				}
				else if (field[y][x].getSweepState() == SweepState.FLAGGED
						&& field[y][x].isMined()) {
					flaggedMines++;
				}
			}
		}

		if (flaggedMines == mineCount) {
			gameState = GameState.WON;
			revealGameWon();
		}
	}

	private void revealGameWon() {
		for (MineFieldCell[] row : field) {
			for (MineFieldCell cell : row) {
				cell.togglePressDown();
				cell.expose();
			}
		}
	}

	private void revealGameOver() {
		for (MineFieldCell[] row : field) {
			for (MineFieldCell cell : row) {
				cell.revealTrueState();
			}
		}
	}

	private void exposeNeighboursIfBlank(int x, int y) {
		MineFieldCell currentCell = field[y][x];

		if (!currentCell.isMined() && getNeighbouringMinesCount(x, y) == 0) {

			for (int neighbourY = Math.max(y - 1, 0); neighbourY < Math.min(y + 2, field.length); neighbourY++) {

				for (int neighbourX = Math.max(x - 1, 0); neighbourX < Math.min(x + 2, field[neighbourY].length); neighbourX++) {

					if (field[neighbourY][neighbourX].getSweepState() == SweepState.BLANK) {
						togglePressDown(neighbourX, neighbourY);
						expose(neighbourX, neighbourY);
					}
				}
			}
		}
	}

	public SweepState getCellSweepState(int x, int y) {
		return field[y][x].getSweepState();
	}

	public boolean isMined(int x, int y) {
		return field[y][x].isMined();
	}

	public int getWidth() {
		return field[0].length;
	}

	public int getHeight() {
		return field.length;
	}

	public int getNeighbouringMinesCount(int x, int y) {
		int neighbouringMinesCount = field[y][x].getNeighbouringMinesCount();

		if (neighbouringMinesCount == -1) { //Number is still unknown

			field[y][x].setNeighbouringMinesCount(countNeighbouringMines(x, y));
		}

		return field[y][x].getNeighbouringMinesCount();
	}

	public void togglePressDownNeighbours(int x, int y) {
		int neighbourMinesCount = getNeighbouringMinesCount(x, y);
		int neighboursFlaggedCount = countNeighbouringFlags(x, y);

		if (neighbourMinesCount == neighboursFlaggedCount) {
			for (int neighbourY = Math.max(y - 1, 0); neighbourY < Math.min(y + 2, field.length); neighbourY++) {

				for (int neighbourX = Math.max(x - 1, 0); neighbourX < Math.min(x + 2, field[neighbourY].length); neighbourX++) {

					togglePressDown(neighbourX, neighbourY);
				}
			}
		}
	}

	public void exposeNeighbours(int x, int y) {
		for (int neighbourY = Math.max(y - 1, 0); neighbourY < Math.min(y + 2, field.length); neighbourY++) {

			for (int neighbourX = Math.max(x - 1, 0); neighbourX < Math.min(x + 2, field[neighbourY].length); neighbourX++) {

				expose(neighbourX, neighbourY);
			}
		}
	}

	public GameState getGameState() {
		return gameState;
	}

	public int getTheoreticalMinesRemaining() {
		int flagCount = 0;

		for (MineFieldCell[] row : field) {

			for (MineFieldCell cell : row) {
				if (cell.getSweepState() == SweepState.FLAGGED) {
					flagCount++;
				}
			}
		}

		return mineCount - flagCount;
	}

	private void validateFieldParameters(int width, int height, int numberOfMines) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Dimension cannot be less than 1.");
		}
		if (numberOfMines > width * height) {
			throw new IllegalArgumentException("Number of mines must be smaller than size of field.");
		}
	}

	private void clearField() {
		for (MineFieldCell[] row : this.field) {
			for (int cellIndex = 0; cellIndex < row.length; cellIndex++) {
				row[cellIndex] = new MineFieldCell();
			}
		}
	}

	private void placeMinesWhileExemptingStartingArea(int numberOfMines, int startX, int startY) {
		int minesRemaining = numberOfMines;

		while (minesRemaining > 0) {
			int y = integerGenerator.getIntegerLessThan(field.length);
			int x = integerGenerator.getIntegerLessThan(field[y].length);

			if(!field[y][x].isMined() && !areNeighbours(x, y, startX, startY)) {
				field[y][x].setMined(true);

				minesRemaining--;
			}
		}
	}

	private int countNeighbouringMines(int x, int y) {
		int countResult = 0;

		for (int neighbourY = Math.max(y - 1, 0); neighbourY < Math.min(y + 2, field.length); neighbourY++) {

			for (int neighbourX = Math.max(x - 1, 0); neighbourX < Math.min(x + 2, field[neighbourY].length); neighbourX++) {

				if (isMined(neighbourX,neighbourY)) {
					countResult++;
				}
			}
		}
		return countResult;
	}

	private int countNeighbouringFlags(int x, int y) {
		int countResult = 0;

		for (int neighbourY = Math.max(y - 1, 0); neighbourY < Math.min(y + 2, field.length); neighbourY++) {

			for (int neighbourX = Math.max(x - 1, 0); neighbourX < Math.min(x + 2, field[neighbourY].length); neighbourX++) {

				if (getCellSweepState(neighbourX,neighbourY) == SweepState.FLAGGED) {
					countResult++;
				}
			}
		}
		return countResult;
	}

	private boolean areNeighbours(int x1, int y1, int x2, int y2) {
		return Math.abs(x1-x2) <= 1 && Math.abs(y1-y2) <= 1;
	}
}
