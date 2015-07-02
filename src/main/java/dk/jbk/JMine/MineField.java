package dk.jbk.JMine;


public class MineField {
	private boolean[][] field;
	private IntegerGenerator integerGenerator;

	public MineField(int width, int height, int numberOfMines, IntegerGenerator integerGenerator) {
		validateFieldParameters(width, height, numberOfMines);

		field = new boolean[height][width];
		this.integerGenerator = integerGenerator;

		placeMines(numberOfMines);
	}

	public boolean isMine(int x, int y) {
		return field[y][x];
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

	private void placeMines(int numberOfMines) {
		int minesRemaining = numberOfMines;

		while (minesRemaining > 0) {
			int y = integerGenerator.getIntegerLessThan(field.length);
			int x = integerGenerator.getIntegerLessThan(field[y].length);

			if(field[y][x] == false) {
				field[y][x] = true;

				minesRemaining--;
			}
		}
	}
}
