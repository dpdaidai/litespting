package top.dpdaidai.mn.test.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        ClassReaderTest.class,
        PackageResourceLoaderTest.class,
        MetadataReaderTest.class,
        ClassPathBeanDefinitionScannerTest.class,
        XmlBeanDefinitionReaderTest.class
})
public class V4AllTests {

}
