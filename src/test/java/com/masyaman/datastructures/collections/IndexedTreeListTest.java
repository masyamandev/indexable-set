package com.masyaman.datastructures.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class IndexedTreeListTest {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;
    private List<Long> removedList;

    private IndexedTreeList<Long> testList;

    private int seed;
    private int iterations;

    public IndexedTreeListTest(int seed, int iterations) {
        this.seed = seed;
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(seed);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        removedList = new ArrayList<>();
        testList = new IndexedTreeList<>();
    }

    @Parameterized.Parameters(name = "{0} {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
                {9999, 1},
                {9999, 2},
                {9999, 3},
                {9999, 4},
                {9999, 5},
                {9999, 10},
                {9999, 100},
                {9999, 1000},
//                {9999, 10000},
        });
    }

//    @Parameterized.Parameters(name = "{0} {1}")
//    public static Collection parameters() {
//        ArrayList params = new ArrayList();
//        Random r = new Random();
//        for (int i = 0; i < 1000; i++) {
//            params.add(new Object[] {r.nextInt(), r.nextInt(1 << (r.nextInt(12))) + 1});
//        }
//        return params;
//    }

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
            assertThat(testList.contains(value)).isTrue();
            testList.remove(value);
            assertThat(testList.contains(value)).isFalse();
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
            assertThat(testList.get(index)).isEqualTo(value);
        }
        assertReference();
    }

    @Test
    public void indexOf() throws Exception {
        init();
        int initialSize = elementsList.size();
        for (int i = 0; i < initialSize; i++) {
            elementsList.add(elementsList.get(i));
            testList.add(elementsList.get(i));
        }
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(initialSize);
            Long value = elementsList.get(index);
            assertThat(testList.indexOf(value)).isEqualTo(index);
        }
        assertReference();
    }

    @Test
    public void lastIndexOf() throws Exception {
        init();
        int initialSize = elementsList.size();
        for (int i = 0; i < initialSize; i++) {
            elementsList.add(elementsList.get(i));
            testList.add(elementsList.get(i));
        }
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(initialSize);
            Long value = elementsList.get(index);
            assertThat(testList.lastIndexOf(value)).isEqualTo(index + initialSize);
        }
        assertReference();
    }

    @Test
    public void indexes() throws Exception {
        init();
        int initialSize = elementsList.size();
        for (int i = 0; i < initialSize; i++) {
            elementsList.add(elementsList.get(i));
            testList.add(elementsList.get(i));
        }
        for (int i = 0; i < initialSize; i++) {
            elementsList.add(elementsList.get(i));
            testList.add(i, elementsList.get(i));
        }
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(initialSize);
            Long value = elementsList.get(index);
            assertThat(testList.indexes(value)).containsExactly(index, index + initialSize, index + initialSize * 2);
        }
        assertReference();
    }

    @Test
    public void addRandomExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            Long randomExisting = getRandomExisting();
            testList.add(randomExisting);
            elementsList.add(randomExisting);
            assertReference();
        }
    }

    @Test
    public void addSameExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testList.add(addRandom());
            testList.add(elementsList.get(0));
            elementsList.add(elementsList.get(0));
            assertReference();
        }
    }

    @Test
    public void addContainsIndexOfRemove() throws Exception {
        init();
        while (!testList.isEmpty()) {
            // add
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index));

            // contains
            assertThat(testList.contains(getRandomExisting())).isTrue();
            assertThat(testList.contains(getRandomNotExisting())).isFalse();

            // indexOf
            index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testList.indexOf(value)).isEqualTo(index);

            // remove
            testList.remove(removeRandomValue());
            testList.remove(removeRandomIndex());

            assertReference();
        }
    }

    private void init() {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testList.add(index, addRandom(index)); // can be optimized
        }
        assertReference();
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
        while (true) {
            Long value = random.nextLong();
            if (elementsSet.add(value)) {
                elementsList.add(index, value);
                return value;
            }
        }
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

    private Long getRandomRemoved() {
        if (removedList.isEmpty()) {
            return null;
        }
        return removedList.get(random.nextInt(removedList.size()));
    }

    private Long removeRandomValue() {
        if (elementsList.isEmpty()) {
            return null;
        }
        int index = random.nextInt(elementsList.size());
        Long value = elementsList.get(index);
        elementsSet.remove(value);
        elementsList.remove(index);
        removedList.add(value);
        return value;
    }

    private int removeRandomIndex() {
        if (elementsList.isEmpty()) {
            return -1;
        }
        int index = random.nextInt(elementsList.size());
        Long value = elementsList.get(index);
        elementsSet.remove(value);
        elementsList.remove(index);
        removedList.add(value);
        return index;
    }

    private void assertReference() {
        assertThat(testList).hasSameSizeAs(elementsList);
        assertThat(testList).isEqualTo(elementsList);
        assertThat(testList.uniqueValues()).isEqualTo(elementsSet);
        testList.assertConsistent();
    }
}