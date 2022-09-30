# Collection接口
```java
public interface Collection<E> extends Iterable<E> {
int size()  //容器大小
boolean isEmpty()  //判断是否为空
boolean contains(Object o)  // 是否包含指定对象
Iterator<E> iterator();
Object[] toArray();
<T> T[] toArray(T[] a);
boolean add(E e);
boolean remove(Object o);
boolean containsAll(Collection<?> c);
boolean addAll(Collection<? extends E> c);
}
```

迭代器

Collection的子接口有Set、List、Queue
Set的实现类有HashSet（基于HashMap实现），LinkedHashSet