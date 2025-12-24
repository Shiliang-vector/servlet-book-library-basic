package com.test.pojo;

/**
 * 图书查询条件封装，包含标题关键字、分类、状态及分页参数。
 */
public class BookCriteria {
    private String title;// 名称
    private Integer categoryId;// 分类的id
    private String status;// 状态
    private Integer limit;// 用于分页，从哪条开始
    private Integer pageSize;// 用于分页，每页几条

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
