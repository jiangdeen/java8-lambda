package com.jiangdeen.lambda.day03;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public class Test3 {

    /**
     * 重载继承关系的接口函数
     *
     * @param lambda
     */
    private void overloadedMethod(BinaryOperator<Integer> lambda) {
        System.out.println("BinaryOperator,父类");
    }

    private void overloadedMethod(IntegerBiFunction lambda) {
        System.out.println("IntegerBiFunction,子类");
    }


    /**
     * 重载的两个方法,入参没有关系,但是都是接口函数
     *
     * @param lambda
     */
    private void overloadedMethod01(Predicate<Integer> lambda) {
        System.out.println("Predicate");
    }

    private void overloadedMethod01(MyPredicate<Integer> lambda) {
        System.out.println("MyPredicate");
    }


    public static void main(String[] args) {
        Test3 t3 = new Test3();
        t3.overloadedMethod((a, b) -> a + b);
        t3.overloadedMethod01((MyPredicate<Integer>) a -> a > 7);
    }
}







