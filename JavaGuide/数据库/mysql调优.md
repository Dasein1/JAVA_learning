# 性能监控
## show profile查询剖析工具
set profiling=1;
select * from emp;
show profiles;   
show profile;  查看细节
show profile for query=2; 根据queryID查看profile
