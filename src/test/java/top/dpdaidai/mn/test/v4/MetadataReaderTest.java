package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.core.annotation.AnnotationAttributes;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.type.AnnotationMetadata;
import top.dpdaidai.mn.core.type.classreading.MetadataReader;
import top.dpdaidai.mn.core.type.classreading.SimpleMetadataReader;
import top.dpdaidai.mn.stereotype.Component;

import java.io.IOException;

/**
 * @Author chenpantao
 * @Date 9/15/21 5:43 PM
 * @Version 1.0
 */
public class MetadataReaderTest {

    @Test
    public void testGetMetadata() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("top/dpdaidai/mn/service/v4/PetStoreService.class");

        MetadataReader reader = new SimpleMetadataReader(classPathResource);

        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();

        String component = Component.class.getName();

        Assert.assertTrue(annotationMetadata.hasAnnotation(component));

        AnnotationAttributes annotationAttributes = annotationMetadata.getAnnotationAttributes(component);

        Assert.assertEquals("petStore", annotationAttributes.get("value"));

        Assert.assertFalse(annotationMetadata.isAbstract());
        Assert.assertFalse(annotationMetadata.isFinal());
        Assert.assertEquals("top.dpdaidai.mn.service.v4.PetStoreService", annotationMetadata.getClassName());

    }

}
