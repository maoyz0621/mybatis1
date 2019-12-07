package com.myz.flyway;

import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.util.Properties;

/**
 * 使用main就是执行不成功 ......
 *
 * @author maoyz
 */
public class FlywayApplication {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        String url = properties.getProperty("jdbc.url");
        String user = properties.getProperty("jdbc.username", "root");
        String password = properties.getProperty("jdbc.password", "root");
        Flyway flyway = Flyway.configure().dataSource(url, user, password).load();

        // 删除当前 schema 下所有表
        // flyway.clean();

        // 创建 flyway_schema_history 表
        flyway.baseline();

        // 删除 flyway_schema_history 表中失败的记录
        // flyway.repair();

        // 检查 sql 文件
        flyway.validate();

        // 执行数据迁移
        flyway.migrate();
    }
}