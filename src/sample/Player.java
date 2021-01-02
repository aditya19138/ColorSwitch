package sample;

import java.io.Serializable;

public class Player implements Serializable {
    private int score;
    private final Ball ball;

    public Player(int score, Ball ball){
        this.ball = ball;
        this.score = score;
    }

    public void decreaseScore(int score){
        this.score = this.score - score;
    }

    public void setScore(){
        this.score = this.score + 1;
    }

    public int getScore(){
        return this.score;
    }
}
