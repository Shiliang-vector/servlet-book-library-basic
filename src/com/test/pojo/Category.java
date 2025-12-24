package com.test.pojo;

/**
 * 分类实体：用于描述书籍所属的类型，例如“文学”“科技”。
 */
public class Category {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
