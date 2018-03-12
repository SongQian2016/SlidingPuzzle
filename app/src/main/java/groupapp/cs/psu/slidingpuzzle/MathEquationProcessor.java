package groupapp.cs.psu.slidingpuzzle;

/**
 * Created by payaljoshi on 3/9/18.
 */

public class MathEquationProcessor {
    /**
     * This method checks if the submitted equation has binary operators and operands at right places
     * @param submittedValues
     * @return
     */
    public boolean validateValues(Object[] submittedValues){
        boolean isOperator = false;
        boolean isEqualTo = false;
        boolean isInt = false;

        Object operator = submittedValues[1];
        Object equalTo = submittedValues[3];

        //if(operator instanceof Character){
        if(operator.toString().equals("+") || operator.toString().equals("-")
                || operator.toString().equals("*") || operator.toString().equals("/")){
            isOperator = true;
        }
        //}

        //if(equalTo instanceof Character) {
        if(equalTo.toString().equals("=")){
            isEqualTo = true;
        }
        // }

        /*if(submittedValues[0] instanceof Integer && submittedValues[2] instanceof Integer && submittedValues[4] instanceof Integer) {
            isInt = true;
        }*/
        try {
            Integer.parseInt(submittedValues[0].toString());
            Integer.parseInt(submittedValues[2].toString());
            Integer.parseInt(submittedValues[4].toString());
            isInt = true;
        }catch (NumberFormatException e){
            isInt = false;
        }


        return isOperator && isEqualTo && isInt;

    }




    public int solveEquation(Object[] submittedValues) {
        int returnValue = -1;
        if (validateValues(submittedValues)) {
            Object operator = submittedValues[1];
            Integer operand1 = Integer.parseInt(submittedValues[0].toString());
            Integer operand2 = Integer.parseInt(submittedValues[2].toString());
            Integer result = Integer.parseInt(submittedValues[4].toString());

            switch (operator.toString()) {
                case "+":
                    if (operand1 + operand2 == result) {
                        returnValue = result;
                    }
                    break;

                case "-":
                    if (operand1 - operand2 == result) {
                        returnValue = result;
                    }
                    break;

                case "*":
                    if (operand1 * operand2 == result) {
                        returnValue = result;
                    }
                    break;

                case "/":
                    if (operand2 != 0 && operand1 / operand2 == result) {
                        returnValue = result;
                    }
                    break;
            }

        }
        return returnValue;
    }

    }
