package game;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Obstacle {

	private ImageView bottom = null;
	private ImageView top = null;
	private TranslateTransition transTransition;
	private TranslateTransition TransTransition;
	private double gap = 130; //distance between the two pipes 
	private int a = -1;
	private int clicks = 0;
	private double sceneHeight;
	private double sceneWidth;
	private boolean bound = true;

	
	public Obstacle(String n1, String n2) {
		String url = getClass().getResource(n1).toString();
		this.bottom = new ImageView(url);
		url = getClass().getResource(n2).toString();
		this.top = new ImageView(url);
		
	}
	
	public void movingGround(double height, double width){
		this.sceneHeight = height;
		this.sceneWidth = width;
		this.bottom.setLayoutX(425);
		this.bottom.setLayoutY(sceneHeight*0.9 - 100);
		this.top.setLayoutX(425);
		this.top.setLayoutY(bottom.getLayoutY() - 320 - gap); //bottomY - pipe height - gap
		transTransition = new TranslateTransition(new Duration(2500), this.bottom);
		TransTransition  = new TranslateTransition(new Duration(2500), this.top);
		TransTransition.setToX(-sceneWidth - 75);
		TransTransition.setInterpolator(new Interpolator() {
			@Override
			protected double curve(double t) {
				if(t == 1) {
					bound = true;
					random();
				}
//				System.out.println(t);
				if(t >= .6 && bound && t != 1) {
					bound = false;
					Main.score += 1;
				}
				return t;
			}
		});
		TransTransition.setCycleCount(Timeline.INDEFINITE);
		transTransition.setToX(-sceneWidth - 75);
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.setCycleCount(Timeline.INDEFINITE);
	}
	
	public void random() {
		this.bottom.setLayoutY(sceneHeight*0.9 - (50 +  a *((double) Math.random() * 150))); // random * range + minimum
		if(clicks % 2 == 0) {
			a *= -1;
		}
		clicks++;
		this.top.setLayoutY(bottom.getLayoutY() - 320 - gap); //bottomY - pipe height - gap

	}
	
	public ImageView getImageView1() {
		return this.bottom;
	}
	
	public ImageView getImageView2()  {
		return this.top;
	}
	
	public double getX1() {
		return bottom.layoutXProperty().doubleValue() + bottom.getTranslateX(); // + bottom.xProperty().doubleValue();
	}
	
	public double getX2() {
		return top.layoutXProperty().doubleValue() + top.getTranslateX();// + top.xProperty().doubleValue();
	}
	
	public double getY1() {
		return bottom.layoutYProperty().doubleValue();// + bottom.getTranslateY() + bottom.yProperty().doubleValue();
	}
	
	public double getY2() {
		return top.layoutYProperty().doubleValue();// + top.getTranslateY() + top.yProperty().doubleValue();
	}
	
	public void play(){
		TransTransition.play();
		transTransition.play();
	}
	
	public void stop() {
		transTransition.stop();
		TransTransition.stop();
	}
	
	
}

