package com.zwayam.appliespuller.dbconfig;

import java.util.HashMap;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(basePackages = "com.zwayam.appliespuller.mysql",transactionManagerRef = "feedGeneratorTransactionManager",entityManagerFactoryRef = "feedGeneratorEntityManager")
public class ReportGeneratorDBConfig{
	
	@Autowired
	private Environment env;

	@Bean
    public DataSource genericFeedsDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.genericfeeds.url"));
        dataSource.setUsername(env.getProperty("spring.genericfeeds.username"));
        dataSource.setPassword(env.getProperty("spring.genericfeeds.password"));
        return dataSource;
    }

	@Bean
    public LocalContainerEntityManagerFactoryBean feedGeneratorEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(genericFeedsDataSource());
        em.setPackagesToScan(new String[] { "com.zwayam.appliespuller.mysql" });
        em.setPersistenceUnitName("appliespuller_em");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto","none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        em.setJpaPropertyMap(properties);
 
        return em;
    }

    @Bean
    public PlatformTransactionManager feedGeneratorTransactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(feedGeneratorEntityManager().getObject());
        return txManager;
    }

}
