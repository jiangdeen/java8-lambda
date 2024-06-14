# Lambda表达式深度解析(4)

### 一、方法引用

在`lambda`表达式中，有一个常见的语法，例如打印一个字符串，lambda表达式如下：

```java
cla -> Class::getTeacher
```

这种写法也可以引用构造函数来new一个对象:

```
Class::new
```

这种写法是`java8`为其提供的一种简写语法叫做`方法引用`。**虽然这是一个方法,,但是后面不需要加括号,因为这里只是一个方法引用,并不是真正的调用. 方法引用到真正执行的时候,会根据类型推断来进行实际的方法调用,这里实际上是包装了一个语法糖.**



### 二、元素顺序

你可能知道，一些集合类型中的元素都是按照顺序排列的，比如`List`;而另一些则是无序的，比如`HashSet`.增加了流的操作，顺序问题变得更为复杂。

直观上看，流是有序的，因为流中的元素都是按照顺序处理的。这种顺序称为`出现顺序`,出现顺序的定义依赖于数据源和对流的操作.

下面的代码测试永远通过:

```java
 @Test
    public void listSort() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<Integer> sameOrder = numbers.stream().collect(Collectors.toList());
        sameOrder.forEach(System.out::println);
        Assert.assertEquals(numbers,sameOrder);
    }
```

如果集合本身是无序的,由此生成的流也是无序的.`HashSet`就是一种无序的集合,因此不能保证每次都能通过.

```java
/**
     * 测试通过stream的HashSet是否有序
     */
    @Test
    public void hashSetSort() {
        Set<Integer> numbers= new HashSet<>(Arrays.asList(4,3,2,1));
        List<Integer> sameOrder = numbers.stream().collect(Collectors.toList());
        Assert.assertEquals(Arrays.asList(4,3,2,1),sameOrder);
    }
```

流的目的不仅是在集合类之间做转换, 而且还提供了一组处理数据的通用操作.有些集合本身无序,但有些操作有时会产生顺序

```java
 @Test
    public void hashSetSortOrdered() {
        Set<Integer> numbers= new HashSet<>(Arrays.asList(4,3,2,1));
        List<Integer> sameOrder = numbers.stream()
                .sorted()
                .collect(Collectors.toList());
        Assert.assertEquals(Arrays.asList(1,2,3,4),sameOrder);
    }
```

### 三、使用收集器

前面我们使用过`collect(toList())`在流中生成列表,下面我们来看一下生成其它集合或者值的用法.

#### 3.1 转成其他集合

```java
 @Test
    public void converTreeSet() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        LinkedList sameOrder = numbers.stream()
                .collect(Collectors.toCollection(LinkedList::new));
        Assert.assertEquals(Arrays.asList(1,2,3,4),sameOrder);
    }

stream().collect(Collectors.toCollection(TreeSet::new));
```

#### 3.2 转换成值

###### `minBy`,`maxBy`允许用户按照某种特定的顺序,生成一个值.

下面的代码就是找到学生总分数加起来最多的那个班级

```java
 @Test
    public void converValue() {
        School school = School.getSchool();
        Optional<School.Class> opt = school.getClassList().stream().collect(Collectors.maxBy(
                Comparator.comparing(it ->
                        it.getStudents().stream()
                                .collect(Collectors.summingDouble(s->s.getLanguageFraction()+s.getMathFraction())))
        ));
        System.out.println(opt.get().getTeacher());
        Assert.assertTrue(opt.isPresent());
    }
```

###### `averagingDouble` 计算平均数

找出全校语文平均分:

```java
 @Test
    public void average() {
        School school = School.getSchool();
        Double d = school.getClassList().stream().collect(
                Collectors.averagingDouble(
                        it -> it.getStudents().stream().mapToDouble(School.Class.Student::getLanguageFraction).sum()
                )

        );
        System.out.println(d);
        Assert.assertTrue(d > 60);
    }
```

#### 3.3 数据分组

###### `partitioningBy`收集器

```java
@Test
    public void partitioningBy() {
        School school = School.getSchool();
        Map<Boolean, List<School.Class>> d = school.getClassList().stream()
                .collect(
                        Collectors.partitioningBy(it -> it.getAge() > 40)
                );
        System.out.println(JSONObject.toJSON(d));
        Assert.assertTrue(d.size() > 1);
    }
```

`partitioningBy`接收一个`Predicate` 函数,返回一个`true`或者`false`,把一个列表一份为二,` ture`的形成一个集合,`false`的形成一个集合.

###### groupingBy收集器

`groupingBy`类似于`sql`当中`group by` 的作用,对列表按照指定的字段分组.

```java
@Test
public void groupBy() {
    School school = School.getSchool();
    Map<Integer, List<School.Class.Student>> listMap = school.getClassList().stream()
            .flatMap(it -> it.getStudents().stream())
            .collect(
                    Collectors.groupingBy(School.Class.Student::getAge)
            );
    System.out.println(JSONObject.toJSON(listMap));
    Assert.assertTrue(listMap.size() > 1);
}
```

### 四、自定义收集器

```java
package com.jiangdeen.lambda.reconfig;

public class StringCombiner {

    private final String prefix;
    private final String suffix;
    private final String delim;
    private final StringBuilder buIlder;

    public StringCombiner(String delim, String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.delim = delim;
        this.buIlder = new StringBuilder();
    }

    // BEGIN add
    public StringCombiner add (String word) {
        if(!this.areAtStart()) {
            this.buIlder.append(delim);
        }
        this.buIlder.append(word);

        return this;
    }
    // END add

    // BEGIN merge
    public StringCombiner merge (StringCombiner other) {
        if(!other.equals(this)) {
            if(!other.areAtStart() && !this.areAtStart()){
                other.buIlder.insert(0, this.delim);
            }
            this.buIlder.append(other.buIlder);
        }
        return this;
    }
    // END merge

    // BEGIN toString
    @Override
    public String toString() {
        return prefix + buIlder.toString() + suffix;
    }
    // END toString

    // BEGIN areAtStart
    private boolean areAtStart() {
        return buIlder.length() == 0;
    }
    // END areAtStart
}
```

#### 自定义字符串收集器

```java
package com.jiangdeen.lambda.reconfig;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 自定义收集器
 */
public class StringCollector implements Collector<String, StringCombiner,String> {

    private static final Set<Characteristics> characteristics = Collections.emptySet();

    private final String delim;
    private final String prefix;
    private final String suffix;

    public StringCollector(String delim, String prefix, String suffix) {
        this.delim = delim;
        this.prefix = prefix;
        this.suffix = suffix;
    }


    /**
     * 创建一个接收结果的可变容器
     * @return
     */
    @Override
    public Supplier<StringCombiner> supplier() {
        return ()-> new StringCombiner(delim,prefix,suffix);
    }

    /**
     * 将流中的元素放入可变容器中的逻辑, 方法
     * @return
     */
    @Override
    public BiConsumer<StringCombiner, String> accumulator() {
        return StringCombiner::add;
    }

    /**
     * 组合结果，当流被拆分成多个部分时，需要将多个结果合并。
     * @return
     */
    @Override
    public BinaryOperator<StringCombiner> combiner() {

        return StringCombiner::merge;
    }

    /**
     * 最后调用：在遍历完流后将结果容器A转换为最终结果R
     * @return
     */
    @Override
    public Function<StringCombiner, String> finisher() {
        return StringCombiner::toString;
    }

    /**
     * 返回一个描述收集器特征的不可变集合，用于告诉收集器时可以进行哪些优化，如并行化
     * @return
     */
    @Override
    public Set<Characteristics> characteristics() {
        return characteristics;
    }
}
```

1. 自定义收集器首先需要实现Collector接口

2. 然后需要重写接口当中的五个方法，方法名及作用如下

   

3. | 方法名            | 描述                                                         |
   | ----------------- | ------------------------------------------------------------ |
   | supplier()        | 创建一个接收结果的可变容器，在本例中使用自定类来做容器 `StringCombiner` |
   | accumulator()     | 将流中的元素放入可变容器中的逻辑, 方法 本例中使用`StringCombiner::add`方法来实现 |
   | combiner()        | 组合结果，当流被拆分成多个部分时，需要将多个结果合并。本例中使用`StringCombiner::merge`实现 |
   | finisher()        | 最后调用：在遍历完流后将结果容器A转换为最终结果R 把`StringCombiner`类`toString` |
   | characteristics() | 返回一个描述收集器特征的不可变集合，用于告诉收集器时可以进行哪些优化，如并行化 |

##### 自定义字符串收集器的用法：

```java
@Test
public void custom() {
    School school = School.getSchool();
    String s = school.getClassList().stream()
            .map(School.Class::getTeacher)
            .collect(new StringCollector(", ", "[", "]"));
    System.out.println(s);
    Assert.assertFalse(s.isEmpty());
}
```

`GitHub`地址:  [如果喜欢给个start;](https://github.com/jiangdeen/java8-lambda)