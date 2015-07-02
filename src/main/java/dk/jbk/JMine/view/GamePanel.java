package dk.jbk.JMine.view;

import dk.jbk.JMine.controller.LibraryIntegerGenerator;
import dk.jbk.JMine.controller.MineField;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
	private static final int CELL_SIZE = 16;

	private MineField mineField;
	private JPanel mineFieldPanel;

	public GamePanel() {
		mineField = new MineField(30, 16, 99, new LibraryIntegerGenerator());
		setupUI();
	}

	private void setupUI() {
		int gridWidth = mineField.getWidth();
		int gridHeight = mineField.getHeight();
		mineFieldPanel = new JPanel(new GridLayout(gridHeight, gridWidth));

		JFrame frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}
}
