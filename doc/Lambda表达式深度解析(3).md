

# Lambda表达式深度解析(3)

### 一、接口函数重载解析

我们都知道`java`方法可以重载，重载的定义：接口名称相同，参数列表不同。

1.1 那么如果两个方法的入参都是lambda表达式呢？ 并且这个时候重载的方法参数为,继承关系的时候呢？

示例：

```java
package com.jiangdeen.lambda.day03;

import java.util.function.BinaryOperator;

public interface IntegerBiFunction extends BinaryOperator<Integer> {
}


// ------------------------------------test-------------------------------------------
package com.jiangdeen.lambda.day03;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

public class Test3 {

    private void overloadedMethod(BinaryOperator<Integer> lambda) {
        System.out.println("binaryOperator");
    }

    private void overloadedMethod(IntegerBiFunction lambda) {
        System.out.println("biFunction");
    }

    public static void main(String[] args) {
        Test3 t3 = new Test3();
        t3.overloadedMethod((a, b) -> a + b);
    }
}

```

自定义`IntegerBiFunction`类，并且继承`BinaryOperator`, 相当于这两个类都是`接口函数`重载两个函数,直接使用`lambda表达式`调用这两个函数.

大家来猜猜是什么结果?

![image-20240611072048424](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406110720532.png)

从编译上就可以看出结果,调用的是子类方法,由此我们得出一个重要结论,<font color=red>**如果重载入参为`lambda表达式`的方法,那么在使用lambda表达式调用的时候,子类优先父类**</font>

1.2 如果重载的方法入参,都是接口函数,但是参数之间不存在继承关系,会是什么情况呢? 

```java
package com.jiangdeen.lambda.day03;

/**
 * 测试lambda 重载机制
 */
public interface MyPredicate<T> {

    boolean test(T t);
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
        t3.overloadedMethod01(a -> a > 7);
    }

```

![image-20240611073336460](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406110733525.png)

在这种情况下,编译就会报错,提示调用不明确,二者都可以. 这里我们又得出一个重要结论: <font color=red>**当重载方法的入参都是接口函数,并且两个入参之间没有继承关系时,使用lambda表达式构造入参会提示编译错误**</font>

那么在这种情况下想要实现调用,必须指定类型 例如:`t3.overloadedMethod01((MyPredicate<Integer>) a -> a > 7);`

### 二、`@FunctionalInterface` 注解

`@FunctionalInterface` 注解用于指定一个接口是否是`接口函数`,事实上如果被当做`接口函数`使用的接口都要加上这个注解,加上这注解的好处是,如果在接口函数中又添加了新的抽象方法,那么就会提示编译错误.

![image-20240611074606402](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406110746479.png)



### 三、接口默认方法

不知道你有没有注意到,`java8`接口新增了默认方法, 在接口中用`default`来进行声明.

`Collection`接口中增加了心得`Stream`方法,如何才能让用户的 `自定义List` 在不知道该方法的情况下通过编译? `java8`通过如下方法解决该问题:

`Collection`接口告诉它所有的子类:如果你没有实现`Stream`方法,就使用我的吧. 接口中这样的方法叫做`默认方法`,在任何接口中,无论是函数接口还是非函数接口,都可以使用该方法.

定义父类:

```java
package com.jiangdeen.lambda.day03;

public interface Parent {

    void message(String body);

    default void welcome() {
        message("parent");
    }

    String getLastMessage();
}
```

字类重写父类默认方法:

```java
package com.jiangdeen.lambda.day03;

public interface Child extends Parent {

    @Override
    default void welcome() {
        message("Child");
    }

}
```

测试默认方法优先级: 

```java
package com.jiangdeen.lambda.day03;

/**
 * 默认方法测试类
 */
public class DefaultTest {

    public static void main(String[] args) {
        test1();
        test2();
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
```

**最终我们看到,子类中的方法优先于父类当中的默认方法.**

#### 3.1 默认方法的多重继承

```java
package com.jiangdeen.lambda.day03;

public interface Carriage {
    default String rock() {
        return "Carriage.rock";
    }
}
// -----------------------------------------------------------------------
package com.jiangdeen.lambda.day03;

public interface Jukebox {
    default String rock() {
        return "Jukebox.rock";
    }
}

// -------------------------------------------------------------------------
public class MusicalCarriage implements Carriage,Jukebox{
}

```

![image-20240611084018935](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406110840013.png)

一个类实现了两个接口,两个接口中有相同的默认方法, 按照道理来说,默认方法是不会强制重写的,但是这个时候,`java`编译器会提示我们需要强制重写,不然编译无法通过.

### 四、接口当中静态方法

接口当中的静态方法也是`java8`当中添加的新特性,一般情况下,我们会把包含了很多静态代码块的方法整理成一个工具类,如果一个方法有充分的语义原因和某个概念强相关,那么就可以放在接口中.


`GitHub`地址:  [如果喜欢给个start;](https://github.com/jiangdeen/java8-lambda)



