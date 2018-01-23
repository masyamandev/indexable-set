# JMH tests

Compare collections performance with reference apache TreeList implementation.
TreeListSet with maxValue = 10 is irrelevant as it contains only unique elements.

# Usage
```
mvn clean install && java -jar ./target/benchmarks.jar
```

# Output
```
Benchmark                               (className)  (maxValue)   (size)   Mode  Cnt       Score      Error  Units
PerformanceCompare.addRemoveQueue          TreeList          10     1000  thrpt    5    4143.654 ±  407.038  ops/s
PerformanceCompare.addRemoveQueue          TreeList          10  1000000  thrpt    5       1.244 ±    1.946  ops/s
PerformanceCompare.addRemoveQueue          TreeList  1000000000     1000  thrpt    5    2384.755 ± 3383.410  ops/s
PerformanceCompare.addRemoveQueue          TreeList  1000000000  1000000  thrpt    5       1.405 ±    1.737  ops/s
PerformanceCompare.addRemoveQueue       TreeListSet  1000000000     1000  thrpt    5    3031.698 ±  982.893  ops/s
PerformanceCompare.addRemoveQueue       TreeListSet  1000000000  1000000  thrpt    5       0.854 ±    0.462  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList          10     1000  thrpt    5     977.708 ±  323.106  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList          10  1000000  thrpt    5       0.139 ±    0.019  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList  1000000000     1000  thrpt    5    2489.072 ±  281.174  ops/s
PerformanceCompare.addRemoveQueue   IndexedTreeList  1000000000  1000000  thrpt    5       0.582 ±    0.230  ops/s
PerformanceCompare.addRemoveRandom         TreeList          10     1000  thrpt    5    1682.069 ±  131.550  ops/s
PerformanceCompare.addRemoveRandom         TreeList          10  1000000  thrpt    5       0.355 ±    0.060  ops/s
PerformanceCompare.addRemoveRandom         TreeList  1000000000     1000  thrpt    5    1642.948 ±  374.383  ops/s
PerformanceCompare.addRemoveRandom         TreeList  1000000000  1000000  thrpt    5       0.362 ±    0.082  ops/s
PerformanceCompare.addRemoveRandom      TreeListSet  1000000000     1000  thrpt    5    1385.074 ±   40.499  ops/s
PerformanceCompare.addRemoveRandom      TreeListSet  1000000000  1000000  thrpt    5       0.271 ±    0.086  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList          10     1000  thrpt    5     459.362 ±   93.861  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList          10  1000000  thrpt    5       0.052 ±    0.007  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList  1000000000     1000  thrpt    5    1320.454 ±  621.620  ops/s
PerformanceCompare.addRemoveRandom  IndexedTreeList  1000000000  1000000  thrpt    5       0.235 ±    0.031  ops/s
PerformanceCompare.addToMiddle             TreeList          10     1000  thrpt    5    4723.336 ±  293.609  ops/s
PerformanceCompare.addToMiddle             TreeList          10  1000000  thrpt    5       1.035 ±    0.234  ops/s
PerformanceCompare.addToMiddle             TreeList  1000000000     1000  thrpt    5    4219.317 ± 2179.468  ops/s
PerformanceCompare.addToMiddle             TreeList  1000000000  1000000  thrpt    5       0.964 ±    0.066  ops/s
PerformanceCompare.addToMiddle          TreeListSet  1000000000     1000  thrpt    5    3891.215 ± 1503.064  ops/s
PerformanceCompare.addToMiddle          TreeListSet  1000000000  1000000  thrpt    5       0.731 ±    0.134  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList          10     1000  thrpt    5    1807.406 ±   29.119  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList          10  1000000  thrpt    5       0.243 ±    0.128  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList  1000000000     1000  thrpt    5    3164.363 ± 1767.904  ops/s
PerformanceCompare.addToMiddle      IndexedTreeList  1000000000  1000000  thrpt    5       0.719 ±    0.197  ops/s
PerformanceCompare.addToTail               TreeList          10     1000  thrpt    5    8908.907 ±  137.237  ops/s
PerformanceCompare.addToTail               TreeList          10  1000000  thrpt    5       3.796 ±    0.677  ops/s
PerformanceCompare.addToTail               TreeList  1000000000     1000  thrpt    5    8209.302 ± 2743.855  ops/s
PerformanceCompare.addToTail               TreeList  1000000000  1000000  thrpt    5       3.855 ±    0.763  ops/s
PerformanceCompare.addToTail            TreeListSet  1000000000     1000  thrpt    5    6018.175 ± 1415.048  ops/s
PerformanceCompare.addToTail            TreeListSet  1000000000  1000000  thrpt    5       2.383 ±    1.018  ops/s
PerformanceCompare.addToTail        IndexedTreeList          10     1000  thrpt    5    2367.645 ±  101.177  ops/s
PerformanceCompare.addToTail        IndexedTreeList          10  1000000  thrpt    5       0.288 ±    0.021  ops/s
PerformanceCompare.addToTail        IndexedTreeList  1000000000     1000  thrpt    5    6236.922 ±  186.733  ops/s
PerformanceCompare.addToTail        IndexedTreeList  1000000000  1000000  thrpt    5       1.540 ±    1.704  ops/s
PerformanceCompare.get                     TreeList          10     1000  thrpt    5   13007.573 ±  168.763  ops/s
PerformanceCompare.get                     TreeList          10  1000000  thrpt    5       1.675 ±    0.098  ops/s
PerformanceCompare.get                     TreeList  1000000000     1000  thrpt    5   13007.566 ± 1505.722  ops/s
PerformanceCompare.get                     TreeList  1000000000  1000000  thrpt    5       1.527 ±    0.051  ops/s
PerformanceCompare.get                  TreeListSet  1000000000     1000  thrpt    5   12368.792 ±  862.868  ops/s
PerformanceCompare.get                  TreeListSet  1000000000  1000000  thrpt    5       1.690 ±    0.008  ops/s
PerformanceCompare.get              IndexedTreeList          10     1000  thrpt    5   12736.643 ± 2365.239  ops/s
PerformanceCompare.get              IndexedTreeList          10  1000000  thrpt    5       1.829 ±    0.025  ops/s
PerformanceCompare.get              IndexedTreeList  1000000000     1000  thrpt    5   12687.918 ± 1271.508  ops/s
PerformanceCompare.get              IndexedTreeList  1000000000  1000000  thrpt    5       1.668 ±    0.011  ops/s
PerformanceCompare.iterate                 TreeList          10     1000  thrpt    5  147193.407 ± 7184.761  ops/s
PerformanceCompare.iterate                 TreeList          10  1000000  thrpt    5      70.994 ±    2.702  ops/s
PerformanceCompare.iterate                 TreeList  1000000000     1000  thrpt    5  139503.449 ± 1795.040  ops/s
PerformanceCompare.iterate                 TreeList  1000000000  1000000  thrpt    5      57.061 ±    1.404  ops/s
PerformanceCompare.iterate              TreeListSet  1000000000     1000  thrpt    5  121064.460 ± 2969.223  ops/s
PerformanceCompare.iterate              TreeListSet  1000000000  1000000  thrpt    5      46.342 ±    0.707  ops/s
PerformanceCompare.iterate          IndexedTreeList          10     1000  thrpt    5  125625.209 ± 3846.658  ops/s
PerformanceCompare.iterate          IndexedTreeList          10  1000000  thrpt    5      62.972 ±    1.644  ops/s
PerformanceCompare.iterate          IndexedTreeList  1000000000     1000  thrpt    5  107353.890 ± 2719.894  ops/s
PerformanceCompare.iterate          IndexedTreeList  1000000000  1000000  thrpt    5      35.478 ±    1.453  ops/s
```

# Goals

Make performance of TreeListSet and IndexedTreeList with maxValue = 1000000000 close to performance
of reference TreeList. Currently it's 1.5-3 times slower on adding/removing and iteration.