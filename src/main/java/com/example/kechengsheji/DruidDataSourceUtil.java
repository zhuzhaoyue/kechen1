package com.example.kechengsheji;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.*;
import javax.management.*;
/**
 * Druid 数据源工具类，用于获取数据库连接。
 */
public class DruidDataSourceUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final DruidDataSource dataSource;

    static {
        dataSource = new DruidDataSource();
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
    }
    /**
     * 获取数据库连接对象。
     *
     * @return 数据库连接对象
     * @throws SQLException 获取连接时可能发生的 SQL 异常
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
