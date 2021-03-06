package cn.kk20.chat.base.http;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @Description: http请求返回实体
 * @Author: Roy
 * @Date: 2019-10-25 09:47
 * @Version: v1.0
 */
public class ResultData<T> implements Serializable {

    public enum ResultCode {
        SUCCESS(200, "操作成功"),
        REDIRECT(300, "重定向"),
        REQUEST_ERROR(400, "错误请求"),
        CERTIFICATION_FAIL(401, "身份验证未通过"),
        SERVER_ERROR(500, "服务器内部出错"),
        CUSTOM_ERROR(600, "自定义错误信息");

        private int code;
        private String des;

        ResultCode(int code, String des) {
            this.code = code;
            this.des = des;
        }

        public int getCode() {
            return code;
        }

        public String getDes() {
            return des;
        }
    }

    private int code;
    private String msg;
    // 该属性用于开发测试阶段，用于定位问题
    private String exception;
    // 返回实体必须是BaseDto及其子类
    private T data;

    public static <T> ResultData success(T data) {
        ResultData resultData = new ResultData();
        resultData.setCode(ResultCode.SUCCESS.code);
        resultData.setMsg("操作成功");
        resultData.setData(data);
        return resultData;
    }

    public static ResultData successWithMsg(String msg) {
        ResultData resultData = new ResultData();
        resultData.setCode(ResultCode.SUCCESS.code);
        resultData.setMsg(StringUtils.isEmpty(msg) ? "操作成功" : msg);
        return resultData;
    }

    public static ResultData requestError() {
        return createResultData(ResultCode.REQUEST_ERROR, null);
    }

    public static ResultData requestError(String msg) {
        return createResultData(ResultCode.REQUEST_ERROR, msg);
    }

    public static ResultData serverError() {
        return createResultData(ResultCode.SERVER_ERROR, null);
    }

    public static ResultData serverError(String msg) {
        return createResultData(ResultCode.SERVER_ERROR, msg);
    }

    public static ResultData createResultData(ResultCode resultCode, String msg) {
        ResultData resultData = new ResultData();
        resultData.setCode(resultCode.code);
        resultData.setMsg(msg == null ? resultCode.des : msg);
        return resultData;
    }

    public static ResultData fail(ResultCode resultCode, String msg) {
        return fail(resultCode, msg, null);
    }

    public static ResultData fail(ResultCode resultCode, String msg, String exception) {
        ResultData resultData = createResultData(resultCode, msg).setException(exception);
        return resultData;
    }

    public int getCode() {
        return code;
    }

    public ResultData setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getException() {
        return exception;
    }

    public ResultData setException(String exception) {
        this.exception = exception;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultData setData(T data) {
        this.data = data;
        return this;
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isSuccess() {
        return code == ResultCode.SUCCESS.getCode();
    }

}
