import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Cursor {
	int state = 0;
	
	boolean active = false;
	
	Image img = new Image("images/ent.png");
	
	public Cursor() {
		
	}
	
	void render(GraphicsContext gc) {
		if (active) {
			switch (state) {
				case 0: gc.drawImage(img, 190, 350); break;
				case 1: gc.drawImage(img, 190, 450); break;
				case 2: gc.drawImage(img, 190, 550); break;
				case -1: state = 2; break;
			}
		}
	}
	
}
