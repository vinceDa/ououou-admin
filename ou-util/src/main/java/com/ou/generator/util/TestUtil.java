package com.ou.generator.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vince
 * @date 2020/1/3 10:54
 */
public class TestUtil {

    public static void main(String[] args) {
        File file = new File("C:\\Users\\vince\\Desktop\\api.txt");
        String s = FileUtil.readString(file, "utf-8");
        JSONObject parse = JSONUtil.parseObj(s);
        JSONArray infos = parse.getJSONObject("playlist").getJSONArray("tracks");
        List<String> names = new ArrayList<>();
        for (Object info : infos) {
            JSONObject single = (JSONObject) info;
            names.add(single.getStr("name"));
        }
        System.out.println(names.size());
        System.out.println(names.toString());
    }
}
