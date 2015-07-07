package dk.jbk.JMine.controller;

import dk.jbk.JMine.model.SweepState;
import javafx.scene.image.Image;

public class ImageManager {
	private static final Image DEFAULT = new Image("/default.png");
	private static final Image BLANK_TILE = new Image("/blank-tile.png");
	private static final Image PRESSED_TILE = new Image("/pressed-tile.png");
	private static final Image MINE_TILE = new Image("/mine-tile.png");
	private static final Image FLAG_TILE = new Image("/flag-tile.png");
	private static final Image EXPLODED_TILE = new Image("/exploded-tile.png");
	private static final Image CLEAR_FLAGGED_TILE = new Image("/flag-clear-tile.png");

	public static Image getRelevantTile(SweepState cellState, boolean cellIsMined) {
		Image cellTile;

		switch (cellState) {
			case BLANK:
				cellTile = BLANK_TILE;
				break;
			case PRESSED:
				cellTile = PRESSED_TILE;
				break;
			case EXPOSED:
				if (cellIsMined) {
					cellTile = MINE_TILE;
				}
				else {
					cellTile = PRESSED_TILE;
				}
				break;
			case EXPLODED:
				cellTile = EXPLODED_TILE;
				break;
			case FLAGGED:
				cellTile = FLAG_TILE;
				break;
			case CLEAR_FLAGGED:
				cellTile = CLEAR_FLAGGED_TILE;
				break;
			default:
				cellTile = DEFAULT;
		}

		return cellTile;
	}
}
