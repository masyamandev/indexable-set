package com.masyaman.datastructures.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SetPerformanceCompare {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;

    private List<Long> testList;

    private Class clazz;
    private int iterations;

    public SetPerformanceCompare(Class clazz, int iterations) {
        this.clazz = clazz;
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(9999);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        testList = (List<Long>) clazz.newInstance();
    }

    @Parameterized.Parameters(name = "{0} {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
//                {TreeListSet.class, 10000},
//                {TreeList.class, 10000},

                {TreeListSet.class, 10},
                {TreeListSet.class, 100},
                {TreeListSet.class, 1000},
                {TreeListSet.class, 10000},
                {TreeListSet.class, 100000},
//                {TreeListSet.class, 1000000},
//                {TreeListSet.class, 10000000},
        });
    }

    @Test
    public void addToTail() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            assertReference();
        }
    }

    @Test
    public void addToHead() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(0, addRandom(0));
            assertReference();
        }
    }

    @Test
    public void addToMiddle() throws Exception {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index));
            assertReference();
        }
    }

    @Test
    public void addToTreeSetFake() throws Exception {
        TreeSet<Long> tree = new TreeSet<>();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            tree.add(addRandom(index));
            assertReference();
        }
    }

    @Test
    public void removeByIndex() throws Exception {
        init();
        while (!testList.isEmpty()) {
            int index = removeRandomIndex();
            testList.remove(index);
            assertReference();
        }
    }

    @Test
    public void removeByValue() throws Exception {
        init();
        while (!testList.isEmpty()) {
            Long value = removeRandomValue();
            testList.remove(value);
            assertReference();
        }
    }

    @Test
    public void contains() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            assertThat(testList.contains(getRandomExisting())).isTrue();
            assertThat(testList.contains(getRandomNotExisting())).isFalse();
        }
        assertReference();
    }

    @Test
    public void get() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.get(index)).isNotNull();
        }
        assertReference();
    }

    @Test
    public void indexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.indexOf(value)).isNotNull();
        }
        assertReference();
    }

    @Test
    public void lastIndexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.lastIndexOf(value)).isNotNull();
        }
        assertReference();
    }

    @Test
    public void addExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            testList.add(getRandomExisting());
            assertReference();
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
        }
        assertReference();
    }

    private void init() {
//        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index)); // can be optimized
        }
        assertReference();
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

    private void assertReference() {
//        assertThat(elementsSet).isEqualTo(testList);
//        assertThat(elementsList).isEqualTo(testList);
    }
}