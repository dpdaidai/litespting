package top.dpdaidai.mn.test.v4;

import org.junit.Assert;
import org.junit.Test;
import top.dpdaidai.mn.core.io.Resource;
import top.dpdaidai.mn.core.io.support.PackageResourceLoader;

/**
 *
 * 测试PackageResourceLoader , 该类用于将指定包名下的文件加载为resource
 *
 *
 * @Author chenpantao
 * @Date 9/14/21 9:25 PM
 * @Version 1.0
 */
public class PackageResourceLoaderTest {

    @Test
    public void testGetResources() {
        PackageResourceLoader packageResourceLoader = new PackageResourceLoader();
        Resource[] resources = packageResourceLoader.getResources("top.dpdaidai.mn.service.v4");
        Assert.assertEquals(3, resources.length);
        resources = packageResourceLoader.getResources("top.dpdaidai.mn.service.daoV4");
        Assert.assertEquals(2, resources.length);

    }
}
