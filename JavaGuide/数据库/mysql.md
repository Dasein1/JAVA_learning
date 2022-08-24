# mysql是如何查询数据的？
 https://baijiahao.baidu.com/s?id=1693718883799963534&wfr=spider&for=pc
# 当所有字段都是索引列时，无论什么查询都会使用索引
```sql
create table test1 (id int primary key,a int,b int,c int);
alter table test1 add index idx1(a,b,c);
explain select * from test1 where c=1; 
```
![](images/2022-08-16-23-43-53.png)
using where：在索引表中过滤（出现时不代表就查了数据表）
using index:查找索引表不查找数据表

# 范围查询会导致索引失效
https://blog.csdn.net/imagineluopan/article/details/121097361

# 事务
[分布式事务](https://blog.csdn.net/DarzenWong/article/details/122432907)