import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class PlayerShip extends Sprite {

	int size = 20;
	int p_aim = 0; // phaser aim angle offset
	double theta = 0.0;
	double dtheta = 5.0;
	
	Phaser[] beams = new Phaser[5];
	int phaser = beams.length - 1;

	final int PHASERRELOAD = StarTrek.FPS * 10;
	int phaserCount = beams.length;
	boolean phaserEmpty = false;

	int torpedoCount = 0;
	boolean torpedoEmpty = false;

	Torpedo[] torpedos = new Torpedo[3];
	int torpedo = torpedos.length - 1;

	public PlayerShip(double x, double y) {
		super(x, y);

		this.active = true;
		this.visible = true;
	}

	void render(GraphicsContext gc) {
		// render phaser aim
		gc.setStroke(Color.YELLOW);
		gc.strokeLine(x + (size/2), y + (size/2), x + (size/2) + p_aim , y - (size/2) - 10 + (p_aim * 0.5));

		// render ship
		gc.setFill(Color.CADETBLUE);
		gc.fillRect(x, y, size, size);

	}


	void update() {
		x += dx;
		y += dy;
		theta += dtheta;

		if (theta < 0) {
			theta += 360;
		}
		else if (theta > 360) {
			dtheta -= 360;
		}

		if (y + size > StarTrek.HEIGHT - 150) {
			y = StarTrek.HEIGHT - 150 - size;
		}
		if (y < 0) {
			y = 0; 
		}
		if (x + size > StarTrek.WIDTH) {
			x = StarTrek.WIDTH - size;
		}
		if (x < 0) {
			x = 0;
		}
	}

	void shootPhaser() {
		//Phaser p = new Phaser(this.x + (this.size/2) - 2.5, this.y, 3); // type 3 is player
		if (!phaserEmpty) {

			if (phaser >= 0) {
				beams[phaser].x = (this.x + (this.size/2) - 2.5);
				beams[phaser].y = this.y;
				beams[phaser].type = 3;
				beams[phaser].active = true;

				if (phaser == 0) {
					phaserEmpty = true;
					phaser = beams.length - 1;
					return;
				}


				phaser--;


			}
		}
	}

	void shootTorpedo() {
		if (!torpedoEmpty) {

			if (torpedo >= 0) {
				torpedos[torpedo].x = (this.x + (this.size/2) - 2.5);
				torpedos[torpedo].y = this.y;
				torpedos[torpedo].active = true;

				if (phaser == 0) {
					torpedoEmpty = true;
					return;
				}


				torpedo--;


			}
		}


	}



}
