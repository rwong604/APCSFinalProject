package game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private Group root = null;
	private ImageView bkgrd = null ;
	private ImageView flappy = null;
	private ImageView clickRun = new ImageView("clickrun.png");
	private ImageView instruct = new ImageView("instructions.png");
	private ImageView getReady = new ImageView("getready.png");
	private ImageView gameOver = new ImageView("gameover.png");
	private Button	reset = null;
	private Text text = null;

	private Ground ground = null;
	private Obstacle pipe = new Obstacle("/obstacle_bottom.png", "/obstacle_top.png", 105);
	private String url = getClass().getResource("/flappy.png").toString();


	protected static int score = 0;
	private int i = 0;
	final private double pipeHeight = 320;
	final private double pipeWidth = 52;
	final private double gap=100;

	static double g = 300;
	static final double boostV = -150;
	static final double sceneWidth=400;
	static final double sceneHeight=400;
	static final double start_x = 150, start_y = 150; // starting y position
	static final double max_y = sceneHeight*0.9-23; // end position
	static double v, duration, range;

	static Timeline timeline;
	//	static TranslateTransition transTransition;
	static Interpolator interpolator;
	static boolean endGame;
	static boolean pass;
	
	private double calcTime(double distance, double velocity){
		return ((-velocity+Math.sqrt(velocity*velocity+(2*g*distance)))/g);
	}




	private void addActionEventHandler(){ //this is a mess
		reset.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle (ActionEvent event){
				endGame = false;
				root.getChildren().removeAll(gameOver,reset);
				timeline.playFromStart();
				ground.playFromStart();
				pipe.playFromStart();
				addMouseEventHandler();
			}
		});
	}
	
	private void checkLocation(){
		double threshold = 8;
		if(flappy.getY()>=max_y-threshold || flappy.intersects(pipe.getX1(), pipe.getY1(), pipeWidth, pipeHeight) 
				|| flappy.intersects(pipe.getX2(), pipe.getY2(), pipeWidth, pipeHeight) ){ // end game when hits bottom or obstacle
			endGame=true;
			root.getChildren().add(reset);
			addActionEventHandler();
		}
			Rectangle test = new Rectangle(pipe.getX1()+pipeWidth,pipe.getY1()-gap,0,gap);
			if(flappy.intersects(test.boundsInLocalProperty().get())){
				pass = true;
				System.out.println(pass);
			}
	}
	
	
	private void interpolator(){
		interpolator = new Interpolator(){
			@Override
			protected double curve (double t){
				checkLocation();
				root.getChildren().remove(text);
				text = new Text(Integer.toString(score));
				root.getChildren().add(text);

				//				text.setLayoutX(20);
				//				text.setLayoutY(50);
				//				text.setFont(Font.font ("Verdana", 36));
				//				Rectangle rect = new Rectangle();
				//				rect.setLayoutX(20);
				//				rect.setLayoutY(20);
				//				if(score >= 10 && score < 100) {
				//					rect.setWidth(50);
				//				} else if(score >= 100) {
				//					rect.setWidth(100);
				//				} else {
				//					rect.setWidth(23);
				//				}
				//				rect.setHeight(35);
				//				rect.fillProperty().set(Color.rgb(128, 185, 200));
				//
				//				if(i > 0) {
				//					root.getChildren().remove(text);
				//					root.getChildren().remove(rect);
				//					root.getChildren().add(rect);
				//				}
				//				root.getChildren().add(text);
				//				i++;
				if (flappy.getY()<=10 && endGame){ //if hits top, go to free fall
					range=max_y-flappy.getY();
					v=0;
					duration = calcTime(range,v);
				}
				if(endGame) {
					animationStop();
				}
				double time = t * duration;
				double distance = (v*time)+(0.5*g*time*time);	
				return distance/range;
			}
		};
	}

	public void animationStop() {
		timeline.stop();
		ground.stop();
		pipe.stop();
		root.getChildren().add(gameOver);
	}


	private void addMouseEventHandler(){
		root.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
			int n=0;
			@Override
			public void handle(MouseEvent event) {
				n++;
				if (n==1){
					root.getChildren().remove(clickRun);
					root.getChildren().addAll(instruct,getReady);
				}
				else{
					root.getChildren().removeAll(instruct,getReady);
					if(timeline!=null){
						timeline.stop();
					}
					if(!endGame){
						ground.play();
						pipe.play();
						range = max_y-flappy.getY();
						v=boostV;	
						duration = calcTime(range,boostV);
						timeline = new Timeline();
						KeyValue kv = new KeyValue(flappy.yProperty(),max_y, interpolator);
						final KeyFrame kf = new KeyFrame(Duration.millis(duration * 1000), kv);	
						timeline.getKeyFrames().add(kf);
						timeline.play();
					}
				}
			}
		});
	}



	@Override
	public void start(Stage primaryStage) throws Exception {
		//TODO 1: add background
		ground = new Ground("/ground.png");
		ground.movingGround(sceneHeight, sceneWidth);
		bkgrd = new ImageView("background2.png");
		clickRun.setLayoutX(sceneWidth/5);
		clickRun.setLayoutY(sceneHeight/5);
		getReady.setLayoutX(sceneWidth/4);
		getReady.setLayoutY(sceneHeight/5);
		instruct.setLayoutX(sceneWidth/3.5);
		instruct.setLayoutY(sceneHeight/2);
		gameOver.setLayoutX(sceneWidth/5);
		gameOver.setLayoutY(sceneHeight/5);

		reset = new Button("Play Again");
		reset.setLayoutX(sceneWidth*0.8);
		reset.setLayoutY(sceneWidth*0.9);

		text = new Text(Integer.toString(score));
		text.setLayoutX(20);
		text.setLayoutY(50);
		text.setFont(Font.font ("Verdana", 36));
		text.setFill(Color.BLACK);

		//TODO 2: add Flappy
		flappy = new ImageView(url);
		flappy.preserveRatioProperty().set(true);
		flappy.xProperty().set(start_x);
		flappy.yProperty().set(start_y);


		//Create a Group 
		root = new Group( );
		root.getChildren().addAll(bkgrd, flappy, clickRun,text);
		root.getChildren().addAll(ground.getImageView(), pipe.getImageView1(), pipe.getImageView2());

		//TODO 5: add mouse handler to the scene
		addMouseEventHandler();
		interpolator();
		pipe.movingGround(sceneHeight, sceneWidth);


		//Create scene and add to stage
		Scene scene = new Scene(root, sceneWidth, sceneHeight);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}



