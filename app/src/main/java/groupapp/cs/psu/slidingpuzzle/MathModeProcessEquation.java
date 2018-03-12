package groupapp.cs.psu.slidingpuzzle;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import groupapp.cs.psu.slidingpuzzle.grid.objects.Block;
import groupapp.cs.psu.slidingpuzzle.grid.objects.Board;
import groupapp.cs.psu.slidingpuzzle.grid.objects.Coordinates;


public class MathModeProcessEquation {

    /**
     *
     * @param startCoordinates
     * @param endCoordinates
     * @param swipeDirection
     * @return
     *
     * This method is used to check if the submitted equation has valid number of coordinates
     * If number of coordinates swiped is less than 5, then return false
     */
    public boolean validateCoordinates(Coordinates startCoordinates, Coordinates endCoordinates, int swipeDirection){

        Log.i("validateCoordinates","validateCoordinates method start");

        boolean isValidCoordinates = false;

        switch(swipeDirection){

            //Swipe from left to right --> row number remains the same
            case 0:
                if (endCoordinates.getY() - startCoordinates.getY() == 4 && startCoordinates.getX() == endCoordinates.getX() ) {
                    // e.g startCoordinates = (1,0) and endCoordinates (1,4) --> valid

                    Log.i("validateCoordinates", "Swipe from left to right has valid number of coordinates");
                    isValidCoordinates = true;
                    break;
                }

                //Swipe from right to left --> row number remains the same
            case 1:
                if (startCoordinates.getY() - endCoordinates.getY() == 4 && startCoordinates.getX() == endCoordinates.getX()) {
                    // e.g startCoordinates = (1,4) and endCoordinates (1,0) --> valid

                    Log.i("validateCoordinates", "Swipe from right to left has valid number of coordinates");
                    isValidCoordinates = true;
                    break;
                }


                //Swipe from top to bottom --> column number remains the same
            case 2:
                if (endCoordinates.getX() - startCoordinates.getX() == 4  && startCoordinates.getY() ==  endCoordinates.getY()) {
                    // e.g startCoordinates = (0,0) and endCoordinates (4,0) --> valid

                    Log.i("validateCoordinates", "Swipe from top to bottom has valid number of coordinates");
                    isValidCoordinates = true;
                    break;
                }

                //Swipe from bottom to top --> column number remains the same
            case 3:
                if (startCoordinates.getX() - endCoordinates.getX() == 4 && startCoordinates.getY() ==  endCoordinates.getY()) {
                    // e.g startCoordinates = (4,2) and endCoordinates (0,2) --> valid

                    Log.i("validateCoordinates", "Swipe from  bottom to top has valid number of coordinates");
                    isValidCoordinates = true;
                    break;
                }
        }

        Log.i("validateCoordinates","validateCoordinates method end");
        return isValidCoordinates;
    }



    /**
     *
     * @param startCoordinates
     * @param swipeDirection
     * @param board
     * @return
     *
     * Fetch all the coordinates swiped and add them to a list
     */
    public List<Block> fetchBlocksFromSubmittedEquation(Coordinates startCoordinates, int swipeDirection, Board board){
        List<Block> blocks = new ArrayList<>();
        switch(swipeDirection){

            //Swipe from left to right
            case 0:
                //e.g [1,0],[1,1],[1,2],[1,3],[1,4]
                for (int i = 0; i < 5; i++) {
                    blocks.add(board.at((startCoordinates.getY() + i), startCoordinates.getX()).getBlock());
                }
                break;


            //Swipe from right to left
            case 1:
                //e.g [4,4],[4,3],[4,2],[4,1],[4,0]
                for (int i = 0; i < 5; i++){
                    blocks.add(board.at((startCoordinates.getY() - i), startCoordinates.getX()).getBlock());

                }
                break;

                //Swipe from top to bottom
            case 2:
                //e.g [0,0],[1,0],[2,0],[3,0],[4,0]
                for (int i = 0; i < 5; i++){
                    blocks.add(board.at((startCoordinates.getY()),startCoordinates.getX() + i).getBlock());

                }
                break;

                //Swipe from bottom to top
            case 3:
                //e.g [4,0],[3,0],[2,0],[1,0],[0,0]
                for (int i = 0; i < 5; i++){
                    blocks.add(board.at((startCoordinates.getY()),startCoordinates.getX() - i).getBlock());
                }
                break;
        }

        return blocks;

    }



    /**
     * This method checks if the submitted equation has binary operators and operands at right places
     * @return
     */
    public boolean validateEquationFormat(List<Block> blocks){
        char operationCoordinate;
        char equalToCoordinate;
        boolean isOperator, isEqual, isNumber;
        isEqual = false;
        isOperator = false;
        isNumber = false;

        //A complete equation should have 5 different blocks, out of which block at 1st index is operand and at 3rd index is equal operator.
        // e.g 2+5 = 7
        if(!blocks.isEmpty() && blocks.size() == 5) {
            operationCoordinate = blocks.get(1).operation();
            equalToCoordinate = blocks.get(3).operation();


            if (operationCoordinate != ' ' && (operationCoordinate == '+' || operationCoordinate == '-'
                    || operationCoordinate == '/' || operationCoordinate == '*')) {
                isOperator = true;
            }

            if (equalToCoordinate !=' ' && equalToCoordinate == '=') {
                isEqual = true;
            }

            if(blocks.get(0).operation() == ' ' &&  blocks.get(2).operation() == ' ' && blocks.get(4).operation() == ' '){
                isNumber = true;
            }

        }
        return isOperator && isEqual && isNumber;
    }


    /**
     * This method checks if the submitted equation is mathematically correct.
     * e.g 2 + 2 = 4 return true
     * e.g 2 + 2 = 5 return false
     * @return
     */
    public boolean validateEquationCorrectness(List<Block> blocks){
        char operation = blocks.get(1).operation();
        int operand1 = blocks.get(0).number();
        int operand2 = blocks.get(2).number();
        int result = blocks.get(4).number();

        switch(operation){
            case '+':
                if((operand1 + operand2) == result){
                    return true;
                }
            case '-':
                if(operand1 - operand2 == result){
                    return true;
                }
            case '*':
                if(operand1 * operand2 == result){
                    return true;
                }
            case '/':
                if(operand1 / operand2 == result){
                    return true;
                }
        }

        return false;
    }


    public int processEquation(Coordinates startCoordinates, Coordinates endCoordinates, int swipeDirection, Board board){

        if(validateCoordinates(startCoordinates,endCoordinates,swipeDirection)){

            Log.d("validateCoordinates","Valid number of coordinates");

            //If number of coordinates submitted is 5, fetch them
           List<Block> blocks =  fetchBlocksFromSubmittedEquation(startCoordinates, swipeDirection, board);

            if(validateEquationFormat(blocks)){
                Log.d("validateEquationFormat","Operands and operators at correct places");

                if(validateEquationCorrectness(blocks)){
                    Log.d("validateEquationCorrect","Submitted Equation is correct");
                    return blocks.get(4).number();
                }else{
                    Log.d("validateEquationCorrect","Submitted Equation is incorrect");
                    return -3;
                }

            }else{
                Log.d("validateEquationFormat","Operands and operators at incorrect places");
                return -2;
            }
        }else{
            Log.d("validateCoordinates","Invalid number of coordinates");
            return -1;
        }
    }

    /**
     *
     * @param startCoordinates
     * @param swipeDirection
     * @param board
     * @return
     *
     * Fetch all the coordinates swiped and add them to a list
     *//*
    public List<Block> fetchBlocksFromSubmittedEquation(Coordinates startCoordinates, int swipeDirection, Board board){
        List<Block> blocks = new ArrayList<>();
        switch(swipeDirection){

                //Swipe from left to right
            case 0:
                //e.g [1,0],[1,1],[1,2],[1,3],[1,4]
                for (int i = 0; i < 5; i++) {
                    blocks.add(board.at((startCoordinates.getY() + i), startCoordinates.getX()).getBlock());
                }
                break;


                //Swipe from right to left
         *//*   case 1:
                //e.g [4,4],[4,3],[4,2],[4,1],[4,0]
                for (int i = 0; i < 4; i++){
                    coordinates.add(board.at(startCoordinates.getX(),startCoordinates.getY() - i));
                    break;
                }

                //Swipe from top to bottom
            case 2:
                //e.g [0,0],[1,0],[2,0],[3,0],[4,0]
                for (int i = 0; i < 4; i++){
                    coordinates.add(board.at(startCoordinates.getX() + i, startCoordinates.getY()));
                    break;
                }

                //Swipe from bottom to top
            case 3:
                //e.g [4,0],[3,0],[2,0],[1,0],[0,0]
                for (int i = 0; i < 4; i++){
                    coordinates.add(board.at(startCoordinates.getX() - i, startCoordinates.getY()));
                    break;
                }*//*
        }

        return blocks;

    }*/

     /*public int processEquation(Coordinates startCoordinates, Coordinates endCoordinates, int swipeDirection, Board board){

        if(validateCoordinates(startCoordinates,endCoordinates,swipeDirection)){

            Log.d("validateCoordinates","Valid number of coordinates");

            //If number of coordinates submitted is 5, fetch them
           List<Coordinates> coordinates =  fetchBlocksFromSubmittedEquation(startCoordinates, swipeDirection, board);

            if(validateEquationFormat(coordinates)){
                Log.d("validateEquationFormat","Operands and operators at correct places");

                if(validateEquationCorrectness(coordinates)){
                    Log.d("validateEquationCorrect","Submitted Equation is correct");
                    return 0;
                }else{
                    Log.d("validateEquationCorrect","Submitted Equation is incorrect");
                    return -3;
                }

            }else{
                Log.d("validateEquationFormat","Operands and operators at incorrect places");
                return -2;
            }
        }else{
            Log.d("validateCoordinates","Invalid number of coordinates");
            return -1;
        }
    }*/

    /**
     * This method checks if the submitted equation is mathematically correct.
     * e.g 2 + 2 = 4 return true
     * e.g 2 + 2 = 5 return false
     * @return
     *//*
    public boolean validateEquationCorrectness(List<Coordinates> coordinates){
        Coordinates operationCoordinate = coordinates.get(1);
        char operation = operationCoordinate.getBlock().operation();

        Coordinates operand1 = coordinates.get(0);
        Coordinates operand2 = coordinates.get(2);
        Coordinates result = coordinates.get(4);

        switch(operation){
            case '+':
                if((operand1.getBlock().number() + operand2.getBlock().number()) == result.getBlock().number()){
                    return true;
                }
            case '-':
                if((operand1.getBlock().number() - operand2.getBlock().number()) == result.getBlock().number()){
                    return true;
                }
            case '*':
                if((operand1.getBlock().number() * operand2.getBlock().number()) == result.getBlock().number()){
                    return true;
                }
            case '/':
                if((operand1.getBlock().number() / operand2.getBlock().number()) == result.getBlock().number()){
                    return true;
                }
        }

        return false;
    }
*/


    /**
     * This method checks if the submitted equation has binary operators and operands at right places
     * @return
     */
/*    public boolean validateEquationFormat(List<Coordinates> coordinates){
       Coordinates operationCoordinate;
       Coordinates equalToCoordinate;
       boolean isOperator, isEqual;
       isEqual = false;
       isOperator = false;
        //why 5?
       if(!coordinates.isEmpty() && coordinates.size() == 5) {
           operationCoordinate = coordinates.get(1);
           equalToCoordinate = coordinates.get(3);


           if (operationCoordinate.hasBlock() && (operationCoordinate.getBlock().operation() == '+' || operationCoordinate.getBlock().operation() == '-'
                   || operationCoordinate.getBlock().operation() == '/' || operationCoordinate.getBlock().operation() == '*')) {
               isOperator = true;
           }

           if (equalToCoordinate.hasBlock() && equalToCoordinate.getBlock().operation() == '=') {
               isEqual = true;
           }
       }
        return isOperator && isEqual;
    }*/



}
