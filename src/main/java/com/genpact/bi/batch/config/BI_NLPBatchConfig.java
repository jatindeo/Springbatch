package com.genpact.bi.batch.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

//import com.genpact.bi.batch.dto.CustomPojo;
import com.genpact.bi.batch.entity.Pojo;
import com.genpact.bi.batch.entity.Pojo1;
import com.genpact.bi.batch.step.CustomItemProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * This class contains all the configuration of the Spring Batch application
 * used . It contains readers, writers, processors, jobs, steps
 * and all the needed beans.
 * 
 * @author Jatindra Singh Deo
 *
 */
@Configuration
@EnableBatchProcessing
@Slf4j
//@Import({JPAConfig.class,BatchDSConfig.class})
//@ComponentScan("")
public class BI_NLPBatchConfig extends DefaultBatchConfigurer{

	/**
	 * Modes, should be injected as parameter TODO
	 */
	// private String mode = "custom";

	private String mode = "mysql";
	
      @Qualifier("biEntityManagerFactory")
	  @Autowired
	  private EntityManagerFactory entityManagerFactory;
      @Autowired
      @Qualifier("biTransactionManager")
      PlatformTransactionManager transactionManager;
//	  
//	  @Autowired
//	  DataSource dataSource;

	// private String mode = "mongo";

	// private String mode = "flat";

	// private String mode = "hsqldb";

	/* ********************************************
	 * READERS This section contains all the readers
	 * ********************************************
	 */

	/**
	 * 
	 * @return a reader
	 */


	
	@Bean(destroyMethod = "")
	  public JpaPagingItemReader<Pojo> reader() throws Exception {
	    JpaPagingItemReader reader = new JpaPagingItemReader();
	    reader.setEntityManagerFactory(entityManagerFactory);
	    reader.setQueryString("SELECT p FROM Pojo p");
	    //reader.setQueryProvider(jpaQueryProvider);
	    reader.setPageSize(1);
	    reader.afterPropertiesSet();
	    return reader;
	  }

	/* ********************************************
	 * PROCESSORS This section contains all processors
	 * ********************************************
	 */

	/**
	 * 
	 * @return custom item processor -> anything
	 */
	@Bean
	public ItemProcessor<Pojo, Pojo1> processor() {
		return new CustomItemProcessor();
	}

	/* ********************************************
	 * WRITERS This section contains all the writers
	 * ********************************************
	 */

	/**
	 * 
	 * @param dataSource
	 * @return dummy item writer custom
	 */
	@Bean
	public ItemWriter<Pojo1> writer(DataSource dataSource) {
		
			// hsqldb writer using JdbcBatchItemWriter (the difference is the
			// datasource)
			JpaItemWriter<Pojo1> writer = new JpaItemWriter<Pojo1>();
			writer.setEntityManagerFactory(entityManagerFactory);
			return writer;
		
	}

	/* ********************************************
	 * JOBS ***************************************
	 * ********************************************
	 */
	/**
	 * 
	 * @param jobs
	 * @param s1
	 *            steps
	 * @return the Job
	 */
	@Bean
	public Job importUserJob(JobBuilderFactory jobs, Step s1) {
		return jobs.get("importUserJob").incrementer(new RunIdIncrementer())
				.flow(s1).end().build();
	}

	/* ********************************************
	 * STEPS **************************************
	 * ********************************************
	 */

	/**
	 * the step 1 contains a reader a processor and a writer using a chunk of 10
	 * 
	 * @param stepBuilderFactory
	 * @param reader
	 * @param writer
	 * @param processor
	 * @return
	 */
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,
			ItemReader<Pojo> reader, ItemWriter<Pojo1> writer,
			ItemProcessor<Pojo,Pojo1> processor) {
		/* it handles bunches of 10 units */
		return stepBuilderFactory.get("step1").transactionManager(transactionManager)
				.<Pojo, Pojo1> chunk(1).reader(reader)
				.processor(processor).writer(writer).build();
	}

	/* ********************************************
	 * UTILITY BEANS ******************************
	 * ********************************************
	 */

	/**
	 * jdbc template (hsqldb)
	 * 
	 * @param dataSource
	 * @return JdbcTemplate
	 */
	@Bean
	public JdbcTemplate jdbcTemplate(@Qualifier("biDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	
	/*@Configuration
	@EnableTransactionManagement
	@EnableBatchProcessing 
	class Config{ {
	     @Bean
	    public LocalContainerEntityManagerFactoryBean emf() {
	        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
	        emf.setDataSource(dataSource);
	        emf.setPackagesToScan("my.package");
	        emf.setJpaVendorAdapter(jpaAdapter());
	        emf.setJpaProperties(jpaProterties());
	        return emf;
	}
	     @Bean
	     public JpaVendorAdapter jpaVendorAdapter() {
	         HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
	         jpaVendorAdapter.setDatabase(Database.MYSQL);
	         jpaVendorAdapter.setGenerateDdl(true);
	         jpaVendorAdapter.setShowSql(false);

	         jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
	         return jpaVendorAdapter;
	     }*/
	@Override
    @Autowired
    public void setDataSource(@Qualifier("biBatchMetaDataSource") DataSource biBatchMetaDataSource) {
        super.setDataSource(biBatchMetaDataSource);
    }

	/**
	 * jobLauncherTestUtils utility class for testing batches
	 * 
	 * @return JobLauncherTestUtils
	 */
	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() {
		return new JobLauncherTestUtils();
	}

}