package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.pojo.*;
import com.test.util.DbUtils;


/**
 * 读者数据访问对象，负责 book_reader 表的认证、查询、增删改等操作。
 * 提供登录所需的账号查询、分页筛选以及批量删除等常用能力。
 */
public class ReaderDAO {
    /**
     * 根据用户名查询读者账户，用于登录及唯一性校验。
     */
    public User getUserByUsername(String username) {
        String sql = "select id,username,password,name from book_reader where username=?";
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
                user.setNickname(resultSet.getString("name"));
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
     * 按条件分页查询读者列表。
     *
     * @param criteria 查询条件：状态、手机号、证件号以及分页信息
     */
    public List<Reader> selectList(ReaderCriteria criteria) {
        String sql = "select id,username,password,name,mobile,id_card,status from book_reader where 1=1 ";

        List param = new ArrayList();
        if (criteria.getStatus() != null) {
            sql += " and status =? ";
            param.add(criteria.getStatus());
        }
        if (criteria.getMobile() != null) {
            sql += " and mobile =? ";
            param.add(criteria.getMobile());
        }
        if (criteria.getIdCard() != null) {
            sql += " and id_card =? ";
            param.add(criteria.getIdCard());
        }
        sql += " limit ?,?";// 分页参数
        param.add(criteria.getLimit());
        param.add(criteria.getPageSize());

        List<Reader> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reader reader = new Reader();
                reader.setId(resultSet.getInt("id"));
                reader.setUsername(resultSet.getString("username"));
                reader.setPassword(resultSet.getString("password"));
                reader.setName(resultSet.getString("name"));
                reader.setMobile(resultSet.getString("mobile"));
                reader.setIdCard(resultSet.getString("id_card"));
                reader.setStatus(resultSet.getString("status"));
                list.add(reader);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return list;
    }
    /**
     * 统计符合条件的读者数量，用于分页计算。
     */
    public int getCount(ReaderCriteria criteria) {
        String sql = "select count(*) from book_reader where 1=1 ";
        List param = new ArrayList();
        if (criteria.getStatus() != null) {
            sql += " and status =? ";
            param.add(criteria.getStatus());
        }
        if (criteria.getMobile() != null) {
            sql += " and mobile =? ";
            param.add(criteria.getMobile());
        }
        if (criteria.getIdCard() != null) {
            sql += " and id_card =? ";
            param.add(criteria.getIdCard());
        }
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return 0;
    }
    /**
     * 根据主键查询读者详情。
     */
    public Reader selectById(Integer id) {
        String sql = "select id,username,password,name,mobile,id_card,status from book_reader where id=?";
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
                Reader reader = new Reader();
                reader.setId(resultSet.getInt("id"));
                reader.setUsername(resultSet.getString("username"));
                reader.setPassword(resultSet.getString("password"));
                reader.setName(resultSet.getString("name"));
                reader.setMobile(resultSet.getString("mobile"));
                reader.setIdCard(resultSet.getString("id_card"));
                reader.setStatus(resultSet.getString("status"));
                return reader;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return null;
    }

    /**
     * 添加读者记录，注册与后台新增共用此方法。
     */
    public int add(Reader reader) {
        String sql = "insert into book_reader (username,password,name,mobile,id_card,status) values(?,?,?,?,?,?)";
        List param = new ArrayList();
        param.add(reader.getUsername());
        param.add(reader.getPassword());
        param.add(reader.getName());
        param.add(reader.getMobile());
        param.add(reader.getIdCard());
        param.add(reader.getStatus());
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 修改读者密码，调用前需完成校验和加密。
     */
    public int updatePassword(Integer id, String password) {
        String sql = "update book_reader set password=? where id=?";
        List param = new ArrayList();
        param.add(password);
        param.add(id);
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 修改除密码外的读者信息（手机号、状态）。
     */
    public int update(Reader reader) {
        String sql = "update book_reader set mobile=?,status=? where id=?";
        List param = new ArrayList();
        param.add(reader.getMobile());
        param.add(reader.getStatus());
        param.add(reader.getId());
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 批量删除读者。
     *
     * @param ids 读者 id 集合
     * @return 成功删除的数量
     */
    public int deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            placeholders.append("?");
            if (i < ids.size() - 1) {
                placeholders.append(",");
            }
        }
        String sql = "delete from book_reader where id in (" + placeholders + ")";
        List param = new ArrayList();
        param.addAll(ids);
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 删除单个读者。
     */
    public int delete(Integer id) {
        String sql = "delete from book_reader where id=?";
        List param = new ArrayList();
        param.add(id);
        return DbUtils.executeUpdate(sql, param);
    }
}
