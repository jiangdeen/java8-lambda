package com.jiangdeen.lambda.day03;

/**
 * 默认方法测试类
 */
public class DefaultTest {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }

    /**
     * 默认方法测试
     */
    private static void test1() {
        Parent parent = new ParentImpl();
        parent.welcome();
    }

    private static void test2() {
        Child child = new ChildImpl();
        child.welcome();
    }

    private static void test3() {
        Child child = new OverridingChild();
        child.welcome();
    }
}
