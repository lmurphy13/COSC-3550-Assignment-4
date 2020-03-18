import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemyShip extends Sprite {
	
	int type = 0; // type indicates species of enemy: 0 - Klingon, 1 - Romulan, 2 - Borg
	int size = 20;
	
	public EnemyShip(double x, double y) {
		super(x, y);
		
		switch (StarTrek.state) {
			case 3: this.type = 0; break;
			case 4: this.type = 1; break;
			case 5: this.type = 2; break;
			default: this.type = 0; break;
		}
		
		this.active = true;
		this.visible = true;
	}
	
	void moveTowards(PlayerShip ps) {
		return;
	}

	
	void render(GraphicsContext gc) {
		switch (this.type) {
			case 0:
				gc.setFill(Color.DARKRED);
				gc.fillOval(this.x, this.y, 20, 20);
				break;
			case 1:
				gc.setFill(Color.DARKGREEN);
				gc.fillOval(this.x, this.y, 20, 20);
				break;
			case 2:
				gc.setFill(Color.DARKGRAY);
				gc.fillOval(this.x, this.y, 20, 20);
		}
	}
	
	boolean collidesWith(PlayerShip ps) {
		if (ps.x + ps.size > this.x
			&& ps.x < this.x + size
			&& ps.y + ps.size > this.y
			&& ps.y < this.y + size)
			return true;
		else
			return false;
	}
}
