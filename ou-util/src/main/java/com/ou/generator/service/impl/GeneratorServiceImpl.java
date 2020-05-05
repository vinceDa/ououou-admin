/*
package com.ou.generator.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.ou.generator.config.FreemarkerInfoConfig;
import com.ou.generator.domain.*;
import com.ou.generator.domain.vo.GeneratorFieldVO;
import com.ou.generator.repository.*;
import com.ou.generator.service.GeneratorService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

*/
/**
 * @author vince
 * @date 2019/12/30 20:13
 *//*

@Slf4j
@Service
public class GeneratorServiceImpl implements GeneratorService {

    @Resource
    private GeneratorConnectionRepository generatorConnectionRepository;

    @Resource
    private GeneratorDatabaseRepository generatorDatabaseRepository;

    @Resource
    private GeneratorTableRepository generatorTableRepository;

    @Resource
    private GeneratorFieldRepository generatorFieldRepository;

    @Resource
    private GeneratorSettingRepository generatorSettingRepository;

    @Override
    public String generateCode(List<Long> tableIds, String projectName) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String rootDir = File.separator + "template" + File.separator + uuid + File.separator;
        for (Long tableId : tableIds) {
            GeneratorTable table = generatorTableRepository.getOne(tableId);
            List<GeneratorField> fields = generatorFieldRepository.findAllByTableId(tableId);
            List<GeneratorFieldVO> fieldVOS = Convert.convert(new TypeReference<List<GeneratorFieldVO>>() {}, fields);
            for (GeneratorFieldVO single : fieldVOS) {
                single.setHumpName(nameToHump(single.getName(), false));
                String type = single.getType();
                single.setPackagingType(getPackagingTypeByDbType(type));
            }
            GeneratorSetting setting = new GeneratorSetting();
            // setting = generatorSettingRepository.getByTableId(tableId);
            setting.setAuthor("vince");
            setting.setPackageName("com.ou.quartz");
            setting.setInterfaceName("quartz");
            */
/*if (setting == null) {
                throw new BadRequestException("请先配置表信息");
            }*//*

            log.info("setting: {}", setting.toString());
            generateCodeByFreemarker(table, setting, fieldVOS, projectName, rootDir);
        }
        File zip = ZipUtil.zip(rootDir);
        log.info("zip result: {}", zip.exists());
        return rootDir;
    }

    */
/**
     *  根据模板生成代码
     * @param table 表信息
     * @param setting 配置信息
     * @param fields 字段信息
     * @param projectName 工程名, 为空则表示不生成项目结构
     * @param rootDir 文件所属目录
     *//*

    private void generateCodeByFreemarker(GeneratorTable table, GeneratorSetting setting,
                                          List<GeneratorFieldVO> fields, String projectName, String rootDir) {
        boolean generateWithProject = StrUtil.isNotBlank(projectName);
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
        String dateFormat = date.format(dateTimeFormatter);
        String tableName = table.getName();
        // 实体类英文名根据表名小驼峰命名
        String entityName = nameToHump(tableName, true);
        // 实体类中文名设置为前端可配置, 用于接口的描述信息, 将来可用于swagger文档的接口描述
        // fields
        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_29);
        Writer out = null;
        try {
            // step2 获取模版路径
            String freemarkerPath = ResourceUtils.getURL("classpath:freemarker/ou-admin/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(freemarkerPath));
            // step3 创建数据模型
            FreemarkerInfoConfig config = new FreemarkerInfoConfig();
            List<FreemarkerInfoConfig> configs = config.init();
            Map<String, Object> dataMap = new HashMap<>(7);
            String packageName = setting.getPackageName();
            dataMap.put("fields", fields);
            dataMap.put("author", setting.getAuthor());
            dataMap.put("date", dateFormat);
            dataMap.put("package", packageName);
            dataMap.put("project", projectName);
            dataMap.put("tableName", tableName);
            dataMap.put("entityName", entityName);
            dataMap.put("entityChineseName", "entityChineseName");

            for (FreemarkerInfoConfig single : configs) {
                boolean isApplicationFtl = "Application.ftl".equals(single.getTemplateName());
                if (generateWithProject && isApplicationFtl) {
                    GeneratorDatabase database = generatorDatabaseRepository.getOne(table.getDatabaseId());
                    GeneratorConnection connection = generatorConnectionRepository.getOne(database.getConnectionId());
                    // 获取数据库连接的数据, 用于生成application.properties
                    String dbUrl = "jdbc:mysql://" + connection.getHost() + ":" + connection.getPort()
                            + "/" + database.getName() + "?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false";
                    String dbUsername = connection.getUsername();
                    String dbPassword = connection.getPassword();
                    dataMap.put("dbUrl", dbUrl);
                    dataMap.put("dbUsername", dbUsername);
                    dataMap.put("dbPassword", dbPassword);
                }
                if (!generateWithProject && single.getDependOnProject()) {
                    continue;
                }
                String templateName = single.getTemplateName();
                // step4 加载模版文件
                String filename = single.getFilename();
                // 当前文件的所属绝对路径
                String absolutePath = single.getPackageName();
                Template template = configuration.getTemplate(templateName);
                if (filename.contains("projectName")) {
                    filename = filename.replace("projectName", captureName(projectName));
                } else {
                    filename = filename.replace("prefix", entityName);
                }
                // step5 生成数据
                if (generateWithProject) {
                    absolutePath = absolutePath.replace("projectName", projectName);
                } else {
                    absolutePath = absolutePath.replace("projectName.src.main.java.", "");
                }
                absolutePath = rootDir + absolutePath.replace("packageName", packageName);
                String outputPath = absolutePath.replace(".", File.separator);
                File mkdir = FileUtil.mkdir(outputPath);
                log.info("mkdir path: {}, mkdir exist: {}", mkdir.getPath(), mkdir.exists());
                outputPath = outputPath + File.separator + filename;
                File docFile = new File(outputPath);
                // pom / properties 写入一次即可
                if (docFile.exists() && !single.getIsOverride()) {
                    continue;
                }
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
                // step6 输出文件
                template.process(dataMap, out);
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

    */
/**
     * 根据数据库类型获取包装类型
     * 
     * @param type
     *            数据库类型
     * @return 包装类型
     *//*

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

    */
/**
     *  根据路径生成所有的文件夹
     * @param path  路径
     *//*

    private void mkdirs(String path) {
        if (!FileUtil.exist(path)) {
            File mkdir = FileUtil.mkdir(path);
            log.info("mkdir path: {}, exist: {}", mkdir.getPath(), mkdir.exists());
        } else {
            log.info("path: {} is already exist", path);
        }
    }


    */
/**
     * 下划线命名转驼峰 xx_yy -> XxYy /xxYy
     *
     * @param name
     *            源值
     * @param isUpperCamelCase
     *            是否转为大驼峰, 否则为小驼峰
     * @return 转换后的值
     *//*

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

    */
/**
     * 首字母大写
     *
     * @param name
     *            原值
     * @return 转换后的值
     *//*

    private String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
*/
