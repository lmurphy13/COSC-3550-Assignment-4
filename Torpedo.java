import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Torpedo {
	double x, y;
	double dx = 0;
	double dy = -3.0;
	
	double length = 30;
	double diameter = 5;
	
	boolean active;
	
	public Torpedo(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
	
	void render(GraphicsContext gc) {
		gc.setFill(Color.RED);
		gc.fillOval(this.x, this.y - 10, diameter, diameter);
	}
	
	void update() {
		this.x += this.dx;
		this.y += this.dy;
		
		if (this.y + this.length < 0) {
			this.active = false;
		}
	}
}
