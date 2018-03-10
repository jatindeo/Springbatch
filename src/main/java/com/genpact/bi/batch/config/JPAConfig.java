package com.genpact.bi.batch.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@EnableAutoConfiguration

@EnableJpaRepositories(
  entityManagerFactoryRef = "biEntityManagerFactory",
  transactionManagerRef = "biTransactionManager",
  basePackages = { "com.genpact.bi.batch.repo" }
)
public class JPAConfig {
	
//	@Autowired
//	JpaVendorAdapter jpaVendorAdaptor;
	@Autowired
	Environment env;

    @Value("${bischema.datasource.url}")
    private String databaseUrl;

    @Value("${bischema.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${bischema.datasource.driverClassName}")
    private String driverClassName;

    
    @Value("${bischema.jpa.database-platform}")
    private String dialect;
    
    @Value("${bischema.jpa.hibernate.ddl-auto}")
    private String ddl_auto;
    
    @Value("${bischema.jpa.show-sql}")
    private String show_sql;
	
	 @Bean(name = "biDataSource") 
	 @ConfigurationProperties(prefix = "bischema.datasource")
	  public DataSource dataSource() {
	    //return DataSourceBuilder.create().build();
		 final DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource
					.setUrl(databaseUrl);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
	  }
	 
	
	
	@Bean(name = "biEntityManagerFactory")
	  public LocalContainerEntityManagerFactoryBean 
	  biEntityManagerFactory(
	    EntityManagerFactoryBuilder builder,
	    @Qualifier("biDataSource") DataSource dataSource
	  ) {
	    return
	      builder
	        .dataSource(dataSource)
	        .packages("com.genpact.bi.batch.entity")
	        .properties(jpaProperties())
	        
	        .persistenceUnit("bischema")
	        .build();
//		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource);
//        emf.setJpaVendorAdapter(jpaVendorAdapter());
//        emf.setPackagesToScan("com.genpact.bi.batch.entity");
//        emf.setPersistenceUnitName("bischema");   // <- giving 'default' as name
//        emf.afterPropertiesSet();
//        return emf;
	  }
	
	private Map<String, Object> jpaProperties() {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("hibernate.ddl-auto", ddl_auto);
        props.put("hibernate.dialect", dialect);
        props.put("show-sql", show_sql);

        return props;
    }
	
	@Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.MYSQL);
        jpaVendorAdapter.setGenerateDdl(false);
        jpaVendorAdapter.setShowSql(true);

        jpaVendorAdapter.setDatabasePlatform(dialect);
        return jpaVendorAdapter;
    }
    
	  @Bean(name = "biTransactionManager")
	  public PlatformTransactionManager biTransactionManager(
	    @Qualifier("biEntityManagerFactory") EntityManagerFactory
	    biEntityManagerFactory
	  ) {
	    return new JpaTransactionManager(biEntityManagerFactory);
	  }


}
