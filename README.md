# Overview

TreeListSet is a data structure which can be described as a sequence of unique elements. 
All main operations like access to any element by index, insert to and remove from 
any position of sequence have complexity of O(log n). Unlike other sequences it provides 
search capabilities: it’s allows to check if sequence contains some element and get it’s 
position in sequence with complexity O(log n).

TreeListSet implements both List and Set.

IndexedTreeSet is the similar structure, but it does not have restriction for unique objects.
This structure is slightly slower than TreeListSet.

# Comparing with other data structures

Feature | ArrayList | LinkedList | TreeSet | HashSet | (apache) TreeList | TreeListSet | IndexedTreeListSet*
--- | --- | --- | --- | --- | --- | --- | ---
Sequence (List) | + | + | - | - | + | + | +
Unique elements (Set) | - | - | + | + | - | + | -
Get by index | O(1) | O(n) | N/A | N/A | O(log n) | O(log n) | O(log n)
Add to tail | O(1) | O(1) | O(log n) | O(1) | O(log n) | O(log n) | O((log n) * (1 + log m))
Add to specified position | O(n) | O(n) | N/A | N/A | O(log n) | O(log n) | O((log n) * (1 + log m))
Remove from last position | O(1) | O(1) | N/A | N/A | O(log n) | O(log n) | O((log n) * (1 + log m))
Remove from specified position | O(n) | O(n) | N/A | N/A | O(log n) | O(log n) | O((log n) * (1 + log m))
Remove by value | O(n) | O(n) | O(log n) | O(1) | O(n) | O(log n) | O((log n) * (1 + log m))
Contains | O(n) | O(n) | O(log n) | O(1) | O(n) | O(1) or O(log n) | O(1) or O(log n)
Index of | O(n) | O(n) | O(log n) | O(1) | O(n) | O(log n) | O(log n)

\* m is amount of elements equals to inserted/removed element.

# Internal structure description

Internally it’s represented by binary tree. It’s very similar to TreeList data structure 
from apache commons, but nodes have links to parent. Each node has links lo left node, 
right node, parent node, sequence element and relative position to parent node. Root node 
has absolute position. Additionally nodes may have other fields which can be required for 
balancing by algorithms like AVL tree or red-black tree. For simplicity we can omit 
these fields. Current implementation is based on AVL tree.
```
class Node {
    Node left
    Node right
    Node parent
    Object element
    Int relativePosition
}
```
Example of tree contained elements `[“q”, “w”, “e”, “r”, “t”, “y”, “u”]`, for simplicity 
only element and position is shown:
```
                             [el: r, pos: 3]
                           /        |        \
         [el: w, pos: -2]           |           [el: y, pos: +2]
           /     |    \             |             /    |     \
[el: q, pos: -1] | [el: e, pos: +1] | [el: t, pos: -1] | [el: u, pos: +1]
        |        |         |        |         |        |         |
       [q]      [w]       [e]      [r]       [t]      [y]       [u]
```

## Getting element by index
For example we’re getting element on position 2.
* Starting from root `[el: r, pos: 3]`, it’s absolute position is 3. Index we’re searching 
is 2, it’s less than current node’s absolute position 3, so we move to left subtree.
* Current node is `[el: w, pos: -2]`, it’s relative to parent node position is -2, parent’s 
node absolute position is 3, so absolute position of current node is 3 - 2 = 1. Position 
we’re searching is 2, it’s bigger than current position, so we move to right subtree.
* Current node is `[el: e, pos: +1]`. It’s relative position to parent node is +1, absolute 
position of previous node is 1, so position of current node is 2. It’s the same position 
we’re looking for, so we’ve found correct node and it’s element is ‘e’.

## Adding and removing
Adding and removing is a bit more complex. However it’s pretty similar to the same 
operations in any other tree. Some kind of balanced tree should be used for best performance. 
Main difference is how to select child node. It should be done using algorithm described in 
“Get element by index” section instead of comparing to element in node. Other difference is 
that it should handle updating parent node and absolute position. All these improvements 
do not impact on complexity and along with some balancing algorithm provides 
insertion/removing complexity of O(log n).

## Getting index of an element.
To support getting an index of an element some kind of additional map is required. Elements 
should be mapped to it’s nodes. Updating this map is handled along with updating tree.
Default map implementation is HashMap.

To get an index of element we need to get it’s node using map. Index of an element in 
a sequence is a sum of relative positions from corresponding node to root node.

For example we’re looking for position of element ‘t’. Using map we get node 
`[el: t, pos: -1]`. Set position counter to 0. Then:
* Current node is `[el: t, pos: -1]`. Add to position counter relative position of 
current node. Position counter = 0 + (-1) = -1. Move to parent node `[el: y, pos: +2]`.
* Current node is `[el: y, pos: +2]`. Add to position counter relative position of 
current node. Position counter = -1 + 2 = 1. Move to parent node `[el: r, pos: 3]`.
* Current node is `[el: r, pos: 3]`. Add to position counter relative position of 
current node. Position counter = 1 + 3 = 4. As current node is a root node, it does not 
have parent. Position is found and it’s 4.
