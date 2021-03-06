package dk.jbk.JMine.controller;

import dk.jbk.JMine.model.SweepState;
import dk.jbk.JMine.view.MineFieldPane;
import dk.jbk.JMine.view.TileCanvas;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InputHandler implements EventHandler<MouseEvent> {
	private MineField mineField;
	private MineFieldPane mineFieldPane;

	private DebugCanvas debugCanvas;

	public InputHandler(MineField mineField, MineFieldPane mineFieldPane) {
		this.mineField = mineField;
		this.mineFieldPane = mineFieldPane;

		debugCanvas = setupDebugStage();
	}

	@Override
	public void handle(MouseEvent event) {
		GameState preEventGameState = mineField.getGameState();

		TileCanvas origin = (TileCanvas)event.getSource();
		int cellX = origin.getCellX();
		int cellY = origin.getCellY();

		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			if (event.getButton() == MouseButton.PRIMARY) {
				mineField.togglePressDown(cellX, cellY);
			}
			else if (event.getButton() == MouseButton.MIDDLE) {
				mineField.togglePressDownNeighbours(cellX, cellY);
			}
		}

		else if (event.getEventType() == MouseEvent.MOUSE_RELEASED
				|| event.getEventType() == MouseDragEvent.MOUSE_DRAG_RELEASED) {
			if (event.getButton() == MouseButton.PRIMARY) {
				mineField.expose(cellX, cellY);
			}
			else if (event.getButton() == MouseButton.MIDDLE) {
				mineField.exposeNeighbours(cellX, cellY);
			}
			else if (event.getButton() == MouseButton.SECONDARY) {
				mineField.toggleFlagged(cellX, cellY);
			}
		}

		else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
			((TileCanvas) event.getSource()).startFullDrag();
		}

		else if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_ENTERED) {
			if (event.isPrimaryButtonDown()) {
				mineField.togglePressDown(cellX, cellY);
			}
			else if (event.isMiddleButtonDown()) {
				mineField.togglePressDownNeighbours(cellX, cellY);
			}
		}

		else if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_EXITED) {
			if (mineField.getCellSweepState(cellX, cellY)
					== SweepState.PRESSED) {
				mineField.togglePressDown(cellX, cellY);
			}
			else if (event.isMiddleButtonDown()) {
				mineField.togglePressDownNeighbours(cellX, cellY);
			}
		}

		mineFieldPane.draw();

		// DEBUG CANVAS
		if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
			debugCanvas.redraw(-1, -1);
		}
		else {
			debugCanvas.redraw(origin.getCellX(), origin.getCellY());
		}

		showResultPopupIfNecessary(preEventGameState, mineField.getGameState());
	}

	private DebugCanvas setupDebugStage() {
		Stage debugStage = new Stage();

		debugStage.setTitle("JMine Input Debugger");
		Group root = new Group();
		Scene scene = new Scene(root);
		debugStage.setScene(scene);
		scene.setFill(Color.LIGHTGRAY);

		DebugCanvas debugCanvas = new DebugCanvas(80, 60);
		root.getChildren().add(debugCanvas);

		debugStage.setX(200);
		debugStage.setY(670);
		debugStage.sizeToScene();
		//debugStage.show();

		return debugCanvas;
	}

	private void showResultPopupIfNecessary(GameState preEventGameState, GameState postEventGameState) {
		if (preEventGameState == GameState.RUNNING &&
				(postEventGameState == GameState.WON ||
				postEventGameState == GameState.DEAD)) {
			String message = (postEventGameState == GameState.WON) ? "You won!" : "You died!";

			Stage dialogStage = new Stage();
			Button okButton = new Button("OK");
			okButton.setDefaultButton(true);
			okButton.setOnAction(event -> dialogStage.close());
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setScene(new Scene(VBoxBuilder.create().
					children(new Text(message), okButton).
					alignment(Pos.CENTER).padding(new Insets(20)).
					spacing(20).build()));
			dialogStage.show();
		}

	}

	class DebugCanvas extends Canvas {

		public DebugCanvas(double width, double height) {
			super(width, height);
		}

		public void redraw(int cellX, int cellY) {
			GraphicsContext gc = getGraphicsContext2D();

			gc.setFill(Color.LIGHTGRAY);
			gc.fillRect(0, 0, getWidth(), getHeight());

			gc.setTextBaseline(VPos.CENTER);

			gc.setTextAlign(TextAlignment.LEFT);

			gc.strokeText("X:", 10, 15);
			gc.strokeText("Y:", 10, 40);

			gc.setTextAlign(TextAlignment.RIGHT);

			gc.strokeText(Integer.toString(cellX), 60, 15);
			gc.strokeText(Integer.toString(cellY), 60, 40);
		}
	}
}
