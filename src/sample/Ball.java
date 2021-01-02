package sample;


import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Ball implements Serializable {
    private transient Color color;
    private transient Circle ball;
    private String color_s;
    private double y_pos;
    double v = 0;
    double g = 0.2f;
    private final transient ArrayList<Color> color_list = new ArrayList<Color>();

    public Ball(Color color,double y_pos){
        this.color = color;
        this.y_pos = y_pos;
        color_list.add(Color.RED);
        color_list.add(Color.BLUE);
        color_list.add(Color.GREEN);
        color_list.add(Color.YELLOW);
        ball = new Circle(250,y_pos,10);
        ball.setFill(this.color); //ball
        color_s = getColorCode();
    }

    public double getPos(){
        return y_pos;
    }

    public AnimationTimer applyG(){

        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                v+=g;
                ball.setCenterY(ball.getCenterY() + v - 0.5);
                y_pos = ball.getCenterY();
            }
        };
    }
    public void moveUp(){
        v=-4.15;
        this.ball.setCenterY(ball.getCenterY()-v);
        y_pos = this.ball.getCenterY();
    }

    public Circle getBall(){
        return this.ball;
    }

    public Color getColor(){return this.color; }

    public void setColor(Color color){
        this.color = color;
        color_s = getColorCode();
    }

    public Color getRandomColor(Color col){

        if(col==Color.WHITE){
            int a = (int)(Math.random()*4);
            return color_list.get(a);
        }

        int r = (int)(Math.random()*4);
        Color temp = color_list.get(r);

        while(temp==col){
            r = (int)(Math.random()*4);
            temp = color_list.get(r);
        }

        return temp;
    }
    public String getColorString(){
        return this.color_s;
    }

    public String getColorCode(){

        if(this.color==Color.BLUE){return "B";}
        else if(this.color==Color.RED){return "R";}
        else if(this.color==Color.YELLOW){return "Y";}
        else if(this.color==Color.GREEN){return "G";}

        return "none";
    }
}
