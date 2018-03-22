package groupapp.cs.psu.slidingpuzzle;

import junit.framework.TestCase;
import org.junit.Test;

public class MathEquationProcessorTest extends TestCase{

    @Test
    public void testaddition() {

        MathEquationProcessor proc = new MathEquationProcessor();
        Object[] submittedValues = {"4","+","1","=","5"};
        assertEquals(5,proc.solveEquation(submittedValues));
    }

    @Test
    public void testubtraction() {

        MathEquationProcessor proc = new MathEquationProcessor();
        Object[] submittedValues = {"5","-","1","=","4"};
        assertEquals(4,proc.solveEquation(submittedValues));
    }

    @Test
    public void testmultiply() {

        MathEquationProcessor proc = new MathEquationProcessor();
        Object[] submittedValues = {"2","*","4","=","8"};
        assertEquals(8,proc.solveEquation(submittedValues));
    }

    @Test
    public void testdivision() {

        MathEquationProcessor proc = new MathEquationProcessor();
        Object[] submittedValues = {"6","/","3","=","2"};
        assertEquals(2,proc.solveEquation(submittedValues));
    }

    @Test
    public void testvalidequation1() {

        MathEquationProcessor proc = new MathEquationProcessor();
        Object[] submittedValues = {"6","3","0","2","-"};
        assertEquals(false,proc.validateValues(submittedValues));
    }
    @Test
    public void testvalidequation2() {

        MathEquationProcessor proc = new MathEquationProcessor();
        Object[] submittedValues = {"8","+","0","=","8"};
        assertEquals(true,proc.validateValues(submittedValues));
    }

}