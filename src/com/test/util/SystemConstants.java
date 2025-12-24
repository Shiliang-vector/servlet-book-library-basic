package com.test.util;

/**
 * 系统级常量统一维护处，避免魔法字符串散落各处：
 *   路径常量：统一 JSP 目录与 Session key
 *   用户/账户状态：用于权限控制与前端展示
 *   图书与借阅状态：与数据库枚举值保持一致
 */
public interface SystemConstants {

    // Session 相关
    String SESSION_USER = "SESSION_USER"; // 登录用户存储的 key
    String PAGE_PATH = "/WEB-INF/pages/"; // 所有 JSP 页面存放的位置

    // 用户类型
    String USER_ADMIN = "admin";
    String USER_READER = "reader";

    // 账户状态
    String STATUS_COMMON = "正常";
    String STATUS_DISABLE = "禁用";

    // 书本状态
    String BOOK_OFF = "下架";
    String BOOK_ON = "上架";

    // 借阅状态
    String RECORD_BORROW = "借阅中";
    String RECORD_RETURN = "已归还";

}
