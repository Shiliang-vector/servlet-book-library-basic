package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.test.pojo.Category;
import com.test.pojo.Category;
import com.test.pojo.Reader;
import com.test.util.DbUtils;

/**
 * 图书分类数据访问对象，封装分类的查询、新增、修改与删除。
 * 分类数据在多个页面复用，统一封装便于后续扩展（如排序、层级支持）。
 */
public class CategoryDAO {
    /**
     * 查询所有分类列表，用于下拉框或列表展示。
     *
     * @return 全量分类集合
     */
    public List<Category> selectList() {
        // 语法 select 字段1(主键),字段2(展示名) from 父类表名
        String sql = "select ID,NAME from book_category";
        // 创建结果集，数据类型是List<分类>
        List<Category> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbUtils.open();
            statement = DbUtils.preparedStatement(sql, null, connection);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                // 创建父分类对象
                Category category = new Category();
                // 语法 resultSet.get类型("字段名")
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return list;
    }

    /**
     * 根据主键查询分类。
     *
     * @param id 分类主键
     * @return 匹配到的分类；未找到时返回 {@code null}
     */
    public Category selectById(Integer id) {
        String sql = "select id,name from book_category where id=?";
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
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeAll(connection, statement, resultSet);
        }
        return null;
    }

    /**
     * 新增分类。
     */
    public int add(Category category) {
        // insert into 表名(字段列表) values(占位符)
        String sql = "insert into book_category(name) values(?)";
        List param = new ArrayList();
        // 顺序要和占位符一致
        param.add(category.getName());
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 修改分类名称。
     */
    public int update(Category category) {
        // update 表名 set 字段名=? where id=?
        String sql = "update book_category set name=? where id=?";
        List param = new ArrayList();
        param.add(category.getName());
        param.add(category.getId());
        return DbUtils.executeUpdate(sql, param);
    }

    /**
     * 删除分类。调用前由上层校验是否有图书引用，避免脏数据。
     */
    public int delete(Integer id) {
        // delete from 表名 where id=?
        String sql = "delete from book_category where id=?";
        List param = new ArrayList();
        param.add(id);
        return DbUtils.executeUpdate(sql, param);
    }
}
