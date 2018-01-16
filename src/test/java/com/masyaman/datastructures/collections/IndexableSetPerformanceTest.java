package com.masyaman.datastructures.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class IndexableSetPerformanceTest {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;

    private IndexableSet indexableSet;

    private int iterations;

    public IndexableSetPerformanceTest(int iterations) {
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(9999);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        indexableSet = new IndexableSet();
    }

    @Parameterized.Parameters(name = "{0} {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
                {10},
                {100},
                {1000},
                {10000},
//                {100000},
//                {1000000},
//                {10000000},
        });
    }

    @Test
    public void addToTail() throws Exception {
        for (int i = 0; i < iterations; i++) {
            indexableSet.add(addRandom());
            assertReference();
        }
    }

    @Test
    public void addToHead() throws Exception {
        for (int i = 0; i < iterations; i++) {
            indexableSet.add(0, addRandom(0));
            assertReference();
        }
    }

    @Test
    public void addToMiddle() throws Exception {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            indexableSet.add(index, addRandom(index));
            assertReference();
        }
    }

    @Test
    public void removeByIndex() throws Exception {
        init();
        while (!indexableSet.isEmpty()) {
            int index = removeRandomIndex();
            indexableSet.remove(index);
            assertReference();
        }
    }

    @Test
    public void removeByValue() throws Exception {
        init();
        while (!indexableSet.isEmpty()) {
            Long value = removeRandomValue();
            indexableSet.remove(value);
            assertReference();
        }
    }

    @Test
    public void contains() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            assertThat(indexableSet.contains(getRandomExisting())).isTrue();
            assertThat(indexableSet.contains(getRandomNotExisting())).isFalse();
        }
        assertReference();
    }

    @Test
    public void get() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(indexableSet.get(index)).isNotNull();
        }
        assertReference();
    }

    @Test
    public void indexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(indexableSet.indexOf(value)).isNotNull();
        }
        assertReference();
    }

    @Test
    public void addExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            indexableSet.add(addRandom());
            indexableSet.add(getRandomExisting());
            assertReference();
        }
    }

    @Test
    public void addRemoveContainsIndexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            // add
            int index = random.nextInt(elementsList.size() + 1);
            indexableSet.add(index, addRandom(index));

            // remove
            indexableSet.remove(removeRandomValue());

            // contains
            assertThat(indexableSet.contains(getRandomExisting())).isTrue();
            assertThat(indexableSet.contains(getRandomNotExisting())).isFalse();

            // indexOf
            index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(indexableSet.indexOf(value)).isNotNull();
        }
        assertReference();
    }

    private void init() {
//        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            indexableSet.add(index, addRandom(index)); // can be optimized
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
        elementsList.remove(index);
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
        return index;
    }

    private void assertReference() {
//        assertThat(elementsSet).isEqualTo(indexableSet);
//        assertThat(elementsList).isEqualTo(indexableSet);
    }
}