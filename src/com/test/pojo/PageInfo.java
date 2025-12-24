package com.test.pojo;

/**
 * 分页实体：用于在列表页面输出页码导航相关信息。
 * 计算逻辑固定，默认展示 5 个导航页，便于 JSP 直接使用。
 */
public class PageInfo {

    private Integer pageNum = 1;// 页码

    private Integer pageSize = 2;// 每页几条

    private Integer totalCount = 0;// 总共多少条

    private Integer totalPage = 0;// 总共多少页

    private Integer startPage = 1;// 导航栏开始页码
    private Integer endPage = 5;// 导航栏结束页码

    private Boolean hasNextPage = false;

    public PageInfo(Integer pageNum, Integer pageSize, Integer totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPage = (totalCount + pageSize - 1) / pageSize;
        this.hasNextPage = this.pageNum < this.totalPage;
        if (this.totalPage <= 5) {
            this.startPage = 1;
            this.endPage = totalPage;
            return;
        }
        if (this.pageNum < 3) {
            this.startPage = 1;
            this.endPage = 5;
            return;
        }
        if (this.pageNum + 2 <= this.totalPage) {
            this.startPage = this.pageNum - 2;
            this.endPage = this.pageNum + 2;
        } else {
            this.endPage = this.totalPage;
            this.startPage = this.endPage - 4;
        }
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getStartPage() {
        return startPage;
    }

    public Integer getEndPage() {
        return endPage;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }
}
