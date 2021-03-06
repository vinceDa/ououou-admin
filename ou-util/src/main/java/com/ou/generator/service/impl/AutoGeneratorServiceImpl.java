package com.ou.generator.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorConfig;
import com.ou.generator.domain.GeneratorContent;
import com.ou.generator.domain.GeneratorSetting;
import com.ou.generator.domain.GeneratorTableInfo;
import com.ou.generator.service.AutoGeneratorService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author vince
 * @date 2019/12/30 20:13
 */
@Slf4j
@Service
public class AutoGeneratorServiceImpl implements AutoGeneratorService {

    @Override
    public String generateCode(List<String> tables, GeneratorSetting setting) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String rootDir = File.separator + "template" + File.separator + uuid + File.separator;
        try {
            // 解析配置文件
            String configPath = "http://47.106.148.107/config/Config.json";
            // 创建url对象
            URL urlObj = new URL(configPath);
            // 创建HttpURLConnection对象，通过这个对象打开跟远程服务器之间的连接
            HttpURLConnection httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(5000);
            // 服务器有响应后，会将访问的url页面中的内容放进inputStream中，使用httpConn就可以获取到这个字节流
            InputStream inStream = httpConn.getInputStream();
            byte[] bytes = new byte[inStream.available()];
            inStream.read(bytes);
            String jsonContent = new String(bytes);
            GeneratorConfig config = Convert.convert(GeneratorConfig.class, JSONUtil.parseObj(jsonContent));
            for (String tableName : tables) {
                List<GeneratorTableInfo> tableInfos = listTableInfos(tableName);
                for (GeneratorTableInfo single : tableInfos) {
                    single.setColumnHumpName(nameToHump(single.getColumnName(), false));
                    single.setJavaDataType(getPackagingTypeByDbType(single.getDataType()));
                }
                generateCodeByFreemarker(config,tableName, setting, tableInfos,  rootDir);
            }
            File zip = ZipUtil.zip(rootDir);
            log.info("zip result: {}", zip.exists());
            return rootDir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  根据模板生成代码
     * @param tableName 表名
     * @param setting 配置信息
     * @param tableInfos 表信息(字段名, 字段类型...)
     * @param rootDir 文件所属目录
     */
    private void generateCodeByFreemarker(GeneratorConfig config, String tableName, GeneratorSetting setting,
                                          List<GeneratorTableInfo> tableInfos, String rootDir) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
        String dateFormat = date.format(dateTimeFormatter);
        // 实体类英文名根据表名大驼峰命名
        String entityName = nameToHump(tableName, true);
        // 实体类中文名设置为前端可配置, 用于接口的描述信息, 将来可用于swagger文档的接口描述
        // tableInfos
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        Writer out = null;
        try {
            String remoteFreemarkerPath = config.getFreemarkerPath();
            // 获取模版路径
            String freemarkerPath = ResourceUtils.getURL("classpath:" + remoteFreemarkerPath).getPath();
            configuration.setDirectoryForTemplateLoading(new File(freemarkerPath));
            // 创建数据模型
            Map<String, Object> dataMap = new HashMap<>(7);
            String packageName = setting.getPackageName();
            dataMap.put("tableInfos", tableInfos);
            dataMap.put("author", setting.getAuthor());
            dataMap.put("date", dateFormat);
            dataMap.put("package", packageName);
            dataMap.put("tableName", tableName);
            dataMap.put("entityName", entityName);
            File templateDir = new File(freemarkerPath);
            List<GeneratorContent> content = config.getContent();
            String packagePrefix = config.getPackagePrefix();
            packagePrefix = completeFormat(packagePrefix);
            for (GeneratorContent singleContent : content) {
                String suffix = singleContent.getSuffix();
                // step4 加载模版文件
                String singleContentPackageName = singleContent.getPackageName();
                String fileType = singleContent.getFileType();
                String packagePath = completeFormat(packageName);
                // 当前文件的所属绝对路径
                String absolutePath = rootDir + packagePrefix + packagePath + singleContentPackageName;
                String filename = entityName + suffix + "." + fileType;
                String templatePath = freemarkerPath + singleContent.getTemplateName();
                log.info("templatePath: {}", templatePath);
                Template template = configuration.getTemplate(singleContent.getTemplateName());
                // step5 生成数据
                String outputPath = absolutePath.replace(".", File.separator);
                File mkdir = FileUtil.mkdir(outputPath);
                log.info("mkdir path: {}, mkdir exist: {}", mkdir.getPath(), mkdir.exists());
                outputPath = outputPath + File.separator + filename;
                File docFile = new File(outputPath);
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
                // step6 输出文件
                template.process(dataMap, out);
            }
            File[] templates = templateDir.listFiles();
            if (templates == null) {
                throw new BadRequestException("error template dir path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }

    /**
     *  将传入的字符串补全为所需的格式，此处为保证末尾有 "."
     */
    private String completeFormat(String target) {
        target = target.endsWith("\\.") ? target : target + "\\.";
        return target;
    }

    private List<GeneratorTableInfo> listTableInfos(String tableName) {
        List<GeneratorTableInfo> tableInfos = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // useOldAliasMetadataBehavior=true 使得别名生效
            String url = "jdbc:mysql://localhost:3306/meteorite?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true&useOldAliasMetadataBehavior=true";
            Connection connection = DriverManager.getConnection(url, "root", "123456");
            List<Map<String, Object>> result = new ArrayList<>();
            String listTablesSql = "select COLUMN_NAME columnName, IS_NULLABLE isNullable, DATA_TYPE dataType,COLUMN_COMMENT columnComment " +
                    "from information_schema.columns where table_name = ?";
            PreparedStatement listTables = connection.prepareStatement(listTablesSql);
            listTables.setString(1, tableName);
            ResultSet resultSet = listTables.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();//获取键名
            int columnCount = md.getColumnCount();//获取行的数量
            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<>();//声明Map
                for (int i = 1; i <= columnCount; i++) {
                    // 获取键名及值
                    rowData.put(md.getColumnName(i), resultSet.getObject(i));
                }
                result.add(rowData);
            }
            tableInfos = Convert.convert(new TypeReference<List<GeneratorTableInfo>>() {}, result);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return tableInfos;
    }

    /**
     * 根据数据库类型获取包装类型
     * 
     * @param type
     *            数据库类型
     * @return 包装类型
     */
    private String getPackagingTypeByDbType(String type) {
        String packingType;
        if (type.contains("bigint")) {
            packingType = "Long";
        } else if (type.contains("char")) {
            packingType = "String";
        } else if (type.contains("time")) {
            packingType = "LocalDateTime";
        } else if (type.contains("tinyint(1)")) {
            packingType = "Boolean";
        } else {
            packingType = "Integer";
        }
        return packingType;
    }

    /**
     *  大驼峰转路径, 用于将使用DaoImpl.ftl生成的文件至于dao.impl的目录下
     */
    private String upperCamelCaseToPath(String origin) {
        List<Integer> upperCameCaseIndexList = new ArrayList<>();
        char[] chars = origin.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i]) && i != 0) {
                upperCameCaseIndexList.add(i);
            }
        }
        StringBuilder sb = new StringBuilder(origin.toLowerCase());
        for (Integer index : upperCameCaseIndexList) {
            sb.insert(index, ".");
        }
        return sb.toString();
    }

    /**
     *  根据路径生成所有的文件夹
     * @param path  路径
     */
    private void mkdirs(String path) {
        if (!FileUtil.exist(path)) {
            File mkdir = FileUtil.mkdir(path);
            log.info("mkdir path: {}, exist: {}", mkdir.getPath(), mkdir.exists());
        } else {
            log.info("path: {} is already exist", path);
        }
    }


    /**
     * 下划线命名转驼峰 xx_yy -> XxYy /xxYy
     *
     * @param name
     *            源值
     * @param isUpperCamelCase
     *            是否转为大驼峰, 否则为小驼峰
     * @return 转换后的值
     */
    private String nameToHump(String name, Boolean isUpperCamelCase) {
        StringBuilder entityName = new StringBuilder();
        String[] strings = name.split("_");
        for (int i = 0; i < strings.length; i++) {
            String single = strings[i].toLowerCase();
            single = (i == 0 && !isUpperCamelCase) ? single : captureName(single);
            entityName.append(single);
        }
        return entityName.toString();
    }

    /**
     * 首字母大写
     *
     * @param name
     *            原值
     * @return 转换后的值
     */
    private String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
