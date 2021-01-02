package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Game implements Serializable {

    Ball ball;
    Player user;
    transient Group[] Nodes;
    List<Integer> arr;
    double[] y;
    double star_pos;
    int last;
    double[] switcher_pos;
    int ring;
    int rect;
    int line;
    String record;
    transient ImageView view;
    transient Group colorSwitcher;
    transient Image image;
    double circle_angle;
    double rect_angle;
    double line_angle;
    transient RotateTransition rotate_circle;
    transient RotateTransition rotate_rect;
    transient TranslateTransition rotate_line;
    double circle_speed;
    double rect_speed;
    transient AudioClip bgClip;
    Boolean playing;

    public Game(Ball ball, Player user) throws NullPointerException {
        this.ball = ball;
        this.user = user;
        this.image = new Image("file:Images\\star.png");
        this.Nodes = new Group[3];
        this.y = new double[4];
        this.arr = new ArrayList<>();
        Nodes[0] = new CircleObstacle().getElement();//0
        ring = 0;
        Nodes[1] = new RectangleObstacle().getElement();//1D
        rect = 1;
        Nodes[1].setLayoutY(-230);
        this.switcher_pos = new double[2];
        colorSwitcher = new colorSwitch().getElement();
        colorSwitcher.setLayoutY(Nodes[rect].getLayoutY() - 200);
        Nodes[2] =  new LineObstacle().getElement();//3
        Nodes[2].setLayoutY(-720);
        line = 2;
        arr.add(0);
        arr.add(1);
        arr.add(2);
        record = "line";
        this.star_pos = 265;
        this.last = 2;
        this.view = new ImageView(image);
        view.setLayoutX(215);
        view.setFitWidth(70);
        view.setFitHeight(70);
        view.setLayoutY(star_pos);

        rotate_circle = new CircleObstacle().doRotate(Nodes[ring],400);
        rotate_rect = new CircleObstacle().doRotate(Nodes[rect],400);
        rotate_line = new LineObstacle().doRotateLine(Nodes[line]);
    }

    public void gameplay(Stage window) {

        bgClip=new AudioClip(new File("AudioFiles\\bg_music.mp3").toURI().toString());
        bgClip.play(0.2);
        playing=true;
        AudioClip jumpAudio=new AudioClip(new File("AudioFiles\\jump.wav").toURI().toString());
        AudioClip starAudio=new AudioClip(new File("AudioFiles\\star.wav").toURI().toString());
        AudioClip deadAudio=new AudioClip(new File("AudioFiles\\dead.wav").toURI().toString());
        AudioClip pauseAudio=new AudioClip(new File("AudioFiles\\pause.wav").toURI().toString());

        ImageView pauseView = new ImageView(new Image("file:Images\\Pause.png"));
        pauseView.setFitHeight(50);
        pauseView.setFitWidth(50);
        pauseView.setLayoutX(20);
        pauseView.setLayoutY(20);
        pauseView.setPreserveRatio(true);

        Button pauseButton = new Button();
        pauseButton.setGraphic(pauseView);
        pauseButton.setPadding(Insets.EMPTY);
        pauseButton.setLayoutX(15);
        pauseButton.setLayoutY(15);

        Button bg_color = new Button("c");
        bg_color.setLayoutX(450);
        bg_color.setLayoutY(620);
        bg_color.setPrefSize(30,30);
        Stage pauseOptions = new Stage();
        Stage gameOver = new Stage();


        rotate_line.play();
        rotate_rect.play();
        rotate_circle.play();

        AnimationTimer gravity = ball.applyG();

        Group main = new Group();

        for (Group node : this.Nodes) {
            main.getChildren().add(node);
        }
        main.getChildren().add(colorSwitcher);



        Label score = new Label();

        final Node[] temp = {this.Nodes[last]};
        final boolean[] clicked = {false};

        score.setTextFill(Color.WHITE);
        score.setLayoutX(10);
        score.setLayoutY(630);
        main.getChildren().add(score);
        main.getChildren().add(pauseButton);
        main.getChildren().add(bg_color);
        main.getChildren().add(this.ball.getBall());
        main.getChildren().add(view);

        ArrayList<Color> colors = new ArrayList<>();
        colors.add(Color.DARKCYAN);
        colors.add(Color.DARKGRAY);
        colors.add(Color.DARKORANGE);
        colors.add(Color.DARKKHAKI);
        Scene scene = new Scene(main, 500, 650);



        bg_color.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int r = (int) (Math.random() * 4);
                scene.setFill(colors.get(r));
            }
        });


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(!bgClip.isPlaying() && playing)
                    bgClip.play(0.2);

                if (ball.getBall().getCenterY() > 630) {
                    ball.getBall().setCenterY(630);
                    clicked[0] = false;
                    gravity.stop();
                } else if (ball.getBall().getCenterY() < 345) {
                    view.setLayoutY(view.getLayoutY() + 2);
                    colorSwitcher.setLayoutY(Nodes[rect].getLayoutY() - 200);
                    switcher_pos[1] = colorSwitcher.getLayoutY();

                    for (int i = 0; i < Nodes.length; i++) {

                        Nodes[i].setLayoutY(Nodes[i].getLayoutY() + 2);

                        if (Nodes[i].getBoundsInParent().getMinY() >= 650) {
                            arr.remove(Integer.valueOf(i));
                            arr.add(i);

                            if (temp[0] == Nodes[rect]) {
                                Nodes[i].setLayoutY(temp[0].getLayoutY() - 600);
                                rotate_rect.stop();
                               // System.out.println(rotate_rect.getDuration().toMillis());
                                if(rotate_rect.getDuration().toMillis()!=10)
                                    rotate_rect.setDuration(rotate_rect.getDuration().subtract(Duration.millis(30)));
                                rotate_rect.setNode(Nodes[rect]);
                                rotate_rect.play();
                            } else if (temp[0] == Nodes[line]) {
                                Nodes[i].setLayoutY(temp[0].getLayoutY() - 560);
                            } else {
                                Nodes[i].setLayoutY(temp[0].getLayoutY() - 400);
                                rotate_circle.stop();
                                if(rotate_circle.getDuration().toMillis()!=10)
                                    rotate_circle.setDuration(rotate_circle.getDuration().subtract(Duration.millis(30)));
                                rotate_circle.setNode(Nodes[ring]);
                                rotate_circle.play();
                            }

                            if (i == rect) {
                                colorSwitcher.setLayoutY(Nodes[rect].getLayoutY() - 100);
                                colorSwitcher.setLayoutX(0);
                                switcher_pos[0] = 0;
                            }

                            if (i == ring) {
                                temp[0] = Nodes[ring];
                                record = "ring";
                            } else if (i == rect) {
                                temp[0] = Nodes[rect];
                                record = "rect";
                            } else if (i == line) {
                                temp[0] = Nodes[line];
                                record = "line";
                            }
                            last = i;
                        } else if (ball.getBall().intersects(colorSwitcher.getBoundsInParent())) {

                            colorSwitcher.setLayoutX(100000);
                            switcher_pos[0] = 100000;
                            Color color = ball.getRandomColor(ball.getColor());
                            ball.setColor(color);

                            //colorswitch collected
                        }

                        if (ball.getBall().intersects(view.getBoundsInParent())) {
                            starAudio.play();
                            view.setLayoutY(view.getLayoutY() - 600);
                            star_pos = view.getLayoutY();
                            if(ball.getColor()!=Color.WHITE)
                                user.setScore();
                            //star collected
                        }

                        ball.getBall().setCenterY(ball.getBall().getCenterY() + 0.5);
                    }
                }

                ball.getBall().setFill(ball.getColor());
                score.setText("Score: " + user.getScore());
                switcher_pos[1] = colorSwitcher.getLayoutY();
                y[0] = Nodes[ring].getLayoutY();
                y[1] = Nodes[rect].getLayoutY();
                y[2] = Nodes[line].getLayoutY();
                circle_angle = Nodes[ring].getRotate();
                rect_angle = Nodes[rect].getRotate();
                line_angle = Nodes[line].getRotate();
                rect_speed = rotate_rect.getDuration().toMillis();
                circle_speed = rotate_circle.getDuration().toMillis();
            }
        };

        score.setText("Score: " + user.getScore());

        AnimationTimer collision  = new AnimationTimer() {
            @Override
            public void handle(long l) {
                for (Group node : Nodes) {
                    for (Node child : node.getChildren()) {
                        Shape intersect;
                        intersect = Shape.intersect(ball.getBall(), (Shape) child);
                        if (intersect.getBoundsInParent().getWidth() != -1) {
                          //  System.out.println(ball.getColorCode()+" "+child.getId());
                            if (!ball.getColorCode().equals(child.getId()) && ball.getColor()!=Color.WHITE) {
                                //song
                                bgClip.stop();
                                playing=false;
                                deadAudio.play();
                                gravity.stop();
                                rotate_rect.stop();
                                rotate_line.stop();
                                rotate_circle.stop();
                                timer.stop();
                                this.stop();
                                circle_angle = Nodes[ring].getRotate();
                                rect_angle = Nodes[rect].getRotate();
                                switcher_pos[0] = colorSwitcher.getLayoutX();
                                switcher_pos[1] = colorSwitcher.getLayoutY();
                                try {
                                    gameOver(window,gameOver);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //gameover loop
                            }
                        }
                    }
                }
            }
        };

        collision.start();


        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                ball.moveUp();

                if (jumpAudio.isPlaying())
                    jumpAudio.stop();
                jumpAudio.play();

                if (!clicked[0]) {
                    gravity.start();
                    clicked[0] = true;
                    timer.start();
                }
            }
        });

        scene.setOnKeyPressed(e->{

            KeyCode keyCode = e.getCode();
            if (keyCode.equals(KeyCode.Q)) {
                bgClip.stop();
                pauseAudio.play();
                timer.stop();
                gravity.stop();
                rotate_rect.stop();
                rotate_line.stop();
                rotate_circle.stop();
                star_pos = view.getLayoutY();
                collision.stop();
                circle_angle = Nodes[ring].getRotate();
                rect_angle = Nodes[rect].getRotate();
                line_angle = Nodes[line].getTranslateX();
                try {
                    pauseMenu(pauseOptions, window, ball, user, this);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        scene.setFill(Color.BLACK);
        window.setScene(scene);
        window.show();
    }

    public void gameOver(Stage window, Stage gameOver) throws IOException {
        try {
            gameOver.initModality(Modality.APPLICATION_MODAL);
            gameOver.setTitle("Pause Menu");
        }
        catch(Exception e){
            gameOver.setTitle("Pause Menu");
        }


        FileReader reader = new FileReader(new File("HighScore.txt"));
        int bestScore = (int)reader.read();
        reader.close();

        if(bestScore<this.user.getScore()) {
            FileWriter myWriter = new FileWriter(new File("HighScore.txt"));
            myWriter.write(this.user.getScore());
            bestScore=this.user.getScore();
            myWriter.close();
        }

        Label highscore = new Label("High Score: "+String.valueOf(bestScore));
        highscore.setLayoutY(280);
        highscore.setLayoutX(230);

        gameOver.setMinWidth(250);
        Label text = new Label("Your Score: "+user.getScore());
        text.setAlignment(Pos.TOP_LEFT);
        Button resumeGame = new Button("Resume Game");
        Button restartGame = new Button("Restart Game");
        Button exitGame = new Button("Exit to Main menu");
        resumeGame.setLayoutX(90);
        resumeGame.setPrefWidth(180);
        resumeGame.setLayoutY(60);
        restartGame.setLayoutX(90);
        restartGame.setPrefWidth(180);
        restartGame.setLayoutY(80);
        exitGame.setLayoutX(90);
        exitGame.setPrefWidth(180);
        exitGame.setLayoutY(100);

        exitGame.setOnAction(e -> {
            try {
                gameOver.close();
                GameApp app = GameApp.getInstance();
                app.mainMenu(window);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });


        if(this.user.getScore()<5) {
            resumeGame.setText("Not enought Score to resume");
            resumeGame.setDisable(true);
        }

        else{
            ball.setColor(Color.WHITE);
            user.decreaseScore(5);
        }

        resumeGame.setOnAction(e->{
            resumeGame(window,gameOver);
        });

        restartGame.setOnAction(e ->restartGame(window,gameOver));

        VBox box = new VBox();
        box.getChildren().addAll(text,resumeGame, restartGame, exitGame,highscore);box.setSpacing(30);box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box, 500, 700);
        window.setTitle("Game Over");
        window.setScene(scene);
        window.show();
    }
    public void pauseMenu(Stage pauseOptions, Stage window, Ball ball, Player user, Game game) throws IOException {

        try {
            pauseOptions.initModality(Modality.APPLICATION_MODAL);
            pauseOptions.setTitle("Pause Menu");
        }
        catch(Exception io){
            pauseOptions.setTitle("Pause Menu");
        }

        pauseOptions.setMinWidth(250);
        Button resumeGame = new Button("Resume Game");
        Button saveGame = new Button("Save Game");
        Button restartGame = new Button("Restart Game");
        Button exitGame = new Button("Exit to Main Menu");
        resumeGame.setLayoutX(90);
        resumeGame.setPrefWidth(130);
        resumeGame.setLayoutY(80);
        saveGame.setLayoutX(90);
        saveGame.setPrefWidth(130);
        saveGame.setLayoutY(130);
        restartGame.setLayoutX(90);
        restartGame.setPrefWidth(130);
        restartGame.setLayoutY(160);
        exitGame.setLayoutX(90);
        exitGame.setPrefWidth(130);
        exitGame.setLayoutY(210);

        resumeGame.setOnAction(e ->resumeGame(window,pauseOptions));

        saveGame.setOnAction(e -> {
            pauseOptions.close();
            try {
                saveGame(game);
                pauseOptions.close();
                GameApp.getInstance().mainMenu(window);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        exitGame.setOnAction(e -> {
            try {
                pauseOptions.close();
                GameApp.getInstance().mainMenu(window);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        restartGame.setOnAction(e ->restartGame(window,pauseOptions));

        Pane box = new Pane();
        box.getChildren().addAll(resumeGame, saveGame, restartGame, exitGame);
        Scene scene = new Scene(box, 300, 300);
        pauseOptions.setScene(scene);
        pauseOptions.show();
    }

    public void resumeGame(Stage window,Stage Options){
        Options.close();
        this.Nodes[line] = new LineObstacle().getElement();
        rotate_line = new LineObstacle().doRotateLine(Nodes[line]);
        this.Nodes[line].setLayoutY(y[2]);
        this.Nodes[line].setTranslateX(line_angle);
        gameplay(window);
    }

    public void restartGame(Stage window, Stage pauseOptions){

        pauseOptions.close();

            this.ball = new Ball(Color.BLUE, 630);
            this.user = new Player(0, ball);
            this.Nodes = new Group[3];
            this.y = new double[4];
            this.arr = new ArrayList<>();
            Nodes[0] = new CircleObstacle().getElement();//0
            ring = 0;
            Nodes[1] = new RectangleObstacle().getElement();//1
            rect = 1;
            Nodes[1].setLayoutY(-230);
            colorSwitcher = new colorSwitch().getElement();
            colorSwitcher.setLayoutY(Nodes[rect].getLayoutY() - 200);
            Nodes[2] = new LineObstacle().getElement();//3
            Nodes[2].setLayoutY(-720);
            line = 2;
            arr.add(0);
            arr.add(1);
            arr.add(2);
            this.star_pos = 265;
            this.last = 2;

            this.view.setLayoutY(star_pos);

        rotate_circle = new CircleObstacle().doRotate(Nodes[ring], 400);
        rotate_rect = new CircleObstacle().doRotate(Nodes[rect], 400);
        rotate_line = new LineObstacle().doRotateLine(Nodes[line]);

        circle_angle = Nodes[ring].getRotate();
        rect_angle = Nodes[rect].getRotate();

        gameplay(window);
    }

    public void saveGame(Game game) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        String filename = dtf.format(now)+"  Score- "+game.user.getScore();
        FileOutputStream out = new FileOutputStream("Database\\"+filename);
        ObjectOutputStream write = new ObjectOutputStream(out);

        write.writeObject(game);
    }

    public void loadGame(Game game, Stage window) {

        this.Nodes = new Group[3];
        this.colorSwitcher = new colorSwitch().getElement();

        switch (game.ball.getColorString()) {
            case "B":
                this.ball = new Ball(Color.BLUE,game.ball.getPos());
                break;
            case "G":
                this.ball = new Ball(Color.GREEN,game.ball.getPos());
                break;
            case "R":
                this.ball = new Ball(Color.RED, game.ball.getPos());
                break;
            case "Y":
                this.ball = new Ball(Color.YELLOW, game.ball.getPos());
                break;
        }

        this.user = game.user;

        Iterator<Integer> iter = game.arr.iterator();

       while(iter.hasNext()) {

           int i = (Integer)iter.next();
           if (i == 0) {
                this.Nodes[i] = new CircleObstacle().getElement();
                this.Nodes[i].setLayoutY(game.y[0]);
                this.Nodes[i].setRotate(game.circle_angle);
                this.ring = i;
            } else if (i == 1) {
                this.Nodes[i] =  new RectangleObstacle().getElement();
                this.Nodes[i].setLayoutY(game.y[1]);
                this.Nodes[i].setRotate(game.rect_angle);
                this.rect = i;
            } else if (i == 2) {
                this.Nodes[i] =  new LineObstacle().getElement();
                this.Nodes[i].setLayoutY(game.y[2]);
                this.line = i;
            }
        }

        switch (record) {
            case "rect":
                this.last = rect;
                break;
            case "ring":
                this.last = ring;
                break;
            case "line":
                this.last = line;
                break;
        }

        this.colorSwitcher.setLayoutY(game.switcher_pos[1]);
        this.colorSwitcher.setLayoutX(game.switcher_pos[0]);

        this.image = new Image("file:Images\\star.png");
        this.view = new ImageView(image);

        this.view.setLayoutX(215);
        this.view.setFitWidth(70);
        this.view.setFitHeight(70);
        this.view.setLayoutY(game.star_pos);

        rotate_circle = new CircleObstacle().doRotate(Nodes[ring],circle_speed);
        rotate_rect = new RectangleObstacle().doRotate(Nodes[rect],rect_speed);
        rotate_line = new LineObstacle().doRotateLine(Nodes[line]);

        gameplay(window);
    }
}