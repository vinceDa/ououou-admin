package com.ou.generator.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import com.ou.common.exception.BadRequestException;
import com.ou.generator.domain.GeneratorConnection;
import com.ou.generator.domain.GeneratorDatabase;
import com.ou.generator.domain.GeneratorField;
import com.ou.generator.domain.GeneratorTable;
import com.ou.generator.domain.dto.GeneratorConnectionDTO;
import com.ou.generator.domain.query.GeneratorConnectionQueryCriteria;
import com.ou.generator.domain.vo.GeneratorConnectionVO;
import com.ou.generator.domain.vo.GeneratorDatabaseVO;
import com.ou.generator.domain.vo.GeneratorTableVO;
import com.ou.generator.repository.*;
import com.ou.generator.security.util.SecurityUtil;
import com.ou.generator.service.GeneratorConnectionService;
import com.ou.generator.util.ConnectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
            List<Long> databaseIds = new ArrayList<>();
            List<Long> tableIds = new ArrayList<>();
            List<Long> fieldIds = new ArrayList<>();
            while (databases.next()) {
                String database = databases.getString("Database");
                log.info("database: {}", database);
                // 排除默认数据库
                if (DEFAULT_DATABASES.contains(database)) {
                    continue;
                }
                GeneratorDatabase byConnectionIdAndAndName = generatorDatabaseRepository.getByConnectionIdAndAndName(id, database);
                GeneratorDatabase databaseResult;
                if (byConnectionIdAndAndName == null) {
                    GeneratorDatabase generatorDatabase = new GeneratorDatabase();
                    generatorDatabase.setName(database);
                    generatorDatabase.setConnectionId(id);
                    databaseResult = updateDatabase(generatorDatabase);
                } else {
                    byConnectionIdAndAndName.setName(database);
                    byConnectionIdAndAndName.setConnectionId(id);
                    databaseResult = updateDatabase(byConnectionIdAndAndName);
                }
                databaseIds.add(databaseResult.getId());
                String listTablesSql = "select TABLE_NAME name, TABLE_COLLATION collation "
                    + "from information_schema.tables  where table_schema = ?";
                PreparedStatement listTables = connection.prepareStatement(listTablesSql);
                listTables.setString(1, database);
                ResultSet tables = listTables.executeQuery();
                while (tables.next()) {
                    String tableName = tables.getString("name");
                    GeneratorTable tableResult;
                    GeneratorTable byDatabaseIdAndName = generatorTableRepository.getByDatabaseIdAndName(databaseResult.getId(), tableName);
                    if (byDatabaseIdAndName == null) {
                        GeneratorTable generatorTable = new GeneratorTable();
                        generatorTable.setCharset("utf-8");
                        generatorTable.setName(tableName);
                        generatorTable.setCollation(tables.getString("collation"));
                        generatorTable.setDatabaseId(databaseResult.getId());
                        tableResult = updateTable(generatorTable);
                    } else {
                        byDatabaseIdAndName.setCharset("utf-8");
                        byDatabaseIdAndName.setName(tableName);
                        byDatabaseIdAndName.setCollation(tables.getString("collation"));
                        byDatabaseIdAndName.setDatabaseId(databaseResult.getId());
                        tableResult = updateTable(byDatabaseIdAndName);
                    }
                    tableIds.add(tableResult.getId());
                    String listFieldsSql =
                        "select COLUMN_NAME name, COLUMN_TYPE type, IS_NULLABLE isRequired, COLUMN_COMMENT comment "
                            + "from information_schema.COLUMNS where table_name = ? and table_schema = ?";
                    PreparedStatement listFields = connection.prepareStatement(listFieldsSql);
                    listFields.setString(1, tableName);
                    listFields.setString(2, database);
                    ResultSet fields = listFields.executeQuery();
                    while (fields.next()) {
                        String fieldName = fields.getString("name");
                        GeneratorField fieldResult;
                        GeneratorField byTableIdAndName = generatorFieldRepository.getByTableIdAndName(tableResult.getId(), fieldName);
                        if (byTableIdAndName == null) {
                            GeneratorField generatorField = new GeneratorField();
                            generatorField.setTableId(tableResult.getId());
                            generatorField.setName(fieldName);
                            generatorField.setType(fields.getString("type"));
                            generatorField.setIsRequired(fields.getBoolean("isRequired"));
                            generatorField.setComment(fields.getString("comment"));
                            fieldResult = updateGeneratorField(generatorField);
                        } else {
                            byTableIdAndName.setTableId(tableResult.getId());
                            byTableIdAndName.setName(fieldName);
                            byTableIdAndName.setType(fields.getString("type"));
                            byTableIdAndName.setIsRequired(fields.getBoolean("isRequired"));
                            byTableIdAndName.setComment(fields.getString("comment"));
                            fieldResult = updateGeneratorField(byTableIdAndName);
                        }
                        fieldIds.add(fieldResult.getId());
                    }
                }
            }
            // 执行更新/增加操作后, 对已删除但是数据库中还未删除的数据库/表/字段进行清理, 删除多余的信息
            generatorDatabaseRepository.deleteAllByIdNotIn(databaseIds);
            generatorTableRepository.deleteAllByIdNotIn(tableIds);
            generatorFieldRepository.deleteAllByIdNotIn(fieldIds);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("prepareStatement error: {}", e.toString());
        }
        return false;
    }

    @Override
    public List<GeneratorConnectionVO> listConnectionInfoTree() {
        Long userId = SecurityUtil.getJwtUserId();
        GeneratorConnectionQueryCriteria param = new GeneratorConnectionQueryCriteria();
        param.setCreateUserId(userId);
        param.setEnable(true);
        List<GeneratorConnectionDTO> generatorConnectionDTOS = this.listAll(param.toSpecification());
        List<GeneratorConnectionVO> convert = Convert.convert(new TypeReference<List<GeneratorConnectionVO>>() {
        }, generatorConnectionDTOS);
        for (GeneratorConnectionVO singleConnection : convert) {
            singleConnection.setKey("connection" + singleConnection.getId());
            singleConnection.setTitle(singleConnection.getName());
            List<GeneratorDatabase> databases = generatorDatabaseRepository.findAllByConnectionId(singleConnection.getId());
            List<GeneratorDatabaseVO> databaseVOS = Convert.convert(new TypeReference<List<GeneratorDatabaseVO>>() {
            }, databases);
            for (GeneratorDatabaseVO singleDatabase : databaseVOS) {
                List<GeneratorTable> tables = generatorTableRepository.findAllByDatabaseIdIn(Collections.singletonList(singleDatabase.getId()));
                singleDatabase.setKey("database" + singleDatabase.getId());
                singleDatabase.setTitle(singleDatabase.getName());
                List<GeneratorTableVO> tableVOS = Convert.convert(new TypeReference<List<GeneratorTableVO>>() {
                }, tables);
                for (GeneratorTableVO tableVO : tableVOS) {
                    tableVO.setKey("table" + tableVO.getId());
                    tableVO.setTitle(tableVO.getName());
                }
                singleDatabase.setChildren(tableVOS);
            }
            singleConnection.setChildren(databaseVOS);
        }
        return convert;
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
