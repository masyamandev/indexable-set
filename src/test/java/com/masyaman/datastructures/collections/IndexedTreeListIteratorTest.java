package com.masyaman.datastructures.collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class IndexedTreeListIteratorTest {

    private Random random;
    private Set<Long> elementsSet;
    private List<Long> elementsList;
    private List<Long> removedList;

    private Class listClass;
    private List<Long> testList;

    private int seed;
    private int iterations;

    public IndexedTreeListIteratorTest(Class listClass, int seed, int iterations) {
        this.listClass = listClass;
        this.seed = seed;
        this.iterations = iterations;
    }

    @Before
    public void setUp() throws Exception {
        random = new Random(seed);
        elementsSet = new HashSet<>();
        elementsList = new ArrayList<>();
        removedList = new ArrayList<>();
        testList = (List<Long>) listClass.newInstance();
    }

    @Parameterized.Parameters(name = "{0} {1} {2}")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
                { IndexedTreeListSet.class, 9999, 1},
                { IndexedTreeListSet.class, 9999, 2},
                { IndexedTreeListSet.class, 9999, 3},
                { IndexedTreeListSet.class, 9999, 4},
                { IndexedTreeListSet.class, 9999, 5},
                { IndexedTreeListSet.class, 9999, 10},
                { IndexedTreeListSet.class, 9999, 100},
                { IndexedTreeListSet.class, 9999, 1000},
//                {TreeListSet.class, 9999, 10000},

                {IndexedTreeList.class, 9999, 1},
                {IndexedTreeList.class, 9999, 2},
                {IndexedTreeList.class, 9999, 3},
                {IndexedTreeList.class, 9999, 4},
                {IndexedTreeList.class, 9999, 5},
                {IndexedTreeList.class, 9999, 10},
                {IndexedTreeList.class, 9999, 100},
                {IndexedTreeList.class, 9999, 1000},
//                {IndexedTreeList.class, 9999, 10000},

//                {TreeList.class, 9999, 1},
//                {TreeList.class, 9999, 2},
//                {TreeList.class, 9999, 3},
//                {TreeList.class, 9999, 4},
//                {TreeList.class, 9999, 5},
//                {TreeList.class, 9999, 10},
//                {TreeList.class, 9999, 100},
//                {TreeList.class, 9999, 1000},
////                {TreeList.class, 9999, 10000},
        });
    }

//    @Parameterized.Parameters(name = "{0} {1} {2}")
//    public static Collection parameters() {
//        ArrayList params = new ArrayList();
//        Random r = new Random();
//        for (int i = 0; i < 1000; i++) {
//            params.add(new Object[] {TreeListSet.class, r.nextInt(), r.nextInt(1 << (r.nextInt(12))) + 1});
//            params.add(new Object[] {IndexedTreeList.class, r.nextInt(), r.nextInt(1 << (r.nextInt(12))) + 1});
//            params.add(new Object[] {TreeList.class, r.nextInt(), r.nextInt(1 << (r.nextInt(12))) + 1});
//        }
//        return params;
//    }
    
    @Test
    public void iteratorTest() throws Exception {
        init();
        Iterator<Long> arrayListIterator = elementsList.iterator();
        Iterator<Long> testListIterator = testList.iterator();
        assertReference();
        while (arrayListIterator.hasNext()) {
            Long element = arrayListIterator.next();
            assertThat(testListIterator.next()).isEqualTo(element);
            if (random.nextBoolean()) {
                arrayListIterator.remove();
                testListIterator.remove();
                removedList.add(element);
            }
        }
        elementsSet.removeAll(removedList);
        assertReference();
    }

    @Test
    public void listIteratorForwardTest() throws Exception {
        init();
        ListIterator<Long> arrayListIterator = elementsList.listIterator();
        ListIterator<Long> testListIterator = testList.listIterator();
        assertReference();
        while (arrayListIterator.hasNext()) {
            Long element = arrayListIterator.next();
            assertThat(testListIterator.next()).isEqualTo(element);
            assertThat(testListIterator.nextIndex()).isEqualTo(arrayListIterator.nextIndex());
            assertThat(testListIterator.previousIndex()).isEqualTo(arrayListIterator.previousIndex());
            if (random.nextBoolean()) {
                arrayListIterator.remove();
                testListIterator.remove();
                removedList.add(element);
            } else if (random.nextBoolean()) {
                Long val = getRandomNotExisting();
                arrayListIterator.set(val);
                testListIterator.set(val);
                removedList.add(element);
                elementsSet.add(val);
            }
            if (random.nextBoolean()) {
                Long val = getRandomNotExisting();
                arrayListIterator.add(val);
                testListIterator.add(val);
                elementsSet.add(val);
            }
        }
        elementsSet.removeAll(removedList);
        assertReference();
    }

    @Test
    public void listIteratorBackwardTest() throws Exception {
        init();
        ListIterator<Long> arrayListIterator = elementsList.listIterator(elementsList.size() - 1);
        ListIterator<Long> testListIterator = testList.listIterator(elementsList.size() - 1);
        assertReference();
        while (arrayListIterator.hasPrevious()) {
            Long element = arrayListIterator.previous();
            assertThat(testListIterator.previous()).isEqualTo(element);
            assertThat(testListIterator.nextIndex()).isEqualTo(arrayListIterator.nextIndex());
            assertThat(testListIterator.previousIndex()).isEqualTo(arrayListIterator.previousIndex());
            if (random.nextBoolean()) {
                arrayListIterator.remove();
                testListIterator.remove();
                removedList.add(element);
            } else if (random.nextBoolean()) {
                Long val = getRandomNotExisting();
                arrayListIterator.set(val);
                testListIterator.set(val);
                removedList.add(element);
                elementsSet.add(val);
            }
            if (random.nextBoolean()) {
                Long val = getRandomNotExisting();
                arrayListIterator.add(val);
                testListIterator.add(val);
                elementsSet.add(val);
            }
        }
        elementsSet.removeAll(removedList);
        assertReference();
    }

    @Test
    public void listIteratorRandomizedTest() throws Exception {
        init();
        int initialPosition = random.nextInt(elementsList.size());
        ListIterator<Long> arrayListIterator = elementsList.listIterator(initialPosition);
        ListIterator<Long> testListIterator = testList.listIterator(initialPosition);
        assertReference();

        Long element = null;
        for (int i = 0; i < iterations; i++) {
            assertThat(testListIterator.hasNext()).isEqualTo(arrayListIterator.hasNext());
            assertThat(testListIterator.hasPrevious()).isEqualTo(arrayListIterator.hasPrevious());
            assertThat(testListIterator.nextIndex()).isEqualTo(arrayListIterator.nextIndex());
            assertThat(testListIterator.previousIndex()).isEqualTo(arrayListIterator.previousIndex());

            int operation = random.nextInt(5);
            //System.out.println("" + i + " : " + arrayListIterator.previousIndex() + ", " + arrayListIterator.nextIndex() + " : " + operation);

            switch (operation) {
                case 0:
                    if (arrayListIterator.hasNext()) {
                        element = arrayListIterator.next();
                        assertThat(testListIterator.next()).isEqualTo(element);
                    }
                    break;
                case 1:
                    if (arrayListIterator.hasPrevious()) {
                        element = arrayListIterator.previous();
                        assertThat(testListIterator.previous()).isEqualTo(element);
                    }
                    break;
                case 2:
                    if (element != null) {
                        arrayListIterator.remove();
                        testListIterator.remove();
                        removedList.add(element);
                        element = null;
                    }
                    break;
                case 3:
                    if (element != null) {
                        Long val = getRandomNotExisting();
                        arrayListIterator.set(val);
                        testListIterator.set(val);
                        removedList.add(element);
                        elementsSet.add(val);
                        element = val;
                    }
                    break;
                case 4:
                    Long val = getRandomNotExisting();
                    arrayListIterator.add(val);
                    testListIterator.add(val);
                    elementsSet.add(val);
                    element = null;
                    break;
            }
            assertThat(testList).isEqualTo(elementsList);
        }
        elementsSet.removeAll(removedList);
        assertReference();
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
        if (testList instanceof Set) {
            assertThat(elementsSet).isEqualTo(testList);
        } else {
            assertThat(elementsSet).isEqualTo(new HashSet<>(testList));
        }
        assertThat(elementsList).isEqualTo(testList);

        if (testList instanceof AbstractIndexedTreeList) {
            ((AbstractIndexedTreeList) testList).assertConsistent();
        }
    }
}