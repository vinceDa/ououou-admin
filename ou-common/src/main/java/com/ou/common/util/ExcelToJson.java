//package com.ou.common.util;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.json.JSONUtil;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.*;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * @Description TODO
// * @Author TT
// * @Date 2019/12/16 15:25
// */
//public class ExcelToJson {
//    public static void main(String[] args) throws IOException {
//        // String path = "/data/IVDP/excel/3333333.xlsx";
//        String path = "C:\\Users\\mi\\Documents\\WeChat Files\\jdzsfksm\\FileStorage\\File\\2020-03\\业务处理接口文档(11111).xlsx";
////        String path = "C:\\Users\\mi\\Documents\\WeChat Files\\jdzsfksm\\FileStorage\\File\\2020-03\\3333333.xlsx";
//        // 文件夹路径
////        String fileFolder = "/data/IVDP/mPassJson";
//        String fileFolder = "/mPassJson";
//        //String mpassAppId = "ONEX71632FB031912";// mpassAppId
//        // mpassAppId
//        String mpassAppId = "ONEXD023482071824";
//        // 系统名称
//        String systemParm = "ououououou";
//        // 系统请求
//        String host = "http://118.144.87.37:13083";
//        // 请求格式
//        String method = "POST";
//
//        JSONArray JSONArray = workbook(path);
//        JSONArray JSONArrayOfResponseType = workbooks(path);
//        JSONObject jsonObject = new JSONObject();
//        JSONObject moduls = new JSONObject();
//        JSONArray apis = new JSONArray();
//        JSONObject systems = system(mpassAppId, systemParm, host);
//        String sysName = systems.getJSONObject(systemParm).getString("sysName");
//        String appId = systems.getJSONObject(systemParm).getString("appId");
//        String id = systems.getJSONObject(systemParm).getString("id");
//        int sysId = Integer.parseInt(id);
//        JSONObject params = new JSONObject();
//        jsonObject.put("date", "1576586178891");
//        jsonObject.put("models", moduls);
//        jsonObject.put("apis", apis);
//        jsonObject.put("systems", systems);
//        jsonObject.put("appId", mpassAppId);
//        jsonObject.put("params", params);
//        jsonObject.put("operator", "ectip");
//        jsonObject.put("workspaceId", "jxnx_mpass_dev");
//        for (int i = 0; i < JSONArray.size(); i++) {
//
//            JSONObject resopnseJSONObject = JSONArray.getJSONObject(i).getJSONObject("resopnseJSONObject");
//            JSONObject requestJSONObject = JSONArray.getJSONObject(i).getJSONObject("requestJSONObject");
//            JSONObject resopnseJSONObjectOfParmType = JSONArrayOfResponseType.getJSONObject(i).getJSONObject("resopnseJSONObject");
//            String url = JSONArray.getJSONObject(i).getString("url");
//            // 暂时以页面sheet名称当作接口中文名
//            String sheetName = JSONArray.getJSONObject(i).getString("sheetName");
//            int apisid = 25000 + i;
//            int paramsid = 311111 + i;
//            apis.add(api(resopnseJSONObject, appId, sysName, url, sheetName, apisid, sysId, method));
//            JSONObject json = api(resopnseJSONObject, appId, sysName, url, sheetName, apisid, sysId, method);
//            int apiId = Integer.parseInt(json.getString("id"));
//            // 引用请求参数
//            String reqmodul = url + "_req";
//            String reqRefType = "req" + getUUID();
//            // 引用返回参数
//            String respmodul = url + "_resp";
//            String respRefType = "resp" + getUUID();
//            // key值不知道，暂时给的随机字符串
//            params.put(url, params(appId, apiId, reqmodul, respmodul, paramsid, requestJSONObject, resopnseJSONObjectOfParmType, method));
//            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
//                if (requestJSONObject != null && !requestJSONObject.isEmpty()) {
//                    moduls(moduls, appId, requestJSONObject, reqRefType, reqmodul, sheetName, i);
//                }
//            }
//
//            if (resopnseJSONObjectOfParmType != null && !resopnseJSONObjectOfParmType.isEmpty()) {
//                responseModuls(moduls, appId, resopnseJSONObjectOfParmType, respRefType, respmodul, sheetName, i);
//            }
//
//        }
//
//        String json = jsonObject.toString();
//
//        String fileName = getUUID();
//        String filePath = fileFolder + "/" + fileName + ".txt";
//
//        File f = new File(fileFolder);
//        @SuppressWarnings("static-access")
//        boolean b = deleteDir(f);
//        if (b) {
//            System.out.print("删除成功" + "\n");
//        }
//
//        boolean bool = writeFile(filePath, json, fileFolder);
//
//        if (bool) {
//            System.out.print("写入成功" + "\n");
//        }
//
//    }
//
//    /* json 往 aips 里面赋值 */
//    public static JSONObject api(JSONObject jsb, String appId, String sysName, String url, String chineseName, int id, int sysId, String method) {
//        JSONObject resultData = new JSONObject();
//        resultData.put("result", jsb);
//        resultData.put("tips", "ok");
//        resultData.put("resultStatus", 1000);
//
//        Map<String, Object> map = new HashMap<>();
//        JSONObject json1 = new JSONObject();
//        JSONObject apiInvoker = new JSONObject();
//        JSONObject httpInvoker = new JSONObject();
//        JSONObject cacheRule = new JSONObject();
//        Map<String, Object> limitRule = new HashMap<>();
//        limitRule.put("defaultResponse", "");
//        limitRule.put("i18nResponse", null);
//        limitRule.put("interval", 1);
//        limitRule.put("limit", 0.0);
//        limitRule.put("mode", "CLOSED");
//        Map<String, Object> migrateRule = new HashMap<>();
//        migrateRule.put("flowPercent", 0);
//        migrateRule.put("needMigrate", false);
//        migrateRule.put("needSwitchCompletely", false);
//        migrateRule.put("sysId", -1);
//        migrateRule.put("sysName", -1);
//        migrateRule.put("upstreamType", null);
//        JSONObject mockRule = new JSONObject();
//        mockRule.put("mockData", resultData);
//        mockRule.put("needMock", true);
//        mockRule.put("percentage", 100);
//        mockRule.put("type", "PERCENT");
//        cacheRule.put("cacheKey", "");
//        cacheRule.put("needCache", "false");
//        cacheRule.put("ttl", "0");
//        httpInvoker.put("charset", "UTF-8");
//        httpInvoker.put("contentType", "application/json");
//        httpInvoker.put("host", "");
//        httpInvoker.put("method", method);
//        // 接口路径
//        httpInvoker.put("path", url);
//        apiInvoker.put("httpInvoker", httpInvoker);
//        apiInvoker.put("rpcInvoker", "null");
//        json1.put("apiInvoker", apiInvoker);
//        // 动态 接口名称 唯一值
//        json1.put("apiName", chineseName);
//        json1.put("apiStatus", "OPENED");
//        json1.put("apiType", "HTTP");
//        // appId 要获取
//        json1.put("appId", appId);
//        json1.put("authRuleName", "");
//        json1.put("cacheRule", cacheRule);
//        // 动态
//        json1.put("description", chineseName);
//        json1.put("gmtCreate", getNowDateTime());
//        json1.put("gmtModified", getNowDateTime());
//        // 随机数
//        json1.put("id", id);
//        json1.put("limitRule", limitRule);
//        json1.put("migrateRule", migrateRule);
//        json1.put("mockRule", mockRule);
//        json1.put("needETag", "T");
//        json1.put("needEncrypt", "");
//        json1.put("needJsonp", "");
//        json1.put("needSign", "T");
//        // 名称
//        json1.put("operationType", url);
//        json1.put("otherConfigMap", map);
//        json1.put("paramGetMethod", "");
//        json1.put("sysId", sysId);
//        // 自己取
//        json1.put("sysName", sysName);
//        json1.put("timeout", "");
//        // 工作环境id，待确认
//        json1.put("workspaceId", "jxnx_mpass_dev");
//        return json1;
//
//    }
//
//    /**
//     * 往mpass json 的modus里面塞值
//     *
//     * @param moduls
//     * @param appId
//     * @param requestJSONObject
//     * @param refType
//     * @param modul
//     * @param i
//     * @return
//     */
//    public static JSONObject moduls(JSONObject moduls, String appId, JSONObject requestJSONObject, String refType, String modul, String sheetName, int i) {
//        int id = 3000000;
//        id = id + i;
//        JSONObject mo = new JSONObject();
//        mo.put("appId", appId);
//        mo.put("gmtCreate", getNowDateTime());
//        mo.put("gmtModified", getNowDateTime());
//        mo.put("id", id);
//        JSONArray items = new JSONArray();
//        mo.put("items", items);
//        JSONObject modelSchema = new JSONObject();
//        JSONArray required = new JSONArray();
//        JSONObject properties = new JSONObject();
//        int ids = 5000000;
//        ids = ids + i;
//        String substring = modul.substring(modul.lastIndexOf(".") + 1, modul.length());
//        for (JSONObject.Entry<String, Object> entry : requestJSONObject.entrySet()) {
//
//            ids = ids + 1;
//            Object valueOjb = entry.getValue();
//
//            cn.hutool.json.JSON jsons = JSONUtil.parse(valueOjb);
//            String ss = jsons.toString();
//            Object jsobj = JSONObject.parse(ss);
//            String key = entry.getKey().toString();
//
//            JSONObject item = new JSONObject();
//
//            modelSchema.put("type", "object");
//            modelSchema.put("title", sheetName);// 命名
//            // array
//            if (jsobj instanceof JSONArray) {
//
//                JSONObject listParam = ofList(jsobj, appId, substring + "_" + entry.getKey(), sheetName, ids);
//                moduls.put(substring + "_" + entry.getKey(), listParam);
//                JSONObject proValue = new JSONObject();
//                JSONObject itemss = new JSONObject();
//                for (int k = 0; k < ((JSONArray) jsobj).size(); k++) {
//                    JSONObject js = ((JSONArray) jsobj).getJSONObject(k);
//                    proValue.put("type", "array");
//                    proValue.put("title", "List");
//                    proValue.put("items", itemss);
//                    item.put("df", "");
//                    item.put("name", key);
//                    item.put("refType", substring + "_" + entry.getKey());// 自己命名
//                    item.put("title", "List");// list类型
//                    item.put("type", "List");// list类型
//                    itemss.put("type", "object");
//                    itemss.put("title", sheetName);// 命名
//                    JSONObject propertiess = new JSONObject();
//                    JSONArray requireds = new JSONArray();
//                    for (JSONObject.Entry<String, Object> entry1 : js.entrySet()) {
//                        String key1 = entry1.getKey();
//                        if (entry1.getValue() instanceof JSONObject) {
//                            String description = ((JSONObject) entry1.getValue()).get("description").toString();
//                            String type1 = ((JSONObject) entry1.getValue()).get("type").toString();
//
//                            JSONObject proValue1 = new JSONObject();
//                            proValue1.put("type", getDataType(type1));
//                            proValue1.put("title", description);
//
//                            requireds.add(key1);
//                            propertiess.put(key1, proValue1);
//                        }
//
//                    }
//
//                    itemss.put("properties", propertiess);
//                    itemss.put("required", requireds);
//
//                    properties.put(key, proValue);
//
//                }
//                items.add(item);
//                required.add(key);
//            }
//            // 字符串
//            if (jsobj instanceof JSONObject) {
//                String jsonType = (String) ((JSONObject) jsobj).get("jsonType");
//                if (jsonType.equals("1")) {
//                    String type = (String) ((JSONObject) jsobj).get("type");
//                    String description = (String) ((JSONObject) jsobj).get("description");
//                    item.put("df", "");
//                    item.put("name", key);
//                    item.put("refType", "");
//                    item.put("title", description);// 描述
//                    item.put("type", type);// 类型
//                    items.add(item);
//                    JSONObject proValue = new JSONObject();
//                    proValue.put("type", getDataType(type));
//                    proValue.put("title", description);
//                    properties.put(key, proValue);
//
//                    required.add(key);
//
//                }
//            }
//
//            // json类型
//
//            if (jsobj instanceof JSONObject) {
//
//                String jsonType = (String) ((JSONObject) jsobj).get("jsonType");
//                if ("2".equals(jsonType)) {
//                    String paramName = substring;
//                    String apikey = paramName + "_" + entry.getKey();
//                    String refType1 = apikey;
//
//                    JSONObject listParm = ofListOfJson(jsobj, appId, refType1, sheetName, ids);
//                    moduls.put(refType1, listParm);
//                    JSONObject proValue = new JSONObject();
//                    JSONObject itemss = new JSONObject();
//
//                    proValue.put("type", "object");
//                    proValue.put("title", "Object");
//                    proValue.put("items", itemss);
//                    item.put("df", "");
//                    item.put("name", key);
//                    item.put("refType", refType1);// 自己命名
//                    item.put("title", "Object");// list类型
//                    item.put("type", "Object");// list类型
//                    itemss.put("type", "object");
//                    itemss.put("title", sheetName);// 命名
//                    JSONObject propertiess = new JSONObject();
//                    JSONArray requireds = new JSONArray();
//                    for (JSONObject.Entry<String, Object> entry1 : ((JSONObject) jsobj).entrySet()) {
//                        String key1 = entry1.getKey();
//                        if (entry1.getValue() instanceof JSONObject) {
//                            String description = ((JSONObject) entry1.getValue()).get("description").toString();
//                            String type1 = ((JSONObject) entry1.getValue()).get("type").toString();
//
//                            JSONObject proValue1 = new JSONObject();
//                            proValue1.put("type", getDataType(type1));
//                            proValue1.put("title", description);
//
//                            requireds.add(key1);
//                            propertiess.put(key1, proValue1);
//                        }
//
//                    }
//                    itemss.put("properties", propertiess);
//                    itemss.put("required", requireds);
//                    properties.put(key, proValue);
//                    items.add(item);
//                    required.add(key);
//                }
//
//            }
//
//            modelSchema.put("properties", properties);
//            modelSchema.put("required", required);
//            mo.put("modelSchema", modelSchema);
//        }
//        String modeulName = substring;
//        mo.put("name", modeulName);
//        // 命名
//        mo.put("title", sheetName);
//        mo.put("workspaceId", "jxnx_mpass_dev");
//        moduls.put(modeulName, mo);
//        return moduls;
//    }
//
//    public static JSONObject responseModuls(JSONObject moduls, String appId, JSONObject requestJSONObject, String refType, String modul, String sheetName, int i) {
//        int id = 3000000;
//        id = id + i;
//        JSONObject mo = new JSONObject();
//        mo.put("appId", appId);
//        mo.put("gmtCreate", getNowDateTime());
//        mo.put("gmtModified", getNowDateTime());
//        mo.put("id", id);
//        JSONArray items = new JSONArray();
//        mo.put("items", items);
//        JSONObject modelSchema = new JSONObject();
//        JSONArray required = new JSONArray();
//        JSONObject properties = new JSONObject();
//        int ids = 5000000;
//        ids = ids + i;
//        for (JSONObject.Entry<String, Object> entry : requestJSONObject.entrySet()) {
//            ids = ids + 1;
//            Object valueOjb = entry.getValue();
//            cn.hutool.json.JSON jsons = JSONUtil.parse(valueOjb);
//            String ss = jsons.toString();
//            Object jsobj = JSONObject.parse(ss);
//            String key = entry.getKey().toString();
//            JSONObject item = new JSONObject();
//            modelSchema.put("type", "object");
//            modelSchema.put("title", sheetName);// 命名
//            // array
//            if (jsobj instanceof JSONArray) {
//
//
//                String paramName = modul.substring(modul.lastIndexOf(".") + 1, modul.length());
//                String apikey = paramName + "_" + entry.getKey();
//                String refType1 = apikey;
//                JSONObject listParm = ofList(jsobj, appId, refType1, sheetName, ids);
//                moduls.put(refType1, listParm);
//                JSONObject proValue = new JSONObject();
//                JSONObject itemss = new JSONObject();
//                for (int k = 0; k < ((JSONArray) jsobj).size(); k++) {
//                    JSONObject js = ((JSONArray) jsobj).getJSONObject(k);
//                    proValue.put("type", "array");
//                    proValue.put("title", "List");
//                    proValue.put("items", itemss);
//                    item.put("df", "");
//                    item.put("name", key);
//                    // 自己命名
//                    item.put("refType", refType1);
//                    // list类型
//                    item.put("title", "List");
//                    // list类型
//                    item.put("type", "List");
//                    itemss.put("type", "object");
//                    // 命名
//                    itemss.put("title", sheetName);
//                    JSONObject propertiess = new JSONObject();
//                    JSONArray requireds = new JSONArray();
//                    for (JSONObject.Entry<String, Object> entry1 : js.entrySet()) {
//                        String key1 = entry1.getKey();
//                        if (entry1.getValue() instanceof JSONObject) {
//                            String description = ((JSONObject) entry1.getValue()).get("description").toString();
//                            String type1 = ((JSONObject) entry1.getValue()).get("type").toString();
//                            JSONObject proValue1 = new JSONObject();
//                            proValue1.put("type", getDataType(type1));
//                            proValue1.put("title", description);
//                            requireds.add(key1);
//                            propertiess.put(key1, proValue1);
//                        }
//                    }
//                    itemss.put("properties", propertiess);
//                    itemss.put("required", requireds);
//                    properties.put(key, proValue);
//                }
//                items.add(item);
//                required.add(key);
//            }
//            // 字符串
//            if (jsobj instanceof JSONObject) {
//                String jsonType = (String) ((JSONObject) jsobj).get("jsonType");
//                if (jsonType.equals("1")) {
//                    String type = (String) ((JSONObject) jsobj).get("type");
//                    String description = (String) ((JSONObject) jsobj).get("description");
//                    item.put("df", "");
//                    item.put("name", key);
//                    item.put("refType", "");
//                    // 描述
//                    item.put("title", description);
//                    // 类型
//                    item.put("type", type);
//                    items.add(item);
//                    JSONObject proValue = new JSONObject();
//                    proValue.put("type", getDataType(type));
//                    proValue.put("title", description);
//                    properties.put(key, proValue);
//                    required.add(key);
//
//                }
//            }
//
//            // json类型
//
//            if (jsobj instanceof JSONObject) {
//                String jsonType = (String) ((JSONObject) jsobj).get("jsonType");
//                if (jsonType.equals("2")) {
//                    String paramName = modul.substring(modul.lastIndexOf(".") + 1, modul.length());
//                    String apikey = paramName + "_" + entry.getKey();
//                    String refType1 = apikey;
//                    JSONObject listParm = ofListOfJson(jsobj, appId, refType1, sheetName, ids);
//                    moduls.put(refType1, listParm);
//                    JSONObject proValue = new JSONObject();
//                    JSONObject itemss = new JSONObject();
//                    proValue.put("type", "object");
//                    proValue.put("title", "Object");
//                    proValue.put("items", itemss);
//                    item.put("df", "");
//                    item.put("name", key);
//                    item.put("refType", refType1);// 自己命名
//                    item.put("title", "Object");// list类型
//                    item.put("type", "Object");// list类型
//                    itemss.put("type", "object");
//                    itemss.put("title", sheetName);// 命名
//                    JSONObject propertiess = new JSONObject();
//                    JSONArray requireds = new JSONArray();
//                    for (JSONObject.Entry<String, Object> entry1 : ((JSONObject) jsobj).entrySet()) {
//                        String key1 = entry1.getKey();
//                        if (entry1.getValue() instanceof JSONObject) {
//                            String description = ((JSONObject) entry1.getValue()).get("description").toString();
//                            String type1 = ((JSONObject) entry1.getValue()).get("type").toString();
//
//                            JSONObject proValue1 = new JSONObject();
//                            proValue1.put("type", getDataType(type1));
//                            proValue1.put("title", description);
//                            requireds.add(key1);
//                            propertiess.put(key1, proValue1);
//                        }
//                    }
//                    itemss.put("properties", propertiess);
//                    itemss.put("required", requireds);
//                    properties.put(key, proValue);
//                    items.add(item);
//                    required.add(key);
//                }
//            }
//            modelSchema.put("properties", properties);
//            modelSchema.put("required", required);
//            mo.put("modelSchema", modelSchema);
//        }
//        String modeulName = modul.substring(modul.lastIndexOf(".") + 1, modul.length());
//        mo.put("name", modeulName);
//        mo.put("title", sheetName);// 命名
//        mo.put("workspaceId", "jxnx_mpass_dev");// 写死了
//        moduls.put(modeulName, mo);
//        return moduls;
//    }
//
//    /**
//     * modus的单条数据塞值
//     *
//     * @param jsobj
//     * @param appId
//     * @param refType
//     * @return
//     */
//    public static JSONObject ofList(Object jsobj, String appId, String refType, String sheetName, int id) {
//        JSONObject mo = new JSONObject();
//        mo.put("appId", appId);
//        mo.put("gmtCreate", getNowDateTime());
//        mo.put("gmtModified", getNowDateTime());
//        mo.put("id", id);
//        JSONArray items = new JSONArray();
//        mo.put("items", items);
//        JSONObject modelSchema = new JSONObject();
//        JSONArray required = new JSONArray();
//        JSONObject properties = new JSONObject();
//
//        for (int k = 0; k < ((JSONArray) jsobj).size(); k++) {
//
//            JSONObject js = ((JSONArray) jsobj).getJSONObject(k);
//            modelSchema.put("type", "object");
//            modelSchema.put("title", sheetName);// 命名
//            for (JSONObject.Entry<String, Object> entry1 : js.entrySet()) {
//                if (entry1.getValue() instanceof JSONObject) {
//                    String key = entry1.getKey();
//                    String description = ((JSONObject) entry1.getValue()).get("description").toString();
//                    String type = ((JSONObject) entry1.getValue()).get("type").toString();
//                    JSONObject item = new JSONObject();
//                    item.put("df", "");
//                    item.put("name", key);
//                    item.put("refType", "");
//                    item.put("title", description);// 动态
//                    item.put("type", type);// 动态
//                    items.add(item);
//                    JSONObject proValue = new JSONObject();
//                    proValue.put("type", getDataType(type));
//                    proValue.put("title", description);
//                    properties.put(key, proValue);
//                    required.add(key);
//                }
//            }
//            modelSchema.put("properties", properties);
//            modelSchema.put("required", required);
//            mo.put("modelSchema", modelSchema);
//        }
//        mo.put("name", refType);
//        mo.put("title", sheetName);// 命名
//        mo.put("workspaceId", "jxnx_mpass_dev");// 写死了
//        // moduls.put(modul, mo);
//
//        return mo;
//    }
//
//    public static JSONObject ofListOfJson(Object jsobj, String appId, String refType, String sheetName, int id) {
//        JSONObject mo = new JSONObject();
//        mo.put("appId", appId);
//        mo.put("gmtCreate", getNowDateTime());
//        mo.put("gmtModified", getNowDateTime());
//        mo.put("id", id);
//        JSONArray items = new JSONArray();
//        mo.put("items", items);
//        JSONObject modelSchema = new JSONObject();
//        JSONArray required = new JSONArray();
//        JSONObject properties = new JSONObject();
//        modelSchema.put("type", "object");
//        modelSchema.put("title", sheetName);// 命名
//        for (JSONObject.Entry<String, Object> entry1 : ((JSONObject) jsobj).entrySet()) {
//            if (entry1.getValue() instanceof JSONObject) {
//                String key = entry1.getKey();
//                String description = ((JSONObject) entry1.getValue()).get("description").toString();
//                String type = ((JSONObject) entry1.getValue()).get("type").toString();
//                JSONObject item = new JSONObject();
//                item.put("df", "");
//                item.put("name", key);
//                item.put("refType", "");
//                item.put("title", description);// 动态
//                item.put("type", type);// 动态
//                items.add(item);
//                JSONObject proValue = new JSONObject();
//                proValue.put("type", getDataType(type));
//                proValue.put("title", description);
//                properties.put(key, proValue);
//                required.add(key);
//            }
//
//        }
//
//        modelSchema.put("properties", properties);
//        modelSchema.put("required", required);
//        mo.put("modelSchema", modelSchema);
//        mo.put("name", refType);
//        mo.put("title", sheetName);// 命名
//        mo.put("workspaceId", "jxnx_mpass_dev");// 写死了
//
//        return mo;
//    }
//
//    /**
//     * parm 里面添加数据
//     *
//     * @param appId
//     * @return
//     */
//    public static JSONArray params(String appId, int apiId, String refType, String respRefType, int paramsid, JSONObject requestJSONObject, JSONObject resopnseJSONObjectOfParmType, String method) {
//        JSONArray jsonAry = new JSONArray();
//        if (method.equalsIgnoreCase("Post")) {
//
//            String refType1 = refType.substring(refType.lastIndexOf(".") + 1, refType.length());
//            String respRefType1 = respRefType.substring(respRefType.lastIndexOf(".") + 1, respRefType.length());
//            JSONObject reapustparams = new JSONObject();
//            reapustparams.put("apiId", apiId);
//            reapustparams.put("appId", appId);
//            reapustparams.put("defaultValue", "");
//            reapustparams.put("description", "");
//            reapustparams.put("id", paramsid);
//            reapustparams.put("location", "ReqBody");
//            reapustparams.put("name", "requestBody");
//            // 请求参数选择的模型
//            reapustparams.put("refType", refType1);
//            reapustparams.put("type", "Object");
//            // 工作环境id，待确定
//            reapustparams.put("workspaceId", "jxnx_mpass_dev");
//            JSONObject responseparams = new JSONObject();
//            responseparams.put("apiId", apiId);
//            responseparams.put("appId", appId);
//            responseparams.put("defaultValue", "");
//            responseparams.put("description", "");
//            responseparams.put("id", paramsid + 1);
//            responseparams.put("location", "RespBody");
//            responseparams.put("name", "responseBody");
//            if (resopnseJSONObjectOfParmType == null || resopnseJSONObjectOfParmType.isEmpty()) {
//                responseparams.put("refType", "String");
//            } else {
//                // 返回参数选择模型，默认String
//                responseparams.put("refType", respRefType1);
//            }
//
//            responseparams.put("type", "Object");
//            // 工作环境id，待确定
//            responseparams.put("workspaceId", "jxnx_mpass_dev");
//
//            if (requestJSONObject != null && !requestJSONObject.isEmpty()) {
//                jsonAry.add(reapustparams);
//            }
//            jsonAry.add(responseparams);
//        }
//        if ("GET".equalsIgnoreCase(method)) {
//            // String refType1 = refType.substring(refType.lastIndexOf(".") + 1, refType.length());
//            String respRefType1 = respRefType.substring(respRefType.lastIndexOf(".") + 1, respRefType.length());
//            //requestJSONObject
//            int paramsids = 6000000;
//            for (JSONObject.Entry<String, Object> entry : requestJSONObject.entrySet()) {
//
//                paramsids = paramsids + 1;
//                Object valueOjb = entry.getValue();
//
//                cn.hutool.json.JSON jsons = JSONUtil.parse(valueOjb);
//                String ss = jsons.toString();
//                Object jsobj = JSONObject.parse(ss);
//                String key = entry.getKey().toString();
//
//                if (jsobj instanceof JSONObject) {
//                    String jsonType = (String) ((JSONObject) jsobj).get("jsonType");
//                    if ("1".equals(jsonType)) {
//                        String type = (String) ((JSONObject) jsobj).get("type");
//                        String description = (String) ((JSONObject) jsobj).get("description");
//                        String value = (String) ((JSONObject) jsobj).get("value");
//
//                        JSONObject reapustparams = new JSONObject();
//                        reapustparams.put("apiId", apiId);
//                        reapustparams.put("appId", appId);
//                        reapustparams.put("defaultValue", value);
//                        reapustparams.put("description", description);
//                        reapustparams.put("id", paramsids);
//                        reapustparams.put("location", "Query");
//                        reapustparams.put("name", key);
//                        reapustparams.put("refType", "");// 请求参数选择的模型
//                        reapustparams.put("type", type);
//                        // 工作环境id，待确定
//                        reapustparams.put("workspaceId", "jxnx_mpass_dev");
//                        if (!requestJSONObject.isEmpty()) {
//                            jsonAry.add(reapustparams);
//                        }
//
//                    }
//                }
//
//
//            }
////            JSONObject reapustparams = new JSONObject();
////            reapustparams.put("apiId", apiId);
////            reapustparams.put("appId", appId);
////            reapustparams.put("defaultValue", "");
////            reapustparams.put("description", "");
////            reapustparams.put("id", paramsid);
////            reapustparams.put("location", "ReqBody");
////            reapustparams.put("name", "requestBody");
////            reapustparams.put("refType", refType1);// 请求参数选择的模型
////            reapustparams.put("type", "Object");
////            reapustparams.put("workspaceId", "jxnx_mpass_dev");// 工作环境id，待确定
//            JSONObject responseparams = new JSONObject();
//            responseparams.put("apiId", apiId);
//            responseparams.put("appId", appId);
//            responseparams.put("defaultValue", "");
//            responseparams.put("description", "");
//            responseparams.put("id", paramsid + 1);
//            responseparams.put("location", "RespBody");
//            responseparams.put("name", "responseBody");
//            if (resopnseJSONObjectOfParmType == null || resopnseJSONObjectOfParmType.isEmpty()) {
//                responseparams.put("refType", "String");
//            } else {
//                // 返回参数选择模型，默认String
//                responseparams.put("refType", respRefType1);
//            }
//
//            responseparams.put("type", "Object");
//            // 工作环境id，待确定
//            responseparams.put("workspaceId", "jxnx_mpass_dev");
//
//
//            jsonAry.add(responseparams);
//        }
//        return jsonAry;
//
//
//    }
//
//    /**
//     * 往系统里面塞值
//     *
//     * @param mpassAppId
//     * @return
//     */
//    public static JSONObject system(String mpassAppId, String systemParms, String host) {
//        Map<String, Object> map = new HashMap<>();
//        JSONObject json1 = new JSONObject();
//        //String ss = "ttTs";
//        JSONObject systemParm = new JSONObject();
//        systemParm.put("accessKey", "");
//        systemParm.put("accessSecret", "");
//        systemParm.put("appId", mpassAppId);
//        systemParm.put("centers", map);
//        systemParm.put("description", "");
//        systemParm.put("host", host);//服务ip地址
//        systemParm.put("id", 5218);// 待确定
//        systemParm.put("needSign", false);
//        systemParm.put("regionMode", false);
//        systemParm.put("signType", null);
//        systemParm.put("sysName", systemParms);// 系统名称
//        systemParm.put("timeout", 3000);
//        systemParm.put("upstreamType", "HTTP");
//        systemParm.put("workspaceId", "jxnx_mpass_dev");// 待确定
//        // json key 要与SysName 一样
//        json1.put(systemParms, systemParm);
//        return json1;
//    }
//
//    public static String getUUID() {
//
//        String id = UUID.randomUUID().toString();
//        String uuid = id.substring(0, 8) +
//                id.substring(9, 13) +
//                id.substring(14, 18) +
//                id.substring(19, 23) +
//                id.substring(24);
//        return uuid.toUpperCase();
//    }
//
//    public static String getNowDateTime() {
//        Date myDate = new Date(System.currentTimeMillis());
//        SimpleDateFormat sDateformat = new SimpleDateFormat("yyyyMMddHHmmss");
//        return sDateformat.format(myDate).toString();
//    }
//
//    /**
//     * 获取excel工作簿的值
//     *
//     * @param path
//     * @return
//     */
//    public static JSONArray workbook(String path) {
//        JSONArray array = new JSONArray();
//        InputStream is = null;
//        @SuppressWarnings("unused")
//        XSSFWorkbook workbook = null;
//
//        try {
//            is = new FileInputStream(path);
//            workbook = new XSSFWorkbook(is);
//
//            @SuppressWarnings("resource")
//            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path);
//
//            String name = xssfWorkbook.getSheetAt(2).getSheetName();
//            JSONObject jsb = apiWord(path, name);
//            for (int numSheet = 3; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++)
//            //for (int numSheet = 3; numSheet < 4; numSheet++)
//
//            {
//                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
//
//                String sheetName = xssfSheet.getSheetName();// sheet名称，用于校验模板是否正确
//                String intStr = "输入";
//                String outStr = "输出";
//                JSONObject totalJson = new JSONObject();
//                @SuppressWarnings("static-access")
//                JSONObject requestJSONObject = readExcelForRequest(path, sheetName, intStr);
//
//                // for (JSONObject.Entry<String, Object> entry1 :
//                // jsbs.entrySet())
//                // {
//                //
//                // String key = entry1.getKey();
//                // Object value = entry1.getValue();
//                // requestJSONObject.put(key, value);// 公共参数添加到每个接口的输入参数
//                // }
//                @SuppressWarnings("static-access")
//                JSONObject resopnseJSONObject = readExcelForResponse(path, sheetName, outStr);
//                totalJson.put("sheetName", sheetName);
//                totalJson.put("requestJSONObject", requestJSONObject);
//                totalJson.put("resopnseJSONObject", resopnseJSONObject);
//                for (JSONObject.Entry<String, Object> entry1 : jsb.entrySet()) {
//                    String key = entry1.getKey();
//                    String value = (String) entry1.getValue();
//                    if (key.equals(sheetName)) {
//                        totalJson.put("url", value);
//                    }
//
//                }
//                array.add(totalJson);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return array;
//    }
//
//    /**
//     * 获取返回值，得到参数类型
//     *
//     * @param path
//     * @return
//     */
//    public static JSONArray workbooks(String path) {
//        JSONArray array = new JSONArray();
//        InputStream is = null;
//        @SuppressWarnings("unused")
//        XSSFWorkbook workbook = null;
//
//        try {
//            is = new FileInputStream(path);
//            workbook = new XSSFWorkbook(is);
//
//            @SuppressWarnings("resource")
//            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(path);
//            String name = xssfWorkbook.getSheetAt(2).getSheetName();
//            JSONObject jsb = apiWord(path, name);
//            for (int numSheet = 3; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++)
//            //for (int numSheet = 3; numSheet < 4; numSheet++)
//            {
//                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
//                String sheetName = xssfSheet.getSheetName();// sheet名称，用于校验模板是否正确
//                String intStr = "输入";
//                String outStr = "输出";
//                JSONObject totalJson = new JSONObject();
//                @SuppressWarnings("static-access")
//                JSONObject requestJSONObject = readExcelForRequest(path, sheetName, intStr);
//
//                @SuppressWarnings("static-access")
//                JSONObject resopnseJSONObject = readExcelForResponseForParmType(path, sheetName, outStr);
//                totalJson.put("sheetName", sheetName);
//                totalJson.put("requestJSONObject", requestJSONObject);
//                totalJson.put("resopnseJSONObject", resopnseJSONObject);
//                for (JSONObject.Entry<String, Object> entry1 : jsb.entrySet()) {
//                    String key = entry1.getKey();
//                    String value = (String) entry1.getValue();
//                    if (key.equals(sheetName)) {
//                        totalJson.put("url", value);
//                    }
//
//                }
//                array.add(totalJson);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return array;
//    }
//
//    // todo excel文件转json 入参
//    public static JSONObject readExcelForRequest(String path, String sheetName, String flagStr) {
//        Workbook workbook = null;
//        Sheet sheet = null;
//        FileInputStream fis = null;
//        JSONObject obj1 = new JSONObject();
//        JSONObject obj2 = null;
//        JSONArray arr = null;
//        int begin = 0;
//        try {
//            fis = new FileInputStream(path);
//            workbook = WorkbookFactory.create(fis);
//            sheet = workbook.getSheet(sheetName);
//            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) {
//                    continue; // 跳过row为null的行，否则会报空指针异常
//                }
//                Cell cell = row.getCell(0); // 获取第一列单元格
//                if (cell != null) {
//                    String out = cell.getStringCellValue(); // 获取String类型的单元格的值
//                    if (out.equals(flagStr)) {
//                        begin = i;
//                        break;
//                    }
//                }
//            }
//
//            for (int i = begin; i <= sheet.getLastRowNum(); i++) {
//                Row r = sheet.getRow(i);
//                if (r == null) {
//                    continue;
//                }
//                Cell cell1 = r.getCell(1);
//                if (cell1 == null) {
//                    continue;
//                }
//                Cell cell2 = r.getCell(2);
//                if (cell2 == null) {
//                    continue;
//                }
//                Cell cell4 = r.getCell(4);
//                if (cell4 == null) {
//                    continue;
//                }
//                Cell cell8 = r.getCell(8);
//                if (cell8 == null) {
//                    continue;
//                }
//                String key1 = cell1.getStringCellValue();
//                String key2 = cell2.getStringCellValue();
//                String type = cell4.getStringCellValue();
//                if ("date".equalsIgnoreCase(type)) {
//                    type = "Long";
//                }
//                if ("Decimal".equalsIgnoreCase(type)) {
//                    type = "String";
//                }
//                String value = getCellValue(workbook, cell8);
//                String description = r.getCell(3).getStringCellValue();
//                Map<String, Object> map = new HashMap<>();
//                String jsonType = "";
//                map.put("type", type);
//                map.put("value", value);
//                map.put("description", description);
//                map.put("jsonType", jsonType);
//                if (!key1.isEmpty() && key2.isEmpty()) {
//                    // 请求数据那一行过滤掉
//                    if (!"请求数据".equals(key1)) {
//                        if (type != null && !"List".equals(type)) {
//                            obj1.put(key1, map);
//
//                            map.put("jsonType", "1");
//
//                        }
//                        if ("".equals(type)) {
//                            obj2 = new JSONObject();
//                            obj2.put("jsonType", "2");
//                            if (key1.isEmpty()) {
//                                obj2.put(key2, map);
//
//                            }
//                            obj1.put(key1, obj2);
//
//                        }
//                    }
//
//                    if ("List".equals(type)) {
//                        arr = new JSONArray();
//                        obj2 = new JSONObject();
//                        obj2.put("jsonType", "3");
//                        arr.add(obj2);
//                        obj1.put(key1, arr);
//                    }
//
//                }
//                if (!key2.isEmpty() && key1.isEmpty() && obj2 != null) {
//                    if (!obj2.containsKey(key2)) {
//                        obj2.put(key2, map);
//                    } else {
//                        if ("List".equals(type)) {
//                            obj2 = new JSONObject();
//                            arr.add(obj2);
//                            obj2.put(key2, map);
//                        }
//                    }
//                }
//                if (key1.isEmpty() && key2.isEmpty()) {
//                    break;
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return obj1;
//    }
//
//    /**
//     * todo excel文件转json 回参
//     *
//     * @param path
//     * @param sheetName
//     * @param flagStr
//     * @return
//     */
//    public static JSONObject readExcelForResponse(String path, String sheetName, String flagStr) {
//        Workbook workbook = null;
//        Sheet sheet = null;
//        FileInputStream fis = null;
//        JSONObject obj1 = new JSONObject();
//        JSONObject obj2 = null;
//        JSONArray arr = null;
//        int begin = 0;
//        try {
//            fis = new FileInputStream(path);
//            workbook = WorkbookFactory.create(fis);
//            sheet = workbook.getSheet(sheetName);
//            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) {
//                    // 跳过row为null的行，否则会报空指针异常
//                    continue;
//                }
//                // 获取第一列单元格
//                Cell cell = row.getCell(0);
//                if (cell != null) {
//                    // 获取String类型的单元格的值
//                    String out = cell.getStringCellValue();
//                    if (out.equals(flagStr)) {
//                        begin = i;
//                        break;
//                    }
//                }
//            }
//            for (int i = begin; i <= sheet.getLastRowNum(); i++) {
//                Row r = sheet.getRow(i);
//                String key1 = r.getCell(1).getStringCellValue();
//                String key2 = r.getCell(2).getStringCellValue();
//                String type = r.getCell(4).getStringCellValue();
//                Cell valueCell = r.getCell(8);
//                // 如果是日期类型，就专类Long类型的时间戳
//                if ("date".equalsIgnoreCase(type)) {
//                    type = "Long";
//                }
//                if ("Decimal".equalsIgnoreCase(type)) {
//                    type = "String";
//                }
//                // String value = getValue(valueCell);
//                String value = getCellValue(workbook, valueCell);
//                // String description = r.getCell(3).getStringCellValue();
//                // Map map = new HashMap<>();
//                // map.put("type", type);
//                // map.put("value", value);
//                // map.put("description", description);
//                if (!key1.isEmpty() && key2.isEmpty()) {
//                    // 请求数据那一行过滤掉
//                    if (!"响应数据".equals(key1)) {
//                        if (!"List".equals(type) && type != null && !"".equals(type)) {
//                            Map<String, Object> map = getValueByType(type, value);
//
//                            obj1.put(key1, map.get("value"));
//                        }
//                        if ("".equals(type)) {
//                            obj2 = new JSONObject();
//                            if (key1.isEmpty()) {
//                                Map<String, Object> map = getValueByType(type, value);
//                                obj2.put(key2, map.get("value"));
//                            }
//
//                            obj1.put(key1, obj2);
//                        }
//                    }
//
//                    if ("List".equals(type)) {
//                        arr = new JSONArray();
//                        obj2 = new JSONObject();
//                        arr.add(obj2);
//                        obj1.put(key1, arr);
//                    }
//
//                }
//                if (!key2.isEmpty() && key1.isEmpty() && obj2 != null) {
//                    if (!obj2.containsKey(key2)) {
//                        Map<String, Object> map = getValueByType(type, value);
//                        obj2.put(key2, map.get("value"));
//                    } else {
//                        obj2 = new JSONObject();
//                        arr.add(obj2);
//                        Map<String, Object> map = getValueByType(type, value);
//                        obj2.put(key2, map.get("value"));
//
//                    }
//                }
//                if (key1.isEmpty() && key2.isEmpty()) {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return obj1;
//    }
//
//    /**
//     * 得到参数类型
//     *
//     * @param path
//     * @param sheetName
//     * @param flagStr
//     * @return
//     */
//    public static JSONObject readExcelForResponseForParmType(String path, String sheetName, String flagStr) {
//        Workbook workbook = null;
//        Sheet sheet = null;
//        FileInputStream fis = null;
//        JSONObject obj1 = new JSONObject();
//        JSONObject obj2 = null;
//        JSONArray arr = null;
//        int begin = 0;
//        try {
//            fis = new FileInputStream(path);
//            workbook = WorkbookFactory.create(fis);
//            sheet = workbook.getSheet(sheetName);
//            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) {
//                    // 跳过row为null的行，否则会报空指针异常
//                    continue;
//                }
//                // 获取第一列单元格
//                Cell cell = row.getCell(0);
//                if (cell != null) {
//                    // 获取String类型的单元格的值
//                    String out = cell.getStringCellValue();
//                    if (out.equals(flagStr)) {
//                        begin = i;
//                        break;
//                    }
//                }
//            }
//            for (int i = begin; i <= sheet.getLastRowNum(); i++) {
//                Row r = sheet.getRow(i);
//                String key1 = r.getCell(1).getStringCellValue();
//                String key2 = r.getCell(2).getStringCellValue();
//                String type = r.getCell(4).getStringCellValue();
//                Cell valueCell = r.getCell(8);
//                // 如果是日期类型，就专类Long类型的时间戳
//                if ("date".equalsIgnoreCase(type)) {
//                    type = "Long";
//                }
//                if ("Decimal".equalsIgnoreCase(type)) {
//                    type = "String";
//                }
//                String value = getCellValue(workbook, valueCell);
//                String description = r.getCell(3).getStringCellValue();
//                Map<String, Object> map = new HashMap<>();
//                String jsonType = "";
//                map.put("type", type);
//                map.put("value", value);
//                map.put("description", description);
//                map.put("jsonType", jsonType);
//
//                if (!key1.isEmpty() && key2.isEmpty()) {
//                    // 请求数据那一行过滤掉
//                    if (!"响应数据".equals(key1)) {
//                        if (!"List".equals(type)) {
//                            obj1.put(key1, map);
//
//                            map.put("jsonType", "1");
//
//                        }
//                        if ("".equals(type)) {
//                            obj2 = new JSONObject();
//                            obj2.put("jsonType", "2");
//                            if (key1.isEmpty()) {
//                                obj2.put(key2, map);
//                            }
//                            obj1.put(key1, obj2);
//                        }
//                    }
//
//                    if ("List".equals(type)) {
//                        arr = new JSONArray();
//                        obj2 = new JSONObject();
//                        obj2.put("jsonType", "3");
//                        arr.add(obj2);
//                        obj1.put(key1, arr);
//                    }
//
//                }
//                if (!key2.isEmpty() && key1.isEmpty() && obj2 != null) {
//                    if (!obj2.containsKey(key2)) {
//                        obj2.put(key2, map);
//                    } else {
//                        if ("List".equals(type)) {
//                            obj2 = new JSONObject();
//                            arr.add(obj2);
//                            obj2.put(key2, map);
//                        }
//                    }
//                }
//                if (key1.isEmpty() && key2.isEmpty()) {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return obj1;
//    }
//
//    /**
//     * 获取aip接口路径
//     *
//     * @param path
//     * @param sheetName
//     * @return
//     */
//    public static JSONObject apiWord(String path, String sheetName) {
//        Workbook workbook = null;
//        Sheet sheet = null;
//        FileInputStream fis = null;
//        JSONObject obj1 = new JSONObject();
//        try {
//            fis = new FileInputStream(path);
//            workbook = WorkbookFactory.create(fis);
//            sheet = workbook.getSheet(sheetName);
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//
//                Row r = sheet.getRow(i);
//                if (r == null) {
//                    // 跳过row为null的行，否则会报空指针异常
//                    continue;
//                }
//                Cell cell1 = r.getCell(1);
//                if (cell1 == null) {
//                    // 跳过row为null的行，否则会报空指针异常
//                    continue;
//                }
//                String value = cell1.getStringCellValue();
//                Cell cell2 = r.getCell(2);
//                if (cell2 == null) {
//                    // 跳过row为null的行，否则会报空指针异常
//                    continue;
//                }
//                String key = cell2.getStringCellValue();
//
//                if (key.isEmpty() && value.isEmpty()) {
//                    break;
//                }
//                obj1.put(key, value);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return obj1;
//    }
//
//    /**
//     * 入参公共参数
//     *
//     * @param path
//     * @param sheetName
//     * @return
//     */
//    // public static JSONObject requestParams(String path, String sheetName)
//    // {
//    // Workbook workbook = null;
//    // Sheet sheet = null;
//    // FileInputStream fis = null;
//    // JSONObject obj1 = new JSONObject();
//    // try
//    // {
//    // fis = new FileInputStream(path);
//    // workbook = WorkbookFactory.create(fis);
//    // sheet = workbook.getSheet(sheetName);
//    // for (int i = 1; i <= sheet.getLastRowNum(); i++)
//    // {
//    //
//    // Row r = sheet.getRow(i);
//    // String value = r.getCell(7).getStringCellValue();
//    // String type = r.getCell(4).getStringCellValue();
//    // String description = r.getCell(3).getStringCellValue();
//    // String key = r.getCell(1).getStringCellValue();
//    // Map<String,Object> map = new HashMap<>();
//    // map.put("type", type);
//    // map.put("value", value);
//    // map.put("description", description);
//    // if (key.isEmpty() && value.isEmpty())
//    // {
//    // break;
//    // }
//    // obj1.put(key, map);
//    // }
//    // }
//    // catch (Exception e)
//    // {
//    // e.printStackTrace();
//    // }
//    // finally
//    // {
//    // if (fis != null)
//    // {
//    // try
//    // {
//    // fis.close();
//    // }
//    // catch (IOException e)
//    // {
//    // e.printStackTrace();
//    // }
//    // }
//    // }
//    // return obj1;
//    // }
//
//    /**
//     * 获取单元格不同属性的值 暂时是boolean，数字，String
//     *
//     * @param xssfCell
//     * @return
//     */
//    /*
//     * public static String getValue(Cell xssfCell) { if (xssfCell.getCellType()
//     * == xssfCell.CELL_TYPE_BOOLEAN) { return
//     * String.valueOf(xssfCell.getBooleanCellValue()); } else if
//     * (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) { return
//     * String.valueOf(xssfCell.getNumericCellValue()); } else { return
//     * String.valueOf(xssfCell.getStringCellValue()); } }
//     */
//
//    /**
//     * 获取单元格具体数据
//     *
//     * @param workbook
//     * @param cell
//     * @return
//     */
//    public static String getCellValue(Workbook workbook, Cell cell) {
//        if (cell != null) {
//            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//            switch (evaluator.evaluateInCell(cell).getCellType()) {
//                case Cell.CELL_TYPE_BOOLEAN:
//                    return String.valueOf(cell.getBooleanCellValue());
//                case Cell.CELL_TYPE_NUMERIC:
//                    DecimalFormat df = new DecimalFormat("0");
//                    String str = df.format(cell.getNumericCellValue());
//                    return str;
//                case Cell.CELL_TYPE_STRING:
//                    return String.valueOf(cell.getStringCellValue());
//                case Cell.CELL_TYPE_ERROR:
//                    return String.valueOf(cell.getErrorCellValue());
//                case Cell.CELL_TYPE_BLANK:
//                    return String.valueOf(cell.getStringCellValue());
//                case Cell.CELL_TYPE_FORMULA:
//                    return String.valueOf(cell.getStringCellValue());
//            }
//        }
//        return "";
//    }
//
//    /**
//     * 基础类型获取封装类型
//     *
//     * @param baseType
//     * @return
//     */
//    public static String getDataType(String baseType) {
//
//        if ("String".equalsIgnoreCase(baseType)) {
//            return "String";
//        } else if ("int".equalsIgnoreCase(baseType)) {
//            return "integer";
//        } else if ("Long".equalsIgnoreCase(baseType)) {
//            return "number";
//        } else if ("Float".equalsIgnoreCase(baseType)) {
//            return "number";
//        } else if ("Double".equalsIgnoreCase(baseType)) {
//            return "number";
//        } else if ("Boolean".equalsIgnoreCase(baseType)) {
//            return "boolean";
//        }
//        return "String";
//
//    }
//
//    /**
//     * 返回参数获取类型转换
//     *
//     * @param type
//     * @param value
//     * @return
//     * @throws Exception
//     */
//    public static Map<String, Object> getValueByType(String type, String value) throws Exception {
//        Map<String, Object> map = new HashMap<>();
//        if ("String".equalsIgnoreCase(type)) {
//            map.put("value", value);
//        }
//        if ("Long".equalsIgnoreCase(type)) {
//            if (value.isEmpty()) {
//                value = "0";
//            }
//            map.put("value", Long.parseLong(value));
//        }
//
//        if ("boolean".equalsIgnoreCase(type)) {
//            if (value.isEmpty()) {
//                value = "false";
//            }
//            map.put("value", Boolean.parseBoolean(value));
//        }
//        if ("int".equalsIgnoreCase(type)) {
//            if (value.isEmpty()) {
//                value = "0";
//            }
//            map.put("value", Integer.parseInt(value));
//        }
//
//        return map;
//    }
//
//    /**
//     * 数据写入txt文本
//     *
//     * @param str
//     * @param fileFolder
//     * @return
//     */
//    private static boolean writeFile(String filePath, String str, String fileFolder) {
//        File file = new File(fileFolder);
//        if (!file.exists()) {
//
//            FileUtil.mkdir(fileFolder);
//        }
//        try {
//            if (file.isFile()) {
//                file.delete();
//            }
//            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
//            bw.write(str);
//            bw.close();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private static boolean deleteFile(String oldWiremockFilePath) {
//        File file1 = new File(oldWiremockFilePath);
//        try {
//            if (file1.isFile()) {
//                file1.delete();
//                return true;
//            }
//            return false;
//        } catch (Exception e) {
//            // TODO: handle exception
//            return false;
//        }
//    }
//
//    private static boolean deleteDir(File dir) {
//        if (!dir.exists()) {
//            return false;
//        }
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            if (children != null) {
//                // 递归删除目录中的子目录下
//                for (String child : children) {
//                    File file1 = new File(dir, child);
//                    file1.delete();
//                }
//            }
//
//        }
//        // 目录此时为空，可以删除
//        return true;
//    }
//
//}
//
