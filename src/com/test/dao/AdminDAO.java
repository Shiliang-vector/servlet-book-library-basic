package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.pojo.User;
import com.test.util.DbUtils;

/**
 * 管理员数据访问对象，封装了后台管理员账号的基础查询与更新操作。
 */
public class AdminDAO {
    /**
     * 根据用户名查询管理员账号，用于登录校验。
     *
     * @param username 登录输入的用户名
     * @return 匹配到的管理员信息；不存在时返回 {@code null}
     */
    public User getUserByUsername(String username) {
        String sql = "select id,username,password,nickname from book_admin where username=?";
        List param = new ArrayList();
        param.add(username);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setNickname(resultSet.getString("nickname"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return null;
    }

    /**
     * 根据主键查询管理员，常用于个人中心展示。
     *
     * @param id 主键 id
     * @return 查询到的管理员；未找到时返回 {@code null}
     */
    public User getUserById(Integer id) {
        String sql = "select id,username,password,nickname from book_admin where id=?";
        List param = new ArrayList();
        param.add(id);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setNickname(resultSet.getString("nickname"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return null;
    }


    /**
     * 修改指定管理员的密码。
     *
     * @param id       管理员主键
     * @param password 经过必要校验/加密后的新密码
     * @return 受影响的行数，正常情况下为 1
     */
    public int updatePassword(Integer id, String password) {
        String sql = "update book_admin set password=? where id=?";
        List param = new ArrayList();
        param.add(password);
        param.add(id);
        return DbUtils.executeUpdate(sql, param);
    }
}
