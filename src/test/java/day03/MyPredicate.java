package day03;

/**
 * 测试lambda 重载机制
 */
@FunctionalInterface
public interface MyPredicate<T> {

    boolean test(T t);

//    void add();
}
