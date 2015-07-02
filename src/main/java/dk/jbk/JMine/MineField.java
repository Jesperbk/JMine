package dk.jbk.JMine;


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

		if (field[y][x].getSweepState() == SweepState.EXPLODED) {
			gameState = GameState.DEAD;
			revealFinalResult();
		}
	}

	private void revealFinalResult() {
		for (MineFieldCell[] row : field) {
			for (MineFieldCell cell : row) {
				cell.revealTrueState();
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

	public int getNumberOfNeighbouringMines(int x, int y) {
		int neighbouringMinesCount = field[y][x].getNeighbouringMinesCount();

		if (neighbouringMinesCount == -1) { //Number is still unknown

			field[y][x].setNeighbouringMinesCount(countNeighbouringMines(x, y));
		}

		return field[y][x].getNeighbouringMinesCount();
	}

	public GameState getGameState() {
		return gameState;
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

		for (int neighbourY = Math.max(y - 1, 0); neighbourY <= Math.min(y + 1, field.length); neighbourY++) {

			for (int neighbourX = Math.max(x - 1, 0); neighbourX <= Math.min(x + 1, field[neighbourY].length); neighbourX++) {

				if (isMined(neighbourX,neighbourY)) {
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
