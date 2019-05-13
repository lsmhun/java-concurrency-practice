package hu.lsm.concureny.practice;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;

import static org.junit.Assert.assertEquals;

public class FunctionalInterfaceExampleTest {

    private FunctionalInterfaceExample functionEx = new FunctionalInterfaceExample();

    @Test
    public void testFunctionNormalBigDecimal(){
        BigDecimal bigIn = BigDecimal.TEN;
        Double res = functionEx.halfBigDecimal.apply(bigIn);
        assertEquals(Double.valueOf(5.0), res);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput(){
        Integer intVal = null;
        functionEx.half.apply(intVal);
    }

    @Test
    public void testListModifyByConsumer(){
        List<Integer> intList = Arrays.asList(3, 4, 6, 7, 10);
        functionEx.consumerListDividedByTwo.accept(intList);
        List<Integer> expectedResult = Arrays.asList(1, 2, 3, 3, 5);
        assertEquals(expectedResult, intList);
    }

    @Test
    public void testListModifyByConsumerPipe() {
        List<Integer> intList = Arrays.asList(3, 4, 6, 7, 10);
        functionEx.consumerListDividedByTwo.accept(intList);
        List<Integer> expectedResult = Arrays.asList(0, 1, 1, 1, 2);
    }

    @Test
    public void composeTest(){
        // BinaryOperator<Function<Integer,Integer>> compose = (f, g) -> x -> g.apply(f.apply(x));
        Function<Integer,Integer> composedFunction = functionEx.compose.apply(functionEx.add1, functionEx.add1);
        int a = 0;
        int res = composedFunction.apply(a);
        assertEquals(2, res);
    }

    @Test
    public void testIntFunctionAndSimpleFunctionImplementation() {
        int myNumber = 42;
        //Function<Integer, String> intToStr = intVal -> String.valueOf(intVal);
        String res = functionEx.intToStr.apply(myNumber);
        assertEquals("42", res);

        // IntFunction<String> intToStr2 = intVal -> String.valueOf(intVal);
        res = functionEx.intToStr2.apply(myNumber);
        assertEquals("42", res);
    }

    @Test
    public void testUsingTripleFunctionalInterface(){
        FunctionalInterfaceExample.Triple triple = a -> a * 3L;
        int incomingA = 3;
        Long expected = 9L;
        Long res = triple.triple(incomingA);
        assertEquals(expected, res);
    }

    @Test
    public void testIntFunction(){
        IntFunction<Integer> duplicate = a -> a * 2;
        Integer res = duplicate.apply(duplicate.apply(10));
        assertEquals(Integer.valueOf(40), res);
    }
}