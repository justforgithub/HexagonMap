package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        Board myBoard = new Board(4,4);

        for(int x = 0; x<myBoard.boardCells.length; x++){
            for (int y = 0; y < myBoard.boardCells[0].length; y++){
                System.out.println(Integer.toString(x) + " " + Integer.toString(y) +" " + myBoard.boardCells[x][y]);
               pane.getChildren().add(myBoard.boardCells[x][y].drawGroup);
            }
        }


        primaryStage.setScene(new Scene(pane, 400, 400));

        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
