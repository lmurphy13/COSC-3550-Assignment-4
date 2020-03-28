public class Sprite {
	double x, y;
	double dx, dy;

	// Finite state machine for movement
	final int GO_UP = 0;
	final int GO_DOWN = 1;
	final int GO_LEFT = 2;
	final int GO_RIGHT = 3;
	
	int state;

	boolean active = true;
	boolean visible = true;

	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
	}

	void update() {
		x += dx;
		y += dy;
	}
	
	void setVelocity(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	boolean isActive() {
		return active;
	}

	void suspend() {
		active = false; visible = false;
	}

	void resume() {
		active = true; visible = true;
	}
	
	void shootPhaser() {
		// definition depending on type of sprite
	}
	
}
