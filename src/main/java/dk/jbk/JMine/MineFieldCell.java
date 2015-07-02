package dk.jbk.JMine;

public class MineFieldCell {
	private boolean isMined;
	private SweepState sweepState;

	public MineFieldCell() {
		isMined = false;
		sweepState = SweepState.BLANK;
	}

	public boolean isMined() {
		return isMined;
	}

	public void setMined(boolean mined) {
		this.isMined = mined;
	}

	public void setSweepState(SweepState sweepState) {
		this.sweepState = sweepState;
	}

	public SweepState getSweepState() {
		return sweepState;
	}
}
