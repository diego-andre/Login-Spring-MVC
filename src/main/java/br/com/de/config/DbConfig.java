package br.com.de.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import br.com.de.utils.EncryptionUtil;

@Configuration
@EnableJpaRepositories(
    basePackages = "br.com.de.persistence", 
    entityManagerFactoryRef = "dbEntityManager", 
    transactionManagerRef = "dbTransactionManager"
)
public class DbConfig {
    
    @Value("${deDts.datasource.db-url}")
    private String url;
    
    @Value("${deDts.datasource.username}")
    private String username;
    
    @Value("${deDts.datasource.password}")
    private String password;
    
    @Autowired 
    private JpaProperties jpaProperties;
    
    
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean dbEntityManager() {
        LocalContainerEntityManagerFactoryBean em
          = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dbDataSource());
        em.setPackagesToScan(new String[] { "br.com.de.persistence" });
 
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);      
        em.setJpaPropertyMap(jpaProperties.getProperties()); 
 
        return em;
    }
 
    @Bean
    @Primary
    public DataSource dbDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(EncryptionUtil.decrypt(password));
 
        return dataSource;
    }
 
    @Bean
    @Primary
    public PlatformTransactionManager dbTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(dbEntityManager().getObject());
        return transactionManager;
    }
}
