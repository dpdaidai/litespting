package top.dpdaidai.mn.context.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.dpdaidai.mn.beans.exception.BeanDefinitionStoreException;
import top.dpdaidai.mn.beans.factory.BeanDefinition;
import top.dpdaidai.mn.beans.factory.support.BeanDefinitionRegistry;
import top.dpdaidai.mn.beans.factory.support.BeanNameGenerator;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.core.io.support.PackageResourceLoader;
import top.dpdaidai.mn.core.type.classreading.MetadataReader;
import top.dpdaidai.mn.core.type.classreading.SimpleMetadataReader;
import top.dpdaidai.mn.stereotype.Component;
import top.dpdaidai.mn.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * 该类用于根据包名 , 扫描包下含有Component注解的类 , 根据class文件 , 解析得到的 beanDefinition 相关信息并进行注册
 *
 * @Author chenpantao
 * @Date 9/17/21 2:58 PM
 * @Version 1.0
 */
public class ClassPathBeanDefinitionScanner {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    private PackageResourceLoader packageResourceLoader = new PackageResourceLoader();

    protected final Log logger = LogFactory.getLog(getClass());

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    public Set<BeanDefinition> doScan(String packagesToScan) {
        String[] basePackages = StringUtils.tokenizeToStringArray(packagesToScan, ",");
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<BeanDefinition>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
            beanDefinitions.addAll(candidateComponents);
        }

        return beanDefinitions;
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();

        Resource[] resources = this.packageResourceLoader.getResources(basePackage);
        for (Resource resource : resources) {
            try {
                MetadataReader metadataReader = new SimpleMetadataReader(resource);
                if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {

                    //生成scannedGenericBeanDefinition
                    ScannedGenericBeanDefinition scannedGenericBeanDefinition =
                            new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());

                    //为bean生成beanName , 就是beanID
                    String beanName = this.beanNameGenerator.generateBeanName(scannedGenericBeanDefinition, this.beanDefinitionRegistry);

                    //设置beanID
                    scannedGenericBeanDefinition.setID(beanName);

                    //注册bean的定义 BeanDefinition
                    this.beanDefinitionRegistry.registerBeanDefinition(beanName, scannedGenericBeanDefinition);

                    candidates.add(scannedGenericBeanDefinition);
                }
            } catch (IOException e) {
                throw new BeanDefinitionStoreException("failed to read candidate component class :" + resource, e);
            }
        }

        return candidates;

    }


}
