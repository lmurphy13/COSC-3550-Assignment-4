import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

public class IntroStar {
	double x;
	double y;
	double z; 
	
	double dx;
	double dy;
	
	double diameter;
	
	boolean active;
	
	public IntroStar() {
		Random rand = new Random();
		
		// random starting position
		
		this.x = -StarTrek.WIDTH + (StarTrek.WIDTH - (-StarTrek.WIDTH)) * rand.nextDouble();
		this.y = -StarTrek.HEIGHT + (StarTrek.HEIGHT - (-StarTrek.HEIGHT)) * rand.nextDouble();
		this.z = StarTrek.WIDTH;
		
		this.diameter = 10;
		
//		// random starting diameter
//		this.diameter = 1 + (10 - 1) * rand.nextDouble();
//		
//		this.dx = -0.5 + (0.5 - (-0.5)) * rand.nextDouble();
//		this.dy = -0.5 + (0.5 - (-0.5)) * rand.nextDouble();
//		// random direction outwards
//		
//		// if on the top left side of the screen
//		if (this.x < StarTrek.WIDTH/2 && this.y < StarTrek.HEIGHT/2) {
//			this.dx = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//			this.dy = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//		}
//		
//		// if on the bottom left side of the screen
//		else if (this.x < StarTrek.WIDTH/2 && this.y > StarTrek.HEIGHT/2) {
//			this.dx = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//			this.dy = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//		}
//		
//		// if on the top right side of the screen
//		else if (this.x > StarTrek.WIDTH/2 && this.y < StarTrek.HEIGHT/2) {
//			this.dx = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//			this.dy = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//		}
//		
//		// if on the bottom left side of the screen
//		else if (this.x > StarTrek.WIDTH/2 && this.y > StarTrek.HEIGHT/2) {
//			this.dx = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//			this.dy = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//		}
		
		this.active = true;
	}
	
	void render(GraphicsContext gc) {
		if (active) {
			
			double sx = map(this.x / this.z, 0, 1, 0, StarTrek.canvas.getWidth());
			double sy = map(this.y / this.z, 0, 1, 0, StarTrek.canvas.getHeight());
			
			gc.setFill(Color.WHITE);
			gc.fillOval(sx, sy, this.diameter, this.diameter);
		}
	}
	
	void update() {
		if (active) {
			this.z = this.z - 1;
			
//			this.x += dx;
//			this.y += dy;
//			
//			this.diameter += 0.01;
//			
//			if (this.x > StarTrek.WIDTH || this.x < 0 || this.y > StarTrek.HEIGHT || this.y < 0) {
//				this.active = false;
//				//reset();
//			}
		}
	}
	

	/* map method borrowed from: https://stackoverflow.com/questions/345187/math-mapping-numbers
	 * because I didn't know how / want to figure out how to do this transformation
	 */
	public static double map(double valueCoord1, double startCoord1, double endCoord1, double startCoord2, double endCoord2) {
		double epsilon = 1e-12;

	    if (Math.abs(endCoord1 - startCoord1) < epsilon) {
	        throw new ArithmeticException("/ 0");
	    }

	    double offset = startCoord2;
	    double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
	    return ratio * (valueCoord1 - startCoord1) + offset;
	}
	
	public static double translate(double n1, double n2) {
		return n1 + n2;
	}
	
	void reset() {
//		Random rand = new Random();
//		
//		this.x = StarTrek.WIDTH * rand.nextDouble();
//		this.y = (StarTrek.HEIGHT - 150) * rand.nextDouble();
//		
//		// random starting diameter
//		this.diameter = 1 + (10 - 1) * rand.nextDouble();
//		
//		this.dx = -0.5 + (0.5 - (-0.5)) * rand.nextDouble();
//		this.dy = -0.5 + (0.5 - (-0.5)) * rand.nextDouble();
//		// random direction outwards
//		
//		// if on the top left side of the screen
//		if (this.x < StarTrek.WIDTH/2 && this.y < StarTrek.HEIGHT/2) {
//			this.dx = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//			this.dy = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//		}
//		
//		// if on the bottom left side of the screen
//		else if (this.x < StarTrek.WIDTH/2 && this.y > StarTrek.HEIGHT/2) {
//			this.dx = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//			this.dy = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//		}
//		
//		// if on the top right side of the screen
//		else if (this.x > StarTrek.WIDTH/2 && this.y < StarTrek.HEIGHT/2) {
//			this.dx = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//			this.dy = -0.5 + (-0.1 - (-0.5)) * rand.nextDouble();
//		}
//		
//		// if on the bottom left side of the screen
//		else if (this.x > StarTrek.WIDTH/2 && this.y > StarTrek.HEIGHT/2) {
//			this.dx = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//			this.dy = 0.1 + (0.5 - (0.1)) * rand.nextDouble();
//		}
//		
//		this.active = true;
	}
}
