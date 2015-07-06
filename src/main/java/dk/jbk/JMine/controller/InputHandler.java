package dk.jbk.JMine.controller;

import dk.jbk.JMine.view.MineFieldCanvas;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class InputHandler implements EventHandler<MouseEvent> {
	private MineField mineField;
	private MineFieldCanvas mineFieldCanvas;

	private DebugCanvas debugCanvas;

	public InputHandler(MineField mineField, MineFieldCanvas mineFieldCanvas) {
		this.mineField = mineField;
		this.mineFieldCanvas = mineFieldCanvas;

		debugCanvas = setupDebugStage();
	}

	@Override
	public void handle(MouseEvent event) {
		EventType eventType = event.getEventType();

		int cellX = mineFieldCanvas.translatePixelToCell((int) event.getX());
		int cellY = mineFieldCanvas.translatePixelToCell((int) event.getY());

		debugCanvas.redraw(cellX, cellY, (int)event.getX(), (int)event.getY());
	}

	private DebugCanvas setupDebugStage() {
		Stage debugStage = new Stage();

		debugStage.setTitle("JMine Input Debugger");
		Group root = new Group();
		Scene scene = new Scene(root);
		debugStage.setScene(scene);
		scene.setFill(Color.LIGHTGRAY);

		DebugCanvas debugCanvas = new DebugCanvas(150, 60);
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

		public void redraw(int cellX, int cellY, int pixelX, int pixelY) {
			GraphicsContext gc = getGraphicsContext2D();

			gc.setFill(Color.LIGHTGRAY);
			gc.fillRect(0, 0, getWidth(), getHeight());

			gc.setTextBaseline(VPos.CENTER);

			gc.strokeText("X:", 30, 15);
			gc.strokeText("Y:", 30, 40);

			gc.setTextAlign(TextAlignment.RIGHT);

			gc.strokeText(Integer.toString(cellX), 80, 15);
			gc.strokeText(Integer.toString(cellY), 80, 40);

			gc.strokeText(Integer.toString(pixelX), 130, 15);
			gc.strokeText(Integer.toString(pixelY), 130, 40);
		}
	}
}
