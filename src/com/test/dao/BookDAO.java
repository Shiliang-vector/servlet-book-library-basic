package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.pojo.Book;
import com.test.pojo.BookCriteria;
import com.test.util.DbUtils;

/**
 * 图书信息数据访问对象，负责 book_info 表的增删改查。
 * 提供分页查询、计数、单条查询及新增/修改/删除操作，供 Servlet 层复用。
 */
public class BookDAO {
    /**
     * 按条件分页查询图书列表。
     *
     * @param criteria 查询条件，包含状态、分类、标题关键字以及分页信息
     * @return 满足条件的图书集合，已填充分类名称（pname）
     */
    public List<Book> selectList(BookCriteria criteria) {
        String sql =
            "select c.id,title,publisher,description,author,publish_date,status,category_id,stock,cover_url,p.name pname from book_info c "
                + " inner join book_category p on c.category_id=p.id where 1=1 ";
        List param = new ArrayList();
        if (criteria.getStatus() != null) {
            sql += " and status =? ";
            param.add(criteria.getStatus());
        }
        if (criteria.getCategoryId() != null) {
            sql += " and category_id =? ";
            param.add(criteria.getCategoryId());
        }
        if (criteria.getTitle() != null) {
            sql += " and title like concat('%',?,'%') ";
            param.add(criteria.getTitle());
        }
        sql += " limit ?,?";// 分页参数
        param.add(criteria.getLimit());
        param.add(criteria.getPageSize());

        List<Book> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, param, connection);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setStatus(resultSet.getString("status"));
                book.setAuthor(resultSet.getString("author"));
                book.setCategoryId(resultSet.getInt("category_id"));
                book.setCategoryName(resultSet.getString("pname"));
                book.setDescription(resultSet.getString("description"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setPublishDate(resultSet.getString("publish_date"));
                book.setStock(resultSet.getInt("stock"));
                book.setTitle(resultSet.getString("title"));
                book.setCoverUrl(resultSet.getString("cover_url"));
                list.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return list;
    }

    /**
     * 根据主键查询单本图书。
     *
     * @param id 图书主键
     * @return 查询到的图书实体；未找到时返回 {@code null}
     */
    public Book selectById(Integer id) {
        // select 字段名 from 表名 where id=?
        String sql = "select id,title,publisher,description,author,publish_date,status,category_id,stock,cover_url from book_info where id=? ";
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
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setStatus(resultSet.getString("status"));
                book.setAuthor(resultSet.getString("author"));
                book.setCategoryId(resultSet.getInt("category_id"));
                book.setDescription(resultSet.getString("description"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setPublishDate(resultSet.getString("publish_date"));
                book.setStock(resultSet.getInt("stock"));
                book.setTitle(resultSet.getString("title"));
                book.setCoverUrl(resultSet.getString("cover_url"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return null;
    }

    /**
     * 根据条件统计总条数，用于分页组件。
     *
     * @param criteria 查询条件，同 {@link #selectList(BookCriteria)}
     * @return 符合条件的记录数
     */
    public int getCount(BookCriteria criteria) {
        String sql = "select count(*) from book_info c inner join book_category p on c.category_id=p.id where 1=1 ";

        List param = new ArrayList();
        if (criteria.getStatus() != null) {
            sql += " and status =? ";
            param.add(criteria.getStatus());
        }
        if (criteria.getCategoryId() != null) {
            sql += " and category_id =? ";
            param.add(criteria.getCategoryId());
        }
        if (criteria.getTitle() != null) {
            sql += " and title like concat('%',?,'%') ";
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
     * 新增图书记录。
     *
     * @param book 待插入的图书实体，需确保必填字段已校验
     * @return 受影响行数
     */
    public int add(Book book) {
        // insert into 表名(字段列表) values(占位符)
        String sql =
            "insert into book_info(title,publisher,description,author,publish_date,status,category_id,stock,cover_url) values(?,?,?,?,?,?,?,?,?)";
        List param = new ArrayList();
        // 顺序要和占位符一致
        param.add(book.getTitle());
        param.add(book.getPublisher());
        param.add(book.getDescription());
        param.add(book.getAuthor());
        param.add(book.getPublishDate());
        param.add(book.getStatus());
        param.add(book.getCategoryId());
        param.add(book.getStock());
        param.add(book.getCoverUrl());
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 根据主键更新图书信息。
     *
     * @param book 含主键及更新字段的实体
     * @return 受影响行数
     */
    public int update(Book book) {
        // update 表名 set 字段名=? where id=?
        String sql =
            "update book_info set title=?,publisher=?,description=?,author=?,publish_date=?,status=?,category_id=?,stock=?,cover_url=? where id=?";
        List param = new ArrayList();
        param.add(book.getTitle());
        param.add(book.getPublisher());
        param.add(book.getDescription());
        param.add(book.getAuthor());
        param.add(book.getPublishDate());
        param.add(book.getStatus());
        param.add(book.getCategoryId());
        param.add(book.getStock());
        param.add(book.getCoverUrl());
        param.add(book.getId());
        return DbUtils.executeUpdate(sql, param);
    }
    /**
     * 删除图书记录。删除前通常应由上层校验关联（如借阅记录、库存等）。
     */
    public int delete(Integer id) {
        // delete from 表名 where id=?
        String sql = "delete from book_info where id=?";
        List param = new ArrayList();
        param.add(id);
        return DbUtils.executeUpdate(sql, param);
    }
}
