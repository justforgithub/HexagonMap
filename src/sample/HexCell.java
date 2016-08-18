package sample;

import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * Created by Deviltech on 11.08.2016.
 */
public class HexCell {

    /**
     * Generate drawGroup without positioning
     * @return
     */

    public Group drawGroup;
    public int level;
    public int score;
    public int x;
    public int y;
    public Board board;

    HexCell(int x,int y,Board board){
        this.drawGroup =new Group();
        drawHexCell();
        this.level = 0;
        this.score = 0;
        this.x = x;
        this.y = y;
        this.board = board;
        prepareEventListeners();
    }

    /**
     * Draws the current HexCell without resetting its translation
     */
    public void drawHexCell(){

        double hor = MyValues.HORIZONTAL_VALUE * MyValues.HEX_SCALE;
        double dia = MyValues.DIAGONAL_VALUE * MyValues.HEX_SCALE;


        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                -(dia + 0.5*hor), 0.0,
                -0.5*hor, -hor,
                +0.5*hor, -hor,
                +(dia + 0.5*hor), 0.0,
                +0.5*hor, hor,
                -0.5*hor, hor,
        });
        // Set cell fill according to hexCell level
        //polygon.setFill(MyValues.HEX_LEVEL_PALETTE[this.level]);
        polygon.setFill(MyValues.HEX_LEVEL_PALETTE[this.level%MyValues.HEX_LEVEL_PALETTE.length]);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(5.0);

        Text text = new Text(Integer.toString(score));
        text.setBoundsType(TextBoundsType.VISUAL);

        drawGroup.getChildren().clear();
        drawGroup.getChildren().addAll(polygon, text);

    }

    /**
     * Adjacent HexCell positioning based on given position of other Hexagon
     * @param otherHexCell
     * @param position
     */
    public void placeHexCell(HexCell otherHexCell, MyValues.HEX_POSITION position){

        double hor = MyValues.HORIZONTAL_VALUE * MyValues.HEX_SCALE;
        double dia = MyValues.DIAGONAL_VALUE * MyValues.HEX_SCALE;

        double x = 0;
        double y = 0;
        switch (position){
            case TOP:
                x = 0;
                y = -2*hor;
                break;
            case TOP_LEFT:
                x = -(hor+dia);
                y = -hor;
                break;
            case TOP_RIGHT:
                x = +(hor+dia);
                y = -hor;
                break;
            case BOT:
                x = 0;
                y = +2*hor;
                break;
            case BOT_LEFT:
                x = -(hor+dia);
                y = +hor;
                break;
            case BOT_RIGHT:
                x = +(hor+dia);
                y = +hor;
                break;
        }
        otherHexCell.drawGroup.setTranslateX(drawGroup.getTranslateX() + x);
        otherHexCell.drawGroup.setTranslateY(drawGroup.getTranslateY() + y);
    }

    /**
     * Prepare Mouse handlers
     */
    private void prepareEventListeners(){
        drawGroup.setOnMouseClicked(event -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                if(level == 0){
                    level = 1;
                    score = 1;
                    System.out.println("auf 1 gesetzt");
                    drawHexCell();
                }
                board.backGroundCheck(this);
            }

        });
    }

    public void setToZero(){
        this.level = 0;
        this.score = 0;
        drawHexCell();
    }

    /**
     * Change Position of x and y
     * @param x
     * @param y
     */
    public void changePosition(double x, double y){
        drawGroup.setTranslateX(x);
        drawGroup.setTranslateY(y);
    }
}
