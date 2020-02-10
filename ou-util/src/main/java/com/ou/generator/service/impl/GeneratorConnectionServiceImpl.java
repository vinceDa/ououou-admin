package com.ou.generator.service.impl;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ou.generator.domain.GeneratorDatabase;
import com.ou.generator.domain.GeneratorField;
import com.ou.generator.domain.GeneratorTable;
import com.ou.generator.domain.dto.GeneratorDatabaseDTO;
import com.ou.generator.domain.dto.GeneratorTableDTO;
import com.ou.generator.domain.vo.GeneratorTableVO;
import com.ou.generator.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorConnection;
import com.ou.generator.domain.dto.GeneratorConnectionDTO;
import com.ou.generator.domain.query.GeneratorConnectionQueryCriteria;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorConnectionService;
import com.ou.generator.util.ConnectionUtil;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vince
 * @date 2019/12/6 16:58
 */
@Slf4j
@Service
public class GeneratorConnectionServiceImpl implements GeneratorConnectionService {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GeneratorConnectionRepository generatorConnectionRepository;

    @Resource
    private GeneratorDatabaseRepository generatorDatabaseRepository;

    @Resource
    private GeneratorTableRepository generatorTableRepository;

    @Resource
    private GeneratorSettingRepository generatorSettingRepository;

    @Resource
    private GeneratorFieldRepository generatorFieldRepository;

    private static final List<String> DEFAULT_DATABASES = Arrays.asList("information_schema", "mysql", "performance_schema", "test");

    @Override
    public List<GeneratorConnectionDTO> listAll() {
        List<GeneratorConnection> all = generatorConnectionRepository.findAll();
        List<GeneratorConnectionDTO> convert =
            Convert.convert(new TypeReference<List<GeneratorConnectionDTO>>() {}, all);
        log.info("listAll, size: {}", convert.size());
        return convert;
    }

    @Override
    public List<GeneratorConnectionDTO> listAll(Specification<GeneratorConnectionQueryCriteria> specification) {
        List<GeneratorConnection> all = generatorConnectionRepository.findAll(specification);
        log.info("listAll, size: {}", all.size());
        return Convert.convert(new TypeReference<List<GeneratorConnectionDTO>>() {}, all);
    }

    @Override
    public Page<GeneratorConnection> listPageable(Pageable pageable) {
        Page<GeneratorConnection> page = generatorConnectionRepository.findAll(pageable);
        List<GeneratorConnection> all = page.getContent();
        log.info("listPageable, size: {}", all.size());
        return page;
    }

    @Override
    public Page<GeneratorConnection> listPageable(Specification<GeneratorConnectionQueryCriteria> specification,
        Pageable pageable) {
        Page<GeneratorConnection> page = generatorConnectionRepository.findAll(specification, pageable);
        List<GeneratorConnection> all = page.getContent();
        log.info("listPageableWithSpecification, size: {}", all.size());
        return page;
    }

    @Override
    public GeneratorConnectionDTO getById(Long id) {
        GeneratorConnection one = generatorConnectionRepository.getOne(id);
        log.info("getById, id: {}, one: {}", id, one.toString());
        return Convert.convert(GeneratorConnectionDTO.class, one);
    }

    @Override
    public GeneratorConnectionDTO getByName(String name) {
        GeneratorConnection generatorConnectionByName = generatorConnectionRepository.getConnectionByName(name);
        log.info("getByName, name: {}, connectionByName: {}", name, generatorConnectionByName.toString());
        return Convert.convert(GeneratorConnectionDTO.class, generatorConnectionByName);
    }

    @Override
    public GeneratorConnectionDTO insert(GeneratorConnectionDTO generatorConnectionDTO) {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorConnection generatorConnectionByName =
            generatorConnectionRepository.getConnectionByName(generatorConnectionDTO.getName());
        if (generatorConnectionByName != null) {
            log.error("insert error, name is exist");
            throw new BadRequestException("连接名已存在");
        }
        GeneratorConnection convert = Convert.convert(GeneratorConnection.class, generatorConnectionDTO);
        convert.setEnable(false);
        convert.setCreateUserId(userId);
        convert.setUpdateUserId(userId);
        convert.setCreateTime(LocalDateTime.now());
        convert.setUpdateTime(LocalDateTime.now());
        GeneratorConnection save = generatorConnectionRepository.save(convert);
        log.info("insert success, info: {}", save.toString());
        return Convert.convert(GeneratorConnectionDTO.class, save);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!this.existsById(id)) {
            log.error("delete error, unknown id");
            throw new BadRequestException("请选择连接删除");
        }
        generatorConnectionRepository.deleteById(id);
        deleteAllConnectionInfoById(id);
    }

    @Override
    public GeneratorConnectionDTO update(GeneratorConnectionDTO generatorConnectionDTO) {
        Optional<GeneratorConnection> byId = generatorConnectionRepository.findById(generatorConnectionDTO.getId());
        if (!byId.isPresent()) {
            log.error("update connection error: unknown id");
            throw new BadRequestException("请选择连接编辑");
        }
        if (StrUtil.isNotBlank(generatorConnectionDTO.getName())) {
            GeneratorConnection byName =
                generatorConnectionRepository.getConnectionByName(generatorConnectionDTO.getName());
            if (byName != null && !byName.getId().equals(generatorConnectionDTO.getId())) {
                log.error("update connection error: connectionName is exist");
                throw new BadRequestException("连接名已存在");
            }
        }
        GeneratorConnection one = byId.get();
        GeneratorConnection convert = Convert.convert(GeneratorConnection.class, generatorConnectionDTO);
        convert.setUpdateTime(LocalDateTime.now());
        Long userId = SecurityUtil.getJwtUserId();
        convert.setUpdateUserId(userId);
        CopyOptions copyOptions = new CopyOptions();
        copyOptions.ignoreNullValue();
        BeanUtil.copyProperties(convert, one, copyOptions);
        GeneratorConnection save = generatorConnectionRepository.save(one);
        log.info("update, save: {}", save.toString());
        return Convert.convert(GeneratorConnectionDTO.class, save);
    }

    @Override
    public Boolean existsById(Long id) {
        return generatorConnectionRepository.existsById(id);
    }

    @Override
    public Connection getConnection(Long id) {
        GeneratorConnection one = generatorConnectionRepository.getOne(id);
        Connection mysqlConnection = ConnectionUtil.getMysqlConnection(one);
        Boolean enable = mysqlConnection != null;
        if (!enable.equals(one.getEnable())) {
            one.setEnable(enable);
            Long userId = SecurityUtil.getJwtUserId();
            one.setUpdateUserId(userId);
            one.setUpdateTime(LocalDateTime.now());
            GeneratorConnection save = generatorConnectionRepository.save(one);
            log.info("getConnection: update connection status: {}", save.toString());
        }
        return mysqlConnection;
    }

    @Override
    public Boolean testConnection(Long id) {
        Connection connection = this.getConnection(id);
        return connection != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refresh(Long id) {
        Connection connection = this.getConnection(id);
        try {
            deleteAllConnectionInfoById(id);
            PreparedStatement listDatabases = connection.prepareStatement("show databases");
            ResultSet databases = listDatabases.executeQuery();
            while (databases.next()) {
                String database = databases.getString("Database");
                log.info("database: {}", database);
                // 排除默认数据库
                if (DEFAULT_DATABASES.contains(database)) {
                    continue;
                }
                GeneratorDatabase generatorDatabase = new GeneratorDatabase();
                generatorDatabase.setName(database);
                generatorDatabase.setConnectionId(id);
                GeneratorDatabase databaseResult = updateDatabase(generatorDatabase);
                String listTablesSql = "select TABLE_NAME name, TABLE_COLLATION collation "
                    + "from information_schema.tables  where table_schema = ?";
                PreparedStatement listTables = connection.prepareStatement(listTablesSql);
                listTables.setString(1, database);
                ResultSet tables = listTables.executeQuery();
                while (tables.next()) {
                    GeneratorTable generatorTable = new GeneratorTable();
                    String tableName = tables.getString("name");
                    generatorTable.setCharset("utf-8");
                    generatorTable.setName(tableName);
                    generatorTable.setCollation(tables.getString("collation"));
                    generatorTable.setDatabaseId(databaseResult.getId());
                    GeneratorTable tableResult = updateTable(generatorTable);
                    String listFieldsSql =
                        "select COLUMN_NAME name, COLUMN_TYPE type, IS_NULLABLE isRequired, COLUMN_COMMENT comment "
                            + "from information_schema.COLUMNS where table_name = ? and table_schema = ?";
                    PreparedStatement listFields = connection.prepareStatement(listFieldsSql);
                    listFields.setString(1, tableName);
                    listFields.setString(2, database);
                    ResultSet fields = listFields.executeQuery();
                    while (fields.next()) {
                        GeneratorField generatorField = new GeneratorField();
                        generatorField.setTableId(tableResult.getId());
                        generatorField.setName(fields.getString("name"));
                        generatorField.setType(fields.getString("type"));
                        generatorField.setIsRequired(fields.getBoolean("isRequired"));
                        generatorField.setComment(fields.getString("comment"));
                        updateGeneratorField(generatorField);
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("prepareStatement error: {}", e.toString());
        }
        return false;
    }

    @Override
    public List<GeneratorConnectionDTO> listConnectionInfoTree() {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorConnectionQueryCriteria param = new GeneratorConnectionQueryCriteria();
        param.setCreateUserId(userId);
        param.setEnable(true);
        List<GeneratorConnectionDTO> generatorConnectionDTOS = this.listAll(param.toSpecification());
        for (GeneratorConnectionDTO singleConnection : generatorConnectionDTOS) {
            List<GeneratorDatabase> databases = generatorDatabaseRepository.findAllByConnectionId(singleConnection.getId());
            List<GeneratorDatabaseDTO> convert = Convert.convert(new TypeReference<List<GeneratorDatabaseDTO>>() {
            }, databases);
            singleConnection.setChildren(convert);
            for (GeneratorDatabaseDTO singleDatabase : convert) {
                List<GeneratorTable> tables = generatorTableRepository.findAllByDatabaseIdIn(Collections.singletonList(singleDatabase.getId()));
                singleDatabase.setKey("database" + singleDatabase.getId());
                List<GeneratorTableDTO> tableDTOS = Convert.convert(new TypeReference<List<GeneratorTableDTO>>() {
                }, tables);
                for (GeneratorTableDTO tableDTO : tableDTOS) {
                    tableDTO.setKey("table" + tableDTO.getId());
                }
                singleDatabase.setChildren(tableDTOS);
            }
        }
        return generatorConnectionDTOS;
    }

    /**
     * 更新/新增数据库中的表的字段信息
     * 
     * @param generatorField
     *            数据库表实体
     * @return 返回新增成功的字段信息
     */
    private GeneratorField updateGeneratorField(GeneratorField generatorField) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorField.setCreateTime(LocalDateTime.now());
        generatorField.setUpdateTime(LocalDateTime.now());
        generatorField.setCreateUserId(userId);
        generatorField.setUpdateUserId(userId);
        return generatorFieldRepository.save(generatorField);
    }

    /**
     * 更新/新增数据库中的表信息
     * 
     * @param generatorTable
     *            数据库表实体
     * @return 返回新增成功的数据库信息
     */
    private GeneratorTable updateTable(GeneratorTable generatorTable) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorTable.setCreateTime(LocalDateTime.now());
        generatorTable.setUpdateTime(LocalDateTime.now());
        generatorTable.setCreateUserId(userId);
        generatorTable.setUpdateUserId(userId);
        return generatorTableRepository.save(generatorTable);
    }


    /**
     *  级联删除连接下的所有信息(数据库/表/字段)
     * @param connectionId 连接的主键id
     */
    private void deleteAllConnectionInfoById(Long connectionId) {
        List<GeneratorDatabase> allByConnectionId = generatorDatabaseRepository.findAllByConnectionId(connectionId);
        List<Long> databaseIds = allByConnectionId.stream().map(GeneratorDatabase::getId).collect(Collectors.toList());
        List<GeneratorTable> allByDatabaseIdIn = generatorTableRepository.findAllByDatabaseIdIn(databaseIds);
        List<Long> tableIds = allByDatabaseIdIn.stream().map(GeneratorTable::getId).collect(Collectors.toList());
        generatorDatabaseRepository.deleteAllByConnectionId(connectionId);
        generatorTableRepository.deleteAllByDatabaseIdIn(databaseIds);
        // 所有和表相关的配置也需要删除
        generatorSettingRepository.deleteAllByTableIdIn(tableIds);
        generatorFieldRepository.deleteAllByTableIdIn(tableIds);
    }

    /**
     * 更新/新增数据库信息
     * 
     * @param generatorDatabase
     *            数据库实体
     * @return 返回新增成功的数据库信息
     */
    private GeneratorDatabase updateDatabase(GeneratorDatabase generatorDatabase) {
        Long userId = SecurityUtil.getJwtUserId();
        generatorDatabase.setCreateTime(LocalDateTime.now());
        generatorDatabase.setUpdateTime(LocalDateTime.now());
        generatorDatabase.setCreateUserId(userId);
        generatorDatabase.setUpdateUserId(userId);
        return generatorDatabaseRepository.save(generatorDatabase);
    }

}
