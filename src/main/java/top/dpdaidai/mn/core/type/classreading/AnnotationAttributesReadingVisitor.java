package top.dpdaidai.mn.core.type.classreading;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.SpringAsmInfo;
import top.dpdaidai.mn.core.annotation.AnnotationAttributes;

import java.util.Map;

/**
 * @Author chenpantao
 * @Date 9/15/21 4:16 PM
 * @Version 1.0
 */
public class AnnotationAttributesReadingVisitor extends AnnotationVisitor {

    private final String annotationType;
    private final Map<String, AnnotationAttributes> attributesMap;

    AnnotationAttributes attributes = new AnnotationAttributes();

    public AnnotationAttributesReadingVisitor(String annotationType, Map<String, AnnotationAttributes> attributesMap) {
        super(SpringAsmInfo.ASM_VERSION);

        this.annotationType = annotationType;
        this.attributesMap = attributesMap;
    }

    public void visit(String attributeName, Object attributeValue) {
        this.attributes.put(attributeName, attributeValue);
    }

    @Override
    public final void visitEnd() {
        this.attributesMap.put(this.annotationType, this.attributes);
    }


}
