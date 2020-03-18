import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Sprite {
	double x, y;
	double dx, dy;


	boolean active = true;
	boolean visible = true;

	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
	}

	void update() {
		x += dx;
		y += dy;
	}
	
	void setVelocity(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	boolean isActive() {
		return active;
	}

	void suspend() {
		active = false; visible = false;
	}

	void resume() {
		active = true; visible = true;
	}
	
	void shootPhaser() {
		// definition depending on type of sprite
	}
	
}
