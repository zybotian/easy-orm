package org.oasis.easy.orm.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.oasis.easy.orm.data.access.DataAccessFactory;
import org.oasis.easy.orm.data.access.DataAccessFactoryImpl;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.oasis.easy.orm.interpreter.InterpreterFactory;
import org.oasis.easy.orm.interpreter.InterpreterFactoryImpl;
import org.oasis.easy.orm.mapper.row.RowMapperFactory;
import org.oasis.easy.orm.mapper.row.RowMapperFactoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

import java.util.Set;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final String baseDir;

    /**
     * 数据访问
     */
    private DataAccessFactory dataAccessFactory;

    /**
     * 行映射
     */
    private RowMapperFactory rowMapperFactory;

    /**
     * sql语句解释
     */
    private InterpreterFactory interpreterFactory;

    public EasyOrmBeanFactoryPostProcessor(String baseDir) {
        this.baseDir = baseDir;
    }

    public DataAccessFactory getDataAccessFactory(ConfigurableListableBeanFactory beanFactory) {
        if (this.dataAccessFactory == null) {
            dataAccessFactory = new DataAccessFactoryImpl(beanFactory);
        }
        return dataAccessFactory;
    }

    public InterpreterFactory getInterpreterFactory(ConfigurableListableBeanFactory beanFactory) {
        if (interpreterFactory == null) {
            interpreterFactory = new InterpreterFactoryImpl(beanFactory);
        }
        return interpreterFactory;
    }

    public RowMapperFactory getRowMapperFactory() {
        if (rowMapperFactory == null) {
            rowMapperFactory = new RowMapperFactoryImpl();
        }
        return rowMapperFactory;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Set<BeanDefinition> candidates = findDaoCandidate();
        if (CollectionUtils.isEmpty(candidates)) {
            return;
        }

        for (BeanDefinition beanDefinition : candidates) {
            registerDaoBeanDefination(beanDefinition, beanFactory);
        }
    }

    private Set<BeanDefinition> findDaoCandidate() {
        if (StringUtils.isEmpty(baseDir)) {
            throw new EasyOrmException(ErrorCode.MISSING_CONFIG, "scan dir is missing");
        }

        EasyOrmComponentProvider daoComponentProvider = new EasyOrmComponentProvider();
        return daoComponentProvider.findCandidateComponents(baseDir);
    }

    private void registerDaoBeanDefination(BeanDefinition beanDefinition, ConfigurableListableBeanFactory beanFactory) {
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

        String daoClassName = beanDefinition.getBeanClassName();
        propertyValues.addPropertyValue("objectType", daoClassName);
        propertyValues.addPropertyValue("dataAccessFactory", getDataAccessFactory(beanFactory));
        propertyValues.addPropertyValue("rowMapperFactory", getRowMapperFactory());
        propertyValues.addPropertyValue("interpreterFactory", getInterpreterFactory(beanFactory));

        ScannedGenericBeanDefinition scannedBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
        scannedBeanDefinition.setPropertyValues(propertyValues);
        scannedBeanDefinition.setBeanClass(EasyOrmFactoryBean.class);

        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        defaultListableBeanFactory.registerBeanDefinition(daoClassName, scannedBeanDefinition);
    }
}
