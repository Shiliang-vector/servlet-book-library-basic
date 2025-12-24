package com.test.util;

import java.sql.*;
import java.util.List;

/**
 * 数据库工具类：集中管理连接获取、预处理语句创建与资源关闭逻辑。
 * 简化各 DAO 的重复代码，后续如需切换连接池（HikariCP、Druid）可在此替换实现。
 */
public class DbUtils {
    // 数据库配置信息，当前使用最基础的 DriverManager 直连方式
    private static final String url ="jdbc:mysql://127.0.0.1:3306/testservlet?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2b8";
    private static final String username = "root";
    private static final String password = "123456";

    // 注册 MySQL JDBC 驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接，演示最基础的 DriverManager 方式
    public static Connection open() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 预处理 SQL，将参数顺序填充到 PreparedStatement 中
    public static PreparedStatement preparedStatement(String sql, List param, Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.size(); i++) {
                    statement.setObject(i + 1, param.get(i));
                }
            }
            return statement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 关闭数据库资源，防止连接泄漏
    public static void closeAll(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 执行增删改（update）操作的便捷方法
    public static int executeUpdate(String sql, List param) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, null);
        }
        return 0;
    }
}
