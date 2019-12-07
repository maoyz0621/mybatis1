# 使用flyway数据库迁移

## 方法1

maven插件形式，其中有2点需要注意：

1. 已创建mybatis的db；
```
<plugins>
    <!-- TODO 方法2 maven插件形式 -->
    <plugin>
        <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
        </dependencies>
        <configuration>
            <!-- TODO 如果已创建mybatis的db时, 使用此url -->
            <url>jdbc:mysql://localhost:13306/mybatis?serverTimezone=Asia/Shanghai&amp;useSSL=false&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;autoReconnect=true&amp;maxReconnects=10&amp;failOverReadOnly=false&amp;useAffectedRows=true&amp;allowMultiQueries=true&amp;jdbcCompliantTruncation=false</url>
            <user>root</user>
            <password>root</password>
        </configuration>

        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-maven-plugin</artifactId>
        <version>6.1.0</version>
    </plugin>
</plugins>
```

2. 没有创建mybatis的db，执行的时候会创建schemas中的schema；
```
<plugins>
    <!-- TODO 方法2 maven插件形式 -->
    <plugin>
        <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
        </dependencies>
        <configuration>
            <url>jdbc:mysql://localhost:13306?serverTimezone=Asia/Shanghai&amp;useSSL=false&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;autoReconnect=true&amp;maxReconnects=10&amp;failOverReadOnly=false&amp;useAffectedRows=true&amp;allowMultiQueries=true&amp;jdbcCompliantTruncation=false</url>
            <user>root</user>
            <password>root</password>
            <schemas>
                <schema>mybatis</schema>
            </schemas>
        </configuration>

        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-maven-plugin</artifactId>
        <version>6.1.0</version>
    </plugin>
</plugins>
```

## 方法2

maven插件形式配合properties, 此时plugins中不需要添加configuration属性

```
<properties>
    <!-- TODO 方法1 配置properties, 以flyway开头 -->
    <flyway.user>root</flyway.user>
    <flyway.password>root</flyway.password>
    <flyway.url>jdbc:mysql://localhost:13306/mybatis?serverTimezone=Asia/Shanghai&amp;useSSL=false&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull&amp;autoReconnect=true&amp;maxReconnects=10&amp;failOverReadOnly=false&amp;useAffectedRows=true&amp;allowMultiQueries=true&amp;jdbcCompliantTruncation=false</flyway.url>
    <!-- List are defined as comma-separated values -->
    <flyway.schemas>mybatis</flyway.schemas>
     <!--Individual placeholders are prefixed by flyway.placeholders.-->
    <flyway.placeholders.keyABC>valueXYZ</flyway.placeholders.keyABC>
    <flyway.placeholders.otherplaceholder>value123</flyway.placeholders.otherplaceholder>
</properties>

<plugins>
    <!-- TODO 方法2 maven插件形式 -->
    <plugin>
        <dependencies>
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql.version}</version>
            </dependency>
        </dependencies>
        
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-maven-plugin</artifactId>
        <version>6.1.0</version>
    </plugin>
</plugins>
```

## 方法3

Main函数启动 。。。