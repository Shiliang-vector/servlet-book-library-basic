package com.test.pojo;

/**
 * 读者筛选条件，供后台列表分页查询使用。
 */
public class ReaderCriteria {
    private String mobile;// 手机
    private String idCard;// 身份证号
    private String status;// 状态
    private Integer limit;// 用于分页，从哪条开始
    private Integer pageSize;// 用于分页，每页几条

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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
