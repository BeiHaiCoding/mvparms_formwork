package com.cwc.mylibrary.utils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json数据处理帮助类
 */
public class JsonHelper {

    /**
     * map集合转为json字符串
     * @param maps
     * @return
     */
    public static String mapListToJsonStr(List<Map<String, Object>> maps) {
        if (maps == null || maps.isEmpty()) {
            return "";
        }
        String jsonStr = "[";

        for (Map<String, Object> map : maps) {
            jsonStr += mapToJsonStr(map)+",";
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);

        jsonStr += "]";
        return jsonStr;
    }

    /**
     * map转为json数据
     * @param map
     * @return
     */
    public static String mapToJsonStr(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        String jsonStr = "{";
        Set<?> keySet = map.keySet();
        for (Object key : keySet) {
            jsonStr += "\"" + key + "\":\"" + map.get(key) + "\",";
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        jsonStr += "}";
        return jsonStr;
    }

    /**
     * json数据转为map
     * @param str
     * @return
     */
    public static Map jsonStrTomap(String str) {
        String sb = str.substring(1, str.length() - 1);
        String[] name = sb.split("\\\",\\\"");
        String[] nn = null;
        Map map = new HashMap();
        for (int i = 0; i < name.length; i++) {
            nn = name[i].split("\\\":\\\"");
            map.put(nn[0], nn[1]);
        }
        return map;
    }

    /**
     * java bean 转化为 json 数据
     * @param obj
     * @return
     */
    public static String modelToJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object jsonToModel(String jsonStr,Class clazz){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr,clazz);
    }


}
