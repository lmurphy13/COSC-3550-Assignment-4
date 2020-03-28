import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.media.AudioClip;

public class PlayerShip extends Sprite {

	int size = 20;
	int p_aim = 0; // phaser aim angle offset
	double theta = 0.0;
	double dtheta = 5.0;

	// Setup phaser parameters
	final int PHASERRELOAD = StarTrek.FPS * 10;
	final int PHASERLIMIT = 11;
	int numPhasers = 0;
	Phaser[] phaserBank = new Phaser[PHASERLIMIT];
	boolean phaserCooldown = false;
	int counter = PHASERRELOAD;
	
	// Setup torpedo parameters
	final int TORPEDOLIMIT = 6;	// one more than the limit. this lets there be a limit, i don't know why
	int numTorpedos;
	Torpedo[] torpedoBank = new Torpedo[TORPEDOLIMIT];
	
	Image img = new Image("images/enterprise.png");
	
	double health = 30;
	
	public static AudioClip playerPhaser;
	

	public PlayerShip(double x, double y) {
		super(x, y);

		this.active = true;
		this.visible = true;
		
		for (int i = 0; i < PHASERLIMIT; i++) {
			phaserBank[i] = new Phaser(0,0,3);
			phaserBank[i].active = false;
		} 
		
		for (int i = 0; i < TORPEDOLIMIT; i++) {
			torpedoBank[i] = new Torpedo(0,0);
			torpedoBank[i].active = false;
		}
		
		playerPhaser = new AudioClip(ClassLoader.getSystemResource("sounds/ent_phaser.mp3").toString());
	}

	void render(GraphicsContext gc) {
		// render phaser aim
//		gc.setStroke(Color.YELLOW);
//		gc.strokeLine(x + (size/2), y + (size/2), x + (size/2) + p_aim , y - (size/2) - 10 + (p_aim * 0.5));

		// render ship
//		gc.setFill(Color.CADETBLUE);
//		gc.fillRect(x, y, size, size);
		gc.drawImage(img,x, y);
		
		renderPhasers(gc);
		renderTorpedos(gc);

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
		
		if (phaserCooldown) {
			//System.out.println(counter);
			if (counter <= 0) {
				phaserCooldown = false;
				
			} else {
				counter--;
				if (counter % StarTrek.FPS == 0) {
					numPhasers--;
				}
			}
		}
		
		updatePhasers();
		updateTorpedos();
	}

	void firePhaser() {
		//Phaser p = new Phaser(this.x + (this.size/2) - 2.5, this.y, 3); // type 3 is player
		if (!phaserCooldown) {
		// If number of phasers shot is less than the number allowed (the size of the phaser bank)
			if (numPhasers < PHASERLIMIT - 1) {
				phaserBank[numPhasers] = new Phaser(this.x + (this.size/2) - 2.5, this.y, 3);
				
				//System.out.println("FIRE" + numPhasers);
				numPhasers++;
				//System.out.println("numPhasers: " + numPhasers);
				playerPhaser.play();
				
				if (numPhasers >= PHASERLIMIT - 1) {
					phaserCooldown = true;
					counter = PHASERRELOAD; 
				}	
			}
		} else {
			StarTrek.unableToComply.play();
		}
	}
	
	void updatePhasers() {
		for (int i = 0; i <= numPhasers; i++) {
			phaserBank[i].update();
		}
	}
	
	void renderPhasers(GraphicsContext gc) {
		for (int i = 0; i <= numPhasers; i++) {
			phaserBank[i].render(gc);
		}
	}

	void fireTorpedo() {
		if (numTorpedos < TORPEDOLIMIT - 1) {
			torpedoBank[numTorpedos] = new Torpedo(this.x + (this.size/2) - 2.5, this.y);
			numTorpedos++;
			StarTrek.torpedoSound.play();
		} else {
			StarTrek.unableToComply.play();
		}
	}	

	void updateTorpedos() {
		for (int i = 0; i <= numTorpedos; i++) {
			if (active)
				torpedoBank[i].update();
		}
	}
	
	void renderTorpedos(GraphicsContext gc) {
		for (int i = 0; i <= numTorpedos; i++) {
			if (active)
				torpedoBank[i].render(gc);
		}
	}
	
	boolean collidesWith(Phaser p) {
		if (p.x + p.width > this.x
				&& p.x < this.x + size
				&& p.y + p.length > this.y
				&& p.y < this.y + size)
				return true;
			else
				return false;
	}
	
	void reset() {
		this.active = true;
		this.visible = true;
		
		for (int i = 0; i < PHASERLIMIT; i++) {
			phaserBank[i] = new Phaser(0,0,3);
			phaserBank[i].active = false;
		} 
		
		numTorpedos = 0;
		for (int i = 0; i < TORPEDOLIMIT; i++) {
			torpedoBank[i] = new Torpedo(0,0);
			torpedoBank[i].active = false;
		}
		
		phaserCooldown = false;
		counter = PHASERRELOAD;
		health = 30;
	}


}
