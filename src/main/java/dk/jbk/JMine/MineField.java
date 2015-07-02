package dk.jbk.JMine;


public class MineField {
	boolean[][] field;

	public MineField(int width, int height, int numberOfMines) {
		field = new boolean[height][width];

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

	private void placeMines(int numberOfMines) {
		int minesRemaining = numberOfMines;

		for(int y = 0; y < field.length; y++) {

			for (int x = 0; x < field[y].length && minesRemaining > 0; x++) {

				field[y][x] = true;
				minesRemaining--;
			}
		}
	}
}
