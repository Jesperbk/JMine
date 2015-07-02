package dk.jbk.JMine;

import org.junit.*;
import static org.junit.Assert.*;

public class MineFieldTest {

	@Test
	public void testCreateOneByOneWithOneMine() throws Exception {
		int fieldWidth = 1, fieldHeight = 1;
		int numberOfMines = 1;
		MineField mineField = new MineField(fieldWidth, fieldHeight, numberOfMines);

		assertTrue(mineField.isMine(0,0));
	}

	@Test
	public void testCreateOneByOneWithNoMine() throws Exception {
		int fieldWidth = 1, fieldHeight = 1;
		int numberOfMines = 0;
		MineField mineField = new MineField(fieldWidth, fieldHeight, numberOfMines);

		assertFalse(mineField.isMine(0, 0));
	}

	@Test
	public void testHasCorrectDimensions() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int numberOfMines = 0;
		MineField mineField = new MineField(fieldWidth, fieldHeight, numberOfMines);

		assertEquals(fieldWidth, mineField.getWidth());
		assertEquals(fieldHeight, mineField.getHeight());
	}

	@Test
	public void testHasCorrectNumberOfMines() throws Exception {
		int fieldWidth = 3, fieldHeight = 3;
		int numberOfMines = 3;
		MineField mineField = new MineField(fieldWidth, fieldHeight, numberOfMines);

		assertEquals(numberOfMines, countMinesInField(mineField));
	}

	private int countMinesInField(MineField mineField) {
		int numberOfMines = 0;

		for (int x = 0; x < mineField.getWidth(); x++) {
			for (int y = 0; y < mineField.getHeight(); y++) {
				if (mineField.isMine(x,y)) {
					numberOfMines++;
				}
			}
		}

		return numberOfMines;
	}
}
