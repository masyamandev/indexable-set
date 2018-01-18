package com.masyaman.datastructures.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TreeListSetTest {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;
    private List<Long> removedList;

    private List<Long> treeListSet;

    private int iterations;

    public TreeListSetTest(int iterations) {
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(9999);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        removedList = new ArrayList<>();
        treeListSet = new TreeListSet<>();
    }

    @Parameterized.Parameters(name = "{0} {1}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
                {1},
                {2},
                {3},
                {4},
                {5},
                {10},
                {100},
                {1000},
//                {10000},
        });
    }

    @Test
    public void addToTail() throws Exception {
        for (int i = 0; i < iterations; i++) {
            treeListSet.add(addRandom());
            assertReference();
        }
    }

    @Test
    public void addToHead() throws Exception {
        for (int i = 0; i < iterations; i++) {
            treeListSet.add(0, addRandom(0));
            assertReference();
        }
    }

    @Test
    public void addToMiddle() throws Exception {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            treeListSet.add(index, addRandom(index));
            assertReference();
        }
    }

    @Test
    public void removeByIndex() throws Exception {
        init();
        while (!treeListSet.isEmpty()) {
            int index = removeRandomIndex();
            treeListSet.remove(index);
            assertReference();
        }
    }

    @Test
    public void removeByValue() throws Exception {
        init();
        while (!treeListSet.isEmpty()) {
            Long value = removeRandomValue();
            assertThat(treeListSet.contains(value)).isTrue();
            treeListSet.remove(value);
            assertThat(treeListSet.contains(value)).isFalse();
            assertReference();
        }
    }

    @Test
    public void contains() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            assertThat(treeListSet.contains(getRandomExisting())).isTrue();
            assertThat(treeListSet.contains(getRandomNotExisting())).isFalse();
        }
        assertReference();
    }

    @Test
    public void get() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(treeListSet.get(index)).isEqualTo(value);
        }
        assertReference();
    }

    @Test
    public void indexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(treeListSet.indexOf(value)).isEqualTo(index);
        }
        assertReference();
    }

    @Test
    public void lastIndexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(treeListSet.lastIndexOf(value)).isEqualTo(index);
        }
        assertReference();
    }

    @Test
    public void addExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            treeListSet.add(addRandom());
            treeListSet.add(getRandomExisting());
            assertReference();
        }
    }

    @Test
    public void addContainsIndexOfRemove() throws Exception {
        init();
        while (!treeListSet.isEmpty()) {
            // add
            int index = random.nextInt(elementsList.size() + 1);
            treeListSet.add(index, addRandom(index));

            // contains
            assertThat(treeListSet.contains(getRandomExisting())).isTrue();
            assertThat(treeListSet.contains(getRandomNotExisting())).isFalse();

            // indexOf
            index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(treeListSet.indexOf(value)).isEqualTo(index);

            // remove
            treeListSet.remove(removeRandomValue());
            treeListSet.remove(removeRandomIndex());

            assertReference();
        }
    }

    private void init() {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            treeListSet.add(index, addRandom(index)); // can be optimized
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
        if (treeListSet instanceof Set) {
            assertThat(elementsSet).isEqualTo(treeListSet);
        } else {
            assertThat(elementsSet).isEqualTo(new HashSet<>(treeListSet));
        }
        assertThat(elementsList).isEqualTo(treeListSet);
    }
}