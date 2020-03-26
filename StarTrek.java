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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.util.Random;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class StarTrek extends Application {
	final String appName = "Star Trek: The Original Game";
	final static int FPS = 30;
	final static int WIDTH = 800;
	final static int HEIGHT = 800;
	final static double FIELDWIDTH = WIDTH;
	final static double FIELDHEIGHT = (HEIGHT - 150);
	Random rand = new Random();
	
	public static Canvas canvas = new Canvas(WIDTH, HEIGHT);
	
	

	/**
	 * States:
	 * 0: Title screen
	 * 1: Controls
	 * 2: Instructions
	 * 3: Level 1
	 * 4: Level 2
	 * 5: Level 3
	 * 6: End Screen
	 */

	static int state = 0;
			; // title screen by default

	/* STATE 0 - TITLE SCREEN */
	
	final int NUMSTARS = 100;
	IntroStar[] introStars = new IntroStar[NUMSTARS];
	
	/* END STATE 0 */
	
	
	/* STATE 3 - GAMEPLAY */
	
	Asteroid[] asts = new Asteroid[20];
	String[] asteroidPics = new String[5];
	
	Star[] stars = new Star[150];

	EnemyShip[] enemies = new EnemyShip[3];
	PlayerShip ps;

	Score livesScore = new Score(30, HEIGHT-60, "Lives: ", 3);
	Score phaserLevel = new Score(30, HEIGHT-90, "Phaser Charge");
	Score torpedoLevel = new Score(WIDTH - 220, FIELDHEIGHT + 20, "Torpedos Remaining");
	
	int lives = 3;
	
	
	final int FIRERATE = FPS * 3;
	int countFire = FIRERATE;
	
	/* END STATE 3 */
	
	/* STATE 6 - END SCREEN */
	
	
	
	/* END STATE 6 */
	
	/* SOUND EFFECTS */
	public static AudioClip playerPhaser;
	public static AudioClip klingonPhaser;
	public static AudioClip romulanPhaser;
	public static AudioClip borgPhaser;
	public static AudioClip torpedoSound;
	public static AudioClip redAlert;
	public static AudioClip unableToComply;
	MediaPlayer mp;
	
	/* END SOUND EFFECTS */

	void initialize() {
		//new AudioClip(ClassLoader.getSystemResource("glass_break.wav").toString());
		
		// Sounds
		klingonPhaser = new AudioClip(ClassLoader.getSystemResource("sounds/klingon_disruptor.mp3").toString());
		romulanPhaser = new AudioClip(ClassLoader.getSystemResource("sounds/romulan_disruptor.mp3").toString());
		borgPhaser = new AudioClip(ClassLoader.getSystemResource("sounds/borg_phaser.mp3").toString());
		torpedoSound = new AudioClip(ClassLoader.getSystemResource("sounds/ent_torpedo.mp3").toString());
		redAlert = new AudioClip(ClassLoader.getSystemResource("sounds/redalert.mp3").toString());
		unableToComply = new AudioClip(ClassLoader.getSystemResource("sounds/unabletocomply.mp3").toString());
		Media titlesong = new Media(ClassLoader.getSystemResource("sounds/maintitle.mp3").toString());
		Media endsong = new Media(ClassLoader.getSystemResource("sounds/endcredits.mp3").toString());
		
		
		if (state == 0) {
			mp = new MediaPlayer(titlesong);
			mp.play();
			
			for (int i = 0; i < introStars.length; i++) {
				introStars[i] = new IntroStar();
			}
			
			
		}
		
		if (state == 3) {
			for (int i = 1; i <= asteroidPics.length; i++) {
				asteroidPics[i - 1] = "images/asteroid" + i + ".png";
			}
			
			ps = new PlayerShip(FIELDWIDTH / 2, FIELDHEIGHT / 2);
			
			for (int i = 0; i < asts.length; i++) {
				int randAsteroid = rand.nextInt(5);
				
				asts[i] = new Asteroid(new Image(asteroidPics[randAsteroid])); 
			}
	
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star();
			}
	
			enemies[0] = new EnemyShip(200,200, 0);
		
			enemies[1] = new EnemyShip(400,100, 1);
			
			enemies[2] = new EnemyShip(600,200, 2);
			
		}
		
		if (state == 6) {
			
		}


	}

	void setHandlers(Scene scene) {
		scene.setOnKeyPressed(
				e -> {
					KeyCode c = e.getCode();

					if (state == 3) {
						switch (c) {
							case UP: ps.setVelocity(ps.dx, -10); break;
							case DOWN: ps.setVelocity(ps.dx, 10); break;
							case LEFT: ps.setVelocity(-10, ps.dy); break;
							case RIGHT: ps.setVelocity(10, ps.dy); break;
							case SPACE: ps.firePhaser(); break;
							case ENTER: ps.fireTorpedo(); break;
						}
					}
				}
				);

		scene.setOnKeyReleased(
				e -> {
					KeyCode c = e.getCode();

					if (state == 3) {
						switch (c) {
							case UP: ps.setVelocity(ps.dx, 0); break;
							case DOWN: ps.setVelocity(ps.dx, 0); break;
							case LEFT: ps.setVelocity(0, ps.dy); break;
							case RIGHT: ps.setVelocity(0, ps.dy); break;
						}
					}
				}
				);

	}

	void update() {
		
		if (state == 0) {
			for (IntroStar s : introStars) {
				s.update();
			}
		}
		
		if (state == 3) {
			// Check for collisions between player and asteroid
			for (Asteroid a : asts) {
				if (a.active) {
				
					a.update();
					if (a.collidesWith(ps) && a.active) {
						ps.x = FIELDWIDTH / 2;
						ps.y = FIELDHEIGHT / 2;
						ps.health -= 10;
					}
		
		
					for (Phaser p : ps.phaserBank) {
						if (a.collidesWith(p)) {
							a.active = false;
							p.active = false;
						}
					}
					
					for (Torpedo t : ps.torpedoBank) {
						if (a.collidesWith(t)) {
							a.active = false;
							t.active = false;
						}
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
	
			ps.update();
			
			for (EnemyShip e : enemies) {
				e.update();
			}
			
			if (lives < 0) {
				lives = 0;
			}
			
			livesScore.updateValue(lives);
			
			if (ps.health <= 0) {
				lives--;
			}
			
			// Enemy fire testing
			
			if (countFire <= 0) {
				enemies[0].firePhaser();
				enemies[1].firePhaser();
				enemies[2].firePhaser();
				countFire = FIRERATE;
			} else {
				countFire--;
			}
		}
	}


	public void render(GraphicsContext gc) {
		if (state == 0) {
			// Draw background
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			
			// Draw starfield
			for (IntroStar s : introStars) {
				s.render(gc);
			}
		}
		
		if (state == 3) {
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
			
			// Draw phaser status indicator
			gc.setFill(Color.BLACK);
			gc.fillRect(30, FIELDHEIGHT + 20, 300, 20);
			gc.setFill(Color.YELLOW);
			//gc.fillRect(35, FIELDHEIGHT + 25, ps.phaser+1 * 10, 10);
			
			// phaser bank is full
			if (ps.numPhasers == 0) {
				gc.fillRect(35, FIELDHEIGHT + 25, 290, 10);
			} else if (ps.numPhasers < ps.PHASERLIMIT) {
				gc.fillRect(35, FIELDHEIGHT + 25, (290 - ps.numPhasers * 29), 10);
			}
			
			// Draw torpedo indicator
			gc.setFill(Color.RED);
			for (int t = 0; t < (ps.TORPEDOLIMIT - 1) - ps.numTorpedos; t++) {
				gc.fillOval(WIDTH - 200 + (t * 50), FIELDHEIGHT + 30, 30, 30);
			}
			
			
			
			livesScore.render(gc);
			phaserLevel.render(gc);
			torpedoLevel.render(gc);
		}
	}

	void renderScan(GraphicsContext gc) {
		if (state == 3) {
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

		
		
		root.getChildren().add(canvas);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		if (state == 0) {
			canvas.setTranslateX(WIDTH/2);
			canvas.setTranslateY(HEIGHT/2);
		}

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
