package com.hflw.vasp.web;

import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public final static String CODE_KEY = "code";
    public final static String MSG_KEY = "msg";
    private final static String DATA_KEY = "data";
    private final static String LIST_KEY = "list";
    private final static String PAGE_KEY = "page";
    private final static String TOTAL_PAGES_KEY = "totalPages";
    private final static String TOTAL_ELEMENTS_KEY = "totalElements";

    public R() {
        put(CODE_KEY, 200);
        put(MSG_KEY, "success");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put(CODE_KEY, code);
        r.put(MSG_KEY, msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put(MSG_KEY, msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R data(Object o) {
        R r = new R();
        r.put(DATA_KEY, o);
        return r;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 分页信息，需要前端自己算分页数
     *
     * @param value
     * @param totalElements
     * @return
     */
    public R putPageData(Object value, long totalElements) {
        Map data = new HashMap(2);
        data.put(LIST_KEY, value);
        data.put(TOTAL_ELEMENTS_KEY, totalElements);
        super.put(DATA_KEY, data);
        return this;
    }

    /**
     * 分页信息
     *
     * @param page
     * @return
     */
    public R putPageData(Page page) {
        Map data = new HashMap(3);
        data.put(LIST_KEY, page.getContent());//分页数据
        data.put(TOTAL_PAGES_KEY, page.getTotalPages());//总页数
        data.put(TOTAL_ELEMENTS_KEY, page.getTotalElements());//总数
        super.put(DATA_KEY, data);
        return this;
    }

    /**
     * 分页信息，需要前端自己算分页数
     *
     * @param page
     * @return
     */
    public R putPageData(Pagination page) {
        Map data = new HashMap(2);
        data.put(LIST_KEY, page.getList());
        data.put(TOTAL_ELEMENTS_KEY, page.getTotal());//总数
        super.put(DATA_KEY, data);
        return this;
    }

    public void setMessage(String formatMessage) {
        this.put(MSG_KEY, formatMessage);
    }

    public String getMessage() {
        return (String) this.get(MSG_KEY);
    }

    public void setCode(int code) {
        this.put(CODE_KEY, code);
    }

    public int getCode() {
        return (int) this.get(CODE_KEY);
    }

}
