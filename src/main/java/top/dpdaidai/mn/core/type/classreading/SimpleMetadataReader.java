package top.dpdaidai.mn.core.type.classreading;

import org.springframework.asm.ClassReader;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.core.type.AnnotationMetadata;
import top.dpdaidai.mn.core.type.ClassMetadata;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 对ASM相关的visitor等类进行封装
 *
 * @Author chenpantao
 * @Date 9/15/21 5:44 PM
 * @Version 1.0
 */
public class SimpleMetadataReader implements MetadataReader {

    private final Resource resource;

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource) throws IOException {
        InputStream is = new BufferedInputStream(resource.getInputStream());
        ClassReader classReader;
        try {
            classReader = new ClassReader(is);
        } finally {
            is.close();
        }

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor, ClassReader.SKIP_DEBUG);

        this.annotationMetadata = visitor;
        this.classMetadata = visitor;
        this.resource = resource;

    }


    public Resource getResource() {
        return resource;
    }

    public ClassMetadata getClassMetadata() {
        return classMetadata;
    }

    public AnnotationMetadata getAnnotationMetadata() {
        return annotationMetadata;
    }
}
