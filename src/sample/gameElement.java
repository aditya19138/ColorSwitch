package sample;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.io.Serializable;

abstract class GameElement implements Serializable {
    Group element;

    public final Group getElement(){
        element=getShape();
        return element;

    }
    public Line makeLine(int startX,int startY,int endX,int endY,int thick,Color color,String id ){
        Line line =new Line(startX,startY,endX,endY);
        line.setId(id);
        line.setStrokeWidth(thick);
        line.setStroke(color);
        return line;
    }

    public Arc makeArc(int radius, int startAngle, int width, Color color,String id) {
        Arc arc1 = new Arc();
        arc1.setCenterX(250);
        arc1.setCenterY(300);
        arc1.setRadiusX(radius);
        arc1.setRadiusY(radius);
        arc1.setStartAngle(startAngle);
        arc1.setLength(90);
        arc1.setType(ArcType.OPEN);
        arc1.setFill(javafx.scene.paint.Color.TRANSPARENT);
        arc1.setStroke(color);
        arc1.setStrokeWidth(width);
        arc1.setId(id);

        return arc1;
    }

    abstract Group getShape();


}

class LineObstacle extends GameElement{
    @Override
    public Group getShape(){

        Line line1=makeLine(0,200,175,200,20,Color.RED,"R");
        Line line2=makeLine(175,200,350,200,20,Color.BLUE,"B");
        Line line3=makeLine(350,200,525,200,20,Color.YELLOW,"Y");
        Line line4=makeLine(525,200,700,200,20,Color.GREEN,"G");
        Line line5=makeLine(-700,200,-525,200,20,Color.RED,"R");
        Line line6=makeLine(-525,200,-350,200,20,Color.BLUE,"B");
        Line line7=makeLine(-350,200,-175,200,20,Color.YELLOW,"Y");
        Line line8=makeLine(-175,200,0,200,20,Color.GREEN,"G");
        Line line9=makeLine(0,200,175,200,20,Color.RED,"R");
        Line line10=makeLine(175,200,350,200,20,Color.BLUE,"B");
        Line line11=makeLine(350,200,525,200,20,Color.YELLOW,"Y");
        Line line12=makeLine(525,200,700,200,20,Color.GREEN,"G");

        Group lines=new Group();
        lines.getChildren().addAll(line1,line2,line3,line4,line5,line6,line7,line8,line9,line10,line11,line12);

        return lines;

    }

    public TranslateTransition doRotateLine(Group lines){

        TranslateTransition transition=new TranslateTransition();
        transition.setByX(500);
        transition.setDuration(Duration.millis(2000));transition.setCycleCount(500);
        transition.setAutoReverse(true);
        transition.setNode(lines);
        transition.play();

        return transition;


    }

}
class CircleObstacle extends GameElement {

    @Override
    public Group getShape(){

        Arc arc1=makeArc(115,0,20, Color.YELLOW,"Y");
        Arc arc2=makeArc(115,90,20,Color.RED,"R");
        Arc arc3=makeArc(115,180,20,Color.BLUE,"B");
        Arc arc4=makeArc(115,270,20, Color.GREEN,"G");

        Group root = new Group();
        root.getChildren().addAll(arc1,arc2,arc3,arc4);


        return root;
    }
    public RotateTransition doRotate(Group root, double time) {

        RotateTransition rotate2 = new RotateTransition();
        rotate2.setAxis(Rotate.Z_AXIS);
        rotate2.setByAngle(360);
        rotate2.setCycleCount(Animation.INDEFINITE);
        rotate2.setInterpolator(Interpolator.LINEAR);
        rotate2.setRate(0.07);
        rotate2.setDuration(Duration.millis(time));
        rotate2.setNode(root);
        rotate2.play();

        return rotate2;
    }

}

class RectangleObstacle extends GameElement{

    @Override
    public Group getShape(){
        Line line1 =makeLine(0,0,170,0,15,Color.RED,"R");
        Line line2 = makeLine(0,0,0,180,15,Color.BLUE,"B");
        Line line3 = makeLine(170,0,170,180,15,Color.GREEN,"G");
        Line line4 = makeLine(0,180,170,180,15,Color.YELLOW,"Y");

        Group layout1 = new Group(line1,line2,line3,line4);
        layout1.setLayoutX(180);

        return layout1;
    }
    public RotateTransition doRotate(Group root, double time) {

        RotateTransition rotate2 = new RotateTransition();
        rotate2.setAxis(Rotate.Z_AXIS);
        rotate2.setByAngle(360);
        rotate2.setCycleCount(Animation.INDEFINITE);
        rotate2.setInterpolator(Interpolator.LINEAR);
        rotate2.setRate(0.07);
        rotate2.setDuration(Duration.millis(time));
        rotate2.setNode(root);
        rotate2.play();

        return rotate2;
    }
}

class colorSwitch extends GameElement{
    private Arc getQuarterCircle(int startAngle,Color color){
        Arc a = new Arc();
        a.setFill(color);
        a.setCenterX(250);
        a.setCenterY(30);
        a.setRadiusX(20);
        a.setRadiusY(20);
        a.setStartAngle(startAngle);
        a.setLength(90);
        a.setType(ArcType.ROUND);
        return a;
    }

    @Override
    public Group getShape() {

        Arc a = getQuarterCircle(0,Color.RED);
        Arc b = getQuarterCircle(90,Color.BLUE);
        Arc c = getQuarterCircle(180,Color.YELLOW);
        Arc d =getQuarterCircle(270,Color.GREEN);

        Group colorSwitch = new Group();
        colorSwitch.getChildren().addAll(a, b, c, d);

        return colorSwitch;
    }
}
