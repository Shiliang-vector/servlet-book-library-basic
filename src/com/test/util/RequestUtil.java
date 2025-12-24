package com.test.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理请求参数的工具类。
 * Servlet 请求参数默认是字符串，此处提供便捷的类型转换与空值处理，避免在 Servlet 中反复写 try/catch。
 */
public class RequestUtil {
    /**
     * 将请求参数转换为 Integer，无法转换或为空时返回 null。
     */
    public static Integer getInteger(HttpServletRequest request, String name) {
        String s = request.getParameter(name);
        if (s != null && s.trim().length() > 0) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
            }
        }
        return null;
    }
    /**
     * 将请求参数转换为 Double，无法转换或为空时返回 null。
     */
    public static Double getDouble(HttpServletRequest request, String name) {
        String s = request.getParameter(name);
        if (s != null && s.trim().length() > 0) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
            }
        }
        return null;
    }
    //转换为Double

    /**
     * 返回去除空串后的字符串参数，空白字符串将返回 null。
     */
    public static String getString(HttpServletRequest request, String name) {
        String s = request.getParameter(name);
        if (s != null && s.trim().length() > 0) {
            try {
                return s;
            } catch (Exception e) {
            }
        }
        return null;
    }
    //判断是否为空字符串，非空返回，为空返回null

    /**
     * 直接返回原始字符串参数，保留空串，便于特定场景自行处理。
     */
    public static String getStringWithBlank(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }
    //不进行判断直接返回参数

    /**
     * 按指定日期格式解析请求参数，解析失败或为空时返回 null。
     */
    public static Date getDate(HttpServletRequest request, String name, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String s = request.getParameter(name);
        if (s != null && s.trim().length() > 0) {
            try {
                return format.parse(s);
            } catch (Exception e) {
            }
        }
        return null;
    }
    //根据给定的时间格式，将字符串转换成 java.util.Date 对象。
}
