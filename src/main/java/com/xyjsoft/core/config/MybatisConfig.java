package com.xyjsoft.core.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Mybatis配置
 * @author gsh456
 * @date 2019-03-15 11:10
 */
@Configuration
@MapperScan("com.xyjsoft.*.dao")	// 扫描DAO
public class MybatisConfig {
  @Autowired
  private DataSource dataSource;

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    sessionFactory.setTypeAliasesPackage("com.xyjsoft.*.model");    // 扫描Model
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sessionFactory.setMapperLocations(resolver.getResources("classpath*:**/sqlmap/*.xml"));    // 扫描映射文件
    org.apache.ibatis.session.Configuration configuration = sessionFactory.getObject().getConfiguration();
    configuration.setMapUnderscoreToCamelCase(true);
    configuration.setCallSettersOnNulls(true);//设置返回HashMap，字段值为null时保存key，如果不设置默认是false
    return sessionFactory.getObject();
  }

  @Bean
  public DatabaseIdProvider getDatabaseIdProvider() {
    DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    Properties properties = new Properties();
    properties.setProperty("Oracle", "oracle");
    properties.setProperty("MySQL", "mysql");
//    properties.setProperty("DB2", "db2");
//    properties.setProperty("Derby", "derby");
//    properties.setProperty("H2", "h2");
//    properties.setProperty("HSQL", "hsql");
//    properties.setProperty("Informix", "informix");
    properties.setProperty("MS-SQL", "ms-sql");
//    properties.setProperty("PostgreSQL", "postgresql");
//    properties.setProperty("Sybase", "sybase");
//    properties.setProperty("Hana", "hana");
    databaseIdProvider.setProperties(properties);
    return databaseIdProvider;
  }

  @Bean
  public ConfigurationCustomizer configurationCustomizer(){
    return new MybatisPlusCustomizers();
  }
  class MybatisPlusCustomizers implements ConfigurationCustomizer {
    @Override
    public void customize(org.apache.ibatis.session.Configuration configuration) {
      configuration.setJdbcTypeForNull(JdbcType.NULL);
    }
  }
}