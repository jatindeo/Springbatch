package com.genpact.bi.batch.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableTransactionManagement
public class BatchDSConfig {
	
	@Autowired
	Environment env;

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    
    @Value("${spring.batch.schema}")
    private String schema;
    
	 @Primary
	 @Bean(name = "biBatchMetaDataSource")
	 @ConfigurationProperties(prefix = "spring.datasource")
	  public DataSource biBatchMetaDataSource() {
	   // return DataSourceBuilder.create().build();
		 final DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource
					.setUrl(databaseUrl);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			//dataSource.setSchema(schema);
			return dataSource;

	  }
	 

}
