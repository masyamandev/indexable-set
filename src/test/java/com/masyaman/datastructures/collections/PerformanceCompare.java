package com.masyaman.datastructures.collections;

import org.apache.commons.collections4.list.TreeList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class PerformanceCompare {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;

    private List<Long> testList;

    private Class clazz;
    private int iterations;

    private long maxTimestamp;

    public PerformanceCompare(Class clazz, int iterations) {
        this.clazz = clazz;
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(9999);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        testList = (List<Long>) clazz.newInstance();
        maxTimestamp = System.currentTimeMillis() + 10000;
    }

    @Parameterized.Parameters(name = "{0} {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
//                {TreeListSet.class, 10000},
//                {IndexedTreeList.class, 10000},
//                {TreeList.class, 10000},
//                {ArrayList.class, 10000},
//                {TreeListSet.class, 100000},
//                {IndexedTreeList.class, 100000},
//                {TreeList.class, 100000},
//                {ArrayList.class, 100000},

                {TreeListSet.class, 10},
                {TreeListSet.class, 100},
                {TreeListSet.class, 1000},
                {TreeListSet.class, 10000},
                {TreeListSet.class, 100000},
//                {TreeListSet.class, 1000000},
//                {TreeListSet.class, 10000000},

                {IndexedTreeList.class, 10},
                {IndexedTreeList.class, 100},
                {IndexedTreeList.class, 1000},
                {IndexedTreeList.class, 10000},
                {IndexedTreeList.class, 100000},
//                {IndexedTreeList.class, 1000000},
//                {IndexedTreeList.class, 10000000},

//                {TreeList.class, 10},
//                {TreeList.class, 100},
//                {TreeList.class, 1000},
//                {TreeList.class, 10000},
//                {TreeList.class, 100000},
////                {TreeList.class, 1000000},
////                {TreeList.class, 10000000},
//
//                {ArrayList.class, 10},
//                {ArrayList.class, 100},
//                {ArrayList.class, 1000},
//                {ArrayList.class, 10000},
//                {ArrayList.class, 100000},
////                {ArrayList.class, 1000000},
////                {ArrayList.class, 10000000},
        });
    }

    @Test
    public void addToTail() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            checkTime(i);
        }
    }

    @Test
    public void addToHead() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(0, addRandom(0));
            checkTime(i);
        }
    }

    @Test
    public void addToMiddle() throws Exception {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index));
            checkTime(i);
        }
    }

    @Test
    public void addToTreeSetFake() throws Exception {
        TreeSet<Long> tree = new TreeSet<>();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            tree.add(addRandom(index));
            checkTime(i);
        }
    }

    @Test
    public void removeByIndex() throws Exception {
        init();
        while (!testList.isEmpty()) {
            int index = removeRandomIndex();
            testList.remove(index);
            checkTime(iterations - testList.size());
        }
    }

    @Test
    public void removeByValue() throws Exception {
        init();
        while (!testList.isEmpty()) {
            Long value = removeRandomValue();
            testList.remove(value);
            checkTime(iterations - testList.size());
        }
    }

    @Test
    public void contains() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            assertThat(testList.contains(getRandomExisting())).isTrue();
            assertThat(testList.contains(getRandomNotExisting())).isFalse();
            checkTime(i);
        }
    }

    @Test
    public void get() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.get(index)).isNotNull();
            checkTime(i);
        }
    }

    @Test
    public void indexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.indexOf(value)).isNotNull();
            checkTime(i);
        }
    }

    @Test
    public void lastIndexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.lastIndexOf(value)).isNotNull();
            checkTime(i);
        }
    }

    @Test
    public void addRandomExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            testList.add(getRandomExisting());
            checkTime(i);
        }
    }

    @Test
    public void addSameExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            testList.add(elementsList.get(0));
            checkTime(i);
        }
    }

    @Test
    public void addRemoveContainsIndexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            // add
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index));

            // remove
            testList.remove(removeRandomValue());

            // contains
            assertThat(testList.contains(getRandomExisting())).isTrue();
            assertThat(testList.contains(getRandomNotExisting())).isFalse();

            // indexOf
            index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.indexOf(value)).isNotNull();

            checkTime(i);
        }
    }

    private void init() {
//        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index)); // can be optimized
        }
        checkTime(0);
//        System.out.println("Init time: " + (System.currentTimeMillis() - start));
    }

    private Long addRandom() {
        while (true) {
            Long value = random.nextLong();
            if (elementsSet.add(value)) {
                elementsList.add(value);
                return value;
            }
        }
    }

    private Long addRandom(int index) {
        return addRandom();
    }

    private Long getRandomExisting() {
        if (elementsList.isEmpty()) {
            return null;
        }
        return elementsList.get(random.nextInt(elementsList.size()));
    }

    private Long getRandomNotExisting() {
        while (true) {
            Long value = random.nextLong();
            if (!elementsSet.contains(value)) {
                return value;
            }
        }
    }

    private Long removeRandomValue() {
        if (elementsList.isEmpty()) {
            return null;
        }
        int index = random.nextInt(elementsList.size());
        Long value = elementsList.get(index);
        elementsSet.remove(value);
        elementsList.set(index, elementsList.get(elementsList.size() - 1));
        elementsList.remove(elementsList.size() - 1);
        return value;
    }

    private int removeRandomIndex() {
        if (elementsList.isEmpty()) {
            return -1;
        }
        int index = random.nextInt(elementsList.size());
        Long value = elementsList.get(index);
        elementsSet.remove(value);
        elementsList.set(index, elementsList.get(elementsList.size() - 1));
        elementsList.remove(elementsList.size() - 1);
        return index;
    }

    private void checkTime(int i) {
        if (System.currentTimeMillis() > maxTimestamp) {
            fail("Too long execution, done " + i + " iterations of total " + iterations);
        }
    }
}