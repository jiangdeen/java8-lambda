package com.jiangdeen.lambda.oneday;

/**
 * 1.接口当中有且仅有一个抽象函数，那么我们就可以把这个这个函数称为接口函数
 * 2. 接口当中的默认（default）方法，不影响接口函数的使用
 * 3. 接口当中的静态函数，也不影响接口函数的使用
 */
public interface Car {

    /**
     * 汽车可以跑
     */
    void run();

    /**
     * 给汽车加油
     * 如果多了一个加油的抽象方法,则就不是结构函数了
     */
//   void refueling();


    /**
     * 汽车品牌
     *
     * @return
     */
    default String getBrand() {
        return "Volvo";
    }

    /**
     * 汽车类型
     *
     * @return
     */
    static String getType() {
        return "SUV";
    }
}
