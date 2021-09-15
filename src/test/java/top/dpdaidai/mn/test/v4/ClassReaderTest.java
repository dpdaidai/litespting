package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.asm.ClassReader;
import top.dpdaidai.mn.core.annotation.AnnotationAttributes;
import top.dpdaidai.mn.core.io.ClassPathResource;
import top.dpdaidai.mn.core.type.classreading.AnnotationMetadataReadingVisitor;
import top.dpdaidai.mn.core.type.classreading.ClassMetadataReadingVisitor;

import java.io.IOException;
import java.lang.annotation.ElementType;

/**
 *
 *  利用ASM提供的功能 , 测试对指定类的字节码解析是否正确
 *
 * @Author chenpantao
 * @Date 9/15/21 3:01 PM
 * @Version 1/0
 */
public class ClassReaderTest {

    @Test
    public void testGetClassMetaData() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("top/dpdaidai/mn/service/v4/PetStoreService.class");
        ClassReader classReader = new ClassReader(classPathResource.getInputStream());

        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        Assert.assertFalse(visitor.isAbstract());
        Assert.assertFalse(visitor.isInterface());
        Assert.assertFalse(visitor.isFinal());
        Assert.assertEquals("top.dpdaidai.mn.service.v4.PetStoreService", visitor.getClassName());
        Assert.assertEquals("java.lang.Object", visitor.getSuperClassName());
        Assert.assertEquals(0, visitor.getInterfaceNames().length);


    }

    @Test
    public void testGetAnnotation() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("top/dpdaidai/mn/service/v4/PetStoreService.class");
//        ClassPathResource classPathResource = new ClassPathResource("top/dpdaidai/mn/beans/factory/annotation/Autowired.class");
        ClassReader classReader = new ClassReader(classPathResource.getInputStream());

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        String annotation = "top.dpdaidai.mn.stereotype.Component";
//        String annotation = "java.lang.annotation.Target";
        Assert.assertTrue(visitor.hasAnnotation(annotation));

        AnnotationAttributes annotationAttributes = visitor.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore", annotationAttributes.get("value"));
//        Assert.assertEquals(4, ((ElementType[]) annotationAttributes.get("value")).length);

    }

}
