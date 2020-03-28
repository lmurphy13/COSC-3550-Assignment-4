import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class EnemyShip extends Sprite {
	
	int type; // type indicates species of enemy: 0 - Klingon, 1 - Romulan, 2 - Borg
	int size = 40;
	double health = 30;
	
	Image img;
	
	// Setup phaser parameters
	final int PHASERRELOAD = StarTrek.FPS * 10;
	final int PHASERLIMIT = 11;
	int numPhasers = 0;
	Phaser[] phaserBank = new Phaser[PHASERLIMIT];
	boolean phaserCooldown = false;
	int counter = PHASERRELOAD;
	
	public EnemyShip(double x, double y, int t) {
		super(x, y);
		this.dx = 0;
		this.dy = 0;
		this.type = t;
		
		switch (this.type) {
			case 0: 
					this.img = new Image("images/birdofprey.png");
					break;
			case 1: 
					this.img = new Image("images/warbird.png");
					break;
			case 2: 
					this.img = new Image("images/cube.png");
					break;
			default: break;
		}
		
		for (int i = 0; i < PHASERLIMIT; i++) {
			phaserBank[i] = new Phaser(0,0,this.type);
			phaserBank[i].active = false;
		} 
		
		this.active = true;
		this.visible = true;
	}
	
	void moveTowards(PlayerShip ps) {
		return;
	}

	void firePhaser() {
		//Phaser p = new Phaser(this.x + (this.size/2) - 2.5, this.y, 3); // type 3 is player
		if (!phaserCooldown) {
		// If number of phasers shot is less than the number allowed (the size of the phaser bank)
			if (numPhasers < PHASERLIMIT - 1) {
				phaserBank[numPhasers] = new Phaser(this.x + (this.size/2) - 2.5, this.y, this.type);
				
				//System.out.println("FIRE" + numPhasers);
				numPhasers++;
				//System.out.println("numPhasers: " + numPhasers);
				switch (this.type) {
					case 0: StarTrek.klingonPhaser.play(); break;
					case 1: StarTrek.romulanPhaser.play(); break;
					case 2: StarTrek.borgPhaser.play(); break;
				}
				
				if (numPhasers >= PHASERLIMIT - 1) {
					phaserCooldown = true;
					counter = PHASERRELOAD; 
				}	
			}
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
	
	void update() {
		
		if (this.x <= 10) {
			this.state = GO_RIGHT;
			this.dx = -this.dx;
		}
		
		if (this.x >= StarTrek.FIELDWIDTH - this.size - 10) {
			this.state = GO_LEFT;
			this.dx = -this.dx;
		}
		
		if (this.y <= 10) {
			this.state = GO_DOWN;
			this.dy = -this.dy;
		}
		
		if (this.y >= StarTrek.FIELDHEIGHT - this.size - 10) {
			this.state = GO_UP;
			this.dy = -this.dy;
		}
		
		this.x += this.dx;
		this.y += this.dy;
		
		
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
		
	}

	
	void render(GraphicsContext gc) {
		
		if (active) {
			// Draw ship image
			gc.drawImage(this.img, this.x, this.y);
			
			// Draw health bar
			gc.setFill(Color.RED);
			gc.fillRect(this.x + 5, this.y - 10, this.health, 5);
			
			// Render phasers
			renderPhasers(gc);
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
	
	boolean collidesWith(Phaser p) {
		if (p.x + p.width > this.x
				&& p.x < this.x + size
				&& p.y + p.length > this.y
				&& p.y < this.y + size)
				return true;
			else
				return false;
	}
	
	boolean collidesWith(Torpedo t) {
		if (t.x + t.diameter > this.x
				&& t.x < this.x + size
				&& t.y + t.diameter > this.y
				&& t.y < this.y + size)
				return true;
			else
				return false;
	}
}
