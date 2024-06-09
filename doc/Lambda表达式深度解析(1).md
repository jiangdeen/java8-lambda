# Lambda表达式深度解析(1)

学习lambda之前，我们先来明确几个概念。

### 一、 什么是接口函数？

简单来说，接口函数就是，**一个接口有且只有一个抽象方法。**

接口当中的`default`方法和`static`方法,都不影响该接口的效果.

例如：

```java
package com.jiangdeen.lambda.oneday;

/**
 * 1. 接口当中有且仅有一个抽象函数，那么我们就可以把这个这个函数称为接口函数
 * 2. 接口当中的默认（default）方法，不影响接口函数的使用
 * 3. 接口当中的静态函数，也不影响接口函数的使用
 */
public interface Car {

    /**
     * 汽车可以跑
     */
    void run();


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

```



### 二、什么是lambda表达式？

lambda 表达式的核心是**代码即是数据**.

#### 那么,你会问什么是代码即数据?

这里我们就要想一想，在写代码的时候一般都是把数据作为参数，传给一个接口。

lambda表达式,就是把一段代码传给接口，把代码当成数据来处理。

举例：

如果我们做一个用户登陆的接口，用户传过来一个User对象，里面包含`username`和`password`,我们接收到了这个对象就去数据库中匹配,并且返回登陆成功或者失败.

```java
public boolean login(User user){
    if("小明".equals(user.getUsername()) && "123456".equals(user.getPassword()){
        return true;
    }
       return false;    
}
```

那么接口函数就是把这样一个方法,抽象成一个函数,把函数当做数据那样,传来传去.

```java
   public boolean checkUser(String username, String password) {
        Supplier<Boolean> predicate = () -> "小明".equals(username) && "123456".equals(password);
        return predicate.get();
    }
```

这段代码的核心` () -> "小明".equals(username) && "123456".equals(password);` 我们可以把他看成一个行为, 把行为封装成对象,就是lambda的概念.



### 三、 接口函数和lambda表达式和接口函数是什么关系？

在`java`中接口函数是使用`lambda表达式`的关键和基础.  是有构造了接口函数才能使用lambda表达式.

接上面的例子: 

1. 有一个Car 汽车接口, 当中有一个抽象方法, run();  那么我们在构建Car这个接口对象的时候,就可以使用lambda表达式来构建.函数即对象. 关键代码`  Car car = () -> System.out.println("汽车跑起来了");`.

```java
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

```



### 四、Java中重要的接口函数

这一小节,我们来学习一下,Java中自带的一些重要的接口函数,并且我们日常写代码完全用得上.

| 接口              | 参数   | 返回类型 | 作用       |
| ----------------- | ------ | -------- | ---------- |
| Predicate<T>      | T      | boolean  | 断言型接口 |
| Consumer<T>       | T      | void     | 消费型接口 |
| Function<T,R>     | T      | R        | 函数型接口 |
| Supplier<T>       | None   | T        | 共给型接口 |
| UnaryOperator<T>  | T      | T        |            |
| BinaryOperator<T> | (T，T) | T        |            |



4.1 那么我们来看一下`Predicate `的实际应用

```java
 /**
     * 找出所有用户中年龄大于60的人
     */
    public List<User> getAgeThanSixty() {
        User xiaoming = new User("小明", 12, null);
        User xiaohong = new User("小红", 13, null);
        User daming = new User("大明", 61, null);
        return Stream.of(xiaoming, xiaohong, daming)
                .filter(x -> x.getAge() > 60)
                .collect(Collectors.toList());
    }
```

你可能好奇,这段代码看起来没有用到,`Predicate`这个类啊?

错了,你可以点进`Stream`的`filter`方法去看一看.

![image-20240609152640704](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406091526754.png)

filter方法接受的正是一个`Predicate`对象, 那么`x -> x.getAge() > 60`这一段函数就代表了`predicate`对象.

看到这里你是不是对接口函数有了更深一层的了解了呢? 



文档中用到的代码:  https://github.com/jiangdeen/java8-lambda

