## mybatis-base

此工程为mybatis的基本使用

## mybatis-aop

此工程是基于aop实现的读写分离

## mybatis-aop-plugin

此工程是基于mybatis的Interceptor拦截器实现的读写分离

## mybatis-transaction

此工程是基于DataSourceTransactionManager的事务isReadOnly()实现的读写分离, 对于未开启事务的, 采用数据库的Master数据源

问题：  
Mapper中存在入参不同的同名方法，此时mybatis是否能够入参查询到数据？  
解答：不能，项目在启动的时候，会加载所有的Mapper，提示存在相同的全限定名ID：Mapped Statements collection already contains value for 全限定名.method，