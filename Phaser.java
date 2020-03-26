import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Phaser {
	
	double x, y;
	double dx, dy;
	
	double length = 30;
	double width = 5;
	
	int type;
	
	boolean active, visible;
	
	/*
	 * Types:
	 * 0 - Klingon
	 * 1 - Romulan
	 * 2 - Borg
	 * 3 - Player
	 */
	
	public Phaser(double x, double y, int t) {
		this.x = x;
		this.y = y;
		this.type = t;
		
		this.dx = 0;
		this.dy = -10;
		
		this.active = true;
	}
	
	void render(GraphicsContext gc) {
		if (active) {
			switch (this.type) {
				case 0:
					gc.setFill(Color.DARKRED);
					gc.fillRect(this.x, this.y, this.width, this.length);			
					break;
				case 1:
					gc.setFill(Color.DARKGREEN);
					gc.fillRect(this.x, this.y, this.width, this.length);	
					break;
				case 2:
					gc.setFill(Color.DARKGRAY);
					gc.fillRect(this.x, this.y, this.width, this.length);	
					break;
				case 3:
					gc.setFill(Color.YELLOW);
					gc.fillRect(this.x, this.y, this.width, this.length);	
					break;
			}
		}
	}
	
	void update() {
		
		switch (this.type) {
			// Player case
			case 3:
				this.x += dx;
				this.y += dy;
				
				if (this.y + this.length < 0) {
					this.active = false;
				}
				break;
			// Otherwise enemy
			default:
				this.x += dx;
				this.y -= dy;
				
				if (this.y + this.length > StarTrek.FIELDHEIGHT) {
					this.active = false;
				}
				break;
		}
	}
	
	
}
