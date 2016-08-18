package sample;

import java.util.ArrayList;

/**
 * Created by Deviltech on 11.08.2016.
 */
public class Board {

    public HexCell[][] boardCells;
    private int x;
    private int y;

    Board(int x, int y){
        this.x = x;
        this.y = y;
        boardCells = new HexCell[x][y];
        fillBoardWithCells();
    }

    /**
     * Fill the board with x*y level 0 cells
     */
    private void fillBoardWithCells(){
        double posx = MyValues.HORIZONTAL_VALUE*0.5*MyValues.HEX_SCALE + MyValues.DIAGONAL_VALUE*MyValues.HEX_SCALE;
        double posy = 2*MyValues.DIAGONAL_VALUE*MyValues.HEX_SCALE ;
        HexCell startCell = new HexCell(0,0, this);
        startCell.changePosition(posx, posy);
        boardCells[0][0] = startCell;
        for (int i = 0; i< x-1; i++) {
                HexCell currentCell = new HexCell(i+1, 0, this);
                    boardCells[i+1][0] = currentCell;
                //i mod 2 = 0: Bottom
                if (i % 2 == 0) {
                    boardCells[i][0].placeHexCell(currentCell, MyValues.HEX_POSITION.BOT_RIGHT );
                } else {
                    //i mod 2 =1: Top
                    boardCells[i][0].placeHexCell(currentCell, MyValues.HEX_POSITION.TOP_RIGHT );
                }
        }
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y-1; j++){
                HexCell currentCell = new HexCell(i, j+1, this);
                //System.out.println(Integer.toString(i) + Integer.toString(j));
                boardCells[i][j+1] = currentCell;
                boardCells[i][j].placeHexCell(currentCell, MyValues.HEX_POSITION.BOT);
            }
        }
    }

    /**
     * Return specific hexcell
     * @param x
     * @param y
     * @return
     */
    public HexCell getCell(int x, int y){
        //TODO Check bounds
        return boardCells[x][y];
    }

    /**
     * Gets the adjacent cell according to position
     * @param currentCell
     * @param position
     * @return
     */
    public HexCell getAdjacentCell(HexCell currentCell, MyValues.HEX_POSITION position){
        int cellX = currentCell.x;
        int cellY = currentCell.y;
        HexCell adjacentCell = null;
        switch (position){
            case TOP:
                if(isInBound(cellY-1, y)){
                    adjacentCell = getCell(cellX,
                            cellY-1);
                }
                break;
            case BOT:
                if(isInBound(cellY+1, y)){
                    adjacentCell = getCell(cellX, cellY+1);
                }
                break;
            case TOP_LEFT:
                if(isInBound(cellX -1, x)){
                    if(cellX % 2 ==0){
                        if(isInBound(cellY-1, y)){
                            adjacentCell = getCell(cellX-1, cellY-1);
                        }
                    } else{
                        adjacentCell = getCell(cellX-1, cellY);
                    }
                }
                break;
            case TOP_RIGHT:
                if(isInBound(cellX +1, x)){
                    if(cellX % 2 ==0){
                        if(isInBound(cellY-1, y)){
                            adjacentCell = getCell(cellX+1, cellY-1);
                        }
                    } else{
                        adjacentCell = getCell(cellX+1, cellY);
                    }
                }
                break;
            case BOT_LEFT:
                if(isInBound(cellX -1, x)){
                    if(cellX % 2 ==1){
                        if(isInBound(cellY+1, y)){
                            adjacentCell = getCell(cellX-1, cellY+1);
                        }
                    } else{
                        adjacentCell = getCell(cellX-1, cellY);
                    }
                }
                break;
            case BOT_RIGHT:
                if(isInBound(cellX +1, x)){
                    if(cellX % 2 ==1){
                        if(isInBound(cellY+1, y)){
                            adjacentCell = getCell(cellX+1, cellY+1);
                        }
                    } else{
                        adjacentCell = getCell(cellX+1, cellY);
                    }
                }
                break;
        }
        return adjacentCell;
    }

    /**
     * Places a level 1 HexCell, or merges several into a bigger one
     * @param currentCell
     */
    public void backGroundCheck(HexCell currentCell){
        // When Cell already clicked, do nothing
        boolean checkStatus = true;
        int checkLevel = 1;

        while (checkStatus){

                ArrayList<HexCell> toDoStack = new ArrayList<>();
                ArrayList<HexCell> checkedStack = new ArrayList<>();
                ArrayList<HexCell> appliedStack = new ArrayList<>();

                // fill all adjacent cells into toDoStack
                for(MyValues.HEX_POSITION position: MyValues.HEX_POSITION.values()){
                    HexCell adjacentCell = getAdjacentCell(currentCell, position);
                    if(adjacentCell != null && adjacentCell.level == checkLevel){
                        toDoStack.add(adjacentCell);
                    }
                    // work off all todoStack elements
                    while(!toDoStack.isEmpty()){
                        HexCell currentTodo = toDoStack.get(0);
                        if(currentTodo.level == checkLevel){
                            if(!appliedStack.contains(currentTodo)){
                                appliedStack.add(currentTodo);
                            }
                            if(!checkedStack.contains(currentTodo)) {
                                checkedStack.add(currentTodo);
                            }

                            // check all adjacent cells
                            for(MyValues.HEX_POSITION toDoPosition: MyValues.HEX_POSITION.values()){
                                HexCell adjacentToDoCell = getAdjacentCell(currentTodo, toDoPosition);
                                // if new Cell, add it
                                if((adjacentToDoCell != null) && !toDoStack.contains(adjacentToDoCell) && !checkedStack.contains(adjacentToDoCell) && !appliedStack.contains(adjacentToDoCell)){
                                    toDoStack.add(adjacentToDoCell);
                                }
                            }
                        }
                        toDoStack.remove(0);
                    }
                }
                // Raise Check level
                checkLevel +=1;

                if(appliedStack.size() >= MyValues.MERGE_THRESHOLD){
                    // Sum up all scores of applied cells
                    int summedScore = 0;
                    for(HexCell toBeMergedCell: appliedStack){
                        summedScore += toBeMergedCell.score;
                        toBeMergedCell.setToZero();
                    }
                    currentCell.level=checkLevel;
                    currentCell.score =  summedScore;
                    currentCell.drawHexCell();
                } else {
                    checkStatus = false;
                    break;
                }
        }
    }

    /**
     * Checks current >= 0 && current < bound
     * @param current
     * @param bound
     * @return
     */
    private boolean isInBound(int current, int bound){
        return (current >= 0 && current < bound);
    }

    /**
     * sum up all scores of stack
     * @param stack
     * @return
     */
    private int calculateScore(ArrayList<HexCell> stack){
        int score = 0;
        for(HexCell current: stack){
            score += current.score;
        }
        return score;
    }
}
