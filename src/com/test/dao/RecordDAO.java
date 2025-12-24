package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.pojo.BookCriteria;
import com.test.pojo.Record;
import com.test.pojo.RecordCriteria;
import com.test.util.DbUtils;

/**
 * 借阅记录数据访问对象，负责 book_record 表的查询、新增与更新。
 * 支持按状态、用户、书籍等维度筛选，并提供分页统计。
 */
public class RecordDAO {
    /**
     * 按条件分页查询借阅记录。
     */
    public List<Record> selectList(RecordCriteria criteria) {
        // 语法 select 字段1(主键),字段2(展示名) from 父类表名
        String sql =
            "select r.id,book_id,user_id,borrow_time,return_time,expire_time,r.status,price,book_title,u.name,u.id_card "
                + "from book_record r "
                + "inner join book_reader u on u.id=r.user_id where 1=1 ";
        List param = new ArrayList();
        if (criteria.getStatus() != null) {
            sql += " and r.status =? ";
            param.add(criteria.getStatus());
        }
        if (criteria.getUserId() != null) {
            sql += " and user_id =? ";
            param.add(criteria.getUserId());
        }
        if (criteria.getBookId() != null) {
            sql += " and book_id =? ";
            param.add(criteria.getBookId());
        }
        if (criteria.getIdCard() != null) {
            sql += " and u.id_card =? ";
            param.add(criteria.getIdCard());
        }
        if (criteria.getTitle() != null) {
            sql += " and book_title like concat('%',?,'%') ";
            param.add(criteria.getTitle());
        }
        sql += " order by id desc";
        sql += " limit ?,?";// 分页参数
        param.add(criteria.getLimit());
        param.add(criteria.getPageSize());

        // 创建结果集，数据类型是List<分类>
        List<Record> list = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // 创建父分类对象
                Record record = new Record();
                record.setId(resultSet.getInt("id"));
                record.setBookId(resultSet.getInt("book_id"));
                record.setUserId(resultSet.getInt("user_id"));
                record.setBorrowTime(resultSet.getTimestamp("borrow_time"));
                record.setReturnTime(resultSet.getTimestamp("return_time"));
                record.setExpireTime(resultSet.getTimestamp("expire_time"));
                record.setStatus(resultSet.getString("status"));
                record.setPrice(resultSet.getDouble("price"));
                record.setIdCard(resultSet.getString("id_card"));
                record.setName(resultSet.getString("name"));
                record.setTitle(resultSet.getString("book_title"));
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return list;
    }

    /**
     * 统计满足条件的借阅记录数量，用于分页。
     */
    public int getCount(RecordCriteria criteria) {
        String sql = "select count(*) from book_record r"
            + " inner join book_reader u on u.id=r.user_id where 1=1 ";

        List param = new ArrayList();
        if (criteria.getStatus() != null) {
            sql += " and r.status =? ";
            param.add(criteria.getStatus());
        }
        if (criteria.getUserId() != null) {
            sql += " and user_id =? ";
            param.add(criteria.getUserId());
        }
        if (criteria.getBookId() != null) {
            sql += " and book_id =? ";
            param.add(criteria.getBookId());
        }
        if (criteria.getIdCard() != null) {
            sql += " and u.id_card =? ";
            param.add(criteria.getIdCard());
        }
        if (criteria.getTitle() != null) {
            sql += " and book_title like concat('%',?,'%') ";
            param.add(criteria.getTitle());
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
     * 根据主键查询借阅记录详情。
     */
    public Record selectById(Integer id) {
        String sql =
            "select id,book_id,book_title,user_id,borrow_time,return_time,expire_time,status,price from book_record where id=?";
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
                Record record = new Record();
                record.setId(resultSet.getInt("id"));
                record.setBookId(resultSet.getInt("book_id"));
                record.setUserId(resultSet.getInt("user_id"));
                record.setBorrowTime(resultSet.getTimestamp("borrow_time"));
                record.setReturnTime(resultSet.getTimestamp("return_time"));
                record.setExpireTime(resultSet.getTimestamp("expire_time"));
                record.setStatus(resultSet.getString("status"));
                record.setTitle(resultSet.getString("book_title"));
                record.setPrice(resultSet.getDouble("price"));
                return record;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return null;
    }

    /**
     * 新增借阅记录，借书时调用。
     */
    public int add(Record record) {
        // insert into 表名(字段列表) values(占位符)
        String sql =
            "insert into book_record(book_id,user_id,borrow_time,return_time,expire_time,status,price,book_title) values(?,?,?,?,?,?,?,?)";
        List param = new ArrayList();
        // 顺序要和占位符一致
        param.add(record.getBookId());
        param.add(record.getUserId());
        param.add(record.getBorrowTime());
        param.add(record.getReturnTime());
        param.add(record.getExpireTime());
        param.add(record.getStatus());
        param.add(record.getPrice());
        param.add(record.getTitle());
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 更新借阅记录的归还时间、状态和罚金。
     */
    public int update(Record record) {
        // update 表名 set 字段名=? where id=?
        String sql = "update book_record set return_time=?,status=?,price=? where id=?";
        List param = new ArrayList();
        param.add(record.getReturnTime());
        param.add(record.getStatus());
        param.add(record.getPrice());
        param.add(record.getId());
        return DbUtils.executeUpdate(sql, param);
    }

}
