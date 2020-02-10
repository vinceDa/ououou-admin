package com.ou.generator.config;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.ResourceUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;

/**
 * @author vince
 * @date 2020/1/3 15:35
 */
@Data
public class FreemarkerInfoConfig {

    /**
     *  模板名
     */
    private String templateName;

    /**
     *  所属包名
     */
    private String packageName;

    /**
     *  文件名
     */
    private String filename;

    /**
     *  是否依赖于项目
     */
    private Boolean dependOnProject;

    /**
     *  是否可以覆盖
     */
    private Boolean isOverride;

    public List<FreemarkerInfoConfig> init() {
        List<FreemarkerInfoConfig> res = null;
        try {
            String path = ResourceUtils.getURL("classpath:freemarker/Config.json").getPath();
            String fileContent = FileUtil.readString(path, StandardCharsets.UTF_8);
            JSONObject json = JSONObject.parseObject(fileContent);
            JSONArray jsonArray = json.getJSONArray("config");
            res = Convert.convert(new TypeReference<List<FreemarkerInfoConfig>>() {}, jsonArray);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return res;
    }
}
