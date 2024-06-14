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
