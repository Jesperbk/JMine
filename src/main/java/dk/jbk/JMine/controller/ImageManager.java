package dk.jbk.JMine.controller;

import dk.jbk.JMine.model.SweepState;
import javafx.scene.image.Image;

public class ImageManager {
	private final Image DEFAULT = new Image("/default.png");
	private final Image BLANK_TILE = new Image("/blank-tile.png");
	private final Image PRESSED_TILE = new Image("/pressed-tile.png");

	public Image getRelevantTile(SweepState cellState) {
		Image cellTile = null;

		switch (cellState) {
			case BLANK:
				cellTile = BLANK_TILE;
				break;
			case PRESSED:
				cellTile = PRESSED_TILE;
				break;
			default:
				cellTile = DEFAULT;
		}

		return cellTile;
	}
}
