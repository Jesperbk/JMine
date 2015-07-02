package dk.jbk.JMine;

public class MineFieldCell {
	private boolean isMined;
	private SweepState sweepState;
	private int neighbouringMinesCount;

	public MineFieldCell() {
		isMined = false;
		sweepState = SweepState.BLANK;
		neighbouringMinesCount = -1; //Still unknown
	}

	public boolean isMined() {
		return isMined;
	}

	public void setMined(boolean mined) {
		this.isMined = mined;
	}

	public void toggleFlagged() {
		if (sweepState == SweepState.FLAGGED) {
			sweepState = SweepState.BLANK;
		}
		else if (sweepState == SweepState.BLANK) {
			sweepState = SweepState.FLAGGED;
		}
		//Do nothing if already exposed
	}

	public void expose() {
		if (sweepState == SweepState.PRESSED) {
			sweepState = SweepState.EXPOSED;
		}
	}

	public void togglePressDown() {
		if (sweepState == SweepState.BLANK) {
			sweepState = SweepState.PRESSED;
		}
		else if (sweepState == SweepState.PRESSED) {
			sweepState = SweepState.BLANK;
		}
	}

	public SweepState getSweepState() {
		return sweepState;
	}

	public int getNeighbouringMinesCount() {
		return neighbouringMinesCount;
	}

	public void setNeighbouringMinesCount(int numberOfNeighbouringMines) {
		this.neighbouringMinesCount = numberOfNeighbouringMines;
	}
}
