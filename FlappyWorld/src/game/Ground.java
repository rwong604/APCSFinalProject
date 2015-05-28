package game;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Ground {

	private ImageView ground = null;
	TranslateTransition transTransition;

	
	public Ground(String name) {
		String url = getClass().getResource(name).toString();
		this.ground = new ImageView(url);
	}
	
	public void movingGround(double sceneHeight, double sceneWidth){
		if(Main.masterLevel){
			transTransition = new TranslateTransition(new Duration(1900), this.ground);
		}else{
			transTransition = new TranslateTransition(new Duration(2500), this.ground);

		}
		this.ground.setLayoutX(0);
		this.ground.setLayoutY(sceneHeight*0.9);
		this.ground.setFitWidth(sceneWidth*2);
		transTransition.setToX(-sceneHeight);
		transTransition.setInterpolator(Interpolator.LINEAR);
		transTransition.setCycleCount(Timeline.INDEFINITE);
		
	}
	
	public void play(){
		transTransition.play();
	}
	public ImageView getImageView() {
		return this.ground;
	}
	
	public void stop() {
		transTransition.stop();
	}
}
