# Introduce

Derbysoft-Redis-Sharding 是基于Scala语言封装的Redis客户端，可以用Java或Scala语言调用。

## Derbysoft-Redis-Sharding Particularity
默认要配置32个Redis Server，同时提供了修改Redis Server个数的功能
Redis Server的配置文件格式是Properties,其中Properties文件中Key的前缀必须是“redis.host.”，Ke的值从“redis.host.000”到“redis.host.127”，Value的值格式如下：“10.200.107.10:6379”，完整配置格式见下面：“Redis Serve Config File example”
支持strings, hashes, lists, sets and sorted sets等结构

## What can I do with Derbysoft-Redis-Sharding ?
All of the following redis features are supported:

- Sorting
- Connection handling
- Commands operating on any kind of values
- Commands operating on string values
- Commands operating on hashes
- Commands operating on lists
- Commands operating on sets
- Commands operating on sorted sets
- Transactions
- Pipelining
- Publish/Subscribe
- Persistence control commands
- Remote server control commands
- Connection pooling
- Sharding (MD5, MurmureHash)
- Key-tags for sharding
- Sharding with pipelining

## How do I use it?

use it as a maven dependency

    <dependency>
        <groupId>com.derbysoft.redis</groupId>
        <artifactId>derbysoft-redis-sharding</artifactId>
        <version>1.3</version>
        <type>jar</type>
        <scope>compile</scope>
    </dependency>

Invoke method(DateShardingRedis.init(redisHostsFile)) when start your application

    //redisHostsFile = "/usr/local/xxx/redis.hosts.properties"

    DateShardingRedis.init(redisHostsFile)

To use it just:
    
    RedisCommands redis = ShardingRedis.single();
    redis.set("foo2010-01-01", "bar");
    String value = redis.get("foo2010-01-01");

## Other supported

- Derbysoft-Redis-Sharding 提供了一个Listener，在应用启动的时候调用“DateShardingRedis.init(redisHostsFile)”，如果要用这个Listener有点特殊要求：默认情况下classes目录下要有一个environment.properties文件，在environment.properties文件中添加一个key为“redis.hosts.file”的条目


        environment.properties
        redis.hosts.file = /usr/local/xxx/redis.hosts.properties

    然后在web.xml文件里面添加一个Listener即可


           <listener>
               <listener-class>com.derbysoft.redis.clients.common.listener.RedisHostsInitListener</listener-class>
           </listener>


    当然，如果你项目里面没有environment.properties文件而你又不想新建一个名为environment.properties的文件，也可以在web.xml指定properties文件名，不指定默认的properties文件是environment.properties


           <context-param>
               <param-name>redisHostsConfig</param-name>
               <param-value>environment.properties</param-value>
           </context-param>


- 另外，Derbysoft-Redis-Sharding提供了一个RedisHostsServlet来查看和修改redisHostsFile配置，GET请求是查看，POST请求是修改，在web.xml添加即可


           <servlet>
               <servlet-name>RedisHostsServlet</servlet-name>
               <servlet-class>com.derbysoft.redis.clients.common.servlet.RedisHostsServlet</servlet-class>
           </servlet>
           <servlet-mapping>
               <servlet-name>RedisHostsServlet</servlet-name>
               <url-pattern>/redis</url-pattern>
           </servlet-mapping>


- 修改Redis Server个数


           <context-param>
               <param-name>redisHostsSize</param-name>
               <param-value>32</param-value>
           </context-param>


- 修改RedisPoolConfig配置


           <context-param>
               <param-name>redisPoolConfig</param-name>
               <--minIdle,maxIdle,maxActive,maxWait,testOnBorrow,timeout-->
               <param-value>1,2,-1,-1,true,2000</param-value>
           </context-param>


    如果用这个servlet修改hosts配置，htm文件如下，需要一个name＝“hosts”的文本域，post提交：



         <form method="post" action="redis">
             Hosts:
             <textarea name="hosts" rows="50" cols="30">
                 redis.host.000=localhost:6379
                 redis.host.001=localhost:6380
                 redis.host.002=localhost:6381
                 redis.host.003=localhost:6382
                 redis.host.004=localhost:6383
                 redis.host.005=localhost:6384
                 redis.host.006=localhost:6384
                 redis.host.007=localhost:6384
                 redis.host.008=localhost:6384
                 redis.host.009=localhost:6384
                 redis.host.010=localhost:6384
                 redis.host.011=localhost:6384
                 redis.host.012=localhost:6384
                 redis.host.013=localhost:6384
                 redis.host.014=localhost:6384
                 redis.host.015=localhost:6384
                 redis.host.016=localhost:6384
                 redis.host.017=localhost:6384
                 redis.host.018=localhost:6384
                 redis.host.019=localhost:6384
                 redis.host.020=localhost:6384
                 redis.host.021=localhost:6384
                 redis.host.022=localhost:6384
                 redis.host.023=localhost:6384
                 redis.host.024=localhost:6384
                 redis.host.025=localhost:6384
                 redis.host.026=localhost:6384
                 redis.host.027=localhost:6384
                 redis.host.028=localhost:6384
                 redis.host.029=localhost:6384
                 redis.host.030=localhost:6384
                 redis.host.031=localhost:6384
             </textarea>
             <input type="submit" value="Update"/>
         </form>



## Redis serve config file example

   ＃文件名：redis.hosts.properties


       redis.host.000=10.200.107.10:6000
       redis.host.001=10.200.107.10:6001
       redis.host.002=10.200.107.10:6002
       redis.host.003=10.200.107.10:6003
       redis.host.004=10.200.107.10:6004
       redis.host.005=10.200.107.10:6005
       redis.host.006=10.200.107.10:6006
       redis.host.007=10.200.107.10:6007
       redis.host.008=10.200.107.10:6008
       redis.host.009=10.200.107.10:6009
       redis.host.010=10.200.107.10:6010
       redis.host.011=10.200.107.10:6011
       redis.host.012=10.200.107.10:6012
       redis.host.013=10.200.107.10:6013
       redis.host.014=10.200.107.10:6014
       redis.host.015=10.200.107.10:6015
       redis.host.016=10.200.107.10:6016
       redis.host.017=10.200.107.10:6017
       redis.host.018=10.200.107.10:6018
       redis.host.019=10.200.107.10:6019
       redis.host.020=10.200.107.10:6020
       redis.host.021=10.200.107.10:6021
       redis.host.022=10.200.107.10:6022
       redis.host.023=10.200.107.10:6023
       redis.host.024=10.200.107.10:6024
       redis.host.025=10.200.107.10:6025
       redis.host.026=10.200.107.10:6026
       redis.host.027=10.200.107.10:6027
       redis.host.028=10.200.107.10:6028
       redis.host.029=10.200.107.10:6029
       redis.host.030=10.200.107.10:6030
       redis.host.031=10.200.107.10:6031

## License

Copyright (c) 2012

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

