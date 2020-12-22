#####  一、工具运行说明(执行结果见tidb-trx-test/sql/tidbtrxtest.log)
 
- 编译说明
> mvn package 

- 部署说明:
> 直接取编译好的jar包按运行说明执行即可   
> 位置: tidb-trx-test/target/tidb-trx-test-1.0.jar 

- 配置说明:
> src/main/resources/tidb-datasource.properties 
> 配置tidb数据库 url user password

- 运行说明:  
> 入参为文件名,可以两个或两个以上  
> nohup java -jar tidb-trx-test.jar $1 $2 .. > tidbtrxtest.log &  
> 样例:    
nohup java -jar tidb-trx-test-1.0.jar /Users/yuxiuzhen/IdeaProjects/ALL_IMS/tidb-trx-test2/sql/sql1 /Users/yuxiuzhen/IdeaProjects/ALL_IMS/tidb-trx-test2/sql/sql2 > tidbtrxtest.log &


#####  二、实现思路
> 1. 读取文件的行数N+1( +1:表示commit的执行动作)
> 2. 因每个文件的执行顺序确定,生成一个N+1长度的数组(同一个文件的数组元素全为一个数字,表示一条sql)
> 3. 把需要执行的文件的数组合并
> 4. 递归实现这个有重复元素数组的全排列,列出所有的可能执行顺序
> 5. 按执行顺序依次读取事务文件中的sql,对应的client执行sql
> 6. 将执行结果打印在日志中
> 7. 异常情况  
     - 如果更新条件一致会产生block,测试时的tidb默认60s会返回lock wait锁超时的错误),捕获异常继续执行下一个case  



#####  三、暂未完全实现的地方
> - 如果文件存在相同条件的更新SQL,会产生锁block等待, 超时会执行失败,目前的处理是捕捉异常,判定当前CASE执行失败,继续下一个CASE执行;
> - 程序未判断内存溢出的情况。




#####  四、题目：TiDB SQL 事务测试框架  
>描述：在测试 TiDB 事务时，对指定的两个事务，需要穷举两个客户端按不同的顺序执行 事务中的 SQL。   
例如给定两个事务，分别保存在 sql1 和 sql2 文件中。   
sql1 文件:   
    update X set a=5 where id=1;  
    update X set a=6 where id=2;   
sql2 文件:  
    update X set a=8 where id=8;   
对这个 case，穷举可能的 SQL 执行顺序，有以下三种情况：   
情况 1：   
    client1：update X set a=5 where id=1;   
    client1：update X set a=6 where id=2;   
    client2：update X set a=8 where id=8;   
情况 2：   
    client1：update X set a=5 where id=1;  
    client2：update X set a=8 where id=8;   
    client1：update X set a=6 where id=2;   
情况 3：   
    client2：update X set a=8 where id=8;  
    client1：update X set a=5 where id=1;   
    client1：update X set a=6 where id=2;   
要求：   
    1、写代码根据两个 sql 文件生成事务的 N 种 SQL 执行顺序组合（ 可以假设文件中一行 是一个 sql ）   
    2、写一个测试程序，能模拟两个客户端执行这 N 种组合（不用做结果校验）。   
    3、可以跑在 TiDB 上。  