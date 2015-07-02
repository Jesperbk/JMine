package dk.jbk.JMine;


import java.util.Arrays;

public class MineField {
	private MineFieldCell[][] field;
	private IntegerGenerator integerGenerator;

	public MineField(int width, int height, int numberOfMines, IntegerGenerator integerGenerator) {
		validateFieldParameters(width, height, numberOfMines);

		this.integerGenerator = integerGenerator;

		field = new MineFieldCell[height][width];
		clearField();
		placeMines(numberOfMines);
	}

	public void setFlagged(int x, int y) {
		field[y][x].setSweepState(SweepState.FLAGGED);
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

	private void placeMines(int numberOfMines) {
		int minesRemaining = numberOfMines;

		while (minesRemaining > 0) {
			int y = integerGenerator.getIntegerLessThan(field.length);
			int x = integerGenerator.getIntegerLessThan(field[y].length);

			if(!field[y][x].isMined()) {
				field[y][x].setMined(true);

				minesRemaining--;
			}
		}
	}
}
