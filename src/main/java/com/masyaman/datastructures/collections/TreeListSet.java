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

/**
 * Data structure which implements both <code>List</code> and <code>Set</code>.
 * <p/>
 * As a <code>List</code> this data structure stores order of elements and
 * provides access by index. It's is optimised for fast insertions, removals
 * and searching by any index or object in the list.
 * As a <code>Set</code> this data structure stores unique elements only.
 * <p/>
 * TreeListSet can be suitable for tasks which requires fast modification in the
 * middle of a list and provides fast contains and indexOf.
 * <p/>
 * Insertions (head, tail, middle), removals(by index or by value) and indexOf
 * are all O(log n). Contains is O(1).
 *
 * Internally it uses HashMap and AVL tree.
 *
 * Code is based on apache common collections <code>TreeList</code>.
 */
public class TreeListSet<E> extends AbstractList<E> implements Set<E> {

    private Map<E, AVLNode> nodeMap = new HashMap<>();

    /** The root node in the AVL tree */
    private AVLNode root;

    /** The current size of the list */
    private int size;

    //-----------------------------------------------------------------------
    /**
     * Constructs a new empty list.
     */
    public TreeListSet() {
        super();
    }

    /**
     * Constructs a new empty list that copies the specified collection.
     *
     * @param coll  the collection to copy
     * @throws NullPointerException if the collection is null
     */
    public TreeListSet(final Collection<? extends E> coll) {
        super();
        for (E e : coll) {
            add(e);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the element at the specified index.
     *
     * @param index  the index to retrieve
     * @return the element at the specified index
     */
    @Override
    public E get(final int index) {
        checkInterval(index, 0, size() - 1);
        return root.get(index).getValue();
    }

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
     * Gets an iterator over the list.
     *
     * @return an iterator over the list
     */
    @Override
    public Iterator<E> iterator() {
        // override to go 75% faster
        return listIterator(0);
    }

    /**
     * Gets a ListIterator over the list.
     *
     * @return the new iterator
     */
    @Override
    public ListIterator<E> listIterator() {
        // override to go 75% faster
        return listIterator(0);
    }

    /**
     * Gets a ListIterator over the list.
     *
     * @param fromIndex  the index to start from
     * @return the new iterator
     */
    @Override
    public ListIterator<E> listIterator(final int fromIndex) {
        // override to go 75% faster
        // cannot use EmptyIterator as iterator.add() must work
        checkInterval(fromIndex, 0, size());
        return new TreeListIterator(this, fromIndex);
    }

    /**
     * Searches for the index of an object in the list.
     *
     * @param object  the object to search
     * @return the index of the object, -1 if not found
     */
    @Override
    public int indexOf(final Object object) {
        // override to go 75% faster
        AVLNode node = nodeMap.get(object);
        if (node == null) {
            return -1;
        }
        return node.getPosition();
    }

    /**
     * Searches for the last index of an object in the list.
     *
     * @param object  the object to search
     * @return the index of the object, -1 if not found
     */
    @Override
    public int lastIndexOf(final Object object) {
        return indexOf(object);
    }

    /**
     * Searches for the presence of an object in the list.
     *
     * @param object  the object to check
     * @return true if the object is found
     */
    @Override
    public boolean contains(final Object object) {
        return nodeMap.containsKey(object);
    }

    /**
     * Converts the list into an array.
     *
     * @return the list as an array
     */
    @Override
    public Object[] toArray() {
        // override to go 20% faster
        final Object[] array = new Object[size()];
        if (root != null) {
            root.toArray(array, root.relativePosition);
        }
        return array;
    }

    //-----------------------------------------------------------------------

    @Override
    public boolean add(E e) {
        if (nodeMap.containsKey(e)) {
            return false;
        }
        return super.add(e);
    }

    /**
     * Adds a new element to the list.
     *
     * @param index  the index to add before
     * @param obj  the element to add
     */
    @Override
    public void add(final int index, final E obj) {
        if (nodeMap.containsKey(obj)) {
            return;
        }
        modCount++;
        checkInterval(index, 0, size());
        if (root == null) {
            setRoot(new AVLNode(index, obj, null, null));
        } else {
            setRoot(root.insert(index, obj));
        }
        size++;
    }

    /**
     * Sets the element at the specified index.
     *
     * @param index  the index to set
     * @param obj  the object to store at the specified index
     * @return the previous object at that index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    @Override
    public E set(final int index, final E obj) {
        checkInterval(index, 0, size() - 1);
        final AVLNode node = root.get(index);
        final E result = node.value;
        node.setValue(obj);
        return result;
    }

    /**
     * Removes the element at the specified index.
     *
     * @param index  the index to remove
     * @return the previous object at that index
     */
    @Override
    public E remove(final int index) {
        modCount++;
        checkInterval(index, 0, size() - 1);
        final E result = get(index);
        setRoot(root.remove(index));
        size--;
        return result;
    }

    /**
     * Removes the element at the specified index.
     *
     * @param o element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    @Override
    public boolean remove(Object o) {
        // Some optimization can be done here
        int index = indexOf(o);
        if (index < 0) {
            return false;
        }
        remove(index);
        return true;
    }

    /**
     * Clears the list, removing all entries.
     */
    @Override
    public void clear() {
        modCount++;
        root = null;
        nodeMap.clear();
        size = 0;
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, Spliterator.ORDERED);
    }

    private void setRoot(AVLNode node) {
        root = node;
        if (node != null) {
            node.parent = null;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Checks whether the index is valid.
     *
     * @param index  the index to check
     * @param startIndex  the first allowed index
     * @param endIndex  the last allowed index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    private void checkInterval(final int index, final int startIndex, final int endIndex) {
        if (index < startIndex || index > endIndex) {
            throw new IndexOutOfBoundsException("Invalid index:" + index + ", size=" + size());
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Implements an AVLNode which keeps the offset updated.
     * <p>
     * This node contains the real work.
     * TreeList is just there to implement {@link java.util.List}.
     * The nodes don't know the index of the object they are holding.  They
     * do know however their position relative to their parent node.
     * This allows to calculate the index of a node while traversing the tree.
     * <p>
     * The Faedelung calculation stores a flag for both the left and right child
     * to indicate if they are a child (false) or a link as in linked list (true).
     */
    private class AVLNode {
        /** Parent node */
        private AVLNode parent;
        /** The left child node or the predecessor if {@link #leftIsPrevious}.*/
        private AVLNode left;
        /** Flag indicating that left reference is not a subtree but the predecessor. */
        private boolean leftIsPrevious;
        /** The right child node or the successor if {@link #rightIsNext}. */
        private AVLNode right;
        /** Flag indicating that right reference is not a subtree but the successor. */
        private boolean rightIsNext;
        /** How many levels of left/right are below this one. */
        private int height;
        /** The relative position, root holds absolute position. */
        private int relativePosition;
        /** The stored element. */
        private E value;

        /**
         * Constructs a new node with a relative position.
         *
         * @param relativePosition  the relative position of the node
         * @param obj  the value for the node
         * @param rightFollower the node with the value following this one
         * @param leftFollower the node with the value leading this one
         */
        private AVLNode(final int relativePosition, final E obj,
                        final AVLNode rightFollower, final AVLNode leftFollower) {
            this.relativePosition = relativePosition;
            rightIsNext = true;
            leftIsPrevious = true;
            setRight(rightFollower);
            setLeft(leftFollower);
            setValue(obj);
        }

        /**
         * Gets the value.
         *
         * @return the value of this node
         */
        E getValue() {
            return value;
        }

        /**
         * Sets the value.
         *
         * @param obj  the value to store
         */
        void setValue(final E obj) {
            if (this.value != null) {
                nodeMap.remove(this.value);
            }
            this.value = obj;
            nodeMap.put(obj, this);
        }

        /**
         * Locate the element with the given index relative to the
         * offset of the parent of this node.
         */
        AVLNode get(final int index) {
            final int indexRelativeToMe = index - relativePosition;

            if (indexRelativeToMe == 0) {
                return this;
            }

            final AVLNode nextNode = indexRelativeToMe < 0 ? getLeftSubTree() : getRightSubTree();
            if (nextNode == null) {
                return null;
            }
            return nextNode.get(indexRelativeToMe);
        }

        /**
         * Get position of this node.
         */
        int getPosition() {
            int position = 0;
            AVLNode node = this;
            while (node != null) {
                position += node.relativePosition;
                node = node.parent;
            }
            return position;
        }

        /**
         * Stores the node and its children into the array specified.
         *
         * @param array the array to be filled
         * @param index the index of this node
         */
        void toArray(final Object[] array, final int index) {
            array[index] = value;
            if (getLeftSubTree() != null) {
                left.toArray(array, index + left.relativePosition);
            }
            if (getRightSubTree() != null) {
                right.toArray(array, index + right.relativePosition);
            }
        }

        /**
         * Gets the next node in the list after this one.
         *
         * @return the next node
         */
        AVLNode next() {
            if (rightIsNext || right == null) {
                return right;
            }
            return right.min();
        }

        /**
         * Gets the node in the list before this one.
         *
         * @return the previous node
         */
        AVLNode previous() {
            if (leftIsPrevious || left == null) {
                return left;
            }
            return left.max();
        }

        /**
         * Inserts a node at the position index.
         *
         * @param index is the index of the position relative to the position of
         * the parent node.
         * @param obj is the object to be stored in the position.
         */
        AVLNode insert(final int index, final E obj) {
            final int indexRelativeToMe = index - relativePosition;

            if (indexRelativeToMe <= 0) {
                return insertOnLeft(indexRelativeToMe, obj);
            }
            return insertOnRight(indexRelativeToMe, obj);
        }

        private AVLNode insertOnLeft(final int indexRelativeToMe, final E obj) {
            if (getLeftSubTree() == null) {
                setLeft(new AVLNode(-1, obj, this, left), null);
            } else {
                setLeft(left.insert(indexRelativeToMe, obj), null);
            }

            if (relativePosition >= 0) {
                relativePosition++;
            }
            final AVLNode ret = balance();
            recalcHeight();
            return ret;
        }

        private AVLNode insertOnRight(final int indexRelativeToMe, final E obj) {
            if (getRightSubTree() == null) {
                setRight(new AVLNode(+1, obj, right, this), null);
            } else {
                setRight(right.insert(indexRelativeToMe, obj), null);
            }
            if (relativePosition < 0) {
                relativePosition--;
            }
            final AVLNode ret = balance();
            recalcHeight();
            return ret;
        }

        //-----------------------------------------------------------------------
        /**
         * Gets the left node, returning null if its a faedelung.
         */
        private AVLNode getLeftSubTree() {
            return leftIsPrevious ? null : left;
        }

        /**
         * Gets the right node, returning null if its a faedelung.
         */
        private AVLNode getRightSubTree() {
            return rightIsNext ? null : right;
        }

        /**
         * Gets the rightmost child of this node.
         *
         * @return the rightmost child (greatest index)
         */
        private AVLNode max() {
            return getRightSubTree() == null ? this : right.max();
        }

        /**
         * Gets the leftmost child of this node.
         *
         * @return the leftmost child (smallest index)
         */
        private AVLNode min() {
            return getLeftSubTree() == null ? this : left.min();
        }

        /**
         * Removes the node at a given position.
         *
         * @param index is the index of the element to be removed relative to the position of
         * the parent node of the current node.
         */
        AVLNode remove(final int index) {
            final int indexRelativeToMe = index - relativePosition;

            if (indexRelativeToMe == 0) {
                nodeMap.remove(value);
                return removeSelf();
            }
            if (indexRelativeToMe > 0) {
                setRight(right.remove(indexRelativeToMe), right.right);
                if (relativePosition < 0) {
                    relativePosition++;
                }
            } else {
                setLeft(left.remove(indexRelativeToMe), left.left);
                if (relativePosition > 0) {
                    relativePosition--;
                }
            }
            recalcHeight();
            return balance();
        }

        private AVLNode removeMax() {
            if (getRightSubTree() == null) {
                return removeSelf();
            }
            setRight(right.removeMax(), right.right);
            if (relativePosition < 0) {
                relativePosition++;
            }
            recalcHeight();
            return balance();
        }

        private AVLNode removeMin() {
            if (getLeftSubTree() == null) {
                return removeSelf();
            }
            setLeft(left.removeMin(), left.left);
            if (relativePosition > 0) {
                relativePosition--;
            }
            recalcHeight();
            return balance();
        }

        /**
         * Removes this node from the tree.
         *
         * @return the node that replaces this one in the parent
         */
        private AVLNode removeSelf() {
            if (getRightSubTree() == null && getLeftSubTree() == null) {
                return null;
            }
            if (getRightSubTree() == null) {
                if (relativePosition > 0) {
                    left.relativePosition += relativePosition + (relativePosition > 0 ? 0 : 1);
                }
                left.max().setRight(null, right);
                return left;
            }
            if (getLeftSubTree() == null) {
                right.relativePosition += relativePosition - (relativePosition < 0 ? 0 : 1);
                right.min().setLeft(null, left);
                return right;
            }

            if (heightRightMinusLeft() > 0) {
                // more on the right, so delete from the right
                final AVLNode rightMin = right.min();
                setValue(rightMin.value);
                if (leftIsPrevious) {
                    setLeft(rightMin.left);
                }
                setRight(right.removeMin());
                if (relativePosition < 0) {
                    relativePosition++;
                }
            } else {
                // more on the left or equal, so delete from the left
                final AVLNode leftMax = left.max();
                setValue(leftMax.value);
                if (rightIsNext) {
                    setRight(leftMax.right);
                }
                final AVLNode leftPrevious = left.left;
                setLeft(left.removeMax());
                if (left == null) {
                    // special case where left that was deleted was a double link
                    // only occurs when height difference is equal
                    setLeft(leftPrevious);
                    leftIsPrevious = true;
                }
                if (relativePosition > 0) {
                    relativePosition--;
                }
            }
            recalcHeight();
            return this;
        }

        //-----------------------------------------------------------------------
        /**
         * Balances according to the AVL algorithm.
         */
        private AVLNode balance() {
            switch (heightRightMinusLeft()) {
                case 1 :
                case 0 :
                case -1 :
                    return this;
                case -2 :
                    if (left.heightRightMinusLeft() > 0) {
                        setLeft(left.rotateLeft(), null);
                    }
                    return rotateRight();
                case 2 :
                    if (right.heightRightMinusLeft() < 0) {
                        setRight(right.rotateRight(), null);
                    }
                    return rotateLeft();
                default :
                    throw new RuntimeException("tree inconsistent!");
            }
        }

        /**
         * Gets the relative position.
         */
        private int getOffset(final AVLNode node) {
            if (node == null) {
                return 0;
            }
            return node.relativePosition;
        }

        /**
         * Sets the relative position.
         */
        private int setOffset(final AVLNode node, final int newOffest) {
            if (node == null) {
                return 0;
            }
            final int oldOffset = getOffset(node);
            node.relativePosition = newOffest;
            return oldOffset;
        }

        /**
         * Sets the height by calculation.
         */
        private void recalcHeight() {
            height = Math.max(
                getLeftSubTree() == null ? -1 : getLeftSubTree().height,
                getRightSubTree() == null ? -1 : getRightSubTree().height) + 1;
        }

        /**
         * Returns the height of the node or -1 if the node is null.
         */
        private int getHeight(final AVLNode node) {
            return node == null ? -1 : node.height;
        }

        /**
         * Returns the height difference right - left
         */
        private int heightRightMinusLeft() {
            return getHeight(getRightSubTree()) - getHeight(getLeftSubTree());
        }

        private AVLNode rotateLeft() {
            final AVLNode newTop = right; // can't be faedelung!
            final AVLNode movedNode = getRightSubTree().getLeftSubTree();

            final int newTopPosition = relativePosition + getOffset(newTop);
            final int myNewPosition = -newTop.relativePosition;
            final int movedPosition = getOffset(newTop) + getOffset(movedNode);

            setRight(movedNode, newTop);
            newTop.setLeft(this, null);

            setOffset(newTop, newTopPosition);
            setOffset(this, myNewPosition);
            setOffset(movedNode, movedPosition);
            return newTop;
        }

        private AVLNode rotateRight() {
            final AVLNode newTop = left; // can't be faedelung
            final AVLNode movedNode = getLeftSubTree().getRightSubTree();

            final int newTopPosition = relativePosition + getOffset(newTop);
            final int myNewPosition = -newTop.relativePosition;
            final int movedPosition = getOffset(newTop) + getOffset(movedNode);

            setLeft(movedNode, newTop);
            newTop.setRight(this, null);

            setOffset(newTop, newTopPosition);
            setOffset(this, myNewPosition);
            setOffset(movedNode, movedPosition);
            return newTop;
        }

        /**
         * Sets the left field to the node, or the previous node if that is null
         *
         * @param node  the new left subtree node
         * @param previous  the previous node in the linked list
         */
        private void setLeft(final AVLNode node, final AVLNode previous) {
            leftIsPrevious = node == null;
            setLeft(leftIsPrevious ? previous : node);
            recalcHeight();
        }

        /**
         * Sets the left field to the node, or the previous node if that is null
         *
         * @param node  the new left subtree node
         */
        private void setLeft(final AVLNode node) {
            left = node;
            if (left != null) {
                left.parent = this;
            }
        }

        /**
         * Sets the right field to the node, or the next node if that is null
         *
         * @param node  the new left subtree node
         * @param next  the next node in the linked list
         */
        private void setRight(final AVLNode node, final AVLNode next) {
            rightIsNext = node == null;
            setRight(rightIsNext ? next : node);
            recalcHeight();
        }

        /**
         * Sets the right field to the node, or the next node if that is null
         *
         * @param node  the new left subtree node
         */
        private void setRight(final AVLNode node) {
            right = node;
            if (right != null) {
                right.parent = this;
            }
        }

        /**
         * Used for debugging.
         */
        @Override
        public String toString() {
            return new StringBuilder()
                .append("AVLNode(")
                .append(relativePosition)
                .append(',')
                .append(left != null)
                .append(',')
                .append(value)
                .append(',')
                .append(getRightSubTree() != null)
                .append(", faedelung ")
                .append(rightIsNext)
                .append(" )")
                .toString();
        }
    }

    /**
     * A list iterator over the linked list.
     */
    private class TreeListIterator implements ListIterator<E> {
        /** The parent list */
        private final TreeListSet parent;
        /**
         * Cache of the next node that will be returned by {@link #next()}.
         */
        private AVLNode next;
        /**
         * The index of the next node to be returned.
         */
        private int nextIndex;
        /**
         * Cache of the last node that was returned by {@link #next()}
         * or {@link #previous()}.
         */
        private AVLNode current;
        /**
         * The index of the last node that was returned.
         */
        private int currentIndex;
        /**
         * The modification count that the list is expected to have. If the list
         * doesn't have this count, then a
         * {@link ConcurrentModificationException} may be thrown by
         * the operations.
         */
        private int expectedModCount;

        /**
         * Create a ListIterator for a list.
         *
         * @param parent  the parent list
         * @param fromIndex  the index to start at
         */
        protected TreeListIterator(final TreeListSet<E> parent, final int fromIndex) throws IndexOutOfBoundsException {
            super();
            this.parent = parent;
            this.expectedModCount = parent.modCount;
            this.next = parent.root == null ? null : parent.root.get(fromIndex);
            this.nextIndex = fromIndex;
            this.currentIndex = -1;
        }

        /**
         * Checks the modification count of the list is the value that this
         * object expects.
         *
         * @throws ConcurrentModificationException If the list's modification
         * count isn't the value that was expected.
         */
        protected void checkModCount() {
            if (parent.modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasNext() {
            return nextIndex < parent.size();
        }

        public E next() {
            checkModCount();
            if (!hasNext()) {
                throw new NoSuchElementException("No element at index " + nextIndex + ".");
            }
            if (next == null) {
                next = parent.root.get(nextIndex);
            }
            final E value = next.getValue();
            current = next;
            currentIndex = nextIndex++;
            next = next.next();
            return value;
        }

        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        public E previous() {
            checkModCount();
            if (!hasPrevious()) {
                throw new NoSuchElementException("Already at start of list.");
            }
            if (next == null) {
                next = parent.root.get(nextIndex - 1);
            } else {
                next = next.previous();
            }
            final E value = next.getValue();
            current = next;
            currentIndex = --nextIndex;
            return value;
        }

        public int nextIndex() {
            return nextIndex;
        }

        public int previousIndex() {
            return nextIndex() - 1;
        }

        public void remove() {
            checkModCount();
            if (currentIndex == -1) {
                throw new IllegalStateException();
            }
            parent.remove(currentIndex);
            if (nextIndex != currentIndex) {
                // remove() following next()
                nextIndex--;
            }
            // the AVL node referenced by next may have become stale after a remove
            // reset it now: will be retrieved by next call to next()/previous() via nextIndex
            next = null;
            current = null;
            currentIndex = -1;
            expectedModCount++;
        }

        public void set(final E obj) {
            checkModCount();
            if (current == null) {
                throw new IllegalStateException();
            }
            current.setValue(obj);
        }

        public void add(final E obj) {
            checkModCount();
            parent.add(nextIndex, obj);
            current = null;
            currentIndex = -1;
            nextIndex++;
            expectedModCount++;
        }
    }

}