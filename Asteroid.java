import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.util.Random;

public class Asteroid {
	double x, y;
	double dx, dy;
	int diameter = 10;
	
	boolean active;
	
	Image img;
	
	public Asteroid() {
		
		Random rand = new Random();
		
		// Random position
		this.x = StarTrek.WIDTH * rand.nextDouble();
		this.y = (StarTrek.FIELDHEIGHT - 10) * rand.nextDouble();
		
		
		// Random velocity and direction
		this.dx = -0.5 + (0.5 - (-0.5)) * rand.nextDouble();
		this.dy = -0.5 + (0.5 - (-0.5)) * rand.nextDouble();
		
		this.active = true;
	}
	
	void update() {
		if (active) {
			this.x += this.dx;
			this.y += this.dy;
			
			if (this.x > StarTrek.WIDTH - diameter|| this.x < 0) {
				this.dx = -this.dx;
			}
			if (this.y > StarTrek.HEIGHT - 150 - diameter || this.y < 0 + diameter) {
				this.dy = -this.dy;
			}
		}
	}
	
	boolean collidesWith(PlayerShip ps) {
		if (ps.x + ps.size > this.x
			&& ps.x < this.x + diameter
			&& ps.y + ps.size > this.y
			&& ps.y < this.y + diameter)
			return true;
		else
			return false;
	}
	
	boolean collidesWith(Phaser p) {
		if (p.x + p.width > this.x
				&& p.x < this.x + diameter
				&& p.y + p.length > this.y
				&& p.y < this.y + diameter)
				return true;
			else
				return false;
	}
	
	boolean collidesWith(Torpedo t) {
		if (t.x + t.diameter > this.x
				&& t.x < this.x + diameter
				&& t.y + t.diameter > this.y
				&& t.y < this.y + diameter)
				return true;
			else
				return false;
	}
	
	void render(GraphicsContext gc) {
		if (active) {
			gc.setFill(Color.WHITE);
			gc.fillOval(this.x, this.y, diameter, diameter);
			
//			gc.drawImage(this.img, this.x, this.y);
		}
	}

}
