# JMH tests

Compare collections performance with reference apache TreeList implementation.
TreeListSet with maxValue = 10 is irrelevant as it contains only unique elements.

# Usage
```
mvn clean install && java -jar ./target/benchmarks.jar
```

# Output
```
Benchmark                               (className)  (maxValue)   (size)   Mode  Cnt          Score           Error  Units
PerformanceCompare.addRemoveQueue          TreeList          10     1000  thrpt    5       5701.826 ±       539.291  ops/s
PerformanceCompare.addRemoveQueue          TreeList          10  1000000  thrpt    5          1.554 ±         1.502  ops/s
PerformanceCompare.addRemoveQueue          TreeList  1000000000     1000  thrpt    5       4175.682 ±        68.985  ops/s
PerformanceCompare.addRemoveQueue          TreeList  1000000000  1000000  thrpt    5          1.362 ±         1.694  ops/s
PerformanceCompare.addRemoveQueue       TreeListSet          10     1000  thrpt    5      17559.169 ±       583.771  ops/s
PerformanceCompare.addRemoveQueue       TreeListSet          10  1000000  thrpt    5         17.815 ±         0.405  ops/s
PerformanceCompare.addRemoveQueue       TreeListSet  1000000000     1000  thrpt    5       3318.064 ±       413.046  ops/s
PerformanceCompare.addRemoveQueue       TreeListSet  1000000000  1000000  thrpt    5          0.958 ±         0.649  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList          10     1000  thrpt    5       1161.741 ±        10.572  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList          10  1000000  thrpt    5          0.136 ±         0.028  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList  1000000000     1000  thrpt    5       2408.683 ±       164.806  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList  1000000000  1000000  thrpt    5          0.585 ±         0.240  ops/s
PerformanceCompare.addRemoveRandom         TreeList          10     1000  thrpt    5       1738.378 ±        46.889  ops/s
PerformanceCompare.addRemoveRandom         TreeList          10  1000000  thrpt    5          0.367 ±         0.060  ops/s
PerformanceCompare.addRemoveRandom         TreeList  1000000000     1000  thrpt    5       1743.749 ±       583.758  ops/s
PerformanceCompare.addRemoveRandom         TreeList  1000000000  1000000  thrpt    5          0.370 ±         0.070  ops/s
PerformanceCompare.addRemoveRandom      TreeListSet  1000000000     1000  thrpt    5       1361.795 ±        63.745  ops/s
PerformanceCompare.addRemoveRandom      TreeListSet  1000000000  1000000  thrpt    5          0.294 ±         0.068  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList          10     1000  thrpt    5        481.261 ±        12.079  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList          10  1000000  thrpt    5          0.053 ±         0.012  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList  1000000000     1000  thrpt    5       1493.549 ±       498.510  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList  1000000000  1000000  thrpt    5          0.242 ±         0.028  ops/s
PerformanceCompare.addToMiddle             TreeList          10     1000  thrpt    5       4817.701 ±      3662.652  ops/s
PerformanceCompare.addToMiddle             TreeList          10  1000000  thrpt    5          0.655 ±         0.856  ops/s
PerformanceCompare.addToMiddle             TreeList  1000000000     1000  thrpt    5       4036.662 ±      3355.529  ops/s
PerformanceCompare.addToMiddle             TreeList  1000000000  1000000  thrpt    5          0.842 ±         0.561  ops/s
PerformanceCompare.addToMiddle          TreeListSet          10     1000  thrpt    5      20349.194 ±     18873.468  ops/s
PerformanceCompare.addToMiddle          TreeListSet          10  1000000  thrpt    5         15.641 ±        10.557  ops/s
PerformanceCompare.addToMiddle          TreeListSet  1000000000     1000  thrpt    5       2730.755 ±      3504.948  ops/s
PerformanceCompare.addToMiddle          TreeListSet  1000000000  1000000  thrpt    5          0.978 ±         0.552  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList          10     1000  thrpt    5       2514.959 ±       187.629  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList          10  1000000  thrpt    5          0.269 ±         0.052  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList  1000000000     1000  thrpt    5       4328.997 ±       861.878  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList  1000000000  1000000  thrpt    5          1.183 ±         1.605  ops/s
PerformanceCompare.addToTail               TreeList          10     1000  thrpt    5       9405.475 ±       308.769  ops/s
PerformanceCompare.addToTail               TreeList          10  1000000  thrpt    5          3.880 ±         0.775  ops/s
PerformanceCompare.addToTail               TreeList  1000000000     1000  thrpt    5       8893.614 ±       326.018  ops/s
PerformanceCompare.addToTail               TreeList  1000000000  1000000  thrpt    5          3.787 ±         0.413  ops/s
PerformanceCompare.addToTail            TreeListSet          10     1000  thrpt    5      35620.129 ±     59600.001  ops/s
PerformanceCompare.addToTail            TreeListSet          10  1000000  thrpt    5         59.726 ±         0.841  ops/s
PerformanceCompare.addToTail            TreeListSet  1000000000     1000  thrpt    5       6856.311 ±        37.886  ops/s
PerformanceCompare.addToTail            TreeListSet  1000000000  1000000  thrpt    5          2.396 ±         0.590  ops/s
PerformanceCompare.addToTail        IndexedTreeList          10     1000  thrpt    5       1873.665 ±      1740.983  ops/s
PerformanceCompare.addToTail        IndexedTreeList          10  1000000  thrpt    5          0.243 ±         0.113  ops/s
PerformanceCompare.addToTail        IndexedTreeList  1000000000     1000  thrpt    5       6139.634 ±       376.726  ops/s
PerformanceCompare.addToTail        IndexedTreeList  1000000000  1000000  thrpt    5          1.546 ±         2.224  ops/s
PerformanceCompare.get                     TreeList          10     1000  thrpt    5      12627.910 ±       129.390  ops/s
PerformanceCompare.get                     TreeList          10  1000000  thrpt    5          1.630 ±         0.043  ops/s
PerformanceCompare.get                     TreeList  1000000000     1000  thrpt    5      12682.997 ±       955.918  ops/s
PerformanceCompare.get                     TreeList  1000000000  1000000  thrpt    5          1.552 ±         0.014  ops/s
PerformanceCompare.get                  TreeListSet          10     1000  thrpt    5      28299.905 ±      2142.385  ops/s
PerformanceCompare.get                  TreeListSet          10  1000000  thrpt    5         29.097 ±         0.457  ops/s
PerformanceCompare.get                  TreeListSet  1000000000     1000  thrpt    5      12558.213 ±       419.073  ops/s
PerformanceCompare.get                  TreeListSet  1000000000  1000000  thrpt    5          1.687 ±         0.016  ops/s
PerformanceCompare.get              IndexedTreeList          10     1000  thrpt    5      12643.655 ±      1093.538  ops/s
PerformanceCompare.get              IndexedTreeList          10  1000000  thrpt    5          1.851 ±         0.050  ops/s
PerformanceCompare.get              IndexedTreeList  1000000000     1000  thrpt    5      12229.910 ±       417.968  ops/s
PerformanceCompare.get              IndexedTreeList  1000000000  1000000  thrpt    5          1.664 ±         0.056  ops/s
PerformanceCompare.iterate                 TreeList          10     1000  thrpt    5     129212.519 ±      2556.466  ops/s
PerformanceCompare.iterate                 TreeList          10  1000000  thrpt    5         69.685 ±         1.701  ops/s
PerformanceCompare.iterate                 TreeList  1000000000     1000  thrpt    5     135306.988 ±      2188.409  ops/s
PerformanceCompare.iterate                 TreeList  1000000000  1000000  thrpt    5         56.122 ±         1.396  ops/s
PerformanceCompare.iterate              TreeListSet          10     1000  thrpt    5   12828823.785 ±    368639.744  ops/s
PerformanceCompare.iterate              TreeListSet          10  1000000  thrpt    5   12996396.220 ±    135089.958  ops/s
PerformanceCompare.iterate              TreeListSet  1000000000     1000  thrpt    5      45194.292 ±       748.473  ops/s
PerformanceCompare.iterate              TreeListSet  1000000000  1000000  thrpt    5         16.717 ±         0.335  ops/s
PerformanceCompare.iterate          IndexedTreeList          10     1000  thrpt    5      41257.218 ±       776.194  ops/s
PerformanceCompare.iterate          IndexedTreeList          10  1000000  thrpt    5         19.075 ±         0.960  ops/s
PerformanceCompare.iterate          IndexedTreeList  1000000000     1000  thrpt    5      43525.586 ±       759.321  ops/s
PerformanceCompare.iterate          IndexedTreeList  1000000000  1000000  thrpt    5         15.859 ±         0.462  ops/s
```

# Goals

Make performance of TreeListSet and IndexedTreeList with maxValue = 1000000000 close to performance
of reference TreeList. Currently it's 1.5-3 times slower on adding/removing and iteration.