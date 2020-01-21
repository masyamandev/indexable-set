package com.masyaman.datastructures.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Parameterized.class)
public class IndexedTreeListSetTest {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;
    private List<Long> removedList;

    private IndexedTreeListSet<Long> testListSet;

    private int seed;
    private int iterations;

    public IndexedTreeListSetTest(int seed, int iterations) {
        this.seed = seed;
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(seed);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        removedList = new ArrayList<>();
        testListSet = new IndexedTreeListSet<>();
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
            testListSet.add(addRandom());
            assertReference();
        }
    }

    @Test
    public void addToHead() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testListSet.add(0, addRandom(0));
            assertReference();
        }
    }

    @Test
    public void addToMiddle() throws Exception {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testListSet.add(index, addRandom(index));
            assertReference();
        }
    }

    @Test
    public void setNewValue() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            final Long value = elementsList.get(index);
            elementsSet.remove(value);
            final long newValue = random.nextLong();
            elementsSet.add(newValue);
            elementsList.set(index, newValue);
            final Long oldValue = testListSet.set(index, newValue);
            assertThat(oldValue).isEqualTo(value);
            assertReference();
        }
    }

    @Test
    public void exceptionOnSetNewValue() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int indexFrom = random.nextInt(elementsList.size());
            int indexTo = random.nextInt(elementsList.size());
            final Long value = elementsList.get(indexFrom);
            final Long oldValue = testListSet.set(indexTo, value);
            assertThat(oldValue).isEqualTo(elementsList.get(indexTo));
            if (indexFrom != indexTo) {
                elementsSet.remove(elementsList.get(indexTo));
                elementsList.set(indexTo, elementsList.get(indexFrom));
                elementsList.remove(indexFrom);
            }
            assertReference();
        }
    }

    @Test
    public void removeByIndex() throws Exception {
        init();
        while (!testListSet.isEmpty()) {
            int index = removeRandomIndex();
            testListSet.remove(index);
            assertReference();
        }
    }

    @Test
    public void removeByValue() throws Exception {
        init();
        while (!testListSet.isEmpty()) {
            Long value = removeRandomValue();
            assertThat(testListSet.contains(value)).isTrue();
            testListSet.remove(value);
            assertThat(testListSet.contains(value)).isFalse();
            assertReference();
        }
    }

    @Test
    public void removeFirstLast() throws Exception {
        init();
        while (!testListSet.isEmpty()) {
            removeByIndex(0);
            testListSet.remove(0);
            if (testListSet.size() > 0) {
                removeByIndex(testListSet.size() - 1);
                testListSet.remove(testListSet.size() - 1);
            }
            assertReference();
        }
    }

    @Test
    public void contains() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            assertThat(testListSet.contains(getRandomExisting())).isTrue();
            assertThat(testListSet.contains(getRandomNotExisting())).isFalse();
        }
        assertReference();
    }

    @Test
    public void get() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testListSet.get(index)).isEqualTo(value);
        }
        assertReference();
    }

    @Test
    public void indexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testListSet.indexOf(value)).isEqualTo(index);
        }
        assertReference();
    }

    @Test
    public void lastIndexOf() throws Exception {
        init();
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testListSet.lastIndexOf(value)).isEqualTo(index);
        }
        assertReference();
    }

    @Test
    public void addExisting() throws Exception {
        for (int i = 0; i < iterations; i++) {
            testListSet.add(addRandom());
            testListSet.add(getRandomExisting());
            testListSet.add(random.nextInt(elementsList.size() + 1), getRandomExisting());
            assertReference();
        }
    }

    @Test
    public void addContainsIndexOfRemove() throws Exception {
        init();
        while (!testListSet.isEmpty()) {
            // add
            int index = random.nextInt(elementsList.size() + 1);
            testListSet.add(index, addRandom(index));

            // contains
            assertThat(testListSet.contains(getRandomExisting())).isTrue();
            assertThat(testListSet.contains(getRandomNotExisting())).isFalse();

            // indexOf
            index = random.nextInt(elementsList.size());
            Long value = elementsList.get(index);
            assertThat(testListSet.get(index)).isEqualTo(value);
            assertThat(testListSet.indexOf(value)).isEqualTo(index);

            // remove
            final Long valueToRemove = removeRandomValue();
            assertThat(testListSet.remove(valueToRemove)).isTrue();
            final int indexToRemove = removeRandomIndex();
            final Long removedValue = testListSet.get(indexToRemove);
            assertThat(testListSet.remove(indexToRemove)).isEqualTo(removedValue);

            // check add nulls
            try {
                testListSet.add(null);
                fail("No exception on adding null");
            } catch (NullPointerException e) {}
            try {
                testListSet.add(random.nextInt(elementsList.size() + 1), null);
                fail("No exception on adding null");
            } catch (NullPointerException e) {}
            // check not existing indexOf
            assertThat(testListSet.indexOf(random.nextLong())).isEqualTo(-1);
            assertThat(testListSet.lastIndexOf(random.nextLong())).isEqualTo(-1);
            assertThat(testListSet.remove(random.nextLong())).isFalse();


            assertReference();
        }
    }

    @Test
    public void toArray() {
        init();

        for (int i = 0; i < iterations; i++) {
            testListSet.add(addRandom());
            testListSet.remove(removeRandomValue());
            assertReference();
        }

        assertThat(testListSet.toArray()).containsExactlyElementsOf(elementsList);
        assertThat(testListSet.toArray(new Long[0])).containsExactlyElementsOf(elementsList);
    }

    @Test
    public void clear() {
        init();
        testListSet.clear();
        elementsList.clear();
        elementsSet.clear();
        assertReference();
    }

    @Test
    public void constructorWithColection() {
        init();
        testListSet = new IndexedTreeListSet<>(elementsList);
        assertReference();
    }

    @Test
    public void constructorWithTreeMap() {
        init();
        testListSet = new IndexedTreeListSet<>(elementsList, new TreeMap());
        assertReference();
    }

    private void init() {
        for (int i = 0; i < iterations; i++) {
            int index = random.nextInt(elementsList.size() + 1);
            testListSet.add(index, addRandom(index)); // can be optimized
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
        removeByIndex(index);
        return index;
    }

    private void removeByIndex(final int index) {
        Long value = elementsList.get(index);
        elementsSet.remove(value);
        elementsList.remove(index);
        removedList.add(value);
    }

    private void assertReference() {
        assertThat(testListSet).hasSameSizeAs(elementsList);
        if (testListSet instanceof Set) {
            assertThat(elementsSet).isEqualTo(testListSet);
        } else {
            assertThat(elementsSet).isEqualTo(new HashSet<>(testListSet));
        }
        assertThat(elementsList).isEqualTo(testListSet);
        testListSet.assertConsistent();
    }
}