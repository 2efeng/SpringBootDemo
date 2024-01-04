package com.hzf.auth.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.HashMap;

@Data
@Schema(description = "返回响应数据")
public class ResponseEntity<T> {

    @Schema(description = "编码")
    private int code = 200;

    @Schema(description = "基本信息")
    private String message = "成功";

    @Schema(description = "错误信息")
    private String errorMsg = "";

    @Schema(description = "返回对象")
    private T data;

    /**
     * 成功状态码
     */
    public static final Integer SUCCESS = 200;

    /**
     * 失败状态码
     */
    public static final Integer ERROR = 500;


    private static HashMap<Integer, String> ERROR_CODE = new HashMap<>() {
        {
            put(100, "暂无数据");
            put(200, "成功");
            put(300, "失败");
            put(500, "失败状态码");
            put(10000, "通用错误");
            put(10001, "用户名或密码错误");
            put(10002, "登录状态已过期");
            put(10003, "注册用户已存在");
            put(10004, "账号已被锁定,请在一小时后重试");
            put(10005, "旧密码错误");
            put(10006, "用户名已存在");
            put(10007, "ip没有权限");
            put(10008, "token无效");
            put(10009, "token失效");
            put(20001, "无操作权限");
            put(30001, "非法参数");
            put(30002, "缺少必要参数");
            put(40001, "添加数据失败");
            put(40002, "更新数据失败");
            put(40003, "删除数据失败");
            put(40004, "添加数据失败,对象已经存在，建议修改或者删除");
            put(50001, "不存在的对象");
            put(99999, "无任何资源权限");
            put(990000, "系统错误");
        }
    };


    public ResponseEntity() {
    }

    public static <T> ResponseEntity<T> ok(@Nullable T data) {
        return new ResponseEntity<>(data, 200);
    }

    public static <T> ResponseEntity<T> error(int code) {
        if (ERROR_CODE.containsKey(code)) {
            return new ResponseEntity<>(code, ERROR_CODE.get(code));
        }
        return new ResponseEntity<>(code, "");
    }

    public ResponseEntity(T data) {
        this.data = data;
    }


    public ResponseEntity(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public ResponseEntity(int code, String message) {
        this.message = message;
        this.code = code;
    }

    public ResponseEntity<T> setCode(int code) {
        this.code = code;
        if (ERROR_CODE.containsKey(code)) {
            setMessage(ERROR_CODE.get(code));
        }
        return this;
    }

    public ResponseEntity<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> ResponseEntity<T> def(Class<T> clazz) {
        return new ResponseEntity<>();
    }

//    public ResponseEntity<T> ok() {
//        setCode(200);
//        return this;
//    }
//
//    public ResponseEntity<T> error(int code) {
//        setCode(code);
//        return this;
//    }

    public ResponseEntity<T> message(String message) {
        setMessage(message);
        return this;
    }

    public ResponseEntity<T> data(T data) {
        setData(data);
        return this;
    }

    public ResponseEntity<T> back(int code, String message, T data) {
        setCode(code);
        setMessage(message);
        setData(data);
        return this;
    }

    public static <T> Boolean isError(ResponseEntity<T> r) {
        return !isSuccess(r);
    }

    public static <T> Boolean isSuccess(ResponseEntity<T> r) {
        return ResponseEntity.SUCCESS == r.getCode();
    }
}