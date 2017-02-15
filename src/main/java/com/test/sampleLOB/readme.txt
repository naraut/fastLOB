

Section A: attached solution
 
1. ConcurrentMap to handle concurrency.
2. Additions are constant time. Removals are 2 log(n).
3. modify is log(n). containsKey can be removed.
4. No other details given about frequency of requirements 4,5 and 6 hence addition and removal are favoured.
5. timestamp added to Order object but if order object is not changeable then a simple decorator with a timestamp could be used .
6, Could have used state pattern instead of boiler plate code for switch and if else for B vs A in OrderBookImpl.
 
Section B: Improvements for low latency/ low GC pause times.
 
1. If symbol is fixed character length then compress it into an long.
 
2. Create OrderKey which has id, price, side, and timestamp as variables. This should implement Comparable. The equals/hashCode should use id and side. But compare() should use price and timestamp.
 
3. Current implementations of requirements 4,5,6 iterate and sort collections which is undesirable. The bid/ask Map could be changed to a ConcurrentSkipListMap<OrderKey, Order>(new OrderKey()) OrderKey comparator provided at creation of map will sort elements based on price/timestamp.
 
4. If depth of OrderBook is fixed (say only 10 levels are maintained). large amount of memory can be allocated off-heap by Unsafe and orders beyond the depth can be overwritten. The large memory chunk would be written straight on old gen. Flyweight pattern can be used to reuse objects.
 
5. Lots more....