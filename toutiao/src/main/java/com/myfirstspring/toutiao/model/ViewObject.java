package com.myfirstspring.toutiao.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/20.
 */
public class ViewObject {
    private Map<String, Object> map = new HashMap<String, Object>();
    public void set(String key, Object value){
        map.put(key, value);
    }
    public Object get(String key){
        return map.get(key);
    }

}
