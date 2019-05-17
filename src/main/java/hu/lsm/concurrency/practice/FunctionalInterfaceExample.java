package hu.lsm.concurrency.practice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

/**
 * https://www.geeksforgeeks.org/function-interface-in-java-with-examples/
 * https://medium.com/@gelopfalcon/best-practices-when-you-use-lambda-expressions-in-java-f51e96d44b25
 * <p>
 * https://dzone.com/articles/functional-programming-java-8
 */
public class FunctionalInterfaceExample {

    @FunctionalInterface
    interface Triple {
        Long triple(Integer intValue);
    }

    // Both are the same -- input: Integer   output: String
    Function<Integer, String> intToStr = intVal -> String.valueOf(intVal);
    IntFunction<String> intToStr2 = intVal -> String.valueOf(intVal);

    // example
    UnaryOperator<Integer> add1 = n -> n + 1;
    BinaryOperator<Function<Integer, Integer>> compose = (f, g) -> x -> g.apply(f.apply(x));

    // input: Integer  returnType: Double
    Function<Integer, Integer> half = a -> a / 2;

    // detailed implementation
    Function<BigDecimal, Double> halfBigDecimal = new Function<BigDecimal, Double>() {
        @Override
        public Double apply(BigDecimal bigDecimal) {
            return bigDecimal.divide(BigDecimal.valueOf(2), RoundingMode.FLOOR).doubleValue();
        }
    };

    // you can use it with .foreach()
    Consumer<List<Integer>> consumerListDividedByTwo = list ->
    {
        for (int i = 0; i < list.size(); i++)
            list.set(i, list.get(i) / 2);
    };

    public static void main(String[] args) {
        FunctionalInterfaceExample funcExample = new FunctionalInterfaceExample();
        try {
            Integer a = 12;
            BigDecimal inBig = BigDecimal.TEN;
            double res = funcExample.halfBigDecimal.apply(inBig);
            System.out.println(res);
            List<Integer> intList = Arrays.asList(3, 4, 6, 7, 10);

            funcExample.consumerListDividedByTwo.accept(intList);
            System.out.println(intList);

            Triple triple = incoming -> incoming * 3L;
            Long tripleA = triple.triple(a);
            System.out.println(tripleA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
