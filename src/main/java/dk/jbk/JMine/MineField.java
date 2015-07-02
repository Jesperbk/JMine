package dk.jbk.JMine;


import java.util.Arrays;

public class MineField {
	private MineState[][] field;
	private IntegerGenerator integerGenerator;

	public MineField(int width, int height, int numberOfMines, IntegerGenerator integerGenerator) {
		validateFieldParameters(width, height, numberOfMines);

		this.integerGenerator = integerGenerator;

		field = new MineState[height][width];
		clearField();
		placeMines(numberOfMines);
	}

	public boolean isMine(int x, int y) {
		return field[y][x] == MineState.MINE;
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
		for (MineState[] row : this.field) {
			Arrays.fill(row, MineState.CLEAR);
		}
	}

	private void placeMines(int numberOfMines) {
		int minesRemaining = numberOfMines;

		while (minesRemaining > 0) {
			int y = integerGenerator.getIntegerLessThan(field.length);
			int x = integerGenerator.getIntegerLessThan(field[y].length);

			if(field[y][x] == MineState.CLEAR) {
				field[y][x] = MineState.MINE;

				minesRemaining--;
			}
		}
	}
}
