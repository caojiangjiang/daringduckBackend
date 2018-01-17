package cn.daringduck.communitybuilder;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Settings for Spring
 * 
 * @author Jochem Ligtenberg
 */
@Configuration
@EnableJpaRepositories("cn.daringduck.communitybuilder")
@EnableTransactionManagement
@ImportResource("beans.xml")
public class ApplicationConfig {

	//指定Spring定义的数据源
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/daringduck2?autoReconnect=true&amp;useUnicode=yes&amp;characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("password");
		
		return dataSource;
	}

	//设置实现厂商JPA实现的特定属性
	@Bean
	public JpaVendorAdapter jpaAdapter() {
		return new HibernateJpaVendorAdapter();
	}
	
	//实体管理工厂
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		//指定Spring定义的DataSource
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setPersistenceUnitName("DaringDuck");
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	//Spring事物管理器TransactionManager
	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}
	
	
}