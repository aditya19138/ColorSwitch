package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class GameApp{

    private final Button newGameButton;
    private final Button resumeGameButton;
    private final Button exitGameButton;
    private static final GameApp instace = new GameApp();

    private GameApp(){
        newGameButton = new Button();
        resumeGameButton = new Button();
        exitGameButton = new Button();
    }

    public static GameApp getInstance(){
        return instace;
    }

    public void mainMenu(Stage window){

        FileInputStream inputStream= null;
        try {
            inputStream = new FileInputStream("Images\\playButton.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image img=new Image(inputStream);
        Circle circle=new Circle(250,220,60);
        circle.setFill(new ImagePattern(img));
        Group g1=getRotatingCircle(70,15,360,500);
        Group g2 = getRotatingCircle(90,20,-360,500);
        Group g3=getRotatingCircle(115,25,(720),800);

        Group icon=new Group();
        icon.getChildren().addAll(circle,g1,g2,g3);

        String path = "AudioFiles\\test.mp3";
        //Instantiating MediaPlayer class
        //MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File(path).toURI().toString()));
        //mediaPlayer.setAutoPlay(true);
        window.setTitle("Color switch");
        Ball ball = new Ball(Color.BLUE,630);
        Player user = new Player(0,ball);

        newGameButton.setText("New Game");

        resumeGameButton.setText("Resume Game");

        exitGameButton.setText("Exit Game");

        newGameButton.setTextFill(Color.WHITE);
        newGameButton.setFont(Font.font("times new roman"));
        resumeGameButton.setTextFill(Color.WHITE);
        exitGameButton.setTextFill(Color.WHITE);
        newGameButton.setStyle("-fx-background-color: gray;-fx-font-size:20");
        resumeGameButton.setStyle("-fx-background-color: gray;-fx-font-size:20");
        exitGameButton.setStyle("-fx-background-color: gray;-fx-font-size:20");

        Pane layout = new Pane();

        newGameButton.setLayoutX(190);
        newGameButton.setLayoutY(400);
        newGameButton.setPrefSize(130,50);

        layout.getChildren().add(icon);
        layout.getChildren().add( newGameButton);
        resumeGameButton.setLayoutX(170);
        resumeGameButton.setLayoutY(460);
        resumeGameButton.setPrefSize(170,50);

        layout.getChildren().add(resumeGameButton);
        exitGameButton.setLayoutX(190);
        exitGameButton.setLayoutY(520);
        exitGameButton.setPrefSize(130,50);

        layout.getChildren().add(exitGameButton);


        //actions
        newGameButton.setOnAction(e-> newGame(window,ball,user));
        circle.setOnMouseClicked(e-> newGame(window,ball,user));


        resumeGameButton.setOnAction(e-> {
            try {
                resumeMenu(window);
            } catch (IOException | ClassNotFoundException ioException) {
                System.out.println(ioException.getMessage());
            }
        });

        exitGameButton.setOnAction(e->exitGame());
        //Scene set
        Scene scene = new Scene(layout,500,650);
        Image img2=new Image(new File("Images\\bg.png").toURI().toString());
        BackgroundImage bg=new BackgroundImage(img2, BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        Background background=new Background(bg);
        layout.setBackground(background);
        window.setScene(scene);
        window.show();
    }

    public void newGame(Stage window, Ball ball,Player user){
        Game game = new Game(ball,user);
        game.gameplay(window);
    }

    public void resumeMenu(Stage window) throws IOException, ClassNotFoundException,FileNotFoundException {

        Label text = new Label("Saved Games");
        text.setTextFill(Color.GREEN);
        text.setLayoutX(250);
        text.setPrefSize(100,100);
        text.setAlignment(Pos.TOP_CENTER);

        VBox box = new VBox();
        box.setLayoutY(60);
        File file = new File("Database\\");
        String[] fileList = file.list();
        int pos = 30;
        AtomicReference<String> to_load= new AtomicReference<>("");
        Map<Button,String> list = new HashMap<Button, String>();
        box.setSpacing(10);
        assert fileList != null;

        for(int i=0;i<fileList.length;i++){
            Button button  = new Button((i+1)+") "+fileList[i]);
            button.setTextFill(Color.GREEN);
            button.setLayoutY(pos);
            button.prefWidth(120);
            button.prefHeight(40);
            button.setStyle("-fx-background-color: black;-fx-font-size:12");
            box.getChildren().add(button);

            list.put(button,fileList[i]);
            pos+=40;
        }
        Button clearData  = new Button("Clear Data");
        ImageView img = new ImageView(new Image("file:Images\\dustbin.jpg"));
        img.setFitHeight(80);
        img.setFitWidth(80);
        img.setLayoutX(20);
        img.setLayoutY(20);
        img.setPreserveRatio(true);
        clearData.setLayoutX(510);
        clearData.setLayoutY(520);
        clearData.setGraphic(img);
        clearData.setPadding(Insets.EMPTY);
        clearData.setPrefSize(80,80);
        clearData.setStyle("-fx-background-color: black");

        Button goBack = new Button("Go back");
        goBack.setLayoutX(30);
        goBack.setLayoutY(500);

        Group main= new Group(text, box,clearData,goBack);
        Scene scene = new Scene(main,600,600);
        scene.setFill(Color.BLACK);
        window.setScene(scene);
        window.show();


                for (Button button : list.keySet()){
                    button.setOnAction(e->{
                        Game game;
                        ObjectInputStream read = null;
                        try {
                            read = new ObjectInputStream(new FileInputStream("Database\\"+list.get(button)));
                            game = (Game)read.readObject();
                            game.loadGame(game,window);
                        } catch (IOException | ClassNotFoundException ioException) {
                            ioException.printStackTrace();
                        }
                    });
                }
                clearData.setOnAction(e->{
                    for (String name : list.values()){
                        File myObj = new File("Database\\"+name);
                        if(myObj.delete())
                            System.out.println("DataBase cleared");
                        else
                            System.out.println("DataBase Empty");
                    }
                    mainMenu(window);
                });

                goBack.setOnAction(e->mainMenu(window));
    }

    public void exitGame(){
        System.exit(0);
    }

    private Arc getArc(int radius,int startAngle,int width,Color color){
        Arc arc1=new Arc();
        arc1.setCenterX(250);
        arc1.setCenterY(220);
        arc1.setRadiusX(radius);
        arc1.setRadiusY(radius);
        arc1.setStartAngle(startAngle);
        arc1.setLength(90);
        arc1.setType(ArcType.OPEN);
        arc1.setFill(Color.TRANSPARENT);
        arc1.setStroke(color);
        arc1.setStrokeWidth(width);

        return arc1;


    }
    public Group getRotatingCircle(int radius,int width,int rotateAngle,int time){
        Arc arc1=this.getArc(radius,0,width,Color.RED);
        Arc arc2=this.getArc(radius,90,width,Color.YELLOW);
        Arc arc3=this.getArc(radius,180,width,Color.BLUE);
        Arc arc4=this.getArc(radius,270,width,Color.GREEN);

        Group root=new Group();
        root.getChildren().addAll(arc1,arc2,arc3,arc4);

        RotateTransition rotate = new RotateTransition();
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle(rotateAngle);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setRate(0.1);
        rotate.setDuration(Duration.millis(time));
        rotate.setNode(root);
        rotate.play();

        return root;

    }
}
