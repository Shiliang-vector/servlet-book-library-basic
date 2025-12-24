package com.test.pojo;

/**
 * 借阅记录查询条件，支持按用户、图书、证件号、状态及书名关键字分页检索。
 */
public class RecordCriteria {
    private Integer userId;
    private Integer bookId;
    private String status;// 状态
    private String idCard;// 身份证
    private String title;// 书名
    private Integer limit;// 用于分页，从哪条开始
    private Integer pageSize;// 用于分页，每页几条

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
