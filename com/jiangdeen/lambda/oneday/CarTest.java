package com.jiangdeen.lambda.oneday;

public class CarTest {

    public static void main(String[] args) {
        // 使用接口函数构造对象
        Car car = () -> System.out.println("汽车跑起来了");
        // 执行run方法
        car.run();
        // 获取汽车品牌， 这个是接口默认方法
        System.out.println(car.getBrand());

        // 获取汽车类型，这辆车是suv
        System.out.println(Car.getType());
    }

}
