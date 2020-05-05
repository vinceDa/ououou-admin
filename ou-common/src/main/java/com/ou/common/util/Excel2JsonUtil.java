package com.ou.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * id appId 可以不设置, 导入之后系统自动分配
 *
 * @author vince
 * @date 2020/03/14 11:18
 */
public class Excel2JsonUtil {

    private static final Log log = LogFactory.get();
    private static final String EXCEL_SUFFIX_2003 = "xls";
    private static final String EXCEL_SUFFIX_2007 = "xlsx";
    private static final String NOT_INTERFACE_SHEET = "变更记录, 接口公共规范, 接口文档";
    private static final String FLAG_OF_INTERFACE = "返回";
    /**
     * 指定具体sheet导出
     */
    private static final String SPECIFIC_SHEET = "";

    /**
     * 指定一次读取多少个sheet, mPass文件过大的时候导入会未知报错
     */
    private static final Integer ROUND_SIZE = 10;
    /**
     * 描述性质的行, 用于描述数据的属性, 没有具体含义
     */
    private static final String DESC_ROW = "返回, 输入, 输出, 响应数据";

    /**
     * mock百分比, 0表示测试真实接口, 100表示调用mock数据
     */
    private static int percentage = 0;

    /**
     * 一些外部填写的参数, 暂时写死, 后面可以做一个简单的页面填入联动
     */
    /**
     *  mPass平台导入api时界面显示的AppId
     */
    private static String appId = "ONEXE1B0B3F301538";
    /**
     *  操作人
     */
    private static String operator = "ectip";
    /**
     *  mPass平台导入api时界面显示的当前环境
     */
    private static String workSpaceId = "jxnx_mpass_dev";
    //    private static Integer sysId = 6000;
    private static String sysName = "credit";
    private static String host = "https://a2test.jxnxs.com/mobile-bank/";


    public static void main(String[] args) throws IOException {
        /*String dirPath = "";
        List<String> pathList = generateMultipleJsonByDirPath(dirPath);
        System.out.println(pathList);*/
        String filePath = "C:\\Users\\vince\\Documents\\WeChat Files\\jdzsfksm\\FileStorage\\File\\2020-04\\信用卡接口文档.xlsx";
        File file = new File(filePath);
        List<String> path = generateJsonByExcel(file);
        System.out.println(path);
    }

    /**
     * 根据单个excel文件生成符合mPass要求的json
     *
     * @param singleExcel excel文件
     * @return json文件生成的路径
     * @throws IOException io异常
     */
    private static List<String> generateJsonByExcel(File singleExcel) throws IOException {
        Workbook wb;
        FileInputStream in = new FileInputStream(singleExcel);
        // 判断是否为excel文件
        String excelName = "";
        try {
            String[] split = singleExcel.getName().split("[.]");
            String excelSuffix = split[1];
            excelName = split[0];
            boolean isNotExcel = !singleExcel.isFile() || (!excelSuffix.equals(EXCEL_SUFFIX_2003) && !excelSuffix.equals(EXCEL_SUFFIX_2007));
            if (isNotExcel) {
                log.error("generateJsonByExcel error: file is not excel");
                return null;
            }
            if (excelSuffix.equals(EXCEL_SUFFIX_2003)) {
                //创建excel2003的文件文本抽取对象
                wb = new HSSFWorkbook(new POIFSFileSystem(in));
            } else {
                //创建excel2003的文件文本抽取对象
                wb = new XSSFWorkbook(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        int totalSheets = wb.getNumberOfSheets();
        System.out.println("totalSheets: " + totalSheets);

        // 余数
        int remainder = totalSheets % ROUND_SIZE;
        int rounds = totalSheets / ROUND_SIZE + (remainder == 0 ? 0 : 1);
        List<String> targetPathList = new ArrayList<>();
        for (int i = 1; i <= rounds; i++) {
            // 一个Excel中所有的接口
            JSONObject allInterfaceJson = new JSONObject();
            JSONArray apis = new JSONArray();
            JSONObject models = new JSONObject();
            JSONObject params = new JSONObject();
            int startIndex = (i - 1) * ROUND_SIZE;
            int endIndex = i == rounds ? (i - 1) * ROUND_SIZE + remainder : i * ROUND_SIZE;
            for (int j = startIndex; j < endIndex; j++) {
                // 得到Excel工作表对象
                Sheet sheet = wb.getSheetAt(j);
                String sheetName = sheet.getSheetName();
                // 过滤说明文档
                if (NOT_INTERFACE_SHEET.contains(sheetName)) {
                    continue;
                }
                if (!isInterfaceSheet(sheet)) {
                    continue;
                }
                // 是否为指定的sheet
//                if (!SPECIFIC_SHEET.contains(sheetName)) {
//                    continue;
//                }
                String interfacePath = getInterfacePath(wb, sheetName);
                // https开头代表不是信用卡系统的接口，是公共的接口，不应该导入
                if (interfacePath == null || interfacePath.startsWith("https")) {
                    continue;
                }
                // 一个sheet对应一个api
                getParams(wb, sheet, params, models);
                JSONObject apisJson = getApisJson(wb, sheetName);
                JSONObject mockRuleJson = new JSONObject();
                mockRuleJson.put("mockData", params.get("mockData").toString());
                mockRuleJson.put("needMock", true);
                mockRuleJson.put("percentage", percentage);
                mockRuleJson.put("type", "PERCENT");
                apisJson.put("mockRule", mockRuleJson);
                apis.add(apisJson);
                params.remove("mockData");
            }
            allInterfaceJson.put("date", System.currentTimeMillis());
            allInterfaceJson.put("models", models);
            allInterfaceJson.put("apis", apis);
            allInterfaceJson.put("systems", getSystemsJson());
            allInterfaceJson.put("appId", appId);
            allInterfaceJson.put("params", params);
            allInterfaceJson.put("operator", operator);
            allInterfaceJson.put("workspaceId", workSpaceId);
            String targetPath = "/mPassJson/" + excelName + "_" + i + ".txt";
            File touch = FileUtil.touch(targetPath);
            FileUtil.writeBytes(allInterfaceJson.toString().getBytes(), touch);
            log.info("targetPath: {}", targetPath);
            targetPathList.add(targetPath);
        }
        return targetPathList;
    }

    private static List<String> generateMultipleJsonByDirPath(String dirPath) throws IOException {
        List<String> pathList = new ArrayList<>();
        File[] files = FileUtil.ls(dirPath);
        for (File singleExcel : files) {
            List<String> targetPath = generateJsonByExcel(singleExcel);
            assert targetPath != null;
            pathList.addAll(targetPath);
        }
        return pathList;
    }

    /**
     * 获取sheet中的请求参数和返回参数并把参数封装成符合mPass移动网关api要求的json
     * 例:
     * {
     * "date":1584069574806,
     * "models": models, 下面有数据结构
     * "apis": apis, 下面有数据结构
     * "systems": systems, 下面有数据结构
     * "appId": "随机18位数字字母组合",
     * "params": {
     * "testService.testUserInfo": [{
     * "apiId": 171280,
     * "appId": "ONEXE1B0B3F121610",
     * "defaultValue": "10010",
     * "description": "账号",
     * "id": 442030,
     * "location": "Query",
     * "name": "acNo",
     * "refType": "",
     * "type": "String",
     * "workspaceId": "jxnx_mpass_dev"
     * }]* 	},
     * "operator":"ectip",
     * "workspaceId":"jxnx_mpass_dev"
     * }
     *
     * @param wb     Workbook对象
     * @param sheet  Excel中的sheet
     * @param params 存储参数的json对象
     * @param models 存储数据模型的json对象
     * @return 符合mPass移动网关api要求的json
     */
    private static void getParams(Workbook wb, Sheet sheet, JSONObject params, JSONObject models) {
        JSONArray paramArray = new JSONArray();
        // 获取所有有效的行数
        int totalRow = sheet.getLastRowNum();
        // 读取到的有效行数计数
        int i = 0;
        // 请求参数结束行数
        int requestEndRow = 0;
        // 返回参数开始的行数
        int responseStartRowNum = 0;
        while (i <= totalRow) {
            Row row = sheet.getRow(i);
            if (row == null) {
                i++;
                continue;
            }
            short totalCell = row.getLastCellNum();
            // 空行略过
            if (totalCell == 0) {
                continue;
            }
            // 描述性质的行略过, 算作有效行数
            if (totalCell < 8) {
                i++;
                continue;
            }
            Cell cell = row.getCell(0);
            if (cell != null) {
                String stringCellValue = cell.getStringCellValue();
                // 首列以'输出'开头后下面的参数为response参数
                if ("输出".equals(stringCellValue)) {
                    requestEndRow = i - 1;
                    responseStartRowNum = i + 2;
                    break;
                }
            }
            i++;
        }
        String operationType = getInterfacePath(wb, sheet.getSheetName());
        JSONObject requestParam = getCommonJsonObject();
        requestParam.put("location", "ReqBody");
        requestParam.put("name", "requestBody");
        String refTypeName = operationType + "_req_model";
        requestParam.put("refType", refTypeName);
        paramArray.add(requestParam);
        getRefModel(false, sheet, 2, requestEndRow, 1, refTypeName, models, wb);
        JSONObject responseParam = getCommonJsonObject();
        responseParam.put("location", "RespBody");
        responseParam.put("name", "ResponseBody");
        refTypeName = operationType + "_resp_model";
        responseParam.put("refType", refTypeName);
        paramArray.add(responseParam);
        JSONObject mockData = getRefModel(true, sheet, responseStartRowNum, totalRow, 1, refTypeName, models, wb);
        JSONObject mockDataResult = new JSONObject();
        mockDataResult.put("result", addCommonResponseParam(mockData));
        mockDataResult.put("tips", "ok");
        mockDataResult.put("resultStatus", 1000);
        params.put(operationType, paramArray);
        params.put("mockData", mockDataResult);
    }

    /**
     * 公共参数同意生成
     *
     * @return
     */
    private static JSONObject getCommonJsonObject() {
        JSONObject commonParam = new JSONObject();
        commonParam.put("apiId", generateRandomNum());
        commonParam.put("appId", appId);
        commonParam.put("id", generateRandomNum());
        commonParam.put("workspaceId", workSpaceId);
        commonParam.put("type", "Object");
        commonParam.put("defaultValue", null);
        commonParam.put("description", "");
        return commonParam;
    }

    private static JSONObject getSystemsJson() {
        JSONObject systemsJson = new JSONObject();
        JSONObject systemNameJson = new JSONObject();
        systemNameJson.put("accessKey", "");
        systemNameJson.put("accessSecret", "");
        systemNameJson.put("appId", appId);
        systemNameJson.put("centers", new JSONObject());
        systemNameJson.put("description", "");
        systemNameJson.put("host", host);
//        systemNameJson.put("id", sysId);
        systemNameJson.put("needSign", false);
        systemNameJson.put("regionMode", false);
        systemNameJson.put("signType", null);
        systemNameJson.put("sysName", sysName);
        systemNameJson.put("timeout", 10000);
        systemNameJson.put("upstreamType", "HTTP");
        systemNameJson.put("workspaceId", workSpaceId);
        systemsJson.put(sysName, systemNameJson);
        return systemsJson;
    }

    private static JSONObject getApisJson(Workbook wb, String sheetName) {
        String operationType = getInterfacePath(wb, sheetName);
        JSONObject apiJson = new JSONObject();
//        apiJson.put("id", generateRandomNum());
        apiJson.put("apiName", sheetName);
        apiJson.put("apiStatus", "OPENED");
        apiJson.put("apiType", "HTTP");
        apiJson.put("appId", appId);
        apiJson.put("authRuleName", "");
        apiJson.put("description", "");
        apiJson.put("gmtCreate", System.currentTimeMillis());
        apiJson.put("gmtModified", System.currentTimeMillis());
        apiJson.put("headerRule", new ArrayList<>());
        apiJson.put("needEncrypt", "");
        apiJson.put("needETag", "T");
        apiJson.put("needSign", "T");
        apiJson.put("needJsonp", "");
        apiJson.put("operationType", operationType);
        apiJson.put("otherConfigMap", new JSONObject());
        apiJson.put("paramGetMethod", "");
//        apiJson.put("sysId", sysId);
        apiJson.put("sysName", sysName);
        apiJson.put("timeout", "");
        apiJson.put("workspaceId", workSpaceId);
        JSONObject cacheRuleJson = new JSONObject();
        cacheRuleJson.put("cacheKey", "");
        cacheRuleJson.put("needCache", false);
        cacheRuleJson.put("ttl", 0);
        apiJson.put("cacheRule", cacheRuleJson);
        JSONObject limitRuleJson = new JSONObject();
        limitRuleJson.put("defaultResponse", "");
        limitRuleJson.put("i18nResponse", null);
        limitRuleJson.put("interval", 1);
        limitRuleJson.put("limit", 0);
        limitRuleJson.put("mode", "CLOSED");
        apiJson.put("limitRule", limitRuleJson);
        JSONObject migrateRuleJson = new JSONObject();
        migrateRuleJson.put("flowPercent", 0);
        migrateRuleJson.put("needMigrate", false);
        migrateRuleJson.put("needSwitchCompletely", false);
//        migrateRuleJson.put("sysId", -1);
        migrateRuleJson.put("sysName", "");
        migrateRuleJson.put("upstreamType", null);
        apiJson.put("migrateRule", migrateRuleJson);
        JSONObject apiInvokerJson = new JSONObject();
        JSONObject httpInvokerJson = new JSONObject();
        httpInvokerJson.put("charset", "UTF-8");
        httpInvokerJson.put("contentType", "application/json");
        httpInvokerJson.put("host", "");
        httpInvokerJson.put("method", "POST");
        httpInvokerJson.put("path", getInterfacePath(wb, sheetName));
        apiInvokerJson.put("httpInvoker", httpInvokerJson);
        apiInvokerJson.put("rpcInvoker", null);
        apiJson.put("apiInvoker", apiInvokerJson);
        return apiJson;
    }

    /**
     * 读取返回参数生成对应数据模型并和param中的refType进行绑定
     * 注: 理论上是可以无限递归, 由于excel格式固定只会有二级的数据, 不会继续嵌套, 这里不做递归
     *
     * @param isResponseParam 是否为获取返回参数, 否则为获取请求参数
     * @param sheet           Excel的sheet
     * @param startRowNum     开始行数
     * @param endRowNum       结束行数
     * @param nameCellNum     属性列的下标, 一级属性的下标为1, 二级属性下标为2
     * @param refTypeName     数据模型名称
     * @param models          存储数据模型
     * @param wb
     * @return 返回mockData数据
     */
    private static JSONObject getRefModel(boolean isResponseParam, Sheet sheet, int startRowNum, int endRowNum,
                                          int nameCellNum, String refTypeName, JSONObject models, Workbook wb) {
        JSONObject mockData = new JSONObject();
        JSONObject modelParam = new JSONObject();
        modelParam.put("appId", appId);
        modelParam.put("name", refTypeName);
        modelParam.put("title", refTypeName + "_title");
        modelParam.put("workspaceId", workSpaceId);
        modelParam.put("gmtCreate", System.currentTimeMillis());
        modelParam.put("gmtModified", System.currentTimeMillis());
        JSONArray items = new JSONArray();
        while (startRowNum <= endRowNum) {
            JSONObject singleItem = new JSONObject();
            Row row = sheet.getRow(startRowNum);
            if (row == null || row.getLastCellNum() <= 4) {
                startRowNum++;
                // 空行
                continue;
            }
            String name = row.getCell(nameCellNum).getStringCellValue();
            boolean isSubParam = "list".equalsIgnoreCase(name);
            singleItem.put("df", "");
            if (isResponseParam) {
                if (!isSubParam && StrUtil.isNotEmpty(name)) {
                    mockData.put(name, "");
                }
            }
            if (isSubParam) {
                String listRefTypeName = refTypeName + "List";
                singleItem.put("name", "List");
                singleItem.put("refType", listRefTypeName);
                singleItem.put("title", "list Object");
                singleItem.put("type", "List");
                // 添加数据模型
                JSONObject subMockData = getRefModel(true, sheet, startRowNum + 1, endRowNum, 2, listRefTypeName, models, wb);
                JSONArray subMockDataArray = new JSONArray();
                subMockDataArray.add(subMockData);
                mockData.put("List", subMockDataArray);
            } else {
                singleItem.put("name", name);
                singleItem.put("refType", "");
                singleItem.put("title", row.getCell(3).getStringCellValue());
                singleItem.put("type", row.getCell(4).getStringCellValue());
            }
            items.add(singleItem);
            startRowNum++;
        }
        // 添加公共请求参数
        if (!isResponseParam) {
            items.addAll(addCommonRequestParam(wb, sheet.getSheetName()));
        }
        modelParam.put("items", items);
        models.put(refTypeName, modelParam);
        return mockData;
    }

    /**
     * 根据接口文档的sheet获取其他接口sheet的交易码
     *
     * @param wb        Workbook对象
     * @param sheetName 接口sheet的名称
     * @return 接口sheet的交易码
     */
    private static String getTransactionCode(Workbook wb, String sheetName) {
        Sheet interfaceDocSheet = wb.getSheet("接口文档");
        if (interfaceDocSheet == null) {
            return "credit_info_query";
        }
        Iterator<Row> rowIterator = interfaceDocSheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row next = rowIterator.next();
            Cell interfaceNameCell = next.getCell(2);
            if (interfaceNameCell != null && sheetName.equals(interfaceNameCell.getStringCellValue())) {
                Cell interfacePathCell = next.getCell(1);
                String res = interfacePathCell != null ? interfacePathCell.getStringCellValue() + "" : null;
                if (res == null) {
                    System.err.println(sheetName);
                }
                // TODO 测试完改回来
                return res;
            }
        }
        return null;
    }

    /**
     * 根据接口文档的sheet获取其他接口sheet的接口地址
     *
     * @param wb        Workbook对象
     * @param sheetName 接口sheet的名称
     * @return 接口sheet的接口地址
     */
    private static String getInterfacePath(Workbook wb, String sheetName) {
        Sheet interfaceDocSheet = wb.getSheet("接口文档");
        if (interfaceDocSheet == null) {
            return "000000";
        }
        Iterator<Row> rowIterator = interfaceDocSheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row next = rowIterator.next();
            Cell interfaceNameCell = next.getCell(2);
            if (interfaceNameCell != null && sheetName.equals(interfaceNameCell.getStringCellValue())) {
                Cell interfacePathCell = next.getCell(3);
                String res = interfacePathCell != null ? interfacePathCell.getStringCellValue() + "" : null;
                if (res == null) {
                    System.err.println(sheetName);
                }
                // TODO 测试完改回来
                return res;
            }
        }
        return null;

    }

    /**
     * 判断当前sheet是不是一个接口
     *
     * @param sheet Excel的sheet
     * @return 判断结果
     */
    private static boolean isInterfaceSheet(Sheet sheet) {
        try {
            // 得到Excel工作表的行
            Row row = sheet.getRow(0);
            // 得到Excel工作表指定行的单元格
            Cell cell = row.getCell(0);
            String stringCellValue = cell.getStringCellValue();
            // 真正的接口sheet以 '返回' 开头
            return FLAG_OF_INTERFACE.equals(stringCellValue);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String generateRandomNum() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    /**
     * 给请求参数添加公共的参数
     * @param wb
     * @param sheetName
     */
    private static JSONArray addCommonRequestParam(Workbook wb, String sheetName) {
        JSONArray items = new JSONArray();
        List<String> commonRequestList = Arrays.asList("transId", "userSeq", "accessHost", "operatorSeq", "authorSeq", "branchSeq");
        for (String request : commonRequestList) {
            JSONObject singleItem = new JSONObject();
            if ("transId".equalsIgnoreCase(request)) {
                singleItem.put("df", getTransactionCode(wb, sheetName));
            }
            singleItem.put("name", request);
            singleItem.put("refType", "");
            singleItem.put("title", "");
            singleItem.put("type", "String");
            items.add(singleItem);
        }
        return items;
    }

    /**
     * 给返回参数添加公共的参数
     *
     * @param param 请求参数
     * @return 返回修改后的json对象
     */
    private static JSONObject addCommonResponseParam(JSONObject param) {
        JSONObject res = new JSONObject();
        res.put("code", "");
        res.put("message", "");
        res.put("data", param);
        return res;
    }
}
