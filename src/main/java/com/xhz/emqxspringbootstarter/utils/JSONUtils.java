package com.xhz.emqxspringbootstarter.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Hongzhuo Xu
 * @date 2022/01/10
 */
public class JSONUtils {

    public static JSONObject isOk(JSONObject jsonObject) {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("data",jsonObject);
        return result;
    }

    public static JSONObject isErr(String message) {
        JSONObject result = new JSONObject();
        result.put("code", 500);
        result.put("msg", message);
        return result;
    }
}
