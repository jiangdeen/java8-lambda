# Lambda表达式深度解析(2)

### 一、什么是Stream?

**Stream是用函数式编程方式，在集合类上进行复杂操作的工具。**

### 二、什么是及早求值、惰性求值？

请看示例1：

```java
    /**
     * 惰性求值
     */
    public void inertEvaluation() {
        Stream.of("ab", "ac", "bc")
                .filter(x -> {
                    System.out.println("已经执行了filter方法1");
                    return x.contains("a");
                });
    }
```

请看示例2：

```java
    /**
     * 及早求值
     */
    public void eagerEvaluation() {
        long count = Stream.of("ab", "ac", "bc")
                .filter(x -> {
                    System.out.println("已经执行了filter方法2");
                    return x.contains("a");
                })
                .count();
        System.out.println(count);
    }
```

##### `示例1`和`示例2`有什么区别呢?

`示例1`只调用`Stream`的`filter`,在`filter`方法中打印了一句话,但是在实际调用的时候,发现根本没有输出打印. 

而`示例2`在`filter`调用之后,又调用了`count`方法, 控制台输出`已经执行了filter方法2`,这两个方法的不同,就是及早求值和惰性求值的区别.

**及早求值和惰性求值的定义**:像filter这样, 最终不产生新集合的方法叫做,惰性求值方法,而像`count`这样最终会从Stream产生值的方法叫做及早求值方法.

**惰性求值就像医生配药, 配方都已经准备好了,但是患者还有没吃下去,所以就算医生的配方再高明,也不会产生任何效果.** 

**及早求值就像是患者吃药,患者拿到医生陪好的药,吃了之后立马有反应.**



### 三、Stream 常用API深度解析？

#### 3.1 collect(toList())

`collect(toList)` 是由一个`Stream`里面的值生成一个列表,是一个及早求值.

```java
    /**
     * collect(toList)
     */
    public void toList() {
        List<String> list = Stream.of("ab", "ac", "bc").collect(Collectors.toList());
        list.forEach(System.out::println);
    }
```

#### 3.2 map 和 Function接口的关系

`map`操作可以将一种类型转换成另一种类型.可以将流中的一个值转换成一个新的流.

例如: 将一个列表中的字母转换成大写, 或者把一个对象转换成另一个对象.

```java
/**
     * 把列表中的字母转换成大写
     */
    public void mapToUpperCase() {
        Stream.of("ab", "ac", "bc")
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
```

你可能会觉得,这么简单我早就会用了, 但是我问你一下,你懂的原理吗?

以下是map的方法签名.

- `map`接受一个`Function`类型的参数T,返回一个R类型, 意思就是由T转成R.
- `Function` 接口有且仅有一个抽象方法,所以它是一个接口函数.

- `Function`是一个接口函数,所以你在`map`方法中才能直接用`lambda`表达式.

```java
 <R> Stream<R> map(Function<? super T, ? extends R> mapper);
```

![image-20240610112426648](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406101124730.png)

讲到这里你是不是感觉豁然开朗?

为什么可以在`map`中用`ambda`表达式?

为什么`map`的`lambda`表达式是有返回值的? 并且返回值可以任意设置.

`map`的底层是实现了`Function`接口中的`apply`方法.

#### 3.3 filter 和Predicate的关系

`filter()`和`map`类似,不过`filter`返回值类型必须是`boolean`, 这是因为`filter`的底层是`Predicate`的`test`方法.

那么我们来看一下`test`方法的签名,你就明白怎么回事了.

![image-20240610113838403](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406101138482.png)

`Predicate`的`test`方法返回的是一个`boolean`值,所以`filter`函数也是最终返回的也要是一个`boolean`

#### 3.4 `flatMap`

```java
/**
 * 打印全校学生名称
 */
public void testFlatMap() {
    // 一年级一班
    School.Class cl1 = new School.Class("王老师", Arrays.asList("小明", "小红"));
    // 一年级二班
    School.Class cl2 = new School.Class("力老师", Arrays.asList("大明", "大红"));
    School scl = new School("王校长", Arrays.asList(cl1, cl2));
    // 全校学生
    List<String> allStudents = scl.getClassList().stream()
            .flatMap(it -> it.getStudents().stream())
            .collect(Collectors.toList());

    allStudents.forEach(System.out::println);

}


static class School {

        /**
         * 校长
         */

        private String principal;

        /**
         * 班级
         */
        private List<Class> classList;

        public School(String principal, List<Class> classList) {
            this.principal = principal;
            this.classList = classList;
        }

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public List<Class> getClassList() {
            return classList;
        }

        public void setClassList(List<Class> classList) {
            this.classList = classList;
        }


        static class Class {
            /**
             * 老师
             */
            private String teacher;

            /**
             * 学生
             */
            private List<String> students;

            public String getTeacher() {
                return teacher;
            }

            public void setTeacher(String teacher) {
                this.teacher = teacher;
            }

            public List<String> getStudents() {
                return students;
            }

            public void setStudents(List<String> students) {
                this.students = students;
            }

            public Class(String teacher, List<String> students) {
                this.teacher = teacher;
                this.students = students;
            }
        }
    }
```

从上面的例子可以看出,`flatMap`用于处理, 二维甚至多为集合,那么我们来看一下它的方法签名和map有什么不同吧!

```java
    <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

 	<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```

- 先看`map`和`flatMap`的入参,乍一看都一样,入参都是`Function`接口,看仔细看就会看出不同
- 两个函数的入参泛型不同, 第一个`Function` 入参的泛型T是一样,入参泛型T或者T的字类
- 返回值类型R,`flatMap`的返回值,必须是Stream的子类,就这一点点的区别导致了`map`和`flatMap`的作用完全不同

#### 3.5 max和min

求集合中的最大值最小值, max,min 也是两个常用的函数.并且这两个函数都是及早求值.

```java
/**
     * 打印全校学生名称
     */
    public void testMaxOrMin() {
        // 一年级一班
        School.Class cl1 = new School.Class("王老师", 33, Arrays.asList("小明", "小红"));
        // 一年级二班
        School.Class cl2 = new School.Class("力老师", 44, Arrays.asList("大明", "大红"));
        School scl = new School("王校长", Arrays.asList(cl1, cl2));

        // 全校学生
        School.Class cla1 = scl.getClassList().stream()
                .min(Comparator.comparing(School.Class::getAge))
                .get();

        System.out.println("年龄较小的: "+cla1.getTeacher());

        // 全校学生
        School.Class cla2 = scl.getClassList().stream()
                .max(Comparator.comparing(School.Class::getAge))
                .get();

        System.out.println("年龄较大的: "+ cla2.getTeacher());

    }
```

这里用了比较深层次的函数 `Comparator.comparing()`那么我么一起来解析一下这个函数吧!

```java
   public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
            Function<? super T, ? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
            (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }
```

首先这一段代码非常好理解,`Objects.requireNonNull(keyExtractor);` 判断入参不能为空.

`(Comparator<T> & Serializable)` 这一段代码可能很多人看的都已经迷糊了,这是啥意思?

其实`lambda` 实现接口的写法,我们都知道`lambda`的原理是接口函数,接口函数其实就是匿名内部类,**`(Comparator<T> & Serializable)`代码的含义就是,这个匿名内部类要实现,上述两个接口**

剩下这段代码就非常好理解了,` (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2))`这是一个`lambda`表达式,实现的是`int compare(T o1, T o2);`抽象方法.

![image-20240610131603982](https://cdn.jsdelivr.net/gh/jiangdeen/images@main/202406101316048.png)

还原一下整个函数的实现过程:

```java
  School.Class cla2 = scl.getClassList().stream()
                .max(Comparator.comparing(School.Class::getAge)).get();
```

1. `max` 方法接收一个`Comparator` 对象. 源码如下:
   - `Optional<T> max(Comparator<? super T> comparator);`
2. 用Comparator接口的静态方法 comparing()来构造 Comparator对象:
   - `Comparator.comparing(School.Class::getAge)`
3. `Comparator.comparing(School.Class::getAge)` 静态方法需要传入一个`lambda`表达式`Function`的匿名内部类
       
4. 最终 `Comparator.comparing` 使用了`lambda`表达式来构造`Comparator`对象
   	`public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
               Function<? super T, ? extends U> keyExtractor)
       {
           Objects.requireNonNull(keyExtractor);
           return (Comparator<T> & Serializable)
               (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
       }`
5. `keyExtractor.apply(c1)` `apply`方法的方法体是什么?
   - 答: `School.Class::getAge` 等价于 age-> age; 传入一个age返回一个age

6.  为什么`keyExtractor.apply(c1)`的返回值可以调用 `compareTo` 方法?
   - 因为`age`是`Integer`类型,`Integer`实现了`Comparable`接口

#### 3.6 reduce

```java
/**
 * 全校老师的总年龄
 */
public void testReduce() {
    // 一年级一班
    School.Class cl1 = new School.Class("王老师", 33, Arrays.asList("小明", "小红"));
    // 一年级二班
    School.Class cl2 = new School.Class("力老师", 44, Arrays.asList("大明", "大红"));
    School scl = new School("王校长", Arrays.asList(cl1, cl2));

    // 全校学生
    int sumAge = scl.getClassList().stream()
            .map(School.Class::getAge)
            .reduce(0, Integer::sum);
    System.out.println("全校老师的总年龄: " + sumAge);
}
```

`reduce` 的底层使用的是`BiFunction` 的`R apply(T t, U u);` 方法.



