# Introduce

Derbysoft-Redis-Sharding is a Redis client based on Scala, can use Java or Scala language

## Derbysoft-Redis-Sharding Particularity
Support strings, hashes, lists, sets and sorted sets

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
        <version>2.1.4</version>
        <type>jar</type>
        <scope>compile</scope>
    </dependency>

Add a listener when start your application

     <listener>
         <listener-class>com.derbysoft.redis.clients.common.listener.RedisHostsInitListener</listener-class>
     </listener>

To use it just:
    
    RedisCommands redis = ShardingRedis.single();
    redis.set("foo2010-01-01", "bar");
    String value = redis.get("foo2010-01-01");

If you do not need to sharding, You can use SingleJedis

    RedisCommands redis = new SingleJedis("10.200.107.35:6379");
    redis.set("foo2010-01-01", "bar");
    String value = redis.get("foo2010-01-01");


## Other supported

- Update redis server counts

        redis-sharding-config.properties
        redis.hosts.size = 32

    Default properties file name is redis-sharding-config.properties. You can update it.

           <context-param>
               <param-name>redisConfigName</param-name>
               <param-value>redis-sharding-config</param-value>
           </context-param>

- RedisHostsServlet

           <servlet>
               <servlet-name>RedisHostsServlet</servlet-name>
               <servlet-class>com.derbysoft.redis.clients.common.servlet.RedisHostsServlet</servlet-class>
           </servlet>
           <servlet-mapping>
               <servlet-name>RedisHostsServlet</servlet-name>
               <url-pattern>/redis</url-pattern>
           </servlet-mapping>

- RedisMemoryServlet

           <servlet>
               <servlet-name>RedisMemoryServlet</servlet-name>
               <servlet-class>com.derbysoft.redis.clients.common.servlet.RedisMemoryServlet</servlet-class>
           </servlet>
           <servlet-mapping>
               <servlet-name>RedisMemoryServlet</servlet-name>
               <url-pattern>/redis-memory</url-pattern>
           </servlet-mapping>

- Update RedisPoolConfig

           <context-param>
               <param-name>redisPoolConfig</param-name>
               <--minIdle,maxIdle,maxActive,maxWait,testOnBorrow,timeout-->
               <param-value>1,2,-1,-1,true,2000</param-value>
           </context-param>

## Redis serve config file example

   ＃redis-sharding-config.properties

       redis.hosts.size = 32
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

