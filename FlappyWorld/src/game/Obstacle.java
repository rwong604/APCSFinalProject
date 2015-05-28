package game;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Obstacle {
	final private double pipeHeight = 320;
	final private double pipeWidth = 52;
	private double gap;
	private ImageView bottom = null;
	private ImageView top = null;
	private TranslateTransition transTransition;
	private TranslateTransition TransTransition;
	private double sceneHeight;
	private double sceneWidth;
	private boolean bound = true;
//	Rectangle test = new Rectangle(bottom.getX(),bottom.getY()-gap,pipeWidth,gap);
	
	public Obstacle(String n1, String n2, double gap) {
		String url = getClass().getResource(n1).toString();
		this.bottom = new ImageView(url);
		url = getClass().getResource(n2).toString();
		this.top = new ImageView(url);
		this.gap = gap;
	}
	
	public void movingGround(double sceneHeight, double sceneWidth){
		this.sceneHeight = sceneHeight;
		this.sceneWidth = sceneWidth;
		this.bottom.setLayoutX(425);
		this.bottom.setLayoutY(sceneHeight*0.9 - 100);
		this.top.setLayoutX(425);
		this.top.setLayoutY(bottom.getLayoutY() - pipeHeight - gap);
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
				if(t >= .7 && bound) {
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
		this.bottom.setLayoutY(sceneHeight*0.9 - (int) (Math.random()*200)-50); //(random * range of heights) - min height
		this.top.setLayoutY(bottom.getLayoutY() - pipeHeight - gap);

	}
	
	public ImageView getImageView1() {
		return this.bottom;
	}
	
	public ImageView getImageView2()  {
		return this.top;
	}
	
	public double getX1() {
		return bottom.layoutXProperty().doubleValue() + bottom.getTranslateX() + bottom.xProperty().doubleValue();
	}
	
	public double getX2() {
		return top.layoutXProperty().doubleValue() + top.getTranslateX() + top.xProperty().doubleValue();
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

	public void playFromStart() {
		transTransition.playFromStart();
		TransTransition.playFromStart();
		
	}
	
	
}

