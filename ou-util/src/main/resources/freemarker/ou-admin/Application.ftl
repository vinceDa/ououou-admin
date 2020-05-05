server.port=8080

#配置数据源
spring.datasource.druid.type=com.alibaba.druid.pool.DruidDataSource
#spring.datasource.druid.driverClassName=net.sf.log4jdbc.sql.jdbcapi.DriverSpy
spring.datasource.druid.driverClassName=com.mysql.jdbc.Driver
spring.datasource.druid.url=${dbUrl}
spring.datasource.druid.username=${dbUsername}
spring.datasource.druid.password=${dbPassword}
# 初始化配置
spring.datasource.druid.initial-size=3
# 最小连接数
spring.datasource.druid.min-idle=3
# 最大连接数
spring.datasource.druid.max-active=15
# 获取连接超时时间
spring.datasource.druid.max-wait=5000
# 连接有效性检测时间
spring.datasource.druid.time-between-eviction-runs-millis=90000
# 最大空闲时间
spring.datasource.druid.min-evictable-idle-time-millis=1800000
spring.datasource.druid.test-while-idle=true
spring.datasource.druid.test-on-borrow=false
spring.datasource.druid.test-on-return=false
spring.datasource.druid.validation-query=select 1
#配置 Jpa
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=true
spring.jpa.show-sql=true