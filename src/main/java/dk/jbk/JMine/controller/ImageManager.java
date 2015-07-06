package dk.jbk.JMine.controller;

import dk.jbk.JMine.model.SweepState;
import javafx.scene.image.Image;

public class ImageManager {
	private final Image DEFAULT = new Image("/default.png");
	private final Image BLANK_TILE = new Image("/blank-tile.png");
	private final Image PRESSED_TILE = new Image("/pressed-tile.png");
	private final Image MINE_TILE = new Image("/mine-tile.png");
	private final Image FLAG_TILE = new Image("/flag-tile.png");
	private final Image EXPLODED_TILE = new Image("/exploded-tile.png");

	public Image getRelevantTile(SweepState cellState, boolean cellIsMined) {
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
			default:
				cellTile = DEFAULT;
		}

		return cellTile;
	}
}
