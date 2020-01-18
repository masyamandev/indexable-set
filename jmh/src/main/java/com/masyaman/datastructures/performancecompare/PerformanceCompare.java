package com.masyaman.datastructures.performancecompare;

import com.masyaman.datastructures.collections.IndexedTreeList;
import com.masyaman.datastructures.collections.TreeListSet;
import org.apache.commons.collections4.list.TreeList;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class PerformanceCompare {

    public static final Map<String, Class> CLASSES = Stream.of(TreeList.class, TreeListSet.class, IndexedTreeList.class)
            .collect(Collectors.toMap(c -> c.getSimpleName(), c -> c));

    @State(Scope.Benchmark)
    public static class Plan {

//        @Param({"1000000000"})
        @Param({"10", "1000000000"})
//        @Param({"10", "1000", "1000000000"})
        public int maxValue;

//        @Param({"1000000"})
        @Param({ "1000", "1000000"})
//        @Param({"100", "1000", "10000", "100000", "1000000"})
        public int size;

        @Param({"TreeListSet"})
//        @Param({"TreeList", "TreeListSet", "IndexedTreeList"})
        public String className;

        private Random random;
        private List<Integer> list;

        @Setup
        public void init() throws IllegalAccessException, InstantiationException {
            random = new Random();
            list = (List<Integer>) CLASSES.get(className).newInstance();

            if (list instanceof Set && maxValue <= size) {
                throw new IllegalArgumentException("Too small value is set for Set");
            }

            for (int i = 0; i < size; i++) {
                list.add(random.nextInt(maxValue));
            }
        }
    }


    @Benchmark
    public void addToTail(Plan plan, Blackhole blackhole) {
        List<Integer> list = plan.list;
        Random random = plan.random;
        list.clear();
        for (int i = 0; i < plan.size; i++) {
            list.add(random.nextInt(plan.maxValue));
        }
        blackhole.consume(list);
    }

    @Benchmark
    public void addToMiddle(Plan plan, Blackhole blackhole) {
        List<Integer> list = plan.list;
        Random random = plan.random;
        list.clear();
        for (int i = 0; i < plan.size; i++) {
            list.add(random.nextInt(list.size() + 1), random.nextInt(plan.maxValue));
        }
        blackhole.consume(list);
    }

    @Benchmark
    public void addRemoveRandom(Plan plan, Blackhole blackhole) {
        List<Integer> list = plan.list;
        Random random = plan.random;
        for (int i = 0; i < plan.size; i++) {
            list.add(random.nextInt(list.size() + 1), random.nextInt(plan.maxValue));
            blackhole.consume(list.remove(random.nextInt(list.size())));
        }
    }

    @Benchmark
    public void addRemoveQueue(Plan plan, Blackhole blackhole) {
        List<Integer> list = plan.list;
        Random random = plan.random;
        for (int i = 0; i < plan.size; i++) {
            list.add(random.nextInt(plan.maxValue));
            blackhole.consume(list.remove(0));
        }
    }

    @Benchmark
    public void get(Plan plan, Blackhole blackhole) {
        List<Integer> list = plan.list;
        Random random = plan.random;
        for (int i = 0; i < plan.size; i++) {
            Integer value = list.get(random.nextInt(list.size()));
            blackhole.consume(value);
        }
    }

    @Benchmark
    public void iterate(Plan plan, Blackhole blackhole) {
        List<Integer> list = plan.list;
        Random random = plan.random;
        for (Integer value : list) {
            blackhole.consume(value);
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PerformanceCompare.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
