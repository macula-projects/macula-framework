package org.maculaframework.boot.core.config.jpa;

import org.maculaframework.boot.core.config.repositories.AbstractRepositoryConfigurationSourceSupport;
import org.maculaframework.boot.core.repository.hibernate.audit.AuditedEventListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JPA配置工厂
 *
 * @author Rain
 * @see 2019-2-15
 */

public class EntityManagerFactoryBeanBuilder {

    @Autowired(required = false)
    ObjectProvider<PersistenceUnitManager> persistenceUnitManager;
    private DataSource dataSource;
    @Autowired
    private JpaProperties jpaProperties;
    @Autowired
    private HibernateProperties hibernateProperties;
    private AbstractRepositoryConfigurationSourceSupport.RepositoryConfig repositoryConfig;

    public EntityManagerFactoryBeanBuilder(DataSource dataSource,
                                           AbstractRepositoryConfigurationSourceSupport.RepositoryConfig repositoryConfig) {
        this.dataSource = dataSource;
        this.repositoryConfig = repositoryConfig;
    }

    private Map<String, Object> getVendorProperties() {
        HibernateSettings settings = new HibernateSettings();

        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        customizers.add((Map<String, Object> hibernateProperties) -> {
            hibernateProperties.put("hibernate.ejb.event.post-update", AuditedEventListener.class.getName());
            hibernateProperties.put("hibernate.ejb.event.post-delete", AuditedEventListener.class.getName());
        });

        settings.hibernatePropertiesCustomizers(customizers);

        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), settings);
    }

    private EntityManagerFactoryBuilder getEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(getJpaVendorAdapter(), jpaProperties.getProperties(), persistenceUnitManager.getIfAvailable());
    }

    private JpaVendorAdapter getJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(this.jpaProperties.isShowSql());
        adapter.setDatabase(this.jpaProperties.determineDatabase(this.dataSource));
        adapter.setDatabasePlatform(this.jpaProperties.getDatabasePlatform());
        adapter.setGenerateDdl(this.jpaProperties.isGenerateDdl());
        return adapter;
    }

    public LocalContainerEntityManagerFactoryBean createEntityManagerFactory() {
        return getEntityManagerFactoryBuilder()
                .dataSource(dataSource)
                .properties(getVendorProperties())
                .packages(repositoryConfig.getEntityPackages()) //设置实体类所在位置
                .persistenceUnit(repositoryConfig.getName())
                .build();
    }
}
