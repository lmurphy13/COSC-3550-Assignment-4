import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

public class Star {
	double x;
	double y;
	
	public Star() {
		Random rand = new Random();
		
		this.x = StarTrek.WIDTH * rand.nextDouble();
		this.y = (StarTrek.HEIGHT - 150) * rand.nextDouble();
	}
	
	void render(GraphicsContext gc) {
		gc.setFill(Color.WHITE);
		gc.fillRect(x, y, 1, 1);
	}
}
