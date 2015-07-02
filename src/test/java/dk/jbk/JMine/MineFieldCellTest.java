package dk.jbk.JMine;


import dk.jbk.JMine.model.MineFieldCell;
import dk.jbk.JMine.model.SweepState;
import org.junit.*;
import static org.junit.Assert.*;

public class MineFieldCellTest {
	MineFieldCell mineFieldCell;

	@Before
	public void setUp() throws Exception {
		mineFieldCell = new MineFieldCell();

	}

	@Test
	public void testToggleFlagged() throws Exception {
		mineFieldCell.toggleFlagged();

		assertEquals(SweepState.FLAGGED, mineFieldCell.getSweepState());

		mineFieldCell.toggleFlagged();

		assertEquals(SweepState.BLANK, mineFieldCell.getSweepState());
	}

	@Test
	public void testPressDown() throws Exception {
		mineFieldCell.togglePressDown();

		assertEquals(SweepState.PRESSED, mineFieldCell.getSweepState());

		mineFieldCell.togglePressDown();

		assertEquals(SweepState.BLANK, mineFieldCell.getSweepState());
	}

	@Test
	public void testExpose() throws Exception {
		mineFieldCell.togglePressDown();

		mineFieldCell.expose();

		assertEquals(SweepState.EXPOSED, mineFieldCell.getSweepState());
	}

	@Test
	public void testCannotExposeWithoutPressDown() throws Exception {
		mineFieldCell.expose();

		assertEquals(SweepState.BLANK, mineFieldCell.getSweepState());
	}

	@Test
	public void testCannotPressDownFlagged() throws Exception {
		mineFieldCell.toggleFlagged();

		mineFieldCell.togglePressDown();

		assertEquals(SweepState.FLAGGED, mineFieldCell.getSweepState());
	}

	@Test
	public void testCannotFlagExposed() throws Exception {
		mineFieldCell.togglePressDown();

		mineFieldCell.expose();

		mineFieldCell.toggleFlagged();

		assertEquals(SweepState.EXPOSED, mineFieldCell.getSweepState());
	}

	@Test
	public void testRevealTrueStateUndetectedMine () throws Exception {
		mineFieldCell.setMined(true);

		mineFieldCell.revealTrueState();

		assertEquals(SweepState.EXPOSED, mineFieldCell.getSweepState());
	}

	@Test
	public void testRevealTrueStateMineFlagged () throws Exception {
		mineFieldCell.setMined(true);
		mineFieldCell.toggleFlagged();

		mineFieldCell.revealTrueState();

		assertEquals(SweepState.FLAGGED, mineFieldCell.getSweepState());
	}

	@Test
	public void testRevealTrueStateClearFlagged() throws Exception {
		mineFieldCell.toggleFlagged();

		mineFieldCell.revealTrueState();

		assertEquals(SweepState.CLEAR_FLAGGED, mineFieldCell.getSweepState());
	}
}
