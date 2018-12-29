package org.oasis.easy.orm.core;

import org.oasis.easy.orm.annotations.Dao;
import org.oasis.easy.orm.exception.EasyOrmException;
import org.oasis.easy.orm.exception.ErrorCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.*;

/**
 * @author tianbo
 * @date 2018-12-29
 */
public class EasyOrmComponentProvider implements ResourceLoaderAware {

    private static final String RESOURCE_PATTERN = "**/*.class";

    private final List<TypeFilter> includeFilters = new LinkedList<>();

    private ResourcePatternResolver resourcePatternResolver;

    private MetadataReaderFactory metadataReaderFactory;

    public EasyOrmComponentProvider() {
        setResourceLoader(null);
        // 只处理@Dao注解标记的类
        includeFilters.add(new AnnotationTypeFilter(Dao.class));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    public Set<BeanDefinition> findCandidateComponents(String baseDir) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    baseDir.replace('.', '/') + '/' + RESOURCE_PATTERN;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (!resource.exists() || !resource.isReadable()) {
                    continue;
                }

                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (isCandidateComponent(metadataReader)) {
                    ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(metadataReader);
                    beanDefinition.setResource(resource);
                    beanDefinition.setSource(resource);

                    if (checkCandidate(beanDefinition.getMetadata())) {
                        candidates.add(beanDefinition);
                    }
                }
            }
        } catch (IOException ex) {
            throw new EasyOrmException(ErrorCode.IO_ERROR, "scan candidate components caught io exception");
        }
        return candidates;
    }

    private boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        for (TypeFilter typeFilter : this.includeFilters) {
            if (typeFilter.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCandidate(AnnotationMetadata annotationMetadata) {
        return annotationMetadata.isIndependent() && annotationMetadata.isInterface();
    }
}
