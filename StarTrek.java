import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.util.Random;
import java.util.ArrayList;

public class StarTrek extends Application {
	final String appName = "Star Trek: The Original Game";
	final static int FPS = 30;
	final static int WIDTH = 800;
	final static int HEIGHT = 800;
	final static double FIELDWIDTH = WIDTH;
	final static double FIELDHEIGHT = (HEIGHT - 150);
	
	

	/**
	 * States:
	 * 0: Title screen
	 * 1: Controls
	 * 2: Instructions
	 * 3: Level 1
	 * 4: Level 2
	 * 5: Level 3
	 */

	static int state = 0; // title screen by default

	Asteroid[] asts = new Asteroid[10];
	Star[] stars = new Star[150];

	EnemyShip[] enemies = new EnemyShip[3];
	PlayerShip ps;

	Score livesScore = new Score(30, HEIGHT-60, "Lives: ", 3);
	Score phaserLevel = new Score(30, HEIGHT-90, "Phaser Charge", 0);
	
	

	int lives = 3;

	void initialize() {
		ps = new PlayerShip(FIELDWIDTH / 2, FIELDHEIGHT / 2);
		
		for (int i = 0; i < asts.length; i++) {
			asts[i] = new Asteroid(); 
		}

		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star();
		}

		for (int i = 0; i < ps.beams.length; i++) {
			ps.beams[i] = new Phaser(0,0,0);
		}
		
		for (int i = 0; i < ps.torpedos.length; i++) {
			ps.torpedos[i] = new Torpedo(0,0);
			ps.torpedos[i].active = false;
		}

		enemies[0] = new EnemyShip(200,200);
		enemies[0].type = 0;
		enemies[1] = new EnemyShip(400,100);
		enemies[1].type = 1;
		enemies[2] = new EnemyShip(600,200);
		enemies[2].type = 2;



	}

	void setHandlers(Scene scene) {
		scene.setOnKeyPressed(
				e -> {
					KeyCode c = e.getCode();

					switch (c) {
					case UP: ps.setVelocity(ps.dx, -10); break;
					case DOWN: ps.setVelocity(ps.dx, 10); break;
					case LEFT: ps.setVelocity(-10, ps.dy); break;
					case RIGHT: ps.setVelocity(10, ps.dy); break;
					case SPACE: ps.shootPhaser(); break;
					case ENTER: ps.shootTorpedo(); break;
					}
				}
				);

		scene.setOnKeyReleased(
				e -> {
					KeyCode c = e.getCode();

					switch (c) {
					case UP: ps.setVelocity(ps.dx, 0); break;
					case DOWN: ps.setVelocity(ps.dx, 0); break;
					case LEFT: ps.setVelocity(0, ps.dy); break;
					case RIGHT: ps.setVelocity(0, ps.dy); break;
					}
				}

				);

	}

	void update() {
		// Check for collisions between player and asteroid
		for (Asteroid a : asts) {
			a.update();
			if (a.collidesWith(ps) && a.active) {
				ps.x = FIELDWIDTH / 2;
				ps.y = FIELDHEIGHT / 2;
				lives--;
			}


			for (Phaser p : ps.beams) {
				if (a.collidesWith(p)) {
					a.active = false;
				}
			}
			
			for (Torpedo t : ps.torpedos) {
				if (a.collidesWith(t)) {
					a.active = false;
				}
			}

		}

		for (EnemyShip e : enemies) {
			if (e.collidesWith(ps)) {
				ps.x = FIELDWIDTH / 2;
				ps.y = FIELDHEIGHT / 2;
				lives--;
			}
		}

		for (Phaser p : ps.beams) {
			if (ps.phaserEmpty) {
				if (ps.phaserCount > ps.PHASERRELOAD) {
					if (ps.phaserCount % 5 == 0) {
						p.active = true;
					}
				}
			}
			
			
			p.update();

			if (p.y + p.length < 0) {
				p.active = false;
			}
		}
		
		if (ps.phaserEmpty == true) {
			ps.phaserCount--;
			
			if (ps.phaserCount <= ps.PHASERRELOAD) {
				ps.phaserCount = 5;
				ps.phaserEmpty = false;
			}
		}
		
		for (Torpedo t : ps.torpedos) {
			t.update();
		}


		ps.update();
		
		if (lives < 0) {
			lives = 0;
		}
		
		livesScore.updateValue(lives);
	}


	public void render(GraphicsContext gc) {
		// Draw background
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, WIDTH, HEIGHT);

		// Draw random stars in the background
		for (Star s : stars) {
			s.render(gc);
		}


		// Draw status panel background
		gc.setFill(Color.LIGHTBLUE);
		gc.fillRect(0, HEIGHT-150, WIDTH, 200);

		// Render scanning animation - NOT WORKING YET
		renderScan(gc);


		// Draw asteroids
		for (Asteroid a : asts) {
			a.render(gc);
		}

		// Draw player
		ps.render(gc);

		// Draw enemies
		for (EnemyShip es : enemies) {
			es.render(gc);
		}

		// Draw lives
		gc.setFill(Color.GREEN);
		for (int life = 0; life < lives; life++) {
			gc.fillOval(30 + (life * 50), HEIGHT-50, 30, 30);
		}

		// Draw torpedoes
		for (Torpedo t : ps.torpedos) {
			t.render(gc);
		}

		// Draw phaser beams
		for (Phaser p : ps.beams) {
			p.render(gc);
		}
		
		// Draw phaser status indicator
		gc.setFill(Color.BLACK);
		gc.fillRect(30, FIELDHEIGHT + 20, 300, 20);
		gc.setFill(Color.YELLOW);
		//gc.fillRect(35, FIELDHEIGHT + 25, ps.phaser+1 * 10, 10);
		
		switch (ps.phaserCount) {
			case 0:
				gc.setFill(Color.BLACK);
				gc.fillRect(30, FIELDHEIGHT + 20, 300, 20); break;
			case 1:
				gc.fillRect(35, FIELDHEIGHT + 25, 10, 10); break;
			case 2:
				gc.fillRect(35, FIELDHEIGHT + 25, 20, 10); break;
			case 3:
				gc.fillRect(35, FIELDHEIGHT + 25, 30, 10); break;
			case 4:
				gc.fillRect(35, FIELDHEIGHT + 25, 40, 10); break;
			case 5:
				gc.fillRect(35, FIELDHEIGHT + 25, 50, 10); break;
		}
		
		livesScore.render(gc);
		phaserLevel.render(gc);

	}

	void renderScan(GraphicsContext gc) {
		int scancount = 100;
		int j = 0;

		gc.setFill(Color.BLACK);
		gc.fillRect((WIDTH/4), HEIGHT-30, 400, 20);

		for (int i = WIDTH/4 + 5; i < WIDTH/4 + 395; i += 20) {
			gc.setFill(Color.YELLOW);
			gc.fillRect(i, HEIGHT-25, 10, 10);	
		}

		for (int i = WIDTH/4 + 5; i < WIDTH/4 + 395; i += 20) {
			gc.setFill(Color.YELLOW);
			gc.fillRect(i, HEIGHT-25, 10, 10);

			while (scancount > 0) {
				j += 1;
				scancount--;
			}
			scancount = 100;

			gc.setFill(Color.CHOCOLATE);
			gc.fillRect(i, HEIGHT-25, 10, 10);

			while (scancount > 0) {
				j += 1;
				scancount--;
			}
			scancount = 100;


		}	


	}



	/*
	 * Begin boiler-plate code...
	 * [Animation and events with initialization]
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage theStage) {
		theStage.setTitle(appName);

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		// Initial setup
		initialize();
		setHandlers(theScene);

		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					// update position
					update();
					// draw frame
					render(gc);
				}
				);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();

		theStage.show();
	}
	/*
	 * ... End boiler-plate code
	 */
}
