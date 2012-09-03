package be.kuleuven.researchpad.config.root;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import be.kuleuven.researchpad.domain.Base;

@Configuration
@EnableTransactionManagement
public class PersistenceJPAConfig {
	@Value("${hibernate.format_sql}")
	private String formatSql;
	@Value("${hibernate.naming_strategy}")
	private String ejbNamingStrategy;
	@Value("${hibernate.charSet}")
	private String charSet;
	@Value("${hibernate.generate_ddl}")
	private String generateDdl;
	@Value("${hibernate.dialect}")
	private String dialect;
	@Value("${hibernate.show_sql}")
	private String showSql;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddl;
	@Value("${database.driverClassName}")
	private String driverClassName;
	@Value("${database.url}")
	private String databaseUrl;
	@Value("${database.userName}")
	private String databaseUserName;
	@Value("${database.password}")
	private String databasePassword;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManager() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setJpaVendorAdapter(getJpaVendorAdaptor());
		emf.setJpaPropertyMap(getJpaProperties());
		emf.setPackagesToScan(Base.class.getPackage().getName());
		emf.afterPropertiesSet();
		return emf;
	}
	
	private JpaVendorAdapter getJpaVendorAdaptor() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(Boolean.valueOf(generateDdl));
		jpaVendorAdapter.setShowSql(Boolean.valueOf(showSql));
		jpaVendorAdapter.setDatabasePlatform(dialect);
		return jpaVendorAdapter;
	}
	
	private Map<String, String> getJpaProperties() {
		Map<String, String> jpaProps = new HashMap<String, String>();
		jpaProps.put("hibernate.format_sql", formatSql);
		jpaProps.put("hibernate.ejb.naming_strategy", ejbNamingStrategy);
		jpaProps.put("hibernate.connection.charSet", charSet);
		jpaProps.put("hibernate.hbm2ddl.auto", this.hbm2ddl);
		return jpaProps;
	}

	
	@Bean 
	public BasicDataSource dataSource(){
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(this.driverClassName);
		bds.setUsername(this.databaseUserName);
		bds.setPassword(this.databasePassword);
		bds.setUrl(this.databaseUrl);
		return bds;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManager()
				.getObject());

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
