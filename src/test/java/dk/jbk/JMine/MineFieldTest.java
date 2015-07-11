package dk.jbk.JMine;

import dk.jbk.JMine.controller.GameState;
import dk.jbk.JMine.controller.LibraryIntegerGenerator;
import dk.jbk.JMine.controller.MineField;
import dk.jbk.JMine.model.SweepState;
import org.junit.*;
import static org.junit.Assert.*;

public class MineFieldTest {

	@Test
	public void testHasCorrectDimensions() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 0;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount, new MockIntegerGenerator());

		assertEquals(fieldWidth, mineField.getWidth());
		assertEquals(fieldHeight, mineField.getHeight());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidDimension() throws Exception {
		int fieldWidth = 0, fieldHeight = 3;
		new MineField(fieldWidth, fieldHeight, 0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNumberOfMines() throws Exception {
		int fieldWidth = 2, fieldHeight = 3;
		int mineCount = 7;
		new MineField(fieldWidth, fieldHeight, mineCount, null);
	}

	@Test
	public void testMinePlacement() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 3;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						2,0,
						0,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		assertTrue(mineField.isMined(1, 2));
		assertTrue(mineField.isMined(0, 2));
		assertTrue(mineField.isMined(2, 0));
		assertFalse(mineField.isMined(0, 0));
		assertFalse(mineField.isMined(0, 1));
		assertFalse(mineField.isMined(1, 0));
		assertFalse(mineField.isMined(1, 1));
		assertFalse(mineField.isMined(2, 1));
		assertFalse(mineField.isMined(2, 2));
	}

	@Test
	public void testSkipAlreadyCreatedMines() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						2,1,
						2,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		assertTrue(mineField.isMined(1, 2));
		assertTrue(mineField.isMined(2, 2));
	}

	@Test
	public void testNoMineNearStartingCell() throws Exception {
		int fieldWidth = 4, fieldHeight = 4;
		int mineCount = 1;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(0,0,3,3));

		mineField.togglePressDown(0,1);
		mineField.expose(0, 1);

		assertFalse(mineField.isMined(0, 0));
		assertTrue(mineField.isMined(3, 3));
	}

	@Test
	public void testRandomPlacementOfMines() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 3;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount, new LibraryIntegerGenerator());

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		assertEquals(mineCount, countMinesInField(mineField));
	}

	@Test
	public void testFlagCell() throws Exception {
		int fieldWidth = 2, fieldHeight = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, 0, new MockIntegerGenerator());

		mineField.toggleFlagged(0, 1);

		assertEquals(SweepState.FLAGGED, mineField.getCellSweepState(0, 1));
	}

	@Test
	public void testPressCell() throws Exception {
		int fieldWidth = 2, fieldHeight = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, 0, new MockIntegerGenerator());

		mineField.togglePressDown(0, 1);

		assertEquals(SweepState.PRESSED, mineField.getCellSweepState(0, 1));
	}

	@Test
	public void testExposeCell() throws Exception {
		int fieldWidth = 2, fieldHeight = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, 0, new MockIntegerGenerator());

		mineField.togglePressDown(0, 1);
		mineField.expose(0, 1);

		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(0, 1));
	}

	@Test
	public void testGameWon() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,2,
						1,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		mineField.toggleFlagged(2, 2);
		mineField.toggleFlagged(2, 1);

		assertEquals(SweepState.FLAGGED, mineField.getCellSweepState(2, 2));
		assertEquals(SweepState.FLAGGED, mineField.getCellSweepState(2, 1));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(0, 0));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(0, 1));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(0, 2));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(1, 0));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(1, 1));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(1, 2));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(2, 0));
		assertEquals(GameState.WON, mineField.getGameState());
	}

	@Test
	public void testGameOver() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,2,
						1,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		mineField.togglePressDown(2, 2);
		mineField.expose(2,2);

		assertEquals(SweepState.EXPLODED, mineField.getCellSweepState(2, 2));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(2, 1));
		assertEquals(GameState.DEAD, mineField.getGameState());
	}

	@Test
	public void testGetNeighbourCount() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 3;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						2,0,
						0,2));

		mineField.togglePressDown(0,0);
		mineField.expose(0,0);

		assertEquals(3, mineField.getNeighbouringMinesCount(1, 1));
	}

	@Test
	public void testGetNeighbourCountForBorderCell() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 1;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						0,0));

		mineField.togglePressDown(2,2);
		mineField.expose(2,2);

		assertEquals(1, mineField.getNeighbouringMinesCount(0, 1));
	}

	@Test
	public void testGetTheoreticalMinesRemaining() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						2,0));

		mineField.togglePressDown(0,0);
		mineField.expose(0,0);

		mineField.toggleFlagged(2,2);

		assertEquals(1, mineField.getTheoreticalMinesRemaining());
	}

	@Test
	public void testExposeNeighboursIfBlank() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						1,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(0, 0));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(0, 1));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(1, 0));
		assertEquals(SweepState.EXPOSED, mineField.getCellSweepState(1, 1));
	}

	@Test
	public void testRevealNeighboursWhenSafe() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						1,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		mineField.toggleFlagged(2, 1);
		mineField.toggleFlagged(2, 2);

		mineField.togglePressDownNeighbours(1, 1);
		mineField.exposeNeighbours(1, 1);

		assertEquals(SweepState.EXPLODED, mineField.getCellSweepState(1, 2));
	}

	@Test
	public void testRevealNeighboursOnlyWhenExposed() throws Exception {
		int fieldWidth = 3, fieldHeight = 6;
		int mineCount = 2;
		MineField mineField = new MineField(fieldWidth, fieldHeight, mineCount,
				new MockIntegerGenerator(
						2,1,
						1,2));

		mineField.togglePressDown(0, 0);
		mineField.expose(0, 0);

		mineField.toggleFlagged(0, 2);
		mineField.toggleFlagged(2, 2);

		mineField.togglePressDownNeighbours(2, 5);
		mineField.exposeNeighbours(2, 5);

		assertEquals(SweepState.BLANK, mineField.getCellSweepState(1, 5));
	}

	private int countMinesInField(MineField mineField) {
		int numberOfMines = 0;

		for (int x = 0; x < mineField.getWidth(); x++) {
			for (int y = 0; y < mineField.getHeight(); y++) {
				if (mineField.isMined(x, y)) {
					numberOfMines++;
				}
			}
		}

		return numberOfMines;
	}
}
