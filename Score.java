/**
 * Assignment 2
 * 
 * @author Liam Murphy
 * 
 * Score.java
 */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Score {
	double x;
	double y;
	int value;
	String text = "";
	
	Font font;
	
	public Score(double x, double y, String text, int val) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.value = val;
		font = Font.font("SansSerif", FontWeight.BOLD, 18);
		
	}
	
	public void render(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.setFont(font);
		gc.fillText(text + Integer.toString(this.value), this.x, this.y);
	}
	
	public void updateValue(int v) {
		this.value = v;
	}
	
	public void reset() {
		this.value = 0;
	}

}
