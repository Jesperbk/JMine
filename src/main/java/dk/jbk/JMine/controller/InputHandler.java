package dk.jbk.JMine.controller;

import dk.jbk.JMine.model.SweepState;
import dk.jbk.JMine.view.MineFieldPane;
import dk.jbk.JMine.view.TileCanvas;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class InputHandler implements EventHandler<MouseEvent> {
	private MineField mineField;
	private MineFieldPane mineFieldPane;

	private DebugCanvas debugCanvas;

	public InputHandler(MineField mineField, MineFieldPane mineFieldPane) {
		this.mineField = mineField;
		this.mineFieldPane = mineFieldPane;

		// debugCanvas = setupDebugStage();
	}

	@Override
	public void handle(MouseEvent event) {
		TileCanvas origin = (TileCanvas)event.getSource();
		int cellX = origin.getCellX();
		int cellY = origin.getCellY();

		if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
			if (event.getButton() == MouseButton.PRIMARY) {
				mineField.togglePressDown(cellX, cellY);
			}
		}

		else if (event.getEventType() == MouseEvent.MOUSE_RELEASED
				|| event.getEventType() == MouseDragEvent.MOUSE_DRAG_RELEASED) {
			if (event.getButton() == MouseButton.PRIMARY) {
				mineField.expose(cellX, cellY);
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
		}

		else if (event.getEventType() == MouseDragEvent.MOUSE_DRAG_EXITED) {
			if (mineField.getCellSweepState(cellX, cellY)
					== SweepState.PRESSED) {
				mineField.togglePressDown(cellX, cellY);
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
		debugStage.show();

		return debugCanvas;
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
