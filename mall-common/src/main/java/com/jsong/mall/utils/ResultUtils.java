package com.jsong.mall.utils;

/**
 * 2020/5/13 15:43
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public class ResultUtils<T> {

    Result<T> result;

    public ResultUtils() {
        result = new Result<>();
        result.setCode(200);
        result.setData(null);
        result.setMessage("success");
        result.setTimestamp(System.currentTimeMillis());
    }

    public Result<T> setErrorMsg(String message){
        this.result.setCode(500);
        this.result.setMessage(message);
        this.result.setTimestamp(System.currentTimeMillis());
        this.result.setData(null);
        return this.result;
    }

    public Result<T> setData(T data){
        this.result.setCode(200);
        this.result.setMessage("success");
        this.result.setTimestamp(System.currentTimeMillis());
        this.result.setData(data);
        return this.result;
    }

    public Result<T> ok(){
        return this.result;
    }

}
