package com.masyaman.datastructures.collections;

import java.util.*;

public class IndexableSet<T> extends AbstractList<T> implements List<T>, Set<T> {

    private Map<T, Node> nodeMap = new HashMap<>();

    private Node headNode = null;

//    @Override
//    public boolean add(T element) {
//        if (nodeMap.containsKey(element)) {
//            return false;
//        }
//        nodeMap.put(element, headNode);
//        return super.add(element);
//    }

    @Override
    public void add(int index, T element) {
        if (nodeMap.containsKey(element)) {
            return;
        }
        nodeMap.put(element, getHeadNode().add(index, element));
    }

    @Override
    public boolean contains(Object o) {
        return nodeMap.containsKey(o);
    }

    @Override
    public T remove(int index) {
        T element = headNode.remove(index).element;
        nodeMap.remove(element);
        return element;
    }

    @Override
    public boolean remove(Object o) {
        Node node = nodeMap.remove(o);
        if (node == null) {
            return false;
        }
        node.remove();
        node.updateSize();
        return true;
    }

    @Override
    public T get(int index) {
        return headNode.get(index).element;
    }

    @Override
    public int size() {
        if (nodeMap.size() == 0) {
            assert(headNode == null);
        } else {
            assert(nodeMap.size() == headNode.size);
        }
        return nodeMap.size();
    }

    @Override
    public int indexOf(Object o) {
        Node node = nodeMap.get(o);
        if (node == null) {
            return -1;
        }
        return node.index(true);
    }

    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }

    @Override
    public Spliterator<T> spliterator() {
        return nodeMap.keySet().spliterator();
    }


    private void updateParent(Node node, Node newChild) {
        if (node == null) {
            return;
        }
        if (node.parent == null) {
            headNode = newChild;
        } else if (node.parent.left == node) {
            node.parent.left = newChild;
        } else {
            node.parent.right = newChild;
        }
        if (newChild != null) {
            newChild.parent = node.parent;
        }
    }

    private Node getHeadNode() {
        if (headNode == null) {
            headNode = new Node(null);
        }
        return headNode;
    }

    // TODO make red/black
    private class Node {
        int size = 0;
        T element = null;

        Node parent;
        Node left;
        Node right;

        public Node(Node parent) {
            this.parent = parent;
        }

        Node get(int index) {
            if (index < leftSize()) {
                return getLeft().get(index);
            } else if (index == leftSize()) {
                return this;
            } else {
                return getRight().get(index - leftSize() - 1);
            }
        }

        Node set(int index, T value) {
            if (index < leftSize()) {
                return getLeft().set(index, value);
            } else if (index == leftSize()) {
                element = value;
                return this;
            } else {
                return getRight().set(index - leftSize() - 1, value);
            }
        }

        Node add(int index, T value) {
            size++;
            if (index < leftSize()) {
                return getLeft().add(index, value);
            } else if (index == leftSize()) {
                if (element == null) {
                    element = value;
                    return this;
                } else {
                    return getLeft().add(leftSize(), value);
                }
            } else {
                return getRight().add(index - leftSize() - 1, value);
            }
        }

        Node remove() {
            return remove(leftSize());
        }

        Node remove(int index) {
            if (index < leftSize()) {
                return getLeft().remove(index);
            } else if (index == leftSize()) {
                Node replace;
                Node oldParent;
                if (rightSize() > 0) {
                    replace = getRight().get(0);
                    if (replace.parent != this) {
                        assert (replace.parent.left == replace);
                    }
                    assert(replace.left == null);
                    oldParent = replace.parent;
                    updateParent(replace, replace.right);
                } else if (leftSize() > 0) {
                    replace = getLeft().get(leftSize() - 1);
                    if (replace.parent != this) {
                        assert (replace.parent.right == replace);
                    }
                    assert(replace.right == null);;
                    oldParent = replace.parent;
                    updateParent(replace, replace.left);
                } else {
                    updateParent(this, null);
                    if (parent != null) {
                        parent.updateSize();
                    }
                    return this;
                }

                updateParent(this, replace);
                replace.left = left;
                replace.right = right;
                if (left != null) {
                    left.parent = replace;
                }
                if (right != null) {
                    right.parent = replace;
                }
                // TODO if
                oldParent.updateSize();
                replace.updateSize();

                return this;
            } else {
                return getRight().remove(index - leftSize() - 1);
            }
        }

        public int index(boolean addLeft) {
            int index = 0;
            if (addLeft) {
                index += leftSize();
            }
            if (parent != null) {
                if (parent.right == this) {
                    index += parent.index(true) + 1;
                } else {
                    index += parent.index(false);
                }
            }
            return index;
        }

        private void updateSize() {
            size = leftSize() + rightSize() + 1;
            if (parent != null) {
                parent.updateSize();
            }
        }

        private Node getLeft() {
            if (left == null) {
                left = new Node(this);
            }
            return left;
        }

        private Node getRight() {
            if (right == null) {
                right = new Node(this);
            }
            return right;
        }

        private int leftSize() {
            if (left == null) {
                return 0;
            } else {
                return left.size;
            }
        }

        private int rightSize() {
            if (right == null) {
                return 0;
            } else {
                return right.size;
            }
        }

        @Override
        public String toString() {
            return "" + size + " [" + leftSize() + ", " + rightSize() + "]: " + element;
        }
    }
}
