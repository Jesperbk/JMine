package dk.jbk.JMine.model;

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
		if (getSweepState() == SweepState.FLAGGED) {
			sweepState = SweepState.BLANK;
		}
		else if (getSweepState() == SweepState.BLANK) {
			sweepState = SweepState.FLAGGED;
		}
	}

	public void expose() {
		if (getSweepState() == SweepState.PRESSED) {
			if (isMined()) {
				sweepState = SweepState.EXPLODED;
			}
			else {
				sweepState = SweepState.EXPOSED;
			}
		}
	}

	public void togglePressDown() {
		if (getSweepState() == SweepState.BLANK) {
			sweepState = SweepState.PRESSED;
		}
		else if (getSweepState() == SweepState.PRESSED) {
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

	public void revealTrueState() {
		if (isMined() && getSweepState() == SweepState.BLANK) {
			sweepState = SweepState.EXPOSED;
		}
		else if (getSweepState() == SweepState.FLAGGED && !isMined()) {
			sweepState = SweepState.CLEAR_FLAGGED;
		}
	}
}
