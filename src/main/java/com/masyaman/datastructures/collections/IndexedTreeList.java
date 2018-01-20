/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.masyaman.datastructures.collections;

import java.util.*;
import java.util.function.Function;

/**
 * As a <code>List</code> this data structure stores order of elements and
 * provides access by index. It's is optimised for fast insertions, removals
 * and searching by any index or object in the list.
 * As a <code>Set</code> this data structure stores unique elements only.
 * <p/>
 * TreeListSet can be suitable for tasks which requires fast modification in the
 * middle of a list and provides fast contains and indexOf.
 * <p/>
 * Insertions (head, tail, middle), removals(by index or by value) are O(log n)
 * in most cases but can be O((log n) ^ 2) in cases when List contains small
 * amount of unique elements.
 * indexOf is O(log n).
 * Contains is O(1) or O(log n) depending on Map implementation.
 *
 * Internally it uses HashMap and AVL tree.
 * HashMap can be replaced to TreeMap, this will slightly reduce overall performance
 * but will eliminate problems with hash collisions and hash table resizing.
 *
 * Code is based on apache common collections <code>TreeList</code>.
 * Comparing to <code>TreeList</code> this data structure:
 * <ul>
 * <li>Has slightly slower insertion/removing operations, O(log n) in most cases, O((log n) ^ 2) in
 * worst cases (if TreeMap is used).</li>
 * <li>Has greatly improved contains and indexOf operations, O(log n) while TreeList has O(n)</li>
 * </ul>
 *
 * As this implementation is slightly slower, it's recommended to use <code>TreeListSet</code> in
 * cases where unique elements should be stored.
 *
 * @author Aleksandr Maksymenko
 */
public class IndexedTreeList<E> extends AbstractTreeList<E, TreeSet<AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode>> implements Set<E> {

    private final Comparator<AVLNode> NODE_COMPARATOR = Comparator.comparingInt(AVLNode::getPosition);
    private final Function<E, TreeSet<AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode>> NEW_NODE_TREE_SET = k -> new TreeSet(NODE_COMPARATOR);

    private int size = 0;

    //-----------------------------------------------------------------------
    /**
     * Constructs a new empty list.
     */
    public IndexedTreeList() {
        this(new HashMap<>());
    }

    /**
     * Constructs a new empty list.
     * @param map Map implementation. It defines how elements would be compared. For example HashMap (by hashcode/equals),
     *            TreeMap (by compareTo or Comparator), IdentityHashMap (by identity). Specified map should be empty.
     */
    public IndexedTreeList(final Map map) {
        super(map);
    }

    /**
     * Constructs a new list that copies the specified collection.
     *
     * @param coll The collection to copy
     * @throws NullPointerException if the collection is null
     */
    public IndexedTreeList(final Collection<? extends E> coll) {
        this(coll, new HashMap<>());
    }

    /**
     * Constructs a new list that copies the specified collection.
     *
     * @param coll The collection to copy
     * @param map Map implementation. It defines how elements would be compared. For example HashMap (by hashcode/equals),
     *            TreeMap (by compareTo or Comparator), IdentityHashMap (by identity). Specified map should be empty.
     * @throws NullPointerException if the collection is null
     */
    public IndexedTreeList(final Collection<? extends E> coll, final Map map) {
        super(map);
        for (E e : coll) {
            add(e);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the current size of the list.
     *
     * @return the current size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Searches for the index of an object in the list.
     *
     * @param object  the object to search
     * @return the index of the object, -1 if not found
     */
    @Override
    public int indexOf(final Object object) {
        TreeSet<AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode> nodes = nodeMap.get(object);
        if (nodes == null || nodes.isEmpty()) {
            return -1;
        }
        return nodes.first().getPosition();
    }

    /**
     * Searches for the last index of an object in the list.
     *
     * @param object  the object to search
     * @return the index of the object, -1 if not found
     */
    @Override
    public int lastIndexOf(final Object object) {
        TreeSet<AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode> nodes = nodeMap.get(object);
        if (nodes == null || nodes.isEmpty()) {
            return -1;
        }
        return nodes.last().getPosition();
    }

    @Override
    public void add(int index, E obj) {
        super.add(index, obj);
        size++;
    }

    @Override
    public E remove(int index) {
        E obj = super.remove(index);
        if (obj != null) {
            size--;
        }
        return obj;
    }

    /**
     * Check if set does not contains an object.
     */
    @Override
    protected boolean canAdd(E e) {
        return true;
    }

    /**
     * Add node to nodeMap.
     */
    @Override
    protected void addToNodeMap(AVLNode node) {
        TreeSet<AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode> nodes = nodeMap.computeIfAbsent(node.getValue(), NEW_NODE_TREE_SET);
        nodes.add((AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode) (Object) node); // TODO remove cast
    }

    /**
     * Remove node from nodeMap.
     */
    @Override
    protected void removeFromNodeMap(AVLNode node) {
        TreeSet<AbstractTreeList<E, AbstractTreeList.AVLNode>.AVLNode> nodes = nodeMap.get(node.getValue());
        if (nodes == null) {
            return;
        }
        nodes.remove(node);
        if (nodes.isEmpty()) {
            nodeMap.remove(node.getValue());
        }
    }

}
