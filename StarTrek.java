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
	 * 2: Level 1
	 * 3: Level 2
	 * 4: Level 3
	 * 5: End Screen
	 */
	
	final static int TITLE = 0;
	final static int CONTROLS = 1;
	final static int LEVEL1 = 2;
	final static int LEVEL2 = 3;
	final static int LEVEL3 = 4;
	final static int END = 5;

	static int state = TITLE;
			; // title screen by default

	/* STATE 0 - TITLE SCREEN */
			
			/* Script
			 * 13 seconds of stars
			 * Slide in STAR TREK
			 * wait 5 more seconds
			 * Slide in The Original Game
			 */
	
	final int NUMSTARS = 300;
	Star[] introStars = new Star[NUMSTARS];
	
	Image starTrek = new Image("images/startrek.png");
	int stX = -300;
	
	Image theGame = new Image("images/thegame.png");
	int tgX = WIDTH + 100;
	
	Image author = new Image("images/author.png");
	int aX = -250;
	
	Image play = new Image("images/play.png");
	int pY = HEIGHT + 50;
	
	Image controls = new Image("images/controls.png");
	int cY = HEIGHT + 80;
	
	Image quit = new Image("images/quit.png");
	int qY = HEIGHT + 110;
	
	Cursor cursor = new Cursor();
	

	
	final int INTROCOUNT = FPS * 40;
	int intro = 0;
	/* END STATE 0 */
	
	/* STATE 1 - CONTROLS */
	Image controlTitle = new Image("images/controlTitle.png");
	Image back = new Image("images/back.png");
	Image inst1 = new Image("images/inst1.png");
	Image inst2 = new Image("images/inst2.png");
	Image inst3 = new Image("images/inst3.png");
	/* END STATE 1*/
	
	
	
	/* STATE 2 - 5 | LEVEL 1 - 3 */
	Asteroid[] asts = new Asteroid[20];
	String[] asteroidPics = new String[5];
	
	Star[] stars = new Star[150];

	EnemyShip[] enemies = new EnemyShip[5];
	PlayerShip ps = new PlayerShip(FIELDWIDTH / 2, FIELDHEIGHT / 2);
	int lives = 3;

	Score livesScore = new Score(30, HEIGHT-60, "Lives: ", lives);
	Score phaserLevel = new Score(30, HEIGHT-90, "Phaser Bank Charge");
	Score torpedoLevel = new Score(WIDTH - 260, FIELDHEIGHT + 20, "Photon Torpedos Remaining");
	Score playerHealth = new Score(WIDTH - 240, HEIGHT - 5, "Health: ", ps.health);
	
	
	final int FIRERATE = FPS * 3;
	int countFire = FIRERATE;
	
	final int BORGDELAY = FPS * 18;
	int borgCount = 0;
	/* END STATE 2 - 5 | LEVEL 1 - 3 */

	
	/* STATE 5 - END SCREEN */
	boolean playerWon = false;
	
	Image youWin = new Image("images/youwin.png");
	Image youLose = new Image("images/youlose.png");
	Image congrats = new Image("images/congrats.png");
	/* END STATE 5 */
	
	
	
	/* SOUND EFFECTS */
	public static AudioClip playerPhaser;
	public static AudioClip klingonPhaser;
	public static AudioClip romulanPhaser;
	public static AudioClip borgPhaser;
	public static AudioClip torpedoSound;
	public static AudioClip redAlert;
	public static AudioClip unableToComply;
	public static AudioClip smallExplosion;
	public static AudioClip largeExplosion;
	public static AudioClip borg;
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
		smallExplosion = new AudioClip(ClassLoader.getSystemResource("sounds/smallexplosion.mp3").toString());
		largeExplosion = new AudioClip(ClassLoader.getSystemResource("sounds/largeexplosion.mp3").toString());
		borg = new AudioClip(ClassLoader.getSystemResource("sounds/borg.mp3").toString());
		Media titlesong = new Media(ClassLoader.getSystemResource("sounds/maintitle.mp3").toString());
		Media endsong = new Media(ClassLoader.getSystemResource("sounds/endcredits.mp3").toString());
		
		
		if (state == TITLE) {
			// Play opening song
			mp = new MediaPlayer(titlesong);
			mp.play();
			
			// Spawn star field
			for (int i = 0; i < introStars.length; i++) {
				introStars[i] = new Star();
			}	
		}
		
		if (state == CONTROLS) {
			// Spawn star field
			for (int i = 0; i < introStars.length; i++) {
				introStars[i] = new Star();
			}
			
			
		}
		
		if (state == LEVEL1) {
	
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star();
			}
			
			for (int i = 0; i < asts.length; i++) {
				asts[i] = new Asteroid();
			}
	
			enemies[0] = new EnemyShip(200,100, 0);
			enemies[0].dx = 5;
		
			enemies[1] = new EnemyShip(400,200, 0);
			enemies[1].dx = -5;
			
			enemies[2] = new EnemyShip(600,300, 0);
			enemies[2].dx = 5;
			
			enemies[3] = new EnemyShip(0,0,0);
			enemies[3].active = false;
			
			enemies[4] = new EnemyShip(0,0,0);
			enemies[4].active = false;
			
		}
		
		if (state == LEVEL2) {
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star();
			}
			
			for (int i = 0; i < asts.length; i++) {
				asts[i] = new Asteroid();
			}
			
			ps.x = WIDTH/2;
			ps.y = FIELDHEIGHT - 70;
	
			enemies[0] = new EnemyShip(100,100, 1);
			enemies[0].dx = 7;
		
			enemies[1] = new EnemyShip(200,200, 1);
			enemies[1].dx = -7;
			
			enemies[2] = new EnemyShip(300,300, 1);
			enemies[2].dx = 7;
			
			enemies[3] = new EnemyShip(400,400, 1);
			enemies[3].dx = -7;
			
			enemies[4] = new EnemyShip(0,0,0);
			enemies[4].active = false;
		}
		
		if (state == LEVEL3) {
			
			borg.play();
			
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star();
			}
			
			for (int i = 0; i < asts.length; i++) {
				asts[i] = new Asteroid();
			}
			
			ps.x = WIDTH/2;
			ps.y = FIELDHEIGHT - 70;
	
			enemies[0] = new EnemyShip(WIDTH/2, 30, 2);
			enemies[0].dx = 9;
		
			enemies[1] = new EnemyShip(100,150, 2);
			enemies[1].dx = -9;
			
			enemies[2] = new EnemyShip(WIDTH - 100,150, 2);
			enemies[2].dx = 9;
			
			enemies[3] = new EnemyShip(300,300, 2);
			enemies[3].dx = -9;
			
			enemies[4] = new EnemyShip(WIDTH - 300,300,2);
			enemies[4].dx = 9;
		}
		
		if (state == END) {
			mp = new MediaPlayer(endsong);
			mp.play();
			
			// Spawn star field
			for (int i = 0; i < introStars.length; i++) {
				introStars[i] = new Star();
			}
		}


	}

	void setHandlers(Scene scene) {
		scene.setOnKeyPressed(
				e -> {
					KeyCode c = e.getCode();
					
					if (state == TITLE) {
						switch (c) {
							case SPACE: 
								mp.stop();
								intro = INTROCOUNT;
								break;
							case UP: cursor.state = cursor.state - 1; break;
							case DOWN: cursor.state = (cursor.state + 1) % 3; break;
							case ENTER:
								switch (cursor.state) {
									case 0: 
										mp.stop();
										state = LEVEL1; 
										initialize();
										return;
									case 1:
										mp.stop();
										state = CONTROLS;
										initialize();
										return;
									case 2:
										System.exit(0);
									
								}
						}
					}
					
					if (state == CONTROLS) {
						switch (c) {
							case SPACE: 
								state = TITLE; 
								initialize(); 
								return;
						}
					}

					if (state == LEVEL1 || state == LEVEL2 || state == LEVEL3) {
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

					if (state == LEVEL1 || state == LEVEL2 || state == LEVEL3) {
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
		
		if (state == TITLE) {
			if (intro <= INTROCOUNT + 100) {
				if (intro >= FPS * 13) {	// 13 seconds
					if (stX <= WIDTH/2 - 140) {
						stX += 10;
					}
				}
				
				if (intro >= FPS * 18) {	// 18 seconds
					if (tgX >= WIDTH/2 - 240) {
						tgX -= 10;
					}
				}
				
				if (intro >= FPS * 25) {
					if (aX <= WIDTH/2 - 140) {
						aX += 10;
					}
				}
				
				if (intro >= FPS * 30) {
					if (pY >= 350)
						pY -= 10;
					
					if (cY >= 450)
						cY -=10;
					
					if (qY > 550)
						qY -= 10;
				}
				
				if (intro >= FPS * 32) {
					cursor.active = true;
				}
				
				
				intro++;
				
				
			}
		}
		
		if (state == CONTROLS) {
			// do nothing
		}
		
		if (state == LEVEL1) {
			
			for (Asteroid a : asts) {
				if (a.active) {
					// Check for collisions between player and asteroid
					if (a.collidesWith(ps) && a.active) {
						smallExplosion.play();
						ps.health -= 3.5;
						a.active = false;
					}
		
					// Check for collisions between phaser beam and asteroid
					for (Phaser p : ps.phaserBank) {
						if (a.collidesWith(p)) {
							a.active = false;
							p.active = false;
						}
					}
					
					// Check for collisions between torpedo and asteroid
					for (Torpedo t : ps.torpedoBank) {
						if (a.collidesWith(t)) {
							a.active = false;
							t.active = false;
						}
					}
					
					a.update();
				}
	
			}
			
			for (EnemyShip e : enemies) {
				if (e.active) {
					// Check for collision between enemy ship and player
					if (e.collidesWith(ps)) {
						ps.health -= 10;
						e.health -= 10;
					}
					
					// Check for collision between enemy phaser and player
					for (Phaser p : e.phaserBank) {
						if (p.active) {
							if (ps.collidesWith(p)) {
								ps.health -= 3.5;
								p.active = false;
							}
						}
					}
					
					// Check for collision between player weapons and enemy
					for (Phaser p : ps.phaserBank) {
						if (p.active) {
							if (e.collidesWith(p)) {
								e.health -= 3.5;
								p.active = false;
							}
						}
					}
					
					for (Torpedo t : ps.torpedoBank) {
						if (t.active) {
							if (e.collidesWith(t)) {
								e.health -= 8.5;
								t.active = false;
							}
						}
					}
					
					// Update enemies and check their health
					if (e.health <= 0) {
						largeExplosion.play();
						e.active = false;
					}
					
					e.update();
				}
				
			}
	
			ps.update();
			
			// If all enemies are dead, move to Level 2
			if (!enemies[0].active && !enemies[1].active && !enemies[2].active) {
				state = LEVEL2;
				ps.reset();
				lives = 3;
				
				initialize();
				return;
			}
			
			if (lives <= 0) {
				state = END;
				initialize();
				return;
			}
			
			livesScore.updateValue(lives);
			playerHealth.updateValue(ps.health);
			
			if (ps.health <= 0) {
				largeExplosion.play();
				lives--;
				ps.health = 30;
				ps.x = WIDTH/2;
				ps.y = FIELDHEIGHT/2;
			}
			
			
			
			// Enemy fire testing
			
			if (countFire <= 0) {
				for (EnemyShip e : enemies) {
					if (e.active)
						e.firePhaser();
				}
				countFire = FIRERATE;
			} else {
				countFire--;
			}
		}
		
		if (state == LEVEL2) {
			for (Asteroid a : asts) {
				if (a.active) {
					// Check for collisions between player and asteroid
					if (a.collidesWith(ps) && a.active) {
						smallExplosion.play();
						ps.health -= 3.5;
						a.active = false;
					}
		
					// Check for collisions between phaser beam and asteroid
					for (Phaser p : ps.phaserBank) {
						if (a.collidesWith(p)) {
							a.active = false;
							p.active = false;
						}
					}
					
					// Check for collisions between torpedo and asteroid
					for (Torpedo t : ps.torpedoBank) {
						if (a.collidesWith(t)) {
							a.active = false;
							t.active = false;
						}
					}
					
					a.update();
				}
	
			}
			
			for (EnemyShip e : enemies) {
				if (e.active) {
					// Check for collision between enemy ship and player
					if (e.collidesWith(ps)) {
						ps.health -= 10;
						e.health -= 10;
					}
					
					// Check for collision between enemy phaser and player
					for (Phaser p : e.phaserBank) {
						if (p.active) {
							if (ps.collidesWith(p)) {
								ps.health -= 4.5;
								p.active = false;
							}
						}
					}
					
					// Check for collision between player weapons and enemy
					for (Phaser p : ps.phaserBank) {
						if (p.active) {
							if (e.collidesWith(p)) {
								e.health -= 3.5;
								p.active = false;
							}
						}
					}
					
					for (Torpedo t : ps.torpedoBank) {
						if (t.active) {
							if (e.collidesWith(t)) {
								e.health -= 8.5;
								t.active = false;
							}
						}
					}
					
					// Update enemies and check their health
					if (e.health <= 0) {
						largeExplosion.play();
						e.active = false;
					}
					
					e.update();
				}
				
			}
	
			ps.update();
			
			// If all enemies are dead, move to Level 3
			if (!enemies[0].active && !enemies[1].active && !enemies[2].active && !enemies[3].active) {
				state = LEVEL3;
				ps.reset();
				lives = 3;
				
				initialize();
				return;
			}
			
			if (lives <= 0) {
				state = END;
				initialize();
				return;
			}
			
			livesScore.updateValue(lives);
			playerHealth.updateValue(ps.health);
			
			if (ps.health <= 0) {
				largeExplosion.play();
				lives--;
				ps.health = 30;
				ps.x = WIDTH/2;
				ps.y = FIELDHEIGHT - 70;
			}
			
			
			
			// Enemy fire testing
			
			if (countFire <= 0) {
				for (EnemyShip e : enemies) {
					if (e.active)
						e.firePhaser();
				}
				countFire = FIRERATE / 2;
			} else {
				countFire--;
			}
		}
		
		if (state == LEVEL3) {
			
			
			if (borgCount <= BORGDELAY) {
				borgCount++;
				return;
			}
			
			
			for (Asteroid a : asts) {
				if (a.active) {
					// Check for collisions between player and asteroid
					if (a.collidesWith(ps) && a.active) {
						smallExplosion.play();
						ps.health -= 3.5;
						a.active = false;
					}
		
					// Check for collisions between phaser beam and asteroid
					for (Phaser p : ps.phaserBank) {
						if (a.collidesWith(p)) {
							a.active = false;
							p.active = false;
						}
					}
					
					// Check for collisions between torpedo and asteroid
					for (Torpedo t : ps.torpedoBank) {
						if (a.collidesWith(t)) {
							a.active = false;
							t.active = false;
						}
					}
					
					a.update();
				}
	
			}
			
			for (EnemyShip e : enemies) {
				if (e.active) {
					// Check for collision between enemy ship and player
					if (e.collidesWith(ps)) {
						ps.health -= 10;
						e.health -= 10;
					}
					
					// Check for collision between enemy phaser and player
					for (Phaser p : e.phaserBank) {
						if (p.active) {
							if (ps.collidesWith(p)) {
								ps.health -= 5.5;
								p.active = false;
							}
						}
					}
					
					// Check for collision between player weapons and enemy
					for (Phaser p : ps.phaserBank) {
						if (p.active) {
							if (e.collidesWith(p)) {
								e.health -= 3.5;
								p.active = false;
							}
						}
					}
					
					for (Torpedo t : ps.torpedoBank) {
						if (t.active) {
							if (e.collidesWith(t)) {
								e.health -= 8.5;
								t.active = false;
							}
						}
					}
					
					// Update enemies and check their health
					if (e.health <= 0) {
						largeExplosion.play();
						e.active = false;
					}
					
					e.update();
				}
				
			}
	
			ps.update();
			
			// If all enemies are dead, move to Level 3
			if (!enemies[0].active && !enemies[1].active && !enemies[2].active && !enemies[3].active) {
				playerWon = true;
				state = END;
				initialize();
				return;
			}
			
			if (lives <= 0) {
				state = END;
				initialize();
				return;
			}
			
			livesScore.updateValue(lives);
			playerHealth.updateValue(ps.health);
			
			if (ps.health <= 0) {
				largeExplosion.play();
				lives--;
				ps.health = 30;
				ps.x = WIDTH/2;
				ps.y = FIELDHEIGHT - 70;
			}
			
			
			
			// Enemy fire testing
			
			if (countFire <= 0) {
				for (EnemyShip e : enemies) {
					if (e.active)
						e.firePhaser();
				}
				countFire = FIRERATE / 2;
			} else {
				countFire--;
			}
		}
		
		if (state == END) {
			
		}
	}


	public void render(GraphicsContext gc) {
		if (state == TITLE) {
			// Draw background
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			
			// Draw starfield
			for (Star s : introStars) {
				s.render(gc);
			}
			
			// Draw Title Text
			gc.drawImage(starTrek, stX, 50);
			gc.drawImage(theGame, tgX, 150);
			gc.drawImage(author, aX, 250);
			gc.drawImage(play, WIDTH/2 - 100, pY);
			gc.drawImage(controls, WIDTH/2 - 100, cY);
			gc.drawImage(quit, WIDTH/2 - 100, qY);
			
			// Draw cursor
			cursor.render(gc);
			
		}
		
		if (state == CONTROLS) {
			// Draw background
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, WIDTH, HEIGHT);
		
			// Draw new starfield
			for (Star s : introStars) {
				s.render(gc);
			}
			
			gc.drawImage(controlTitle, WIDTH/2 - 120, 50);
			gc.drawImage(inst1,70, 150);
			gc.drawImage(inst2,90, 300);
			gc.drawImage(inst3,90, 450);
			gc.drawImage(back, WIDTH/2 - 180, HEIGHT-100);
			
			
		}
		
		if (state == LEVEL1) {
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
			
			// Draw health indicator 
			gc.setFill(Color.BLACK);
			gc.fillRect(WIDTH - 330, HEIGHT-40, 300, 20);
			gc.setFill(Color.RED);
			gc.fillRect(WIDTH - 325, HEIGHT-35, ps.health * 9.6, 10);
			
			
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
				gc.fillOval(WIDTH - 250 + (t * 50), FIELDHEIGHT + 30, 30, 30);
			}
			
			livesScore.render(gc);
			phaserLevel.render(gc);
			torpedoLevel.render(gc);
			playerHealth.render(gc);
		}
		
		if (state == LEVEL2) {
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
			
			// Draw health indicator 
			gc.setFill(Color.BLACK);
			gc.fillRect(WIDTH - 330, HEIGHT-40, 300, 20);
			gc.setFill(Color.RED);
			gc.fillRect(WIDTH - 325, HEIGHT-35, ps.health * 9.6, 10);
			
			
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
				gc.fillOval(WIDTH - 250 + (t * 50), FIELDHEIGHT + 30, 30, 30);
			}
			
			livesScore.render(gc);
			phaserLevel.render(gc);
			torpedoLevel.render(gc);
			playerHealth.render(gc);
		}
		
		if (state == LEVEL3) {
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
			
			// Draw health indicator 
			gc.setFill(Color.BLACK);
			gc.fillRect(WIDTH - 330, HEIGHT-40, 300, 20);
			gc.setFill(Color.RED);
			gc.fillRect(WIDTH - 325, HEIGHT-35, ps.health * 9.6, 10);
			
			
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
				gc.fillOval(WIDTH - 250 + (t * 50), FIELDHEIGHT + 30, 30, 30);
			}
			
			livesScore.render(gc);
			phaserLevel.render(gc);
			torpedoLevel.render(gc);
			playerHealth.render(gc);
		}
		
		if (state == END) {
			// Draw background
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, WIDTH, HEIGHT);
			
			// Draw starfield
			for (Star s : introStars) {
				s.render(gc);
			}
			
			if (playerWon) {
				gc.drawImage(congrats, 60, 200);
				gc.drawImage(youWin, WIDTH/2 - 110, HEIGHT/2);
			} else {
				gc.drawImage(youLose, WIDTH/2 - 130, HEIGHT/2 - 100);
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
